package dev.mccue.ansi.parser;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/// Terminal sequence containing a single unicode codepoint.
///
/// This should be a printable character.
public final class Rune extends TerminalSequence {
    private final int value;

    private Rune(int value) {
        this.value = value;
    }

    public static Rune of(int value) {
        return new Rune(value);
    }

    @Override
    public String toString() {
        return new String(Character.toChars(value));
    }

    @Override
    public void writeTo(OutputStream os) throws IOException {
        os.write(toString().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Rune r && value == r.value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
