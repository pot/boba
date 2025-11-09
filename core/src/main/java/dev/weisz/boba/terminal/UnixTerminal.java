package dev.weisz.boba.terminal;

public abstract sealed class UnixTerminal extends Terminal permits LinuxTerminal_arm64, LinuxTerminal_x64, MacTerminal_arm64, MacTerminal_intel {
    public abstract void makeRaw(int fd);
    public abstract void makeCooked(int fd);
}
