package dev.mccue.boba;

import dev.mccue.boba.c.mac.ioctl_h;
import dev.mccue.boba.c.mac.termios;
import dev.mccue.boba.org.apache.commons.lang3.SystemUtils;

public sealed abstract class Terminal
        permits
            MacTerminal,
            LinuxTerminal,
            WindowsTerminal {
    public static Terminal create() {
        if (SystemUtils.IS_OS_UNIX) {
            if (SystemUtils.IS_OS_MAC
                || SystemUtils.IS_OS_FREE_BSD
                || SystemUtils.IS_OS_NET_BSD
                || SystemUtils.IS_OS_OPEN_BSD
            ) {
                return new MacTerminal();
            } else {
                return new LinuxTerminal();
            }
        }
        else {
            return new WindowsTerminal();
        }
    }


    public abstract boolean isTerminal(int fd);
    public abstract void makeRaw(int fd);
}
