package dev.weisz.ansi;

public final class Screen {
    public static final String ERASE_SCREEN_BELOW = "\u001B[J";
    public static final String ERASE_SCREEN_ABOVE = "\u001B[1J";
    public static final String ERASE_ENTIRE_SCREEN = "\u001B[2J";
    public static final String ERASE_ENTIRE_DISPLAY = "\u001B[3J";

    public static final String ERASE_LINE_RIGHT = "\u001B[0K";
    public static final String ERASE_LINE_LEFT = "\u001B[1K";
    public static final String ERASE_ENTIRE_LINE = "\u001B[2K";

    public static String setWindowTitle(String title) {
        return "\u001B]2;" + title + "\u0007";
    }
}
