package dev.mccue.ansi.parser;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public final class SosSequence extends TerminalSequence {
    private final byte[] value;

    private SosSequence(byte[] value) {
        this.value = value;
    }

    public static SosSequence of(byte[] value) {
        return new SosSequence(value);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SosSequence sos
                && Arrays.equals(value, sos.value);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }

    @Override
    public void writeTo(OutputStream os) throws IOException {

    }
}
