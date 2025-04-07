package dev.mccue.boba;

import dev.mccue.ansi.C0;
import dev.mccue.ansi.Cursor;
import dev.mccue.ansi.Mode;
import dev.mccue.ansi.Screen;
import dev.mccue.ansi.parser.Width;
import dev.mccue.boba.tea.Msg;
import dev.mccue.wcwidth.WCWidth;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StandardRenderer implements Renderer {
    private final OutputStream out;
    private ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    private String[] queuedMessageLines;
    private boolean altScreen = false;
    private boolean cursorHidden = false; // no idea what this should be by default TODO
    private int linesRendered = 0;
    private int altLinesRendered = 0;
    private String lastRender; // used to check for any differences in the render
    private String[] lastRenderedLines;
    private boolean bracketedPaste = false;
    private boolean reportFocus;

    private int height;
    private int width;

    public StandardRenderer(OutputStream out) {
        this.out = out;
    }

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

            if (altScreen) {
                buffer.writeBytes(Cursor.CURSOR_HOME_POSITION.getBytes(StandardCharsets.UTF_8));
            } else if (linesRendered > 1) {
                buffer.writeBytes(Cursor.cursorUp(linesRendered - 1).getBytes(StandardCharsets.UTF_8));
            }

            List<String> newLines = Arrays.stream(bufferStr.split("\n")).toList();
            if (height > 0 && newLines.size() > height) {
                newLines = newLines.subList(newLines.size() - height, newLines.size());
            }

            boolean flushQueuedMessages = queuedMessageLines.length > 0 && !altScreen;
            if (flushQueuedMessages) {
                for (String line : queuedMessageLines) {
                    if (Width.width(line) < width) {
                        line += Screen.ERASE_LINE_RIGHT;
                    }

                    buffer.writeBytes(line.getBytes(StandardCharsets.UTF_8));
                    buffer.writeBytes("\r\n".getBytes(StandardCharsets.UTF_8));
                }

                queuedMessageLines = new String[]{};
            }

            for (int i = 0; i < newLines.size(); i++) {
                boolean canSkip = !flushQueuedMessages && lastRenderedLines.length > i && lastRenderedLines[i].equalsIgnoreCase(newLines.get(i));

                // TODO: ignore lines handling
                if (i == 0 && lastRender.isEmpty()) {
                    buffer.writeBytes("\r".getBytes(StandardCharsets.UTF_8));
                }

                String line = newLines.get(i);
            }
        }
    }

    @Override
    public void start() {

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
        lastRenderedLines = null;
    }

    @Override
    public void clearScreen() {
        synchronized (this) {
            // TODO: import ansi
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
}
