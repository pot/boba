package dev.weisz.ansi.parser;

import dev.weisz.wcwidth.WCWidth;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.text.BreakIterator;

public final class Width {
    private Width() {}

    static int utf8ByteLen(int b) {
        if (b <= 0b0111_1111) { // 0x00-0x7F
            return 1;
        } else if (b >= 0b1100_0000 && b <= 0b1101_1111) { // 0xC0-0xDF
            return 2;
        } else if (b >= 0b1110_0000 && b <= 0b1110_1111) { // 0xE0-0xEF
            return 3;
        } else if (b >= 0b1111_0000 && b <= 0b1111_0111) { // 0xF0-0xF7
            return 4;
        }
        return -1;
    }

    public static String hardWrap(String s, int limit, boolean preserveSpace) {
        var bi = BreakIterator.getCharacterInstance();
        bi.setText(s);

        int start = bi.first();
        for (int end = bi.next();
             end != BreakIterator.DONE;
             start = end, end = bi.next()) {
        }

        return "";
    }

    static byte[] toInvalidUtf8(String s) {
        var baos = new ByteArrayOutputStream();
        s.codePoints().forEach(codePoint -> {
            if (codePoint == 0x9D || codePoint == 0x9B) {
                baos.write((byte) codePoint);
            }
            else {
                try {
                    baos.write(
                            new String(Character.toChars(codePoint)).getBytes(StandardCharsets.UTF_8)
                    );
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        });

        return baos.toByteArray();
    }

    public static String strip(String str) {
        var s = toInvalidUtf8(str);
        var buf = new ByteArrayOutputStream();
        int ri = 0;
        int rw = 0;
        var pstate = State.GROUND;

        for (byte b : s) {
            if (pstate == State.UTF8) {
                // During this state, collect rw bytes to form a valid rune in the
                // buffer. After getting all the rune bytes into the buffer,
                // transition to GroundState and reset the counters.
                buf.write(b);
                ri++;
                if (ri < rw) {
                    continue;
                }
                pstate = State.GROUND;
                ri = 0;
                rw = 0;
                continue;
            }

            var transition = TransitionTable.table.Transition(pstate, Byte.toUnsignedInt(b));
            var state = transition.state();
            var action = transition.action();
            switch (action) {
                case Action.COLLECT -> {
                    if (state == State.UTF8) {
                        // This action happens when we transition to the Utf8State.
                        rw = utf8ByteLen(Byte.toUnsignedInt(b));
                        buf.write(b);
                        ri++;
                    }
                }

                case Action.PRINT, Action.EXECUTE -> {
                    // collects printable ASCII and non-printable characters
                    buf.write(b);
                }

            }

            pstate = state;
        }

        return buf.toString(StandardCharsets.UTF_8);
    }

    public static int width(String str) {
        int width = 0;

        var bi = BreakIterator.getCharacterInstance();
        String strippedText = strip(str);
        bi.setText(strippedText);
        int start = bi.first();
        for (int end = bi.next();
             end != BreakIterator.DONE;
             start = end, end = bi.next()) {
            var s = strippedText.substring(start, end);
            int w = WCWidth.of(s.codePoints().findFirst().orElseThrow());
            if (w == -1) {
                w = 0; // idk if this a bad thing to do
            }
            width += w;
        }
        return width;
    }
}
