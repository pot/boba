package dev.mccue.boba.terminal;

import dev.mccue.boba.c.mac.intel.*;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.*;

final class MacTerminal_intel extends Terminal {
    private MemorySegment previousSettings;

    MacTerminal_intel() {}

    @Override
    public boolean isTerminal(int fd) {
        var invoker = ioctl_h.ioctl.makeInvoker(termios.layout());
        try (var arena = Arena.ofConfined()) {
            var t = termios.allocate(arena);
            var result = invoker.apply(fd, ioctl_h.TIOCGETA(), t);
            return result == 0;
        }
    }

    @Override
    public void makeRaw(int fd) {
        try (var arena = Arena.ofConfined()) {
            var t = termios.allocate(arena);
            var oldSettingsInvoker = ioctl_h.ioctl.makeInvoker(termios.layout());
            oldSettingsInvoker.apply(fd, ioctl_h.TIOCGETA(), t);
            previousSettings = termios.allocate(arena);
            previousSettings.copyFrom(t);

            var invoker = ioctl_h.ioctl.makeInvoker(termios.layout());
            invoker.apply(fd, ioctl_h.TIOCGETA(), t);

            var iflag = termios.c_iflag(t);
            iflag &= ~(ioctl_h.IGNBRK()
                    | ioctl_h.BRKINT()
                    | ioctl_h.PARMRK()
                    | ioctl_h.ISTRIP()
                    | ioctl_h.INLCR()
                    | ioctl_h.IGNCR()
                    | ioctl_h.ICRNL()
                    | ioctl_h.IXON());
            termios.c_iflag(t, iflag);

            var oflag = termios.c_oflag(t);
            oflag &= ~ioctl_h.OPOST();
            termios.c_oflag(t, oflag);

            var lflag = termios.c_lflag(t);
            lflag &= ~(ioctl_h.ECHO() | ioctl_h.ECHONL() | ioctl_h.ICANON() | ioctl_h.ISIG() | ioctl_h.IEXTEN());
            termios.c_lflag(t, lflag);

            var cflag = termios.c_cflag(t);
            cflag &= ~(ioctl_h.CSIZE() | ioctl_h.PARENB());
            cflag |= ioctl_h.CS8();
            termios.c_cflag(t, cflag);

            termios.c_cc(t, ioctl_h.VMIN(), (byte) 1);
            termios.c_cc(t, ioctl_h.VTIME(), (byte) 0);

            // apply profile to terminal
            invoker.apply(fd, ioctl_h.TIOCSETA(), t);
        }
    }

    @Override
    public void makeCooked(int fd) {
        try {
            var invoker = ioctl_h.ioctl.makeInvoker(termios.layout());
            invoker.apply(fd, ioctl_h.TIOCSETA(), previousSettings);
        } finally {
            previousSettings = null;
        }
    }

    @Override
    public TerminalSize getTerminalSize() {
        try (var arena = Arena.ofConfined()) {
            MemorySegment size = winsize.allocate(arena);
            ioctl_h.ioctl ioctl = ioctl_h.ioctl.makeInvoker(ADDRESS);
            ioctl.apply(0, ioctl_h.TIOCGWINSZ(), size);

            return new TerminalSize(winsize.ws_row(size), winsize.ws_col(size));
        }
    }
}
