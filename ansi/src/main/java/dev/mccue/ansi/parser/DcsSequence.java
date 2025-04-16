package dev.mccue.ansi.parser;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Objects;

public final class DcsSequence extends TerminalSequence{

    // Params contains the raw parameters of the sequence.
    // This is a slice of integers, where each integer is a 32-bit integer
    // containing the parameter value in the lower 31 bits and a flag in the
    // most significant bit indicating whether there are more sub-parameters.
    private final int[] params;

    // Data contains the string raw data of the sequence.
    // This is the data between the final byte and the escape sequence terminator.
    private final byte[] data;

    // Cmd contains the raw command of the sequence.
    // The command is a 32-bit integer containing the DCS command byte in the
    // lower 8 bits, the private marker in the next 8 bits, and the intermediate
    // byte in the next 8 bits.
    //
    //  DCS > 0 ; 1 $ r <data> ST
    //
    // Is represented as:
    //
    //  'r' | '>' << 8 | '$' << 16
    private final int cmd;

    private DcsSequence(int[] params, byte[] data, int cmd) {
        this.params = Arrays.copyOf(params, params.length);
        this.data = Arrays.copyOf(data, data.length);
        this.cmd = cmd;
    }

    public static DcsSequence of(int[] params, byte[] data, int cmd) {
        return new DcsSequence(params, data, cmd);
    }

    @Override
    public void writeTo(OutputStream os) throws IOException {

    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DcsSequence dcsSequence
                && Arrays.equals(params, dcsSequence.params)
                && Arrays.equals(data, dcsSequence.data)
                && cmd == dcsSequence.cmd;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                Arrays.hashCode(params),
                Arrays.hashCode(data),
                cmd
        );
    }
}
