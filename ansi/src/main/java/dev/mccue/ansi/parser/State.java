package dev.mccue.ansi.parser;

import org.intellij.lang.annotations.MagicConstant;

final class State {
    private State() {}

    static final int GROUND = 0;
    static final int CSI_ENTRY = 1;
    static final int CSI_INTERMEDIATE = 2;
    static final int CSI_PARAM = 3;
    static final int DCS_ENTRY = 4;
    static final int DCS_INTERMEDIATE = 5;
    static final int DCS_PARAM = 6;
    static final int DCS_STRING = 7;
    static final int ESCAPE = 8;
    static final int ESCAPE_INTERMEDIATE = 9;
    static final int OSC_STRING = 10;
    static final int SOS_STRING = 11;
    static final int PM_STRING = 12;
    static final int APC_STRING = 13;
    // Utf8State is not part of the DEC ANSI standard. It is used to handle
    // UTF-8 sequences.
    static final int UTF8 = 14;

    static final String[] names = {
            "GroundState",
            "CsiEntryState",
            "CsiIntermediateState",
            "CsiParamState",
            "DcsEntryState",
            "DcsIntermediateState",
            "DcsParamState",
            "DcsStringState",
            "EscapeState",
            "EscapeIntermediateState",
            "OscStringState",
            "SosStringState",
            "PmStringState",
            "ApcStringState",
            "Utf8State"
    };

    static String name(
            @MagicConstant(flagsFromClass = State.class)
            int value
    ) {
        return names[value];
    }
}
