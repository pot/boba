package dev.weisz.boba.terminal;


import org.apache.commons.lang3.ArchUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.arch.Processor;

public sealed abstract class Terminal
        permits LinuxTerminal_arm64, LinuxTerminal_x64, MacTerminal_arm64, MacTerminal_intel, WindowsTerminal {
    public static Terminal create() {
        Processor processor = ArchUtils.getProcessor();
        if (SystemUtils.IS_OS_UNIX) {
            boolean isMacOrBSD = SystemUtils.IS_OS_MAC
                    || SystemUtils.IS_OS_FREE_BSD
                    || SystemUtils.IS_OS_NET_BSD
                    || SystemUtils.IS_OS_OPEN_BSD;

            if (isMacOrBSD) {
                if (processor.getType() == Processor.Type.AARCH_64) {
                    return new MacTerminal_arm64();
                } else if (processor.getType() == Processor.Type.X86 && processor.getArch() == Processor.Arch.BIT_64) {
                    return new MacTerminal_intel();
                } else {
                    throw new UnsupportedOperationException("Unsupported Mac architecture: " + processor.getType().getLabel());
                }
            } else {
                if (processor.getType() == Processor.Type.AARCH_64) {
                    return new LinuxTerminal_arm64();
                } else if (processor.getType() == Processor.Type.X86 && processor.getArch() == Processor.Arch.BIT_64) {
                    return new LinuxTerminal_x64();
                } else {
                    throw new UnsupportedOperationException("Unsupported Linux architecture: " + processor.getType().getLabel());
                }
            }
        } else if (SystemUtils.IS_OS_WINDOWS) {
            return new WindowsTerminal();
        } else {
            throw new UnsupportedOperationException("Unsupported operating system: " + System.getProperty("os.name"));
        }
    }

    public abstract boolean isTerminal(int fd);
    public abstract void makeRaw(int fd);
    public abstract void makeCooked(int fd);
    public abstract WinSize getWinSize();
}
