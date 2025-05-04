package dev.weisz.ansi.parser;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public final class PmSequence extends TerminalSequence {
    private final byte[] value;

    private PmSequence(byte[] value) {
        this.value = value;
    }

    public static PmSequence of(byte[] value) {
        return new PmSequence(value);
    }



    @Override
    public boolean equals(Object obj) {
        return obj instanceof PmSequence pm
                && Arrays.equals(value, pm.value);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }

    @Override
    public void writeTo(OutputStream os) throws IOException {

    }
}
