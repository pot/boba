package dev.weisz.boba;

import dev.weisz.boba.tea.Msg;
import dev.weisz.boba.terminal.WinSize;

public interface Renderer {
    // Start the renderer.
    void start();

    // Stop the renderer, but render the final frame in the buffer, if any.
    void stop();

    // Stop the renderer without doing any final rendering.
    void kill();

    // Write a frame to the renderer. The renderer can write this data to
    // output at its discretion.
    void write(String frame);

    // Request a full re-render. Note that this will not trigger a render
    // immediately. Rather, this method causes the next render to be a full
    // repaint. Because of this, it's safe to call this method multiple times
    // in succession.
    void repaint();

    // Clears the terminal.
    void clearScreen();

    // Whether or not the alternate screen buffer is enabled.
    boolean altScreen();
    // Enable the alternate screen buffer.
    void enterAltScreen();
    // Disable the alternate screen buffer.
    void exitAltScreen();

    // Show the cursor.
    void showCursor();
    // Hide the cursor.
    void hideCursor();

    // enableMouseCellMotion enables mouse click, release, wheel and motion
    // events if a mouse button is pressed (i.e., drag events).
    void enableMouseCellMotion();

    // disableMouseCellMotion disables Mouse Cell Motion tracking.
    void disableMouseCellMotion();

    // enableMouseAllMotion enables mouse click, release, wheel and motion
    // events, regardless of whether a mouse button is pressed. Many modern
    // terminals support this, but not all.
    void enableMouseAllMotion();

    // disableMouseAllMotion disables All Motion mouse tracking.
    void disableMouseAllMotion();

    // enableMouseSGRMode enables mouse extended mode (SGR).
    void enableMouseSGRMode();

    // disableMouseSGRMode disables mouse extended mode (SGR).
    void disableMouseSGRMode();

    // enableBracketedPaste enables bracketed paste, where characters
    // inside the input are not interpreted when pasted as a whole.
    void enableBracketedPaste();

    // disableBracketedPaste disables bracketed paste.
    void disableBracketedPaste();

    // bracketedPasteActive reports whether bracketed paste mode is
    // currently enabled.
    boolean bracketedPasteActive();

    // Sets the window title.
    void setWindowTitle(String title);

    // reportFocus reports whether focus reporting is enabled.
    boolean reportFocus();

    // enableReportFocus enables focus reporting.
    void enableReportFocus();

    // disableReportFocus disables focus reporting.
    void disableReportFocus();

    // handle any internal messages like update window size ect
    void handleMessages(Msg msg);

    WinSize getWinSize();
}
