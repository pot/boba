package dev.mccue.ansi.parser;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public final class EscSequence extends TerminalSequence {
    private final int value;

    private EscSequence(int value) {
        this.value = value;
    }

    public static EscSequence of(int value) {
        return new EscSequence(value);
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
        return obj instanceof EscSequence r && value == r.value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
