package dev.weisz.boba.terminal;

import dev.weisz.boba.c.windows.x64.windows_wrapper_h;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

public class WindowsOutputStream extends OutputStream {
    private final Arena arena;
    private final MemorySegment outWriteSide;

    public WindowsOutputStream(Arena arena, MemorySegment outWriteSide) {
        this.arena = arena;
        this.outWriteSide = outWriteSide;
    }

    @Override
    public void write(int b) throws IOException {
        windows_wrapper_h.WriteFile(

        )
    }
}
