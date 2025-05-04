package dev.weisz.ansi;

import dev.weisz.ansi.parser.Width;

public class Truncate {

    public static String truncate(String str, int length, String tail) {
        int strWidth = Width.width(str);
        if (strWidth <= length) {
            return str;
        }

        int tailWidth = Width.width(tail);
        length -= tailWidth;
        if (length < 0) {
            return "";
        }

        return str; // MAJOR TODO: actually implement this
    }

    public static void test(String s) {

    }
}
