package dev.weisz.boba;

import dev.weisz.ansi.*;
import dev.weisz.ansi.parser.Width;
import dev.weisz.boba.tea.Msg;
import dev.weisz.boba.terminal.WinSize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class StandardRenderer implements Renderer {
    private static final Logger LOGGER = LoggerFactory.getLogger(StandardRenderer.class);

    private final OutputStream out;
    private final int fps;

    private Timer timer; // timer that calls the flush function (started based on fps)
    private final AtomicBoolean running = new AtomicBoolean(false); // ensure we only stop the timer once

    // set via the update received msg
    private int height;
    private int width;

    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    private boolean altScreen = false;
    private boolean cursorHidden = false; // default value doesn't matter since it's set initially in the Program
    private boolean bracketedPaste = false;
    private boolean reportFocus;

    private int linesRendered = 0;
    private int altLinesRendered = 0;
    private String lastRender = ""; // used to check for any differences in the render
    private List<String> lastRenderedLines = new ArrayList<>();

    public StandardRenderer(OutputStream out, int fps) {
        this.out = out;
        this.fps = fps;
    }

    /**
     * Immediately writes a command/string sequence to the output buffer.
     *
     * @param seq the string sequence that will be written
     */
    private void execute(String seq) {
        try {
            out.write(seq.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // flushes the current buffer to the output stream
    private void flush() {
        synchronized (this) {
            String bufferStr = buffer.toString();
            if (buffer.size() <= 0 || bufferStr.equals(lastRender)) {
                return;
            }

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            if (altScreen) {
                buffer.writeBytes(Cursor.CURSOR_HOME_POSITION.getBytes(StandardCharsets.UTF_8));
            } else if (linesRendered > 1) {
                buffer.writeBytes(Cursor.cursorUp(linesRendered - 1).getBytes(StandardCharsets.UTF_8));
            }

            List<String> newLines = Arrays.stream(bufferStr.split("\n")).toList();
            if (height > 0 && newLines.size() > height) {
                newLines = newLines.subList(newLines.size() - height, newLines.size());
            }

            for (int i = 0; i < newLines.size(); i++) {
                if (i == 0 && lastRender.isEmpty()) {
                    buffer.writeBytes("\r".getBytes(StandardCharsets.UTF_8));
                }

                String line = newLines.get(i);
                if (width > 0) {
                    line = Truncate.truncate(line, width, "");
                }

                if (Width.width(line) < width) {
                    line += Screen.ERASE_LINE_RIGHT;
                }

                // we need to truncate some stuff here
                buffer.writeBytes(line.getBytes(StandardCharsets.UTF_8));

                if (i < newLines.size() - 1) {
                    buffer.writeBytes("\r\n".getBytes(StandardCharsets.UTF_8));
                }
            }

            if (lastRenderedLines.size() > newLines.size()) {
                buffer.writeBytes(Screen.ERASE_SCREEN_BELOW.getBytes(StandardCharsets.UTF_8));
            }

            if (altScreen) {
                buffer.writeBytes(Cursor.cursorPosition(0, newLines.size()).getBytes(StandardCharsets.UTF_8));
                altLinesRendered = newLines.size();
            } else {
                buffer.writeBytes("\r".getBytes(StandardCharsets.UTF_8));
                linesRendered = newLines.size();
            }

            try {
                out.write(buffer.toByteArray());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            lastRender = buffer.toString();
            lastRenderedLines = newLines;
            this.buffer.reset();
        }
    }

    @Override
    public void start() {
        if (timer == null) {
            timer = new Timer();
        }

        running.set(true);

        timer.scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        if (!running.get()) {
                            timer.cancel();
                            return;
                        }

                        flush();
                    }
                },
                0,
                1000 / fps
        );

        LOGGER.info("Standard Renderer started with a new frame being rendered every {}ms.", 1000 / fps);
    }

    @Override
    public void stop() {

    }

    @Override
    public void kill() {

    }

    @Override
    public void write(String frame) {
        synchronized (this) {
            buffer.reset();
            buffer.writeBytes((frame.isEmpty() ? " " : frame).getBytes(StandardCharsets.UTF_8));
        }
    }

    @Override
    public void repaint() {
        lastRender = "";
        lastRenderedLines = new ArrayList<>();
    }

    @Override
    public void clearScreen() {
        synchronized (this) {
            execute(Screen.ERASE_ENTIRE_SCREEN);
            execute(Cursor.CURSOR_HOME_POSITION);

            repaint();
        }
    }

    @Override
    public boolean altScreen() {
        synchronized (this) {
            return altScreen;
        }
    }

    @Override
    public void enterAltScreen() {
        synchronized (this) {
            if (altScreen) {
                return;
            }

            altScreen = true;
            execute(Mode.SET_ALT_SCREEN_SAVE_CURSOR_MODE);

            clearScreen();

            if (cursorHidden) {
                hideCursor();
            } else {
                showCursor();
            }

            altLinesRendered = 0;
            repaint();
        }
    }

    @Override
    public void exitAltScreen() {
        synchronized (this) {
            if (!altScreen) {
                return;
            }

            altScreen = false;
            execute(Mode.RESET_ALT_SCREEN_SAVE_CURSOR_MODE);

            if (cursorHidden) {
                hideCursor();
            } else {
                showCursor();
            }

            repaint();
        }
    }

    @Override
    public void showCursor() {
        synchronized (this) {
            cursorHidden = false;
            execute(Mode.SET_TEXT_CURSOR_ENABLE_MODE);
        }
    }

    @Override
    public void hideCursor() {
        synchronized (this) {
            cursorHidden = true;
            execute(Mode.RESET_TEXT_CURSOR_ENABLE_MODE);
        }
    }

    @Override
    public void enableMouseCellMotion() {
        synchronized (this) {
            execute(Mode.SET_BUTTON_EVENT_MOUSE_MODE);
        }
    }

    @Override
    public void disableMouseCellMotion() {
        synchronized (this) {
            execute(Mode.RESET_BUTTON_EVENT_MOUSE_MODE);
        }
    }

    @Override
    public void enableMouseAllMotion() {
        synchronized (this) {
            execute(Mode.SET_ANY_EVENT_MOUSE_MODE);
        }
    }

    @Override
    public void disableMouseAllMotion() {
        synchronized (this) {
            execute(Mode.RESET_ANY_EVENT_MOUSE_MODE);
        }
    }

    @Override
    public void enableMouseSGRMode() {
        synchronized (this) {
            execute(Mode.SET_SGR_EXT_MOUSE_MODE);
        }
    }

    @Override
    public void disableMouseSGRMode() {
        synchronized (this) {
            execute(Mode.RESET_SGR_EXT_MOUSE_MODE);
        }
    }

    @Override
    public void enableBracketedPaste() {
        synchronized (this) {
            bracketedPaste = true;
            execute(Mode.SET_BRACKETED_PASTE_MODE);
        }
    }

    @Override
    public void disableBracketedPaste() {
        synchronized (this) {
            bracketedPaste = false;
            execute(Mode.RESET_BRACKETED_PASTE_MODE);
        }
    }

    @Override
    public boolean bracketedPasteActive() {
        synchronized (this) {
            return bracketedPaste;
        }
    }

    @Override
    public void setWindowTitle(String title) {
        synchronized (this) {
            execute(Screen.setWindowTitle(title));
        }
    }

    @Override
    public boolean reportFocus() {
        synchronized (this) {
            return reportFocus;
        }
    }

    @Override
    public void enableReportFocus() {
        synchronized (this) {
            reportFocus = true;
            execute(Mode.SET_FOCUS_EVENT_MODE);
        }
    }

    @Override
    public void disableReportFocus() {
        synchronized (this) {
            reportFocus = false;
            execute(Mode.RESET_FOCUS_EVENT_MODE);
        }
    }

    @Override
    public void handleMessages(Msg msg) {
        switch (msg) {
            case Msg.WindowSizeMsg(int w, int h) -> {
                synchronized (this) {
                    this.width = w;
                    this.height = h;

                    repaint();
                }
            }

            case Msg.RepaintMsg() -> {
                synchronized (this) {
                    repaint();
                }
            }

            case Msg.ClearScrollAreaMsg() -> {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            case Msg.PrintLineMsg(String line) -> write(line);

            default -> {}
        }
    }

    @Override
    public WinSize getWinSize() {
        return new WinSize(height, width);
    }
}
