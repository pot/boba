package dev.weisz.boba.terminal;

import dev.weisz.boba.c.windows.x64._COORD;
import dev.weisz.boba.c.windows.x64.windows_wrapper_h;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.foreign.Arena;

final class WindowsTerminal extends Terminal {
    @Override
    public boolean isTerminal(int fd) {
        var handle = windows_wrapper_h.GetStdHandle(windows_wrapper_h.STD_OUTPUT_HANDLE());
        try (var arena = Arena.ofConfined()) {
            var st = arena.allocate(windows_wrapper_h.C_INT);
            return windows_wrapper_h.GetConsoleMode(
                    handle,
                    st
            ) == 0;
        }
    }

    public record WindowsInOut(InputStream in, OutputStream out) {}

    public WindowsInOut createInOut() {
        var inArena = Arena.ofShared();
        var outArena = Arena.ofShared();

        try (var arena = Arena.ofConfined()) {
            var inputReadSide = inArena.allocate(windows_wrapper_h.HANDLE);
            var inputWriteSide = inArena.allocate(windows_wrapper_h.HANDLE);

            var outputReadSide = outArena.allocate(windows_wrapper_h.HANDLE);
            var outputWriteSide = outArena.allocate(windows_wrapper_h.HANDLE);

            if (windows_wrapper_h.CreatePipe(
                    inputReadSide,
                    inputWriteSide,
                    null,
                    0
            ) == 0) {
                // handle GetLastError here
                // https://stackoverflow.com/questions/79679982/java-ffm-linker-option-capturecallstate-does-not-successfully-capture-getlast

                throw new RuntimeException();
            }

            if (windows_wrapper_h.CreatePipe(
                    outputReadSide,
                    outputWriteSide,
                    null,
                    0
            ) == 0) {
                // read above

                throw new RuntimeException();
            }

            var tempSize = arena.allocate(_COORD.layout());
            _COORD.X(tempSize, (short) 250);
            _COORD.Y(tempSize, (short) 250);

            var hPC = arena.allocate(windows_wrapper_h.HPCON);
            windows_wrapper_h.CreatePseudoConsole(
                    _COORD.reinterpret(tempSize, arena, (mem) -> {}),
                    inputReadSide.get(windows_wrapper_h.HANDLE, 0L),
                    outputWriteSide.get(windows_wrapper_h.HANDLE, 0L),
                    0,
                    hPC
            );

            return new WindowsInOut(
                    new WindowsInputStream(inArena, inputWriteSide),
                    null
            );
        }
    }

    @Override
    public WinSize getWinSize() {
        return null;
    }
}
