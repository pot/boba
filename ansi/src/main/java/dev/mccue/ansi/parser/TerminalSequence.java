package dev.mccue.ansi.parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;


public sealed abstract class TerminalSequence
        permits ApcSequence, ControlCode, CsiSequence, DcsSequence, EscSequence, OscSequence, PmSequence, Rune, SosSequence {
    public String toString() {
        try {
            var baos = new ByteArrayOutputStream();
            writeTo(baos);
            return baos.toString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public abstract void writeTo(OutputStream os) throws IOException;
}