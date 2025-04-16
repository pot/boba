package dev.mccue.ansi.parser;

import dev.mccue.ansi.ControlCharacters;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.OptionalInt;

public final class CsiSequence extends TerminalSequence{
    private final int[] params;
    private final int cmd;

    private CsiSequence(int cmd, int[] params) {
        this.params = params;
        this.cmd = cmd;
    }

    public static CsiSequence of(int cmd, int[] params) {
        return new CsiSequence(cmd, params);
    }

    public static CsiSequence of(int cmd) {
        return new CsiSequence(cmd, new int[] {});
    }

    public int marker() {
        return Seq.Marker(cmd);
    }

    public int intermediate() {
        return Seq.Intermediate(cmd);
    }

    public int command() {
        return Seq.Command(cmd);
    }

    public OptionalInt param(int i) {
        var param = Seq.Param(params, i);
        return param == -1 ? OptionalInt.empty() : OptionalInt.of(param);
    }

    @Override
    public void writeTo(OutputStream os) throws IOException {
        os.write((ControlCharacters.ESC + "[").getBytes(StandardCharsets.UTF_8));
        var m = marker();
        if (m != 0) {
            os.write(m);
        }

        Seq.Range(params, (i, param, hasMore) -> {
            if (param >= 0) {
                os.write(String.valueOf(param).getBytes(StandardCharsets.UTF_8));
            }

            if (i < params.length-1) {
                if (hasMore) {
                    os.write(':');
                } else {
                    os.write(';');
                }
            }
            return true;
        });


        var i = intermediate();
        if (i != 0) {
            os.write(i);
        }
        os.write(command());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CsiSequence csiSequence
                && Arrays.equals(params, csiSequence.params)
                && cmd == csiSequence.cmd;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                Arrays.hashCode(params),
                cmd
        );
    }
}
