package dev.mccue.ansi;

import dev.mccue.ansi.parser.TerminalSequence;

import java.io.Console;
import java.io.PrintWriter;

public final class TerminalWriter {
    private final Console console;
    private final PrintWriter writer;

    private TerminalWriter(Console console) {
        this.console = console;
        this.writer = console.writer();
    }

    public static TerminalWriter of(Console console) {
        return new TerminalWriter(console);
    }

    public void execute(TerminalSequence terminalSequence) {
        writer.print(terminalSequence.toString());
    }
}
