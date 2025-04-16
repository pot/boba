package dev.mccue.ansi;

public final class Cursor {
    public static final String SAVE_CURSOR = "\u001B7";
    public static final String RESTORE_CURSOR = "\u001B8";
    public static final String CURSOR_HOME_POSITION = "";
    public static final String RESTORE_CURRENT_CURSOR_POSITION = "\u001B[u";
    public static final String REVERSE_INDEX = "\u001BM";

    public static String cursorUp(int n) {
        String length = n > 1 ? Integer.toString(n) : "";
        return "\u001B[" + length + "A";
    }

    public static String cursorDown(int n) {
        String length = n > 1 ? Integer.toString(n) : "";
        return "\u001B[" + length + "B";
    }

    public static String cursorForward(int n) {
        String length = n > 1 ? Integer.toString(n) : "";
        return "\u001B[" + length + "C";
    }

    public static String cursorBackward(int n) {
        String length = n > 1 ? Integer.toString(n) : "";
        return "\u001B[" + length + "D";
    }

    public static String cursorNextLine(int n) {
        String length = n > 1 ? Integer.toString(n) : "";
        return "\u001B[" + length + "E";
    }

    public static String cursorPrevLine(int n) {
        String length = n > 1 ? Integer.toString(n) : "";
        return "\u001B[" + length + "F";
    }

    public static String cursorHorizontalAbsolute(int n) {
        String length = n > 1 ? Integer.toString(n) : "";
        return "\u001B[" + length + "G";
    }

    public static String cursorPosition(int col, int row) {
        if (col <= 0 && row <= 0) {
            return CURSOR_HOME_POSITION;
        }

        String rowStr = row > 1 ? Integer.toString(row) : "";
        String colStr = col > 1 ? Integer.toString(col) : "";
        return "\u001B[" + rowStr + ";" + colStr + "H";
    }

    public static String cursorHorizontalForwardTab(int n) {
        String length = n > 1 ? Integer.toString(n) : "";
        return "\u001B[" + length + "I";
    }

    public static String eraseCharacter(int n) {
        String length = n > 1 ? Integer.toString(n) : "";
        return "\u001B[" + length + "X";
    }

    public static String cursorBackwardTab(int n) {
        String length = n > 1 ? Integer.toString(n) : "";
        return "\u001B[" + length + "Z";
    }

    public static String verticalPositionAbsolute(int n) {
        String length = n > 1 ? Integer.toString(n) : "";
        return "\u001B[" + length + "d";
    }

    public static String verticalPositionRelative(int n) {
        String length = n > 1 ? Integer.toString(n) : "";
        return "\u001B[" + length + "e";
    }

    public static String setCursorStyle(int style) {
        String styleStr = style > 1 ? Integer.toString(style) : "";
        return "\u001B[" + styleStr + "q";
    }

    public static String setPointerShape(String shape) {
        return "\u001B]22;" + shape + "\u0007";
    }

    public static String horizontalPositionAbsolute(int n) {
        String length = n > 1 ? Integer.toString(n) : "";
        return "\u001B[" + length + "`";
    }

    public static String horizontalPositionRelative(int n) {
        String length = n > 1 ? Integer.toString(n) : "";
        return "\u001B[" + length + "a";
    }
}
