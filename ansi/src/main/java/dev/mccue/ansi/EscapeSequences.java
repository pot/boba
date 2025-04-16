package dev.mccue.ansi;


import java.util.EnumSet;


/// Collection of ANSI escape sequences
@Source("https://github.com/charmbracelet/x/blob/main/ansi/background.go")
@Source("https://github.com/charmbracelet/x/blob/main/ansi/cursor.go")
@Source("https://github.com/charmbracelet/x/blob/main/ansi/ctrl.go")
@Source("https://github.com/charmbracelet/x/blob/main/ansi/ascii.go")
@Source("https://github.com/charmbracelet/x/blob/main/ansi/title.go")
@Source("https://github.com/charmbracelet/x/blob/main/ansi/kitty.go")
public final class EscapeSequences {
    private EscapeSequences() {}

    /// saveCursor (DECSC) is an escape sequence that saves the current cursor
    /// position.
    ///
    ///	ESC 7
    ///
    /// @see <a href="https://vt100.net/docs/vt510-rm/DECSC.html">https://vt100.net/docs/vt510-rm/DECSC.html</a>
    /// @return An escape sequence that saves the current cursor position.
    public static String saveCursor() {
        return "\u001b7";
    }

    /// restoreCursor (DECRC) is an escape sequence that restores the cursor
    /// position.
    ///
    ///	ESC 8
    ///
    /// @see <a href="https://vt100.net/docs/vt510-rm/DECRC.html">https://vt100.net/docs/vt510-rm/DECRC.html</a>
    /// @return An escape sequence that restores the cursor position.
    public static String restoreCursor() {
        return "\u001b8";
    }

    /// requestCursorPosition (CPR) is an escape sequence that requests the current
    /// cursor position.
    ///
    ///	CSI 6 n
    ///
    /// The terminal will report the cursor position as a CSI sequence in the
    /// following format:
    ///
    ///	CSI Pl ; Pc R
    ///
    /// Where Pl is the line number and Pc is the column number.
    /// @see <a href="https://vt100.net/docs/vt510-rm/CPR.html">https://vt100.net/docs/vt510-rm/CPR.html</a>
    /// @return an escape sequence that requests the current cursor position.
    public static String requestCursorPosition() {
        return "\u001b[6n";
    }

    /// requestExtendedCursorPosition (DECXCPR) is a sequence for requesting the
    /// cursor position report including the current page number.
    ///
    ///	CSI ? 6 n
    ///
    /// The terminal will report the cursor position as a CSI sequence in the
    /// following format:
    ///
    ///	CSI ? Pl ; Pc ; Pp R
    ///
    /// Where Pl is the line number, Pc is the column number, and Pp is the page
    /// number.
    /// @see <a href="https://vt100.net/docs/vt510-rm/DECXCPR.html">https://vt100.net/docs/vt510-rm/DECXCPR.html</a>
    /// @return A sequence for requesting the cursor position report including the current page number.
    public static String requestExtendedCursorPosition() {
        return "\u001b[?6n";
    }

    /// cursorUp (CUU) returns a sequence for moving the cursor up n cells.
    ///
    ///	CSI n A
    ///
    /// See: <a href="https://vt100.net/docs/vt510-rm/CUU.html">https://vt100.net/docs/vt510-rm/CUU.html</a>
    /// @return A sequence for moving the cursor up n cells.
    public static String cursorUp(int n)  {
        String s = "";
        if (n > 1) {
            s = Integer.toString(n);
        }
        return "\u001b[" + s + "A";
    }

    /// Equivalent to `cursorUp(1)`
    /// @return A sequence for moving the cursor up n cells.
    public static String cursorUp() {
        return cursorUp(1);
    }

    // CursorDown (CUD) returns a sequence for moving the cursor down n cells.
    //
    //	CSI n B
    //
    // See: https://vt100.net/docs/vt510-rm/CUD.html
    public static String cursorDown(int n) {
        String s = "";
        if (n > 1) {
            s = Integer.toString(n);
        }
        return "\u001b[" + s + "B";
    }

    // CursorDown1 is a sequence for moving the cursor down one cell.
    //
    // This is equivalent to CursorDown(1).
    public static String cursorDown() {
        return cursorDown(1);
    };

    // CursorRight (CUF) returns a sequence for moving the cursor right n cells.
    //
    //	CSI n C
    //
    // See: https://vt100.net/docs/vt510-rm/CUF.html
    public static String cursorRight(int n) {
        String s = "";
        if (n > 1) {
            s = Integer.toString(n);
        }
        return "\u001b[" + s + "C";
    }

    // CursorRight1 is a sequence for moving the cursor right one cell.
    //
    // This is equivalent to CursorRight(1).
    public static String cursorRight() {
        return cursorRight(1);
    }

    // CursorLeft (CUB) returns a sequence for moving the cursor left n cells.
    //
    //	CSI n D
    //
    // See: https://vt100.net/docs/vt510-rm/CUB.html
    public static String cursorLeft(int n) {
        String s = "";
        if (n > 1) {
            s = Integer.toString(n);
        }
        return "\u001b[" + s + "D";
    }

    // CursorLeft1 is a sequence for moving the cursor left one cell.
    //
    // This is equivalent to CursorLeft(1).
    public static String cursorLeft() {
        return cursorLeft(1);
    }

    // CursorNextLine (CNL) returns a sequence for moving the cursor to the
    // beginning of the next line n times.
    //
    //	CSI n E
    //
    // See: https://vt100.net/docs/vt510-rm/CNL.html
    public static String cursorNextLine(int n) {
        String s = "";
        if (n > 1) {
            s = Integer.toString(n);
        }
        return "\u001b[" + s + "E";
    }

    // cursorPreviousLine (CPL) returns a sequence for moving the cursor to the
    // beginning of the previous line n times.
    //
    //	CSI n F
    //
    // See: https://vt100.net/docs/vt510-rm/CPL.html
    public static String cursorPreviousLine(int n) {
        String s = "";
        if (n > 1) {
            s = Integer.toString(n);
        }
        return "\u001b[" + s + "F";
    }

    /// moveCursor (CUP) returns a sequence for moving the cursor to the given row
    /// and column.
    ///
    ///	CSI n ; m H
    ///
    /// @see <a href="https://vt100.net/docs/vt510-rm/CUP.html">https://vt100.net/docs/vt510-rm/CUP.html</a>
    /// @return A sequence for moving the cursor to the given row and column.
    public static String moveCursor(int row, int col) {
        if (row < 0) {
            row = 0;
        }
        if (col < 0) {
            col = 0;
        }
        return "\u001b[" + row + ";" + col + "H";
    }

    // MoveCursorOrigin is a sequence for moving the cursor to the upper left
    // corner of the screen. This is equivalent to MoveCursor(1, 1).
    public static String moveCursorOrigin() {
        return "\u001b[1;1H";
    }

    // SaveCursorPosition (SCP or SCOSC) is a sequence for saving the cursor
    // position.
    //
    //	CSI s
    //
    // This acts like Save, except the page number where the cursor is located is
    // not saved.
    //
    // See: https://vt100.net/docs/vt510-rm/SCOSC.html
    public static String saveCursorPosition() {
        return "\u001b[s";
    }

    // RestoreCursorPosition (RCP or SCORC) is a sequence for restoring the cursor
    // position.
    //
    //	CSI u
    //
    // This acts like Restore, except the cursor stays on the same page where the
    // cursor was saved.
    //
    // See: https://vt100.net/docs/vt510-rm/SCORC.html
    public static String restoreCursorPosition() {
        return "\u001b[u";
    }

    // SetCursorStyle (DECSCUSR) returns a sequence for changing the cursor style.
    //
    //	CSI Ps SP q
    //
    // Where Ps is the cursor style:
    //
    //	0: Blinking block
    //	1: Blinking block (default)
    //	2: Steady block
    //	3: Blinking underline
    //	4: Steady underline
    //	5: Blinking bar (xterm)
    //	6: Steady bar (xterm)
    //
    // See: https://vt100.net/docs/vt510-rm/DECSCUSR.html
    // See: https://invisible-island.net/xterm/ctlseqs/ctlseqs.html#h4-Functions-using-CSI-_-ordered-by-the-final-character-lparen-s-rparen:CSI-Ps-SP-q.1D81
    public static String setCursorStyle(int style) {
        if (style < 0) {
            style = 0;
        }
        return "\u001b[" + style + " q";
    }

    // SetPointerShape returns a sequence for changing the mouse pointer cursor
    // shape. Use "default" for the default pointer shape.
    //
    //	OSC 22 ; Pt ST
    //	OSC 22 ; Pt BEL
    //
    // Where Pt is the pointer shape name. The name can be anything that the
    // operating system can understand. Some common names are:
    //
    //   - copy
    //   - crosshair
    //   - default
    //   - ew-resize
    //   - n-resize
    //   - text
    //   - wait
    //
    // See: https://invisible-island.net/xterm/ctlseqs/ctlseqs.html#h2-Operating-System-Commands
    public static String setPointerShape(String shape) {
        return "\u001b]22;" + shape + "\u0007";
    }

    /// requestXTVersion is a control sequence that requests the terminal's XTVERSION. It responds with a DSR sequence identifying the version.
    ///
    ///	CSI > Ps q
    ///	DCS > | text ST
    ///
    /// @see <a href="https://invisible-island.net/xterm/ctlseqs/ctlseqs.html#h3-PC-Style-Function-Keys">https://invisible-island.net/xterm/ctlseqs/ctlseqs.html#h3-PC-Style-Function-Keys</a>
    /// @return A control sequence that requests the terminal's XTVERSION.
    public static String requestXTVersion() {
        return "\u001b[>0q";
    }

    /// requestPrimaryDeviceAttributes is a control sequence that requests the
    /// terminal's primary device attributes (DA1).
    ///
    ///	CSI c
    ///
    /// @see <a href="https://vt100.net/docs/vt510-rm/DA1.html">https://vt100.net/docs/vt510-rm/DA1.html</a>
    /// @return A control sequence that requests the terminal's primary device attributes (DA1).
    public static String requestPrimaryDeviceAttributes() {
        return "\u001b[c";
    }
/*
    /// setForegroundColor returns a sequence that sets the default terminal
    /// foreground color.
    ///
    ///	OSC 10 ; color ST
    ///	OSC 10 ; color BEL
    ///
    /// Where color is the encoded color number.
    ///
    /// @see <a href="https://invisible-island.net/xterm/ctlseqs/ctlseqs.html#h3-Operating-System-Commands">https://invisible-island.net/xterm/ctlseqs/ctlseqs.html#h3-Operating-System-Commands</a>
    /// @return A sequence that sets the default terminal foreground color.
    public static String setForegroundColor(TerminalColor c) {
        switch (c) {
            case ANSIColor ansiColor -> {
                return "\u001b]10;" + ANSI256Color.ansiHex.get(ansiColor.ordinal());
            }
            case ANSI256Color ansi256Color -> {
                return "\u001b]10;" + ANSI256Color.ansiHex.get(ansi256Color.value());
            }
            case TrueColor trueColor -> {
                return "\u001b]10;" + trueColor.hex();
            }
        }
    }

    /// requestForegroundColor is a sequence that requests the current default
    /// terminal foreground color.
    ///
    /// @see <a href="https://invisible-island.net/xterm/ctlseqs/ctlseqs.html#h3-Operating-System-Commands">https://invisible-island.net/xterm/ctlseqs/ctlseqs.html#h3-Operating-System-Commands</a>
    /// @return A sequence that requests the current default terminal foreground color.
    public static String requestDefaultForegroundColor() {
        return "\u001b]10;?\u0007";
    }

    /// resetForegroundColor is a sequence that resets the default terminal
    /// foreground color.
    ///
    /// @see <a href="https://invisible-island.net/xterm/ctlseqs/ctlseqs.html#h3-Operating-System-Commands">https://invisible-island.net/xterm/ctlseqs/ctlseqs.html#h3-Operating-System-Commands</a>
    /// @return A sequence that resets the default terminal foreground color.
    public static String resetDefaultForegroundColor() {
        return "\u001b]110\u0007";
    }

    /// setBackgroundColor returns a sequence that sets the default terminal
    /// background color.
    ///
    ///	OSC 11 ; color ST
    ///	OSC 11 ; color BEL
    ///
    /// Where color is the encoded color number.
    ///
    /// @see <a href="https://invisible-island.net/xterm/ctlseqs/ctlseqs.html#h3-Operating-System-Commands">https://invisible-island.net/xterm/ctlseqs/ctlseqs.html#h3-Operating-System-Commands</a>
    /// @return A sequence that sets the default terminal background color.
    public static String setDefaultBackgroundColor(TerminalColor c) {
        switch (c) {
            case ANSIColor ansiColor -> {
                return "\u001b]11;" + ANSI256Color.ansiHex.get(ansiColor.ordinal());
            }
            case ANSI256Color ansi256Color -> {
                return "\u001b]11;" + ANSI256Color.ansiHex.get(ansi256Color.value());
            }
            case TrueColor trueColor -> {
                return "\u001b]11;" + trueColor.hex();
            }
        }
    }

    // RequestBackgroundColor is a sequence that requests the current default
    // terminal background color.
    //
    // See: https://invisible-island.net/xterm/ctlseqs/ctlseqs.html#h3-Operating-System-Commands
    static String requestBackgroundColor() {
        return "\u001b]11;?\u0007";
    }

    // ResetBackgroundColor is a sequence that resets the default terminal
    // background color.
    //
    // See: https://invisible-island.net/xterm/ctlseqs/ctlseqs.html#h3-Operating-System-Commands
    static String resetBackgroundColor() {
        return "\u001b]111\u0007";
    }

    // SetCursorColor returns a sequence that sets the terminal cursor color.
    //
    //	OSC 12 ; color ST
    //	OSC 12 ; color BEL
    //
    // Where color is the encoded color number.
    //
    // See: https://invisible-island.net/xterm/ctlseqs/ctlseqs.html#h3-Operating-System-Commands
    static String setCursorColor(TerminalColor c) {
        return "\u001b]12;" + c.toHexString() + "\u0007";
    }

    // RequestCursorColor is a sequence that requests the current terminal cursor
    // color.
    //
    // See: https://invisible-island.net/xterm/ctlseqs/ctlseqs.html#h3-Operating-System-Commands
    static String requestCursorColor() {
        return "\u001b]12;?\u0007";
    }

    // ResetCursorColor is a sequence that resets the terminal cursor color.
    //
    // See: https://invisible-island.net/xterm/ctlseqs/ctlseqs.html#h3-Operating-System-Commands
    static String resetCursorColor() {
        return "\u001b]112\u0007";
    }
*/

    // SetIconNameWindowTitle returns a sequence for setting the icon name and
    // window title.
    //
    //	OSC 0 ; title ST
    //	OSC 0 ; title BEL
    //
    // See: https://invisible-island.net/xterm/ctlseqs/ctlseqs.html#h2-Operating-System-Commands
    public static String setIconNameWindowTitle(String s) {
        return "\u001b]0;" + s + "\u0007";
    }

    /// setIconName returns a sequence for setting the icon name.
    ///
    ///	OSC 1 ; title ST
    ///	OSC 1 ; title BEL
    ///
    /// @see <a href="https://invisible-island.net/xterm/ctlseqs/ctlseqs.html#h2-Operating-System-Commands">https://invisible-island.net/xterm/ctlseqs/ctlseqs.html#h2-Operating-System-Commands</a>
    /// @return A sequence for setting the icon name.
    public static String setIconName(String s) {
        return "\u001b]1;" + s + "\u0007";
    }

    // SetWindowTitle returns a sequence for setting the window title.
    //
    //	OSC 2 ; title ST
    //	OSC 2 ; title BEL
    //
    // See: https://invisible-island.net/xterm/ctlseqs/ctlseqs.html#h2-Operating-System-Commands
    public static String setWindowTitle(String s) {
        return "\u001b]2;" + s + "\u0007";
    }

    /// RequestKittyKeyboard is a sequence to request the terminal Kitty keyboard
    /// protocol enabled flags.
    ///
    /// @see <a href="https://sw.kovidgoyal.net/kitty/keyboard-protocol/">https://sw.kovidgoyal.net/kitty/keyboard-protocol/</a>
    /// @return A sequence to request the terminal Kitty keyboard protocol enabled flags.
    @Kitty
    public static String requestKittyKeyboard() {
        return "\u001b[?u";
    }

    /// kittyKeyboard returns a sequence to request keyboard enhancements from the terminal.
    /// The flags argument is a bitmask of the Kitty keyboard protocol flags. While
    /// mode specifies how the flags should be interpreted.
    ///
    /// @see <a href="https://sw.kovidgoyal.net/kitty/keyboard-protocol/#progressive-enhancement">https://sw.kovidgoyal.net/kitty/keyboard-protocol/#progressive-enhancement</a>
    /// @return A sequence to request keyboard enhancements from the terminal.
    @Kitty
    public static String kittyKeyboard(EnumSet<KittyFlag> flags, KittyMode mode) {
        return "\u001b[=" + KittyFlag.valueFor(flags) + ";" + mode.value + "u";
    }

    /// pushKittyKeyboard returns a sequence to push the given flags to the terminal
    /// Kitty Keyboard stack.
    ///
    ///	CSI > flags u
    ///
    /// @see <a href="https://sw.kovidgoyal.net/kitty/keyboard-protocol/#progressive-enhancement">https://sw.kovidgoyal.net/kitty/keyboard-protocol/#progressive-enhancement</a>
    /// @return A sequence to push the given flags to the terminal Kitty Keyboard stack.
    @Kitty
    public static String pushKittyKeyboard(EnumSet<KittyFlag> flags) {
        var f = "";
        var value = KittyFlag.valueFor(flags);
        if (value > 0) {
            f = Integer.toString(value);
        }

        return "\u001b[>" + f + "u";
    }

    // DisableKittyKeyboard is a sequence to push zero into the terminal Kitty
    // Keyboard stack to disable the protocol.
    //
    // This is equivalent to PushKittyKeyboard(0).
    @Kitty
    public static String disableKittyKeyboard() {
        return "\u001b[>0u";
    }

    /// popKittyKeyboard returns a sequence to pop n number of flags from the
    /// terminal Kitty Keyboard stack.
    ///
    ///	CSI < flags u
    ///
    /// @see <a href="https://sw.kovidgoyal.net/kitty/keyboard-protocol/#progressive-enhancement">https://sw.kovidgoyal.net/kitty/keyboard-protocol/#progressive-enhancement</a>
    /// @return A sequence to pop n number of flags from the terminal Kitty Keyboard stack.
    @Kitty
    public static String popKittyKeyboard(int n) {
        var num = "";
        if (n > 0) {
            num = Integer.toString(n);
        }

        return "\u001b[<" + num + "u";
    }
}
