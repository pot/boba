package dev.mccue.ansi.parser;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

// ControlCode represents a control code character. This is a character that
// is not printable and is used to control the terminal. This would be a
// character in the C0 or C1 set in the range of 0x00-0x1F and 0x80-0x9F.
public final class ControlCode extends TerminalSequence {
    private final byte value;

    private ControlCode(byte value) {
        this.value = value;
    }

    public static ControlCode of(byte value) {
        return new ControlCode(value);
    }

    public static ControlCode of(int value) {
        return new ControlCode((byte) value);
    }


    @Override
    public String toString() {
        return String.valueOf((char) value);
    }

    @Override
    public void writeTo(OutputStream os) throws IOException {
        os.write(toString().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ControlCode r && value == r.value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}

