package dev.mccue.ansi.parser;

import dev.mccue.ansi.ControlCharacters;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class OscSequence extends TerminalSequence {
    private final byte[] data;
    private final int cmd;

    private OscSequence(byte[] data, int cmd) {
        this.data = data;
        this.cmd = cmd;
    }

    public static OscSequence of(byte[] value, int cmd) {
        return new OscSequence(value, cmd);
    }

    public int command() {
        return cmd;
    }

    public List<String> params() {
        return List.of(
                new String(data, StandardCharsets.UTF_8).split(";")
        );
    }

    @Override
    public void writeTo(OutputStream os) throws IOException {
        os.write("\u001b]".getBytes(StandardCharsets.UTF_8));
        os.write(data);
        os.write(ControlCharacters.BEL);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof OscSequence osc
                && Arrays.equals(data, osc.data)
                && data == osc.data;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                Arrays.hashCode(data),
                cmd
        );
    }
}
