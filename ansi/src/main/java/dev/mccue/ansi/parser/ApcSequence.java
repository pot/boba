package dev.mccue.ansi.parser;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public final class ApcSequence extends TerminalSequence {
    private final byte[] value;

    private ApcSequence(byte[] value) {
        this.value = value;
    }

    public static ApcSequence of(byte[] value) {
        return new ApcSequence(value);
    }



    @Override
    public boolean equals(Object obj) {
        return obj instanceof ApcSequence apc
                && Arrays.equals(value, apc.value);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }

    @Override
    public void writeTo(OutputStream os) throws IOException {

    }
}
