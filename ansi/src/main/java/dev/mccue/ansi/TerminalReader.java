package dev.mccue.ansi;

import java.io.Console;

public final class TerminalReader {
    private final Console console;

    private TerminalReader(Console console) {
        this.console = console;
    }

    public static TerminalReader of(Console console) {
        return new TerminalReader(console);
    }
}
