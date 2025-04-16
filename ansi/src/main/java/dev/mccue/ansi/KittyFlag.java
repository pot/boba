package dev.mccue.ansi;

import java.util.EnumSet;

@Kitty
public enum KittyFlag {
    DISAMBIGUATE_ESCAPE_CODES(1 ),
    REPORT_EVENT_TYPES(2),
    REPORT_ALTERNATE_KEYS(4),
    REPORT_ALL_KEYS_AS_ESCAPE_CODES(8),
    REPORT_ASSOCIATED_KEYS(16);

    private final int value;

    KittyFlag(int value) {
        this.value = value;
    }

    static int valueFor(EnumSet<KittyFlag> flags) {
        int value = 0;
        for (var flag : flags) {
            value |= flag.value;
        }
        return value;
    }
}
