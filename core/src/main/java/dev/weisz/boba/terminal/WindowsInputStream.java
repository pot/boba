package dev.weisz.boba.terminal;

import dev.weisz.boba.c.windows.x64.windows_wrapper_h;

import java.io.IOException;
import java.io.InputStream;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

public final class WindowsInputStream extends InputStream {
    private final Arena arena;
    private final MemorySegment inputWriteSide;

    public WindowsInputStream(Arena arena, MemorySegment inputWriteSide) {
        this.arena = arena;
        this.inputWriteSide = inputWriteSide;
    }

    @Override
    public int read() throws IOException {
        return 0;
    }

    @Override
    public void close() throws IOException {
        super.close();
        arena.close();
    }
}
