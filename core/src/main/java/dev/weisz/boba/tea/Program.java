package dev.weisz.boba.tea;

import dev.weisz.boba.Renderer;
import dev.weisz.boba.StandardRenderer;
import dev.weisz.boba.terminal.Terminal;
import dev.weisz.boba.terminal.WinSize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Signal;

import java.io.*;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class Program<Model> {
    private static final Logger LOGGER = LoggerFactory.getLogger(Program.class);

    private final BlockingQueue<Msg> msgQueue = new LinkedBlockingQueue<>();
    private Terminal terminal;
    private Renderer renderer;
    private WinSize minWinSize;

    protected abstract UpdateResult<Model> update(Model model, Msg msg);
    protected abstract String view(Model model);

    public WinSize getWinSize() {
        return renderer.getWinSize();
    }

    public void setMinWinSize(WinSize minWinSize) {
        this.minWinSize = minWinSize;

        if (minWinSize == null) {
            processCmd(Msg.RemoveWindowMinSizeMsg::new);
            return;
        }

        processCmd(() -> new Msg.SetWindowMinSizeMsg(minWinSize.height(), minWinSize.width()));
    }

    public Model run(Model model, ProgramOpts opts) {
        // this might not support any other input and output streams than the default ones
        // we will support another mode of input like tty later
        // 0 = input
        // 1 = output
        // ref look at FileDescriptor.java
        terminal = Terminal.create();
        if (!terminal.isTerminal(0)) {
            throw new UnsupportedOperationException("The system input is not a terminal.");
        }
        if (!terminal.isTerminal(1)) {
            throw new UnsupportedOperationException("The system output is not a terminal.");
        }

        // we need the input and output both to be raw
        terminal.makeRaw(0);

        // rn we will assume that its running inside a terminal but in the future we need to handle everything
        // from my testing, I don't think these signals are ever sent (even when terminal is closed forcefully)
        if (!opts.useDefaultSignalHandler()) {
            Signal.handle(new Signal("INT"), _ -> {
                // always will be successfully since uncapped queue
                msgQueue.offer(new Msg.InteruptMsg());
            });

            Signal.handle(new Signal("TERM"), _ -> {
                msgQueue.offer(new Msg.QuitMsg());
            });
        }

        // TODO: discuss further on discord the usage of this "unsafe" api
        // from some core devs, they dont have any plans to remove it and if they do
        // a actual api will replace it
        // this is how apache handled avoiding the usage of it:
        // https://issues.apache.org/jira/browse/HADOOP-19329
        Signal.handle(new Signal("WINCH"), _ -> {
            processCmd(() -> {
                WinSize WinSize = terminal.getWinSize();
                return new Msg.WindowSizeMsg(WinSize.height(), WinSize.width());
            });
        });

        // TODO: allow configurable renderer
        renderer = new StandardRenderer(opts.output(), opts.fps());
        renderer.hideCursor();

        if (!opts.startupTitle().isEmpty()) {
             renderer.setWindowTitle(opts.startupTitle());
        }

        if (opts.useAltScreen()) {
            renderer.enterAltScreen();
        }

        if (opts.useBracketedPaste()) {
            renderer.enableBracketedPaste();
        }

        if (opts.enableMouseCellMotion()) {
            renderer.enableMouseCellMotion();
            renderer.enableMouseSGRMode();
        }// TODO: mouse all motion opts

        if (opts.reportFocusChange()) {
            renderer.enableReportFocus();
        }

        if (opts.minWinSize() != null) {
            setMinWinSize(opts.minWinSize());
        }

        // set initial size inside the renderer
        processCmd(() -> {
            WinSize WinSize = terminal.getWinSize();
            return new Msg.WindowSizeMsg(WinSize.height(), WinSize.width());
        });

        renderer.start();
        renderer.write(view(model));

        LOGGER.debug("Program started with initial frame printed.");

        if (opts.input() != null) {
            Thread.startVirtualThread(() -> handleUserInput(opts.input()));
        }

        // handle settings term back to old settings on shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            terminal.makeCooked(0);
        }));

        Model finalModel;
        try {
            finalModel = eventLoop(model);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        // write last frame
        renderer.write(view(finalModel));

        return finalModel;
    }

    /**
     * <p>Handles the processing of every message inside the {@link Program#msgQueue}, handles any
     * internal messages such as {@link Msg.QuitMsg} first. After that it passes messages onto the
     * renderer to handle before passing it to the {@link Program#update(Object, Msg)}. When this
     * function returns a value it will indicate that the program has finished. It will only return
     * when an {@link Msg.QuitMsg} or {@link Msg.InteruptMsg} are received.</p>
     */
    private Model eventLoop(Model model) throws InterruptedException {
        while (true) {
            Msg msg = msgQueue.take();

            switch (msg) {
                case Msg.QuitMsg _ -> {
                    return model;
                }
                case Msg.ClearScreenMsg _ ->
                    renderer.clearScreen();

                case Msg.SetTitleMsg(String title) ->
                    renderer.setWindowTitle(title);

                case Msg.WindowSizeMsg(int height, int width) -> {
                    LOGGER.info("Window size changed to: {}", new WinSize(height, width));
                    if (height < minWinSize.height() || width < minWinSize.width()) {
                        terminal.setWinSize(new WinSize(
                                Math.max(height, minWinSize.height()),
                                Math.max(width, minWinSize.width())
                        ));

                        LOGGER.info("Window size was below min size. Resized to: {}", getWinSize());
                    }
                }

                case Msg.SetWindowMinSizeMsg(_, _) -> {
                    WinSize currentSize = getWinSize();
                    if (currentSize.height() < minWinSize.height() || currentSize.width() < minWinSize.width()) {
                        terminal.setWinSize(new WinSize(
                                Math.max(currentSize.height(), minWinSize.height()),
                                Math.max(currentSize.width(), minWinSize.width())
                        ));
                    }
                }


                case BatchCmd.BatchMsg(List<Cmd> cmds) ->
                    cmds.forEach(this::processCmd);

                default -> { /* ignore */ }
            }

            renderer.handleMessages(msg);

            UpdateResult<Model> updateResult = update(model, msg);
            if (updateResult.cmd() != null) {
                // handle command async
                processCmd(updateResult.cmd());
            }

            renderer.write(view(model));
        }
    }

    private void processCmd(Cmd cmd) {
        Thread.startVirtualThread(() -> {
            Msg msg;
            try {
                msg = cmd.execute();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            if (msg != null) {
                msgQueue.add(msg);
            }
        });
    }

    // https://ecma-international.org/wp-content/uploads/ECMA-48_5th_edition_june_1991.pdf
    // ^^^^ use this as a reference
    private void handleUserInput(InputStream in) {
        while (true) {
            try {
                int input = in.read();

                if (input == -1) {
                    continue;
                }

                processCmd(() -> new Msg.KeyClickMsg((char) input));

                LOGGER.debug("Input: (Raw) {} (Char) {}", input, (char) input);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
