package dev.mccue.boba.tea;

import dev.mccue.boba.Renderer;
import dev.mccue.boba.StandardRenderer;
import dev.mccue.boba.Terminal;
import dev.mccue.boba.TerminalSize;
import sun.misc.Signal;
import sun.misc.Unsafe;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// TODO: this needs to be stateful and should likely be an abstract class instead
public abstract class Program<Model> {
    private final BlockingQueue<Msg> msgQueue = new LinkedBlockingQueue<>();
    private Renderer renderer;

    protected abstract UpdateResult<Model> update(Model model, Msg msg);
    protected abstract String view(Model model);

    public Model run(Model model, ProgramOpts opts) {
        // this might not support any other input and output streams than the default ones
        // we will support another mode of input like tty later
        // 0 = input
        // 1 = output
        // ref look at FileDescriptor.java
        Terminal terminal = Terminal.create();
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
                System.err.println("INTERUPT sent");
                msgQueue.offer(new Msg.InteruptMsg());
            });

            Signal.handle(new Signal("TERM"), _ -> {
                System.err.println("TERMINATED sent");
                msgQueue.offer(new Msg.QuitMsg());
            });
        }

        Signal.handle(new Signal("WINCH"), _ -> {
            processCmd(() -> {
                TerminalSize terminalSize = terminal.getTerminalSize();
                return new Msg.WindowSizeMsg(terminalSize.height(), terminalSize.width());
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

        File errFile = new File("err.txt");
        FileOutputStream ops;
        try {
            Files.write(errFile.toPath(), "".getBytes(Charset.defaultCharset()));
            ops = new FileOutputStream(errFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        PrintStream ps = new PrintStream(ops, true);
        System.setErr(ps);

        // set initial size inside the renderer
        processCmd(() -> {
            TerminalSize terminalSize = terminal.getTerminalSize();
            return new Msg.WindowSizeMsg(terminalSize.height(), terminalSize.width());
        });

        renderer.start();
        renderer.write(view(model));

        if (opts.input() != null) {
            Thread.startVirtualThread(() -> handleUserInput(opts.input()));
        }

        Model finalModel;
        try {
            finalModel = eventLoop(model);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Event loop interrupted: " + e.getMessage());
            throw new RuntimeException(e);
        }

        // write last frame
        renderer.write(view(finalModel));

        // TODO; call any shutdown hooks

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

    private void handleUserInput(InputStream in) {
        while (true) {
            try {
                int input = in.read();

                if (input == -1) {
                    continue;
                }

                processCmd(() -> new Msg.KeyClickMsg((char) input));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
