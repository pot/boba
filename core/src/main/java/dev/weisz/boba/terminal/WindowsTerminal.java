package dev.weisz.boba.terminal;

import dev.weisz.boba.c.windows.windows_h;

import java.lang.foreign.Arena;

final class WindowsTerminal extends Terminal {
    @Override
    public boolean isTerminal(int fd) {
        var handle = windows_h.GetStdHandle(windows_h.STD_OUTPUT_HANDLE());
        try (var arena = Arena.ofConfined()) {
            var st = arena.allocate(windows_h.C_INT);
            return windows_h.GetConsoleMode(
                    handle,
                    st
            ) == 0;
        }
    }

    @Override
    public void makeRaw(int fd) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void makeCooked(int fd) {

    }

    @Override
    public TerminalSize getTerminalSize() {
        return null;
    }
}
