package dev.mccue.ansi.parser;

import org.intellij.lang.annotations.MagicConstant;

import java.util.Arrays;

import static dev.mccue.ansi.parser.Action.*;
import static dev.mccue.ansi.parser.State.*;

final class TransitionTable {
    private final int[] t;

    // Table values are generated like this:
    //
    //	index:  currentState << IndexStateShift | charCode
    //	value:  action << TransitionActionShift | nextState
    static final int TransitionActionShift = 4;
    static final int TransitionStateMask   = 15;
    static final int IndexStateShift       = 8;

    // DefaultTableSize is the default size of the transition table.
    static final int DefaultTableSize = 4096;

    TransitionTable(int size) {
        if (size <= 0) {
            size = DefaultTableSize;
        }

        this.t = new int[size];
    }

    void setDefault(
            @MagicConstant(flagsFromClass = Action.class) int action,
            @MagicConstant(flagsFromClass = State.class) int state
    ) {
        Arrays.fill(t, (action << TransitionActionShift) | state);
    }

    void AddOne(
            int code,
            @MagicConstant(flagsFromClass = State.class) int state,
            @MagicConstant(flagsFromClass = Action.class) int action,
            @MagicConstant(flagsFromClass = State.class) int next
    ) {
        int idx = (state <<IndexStateShift) | code;
        var value = (action<<TransitionActionShift) | next;
        t[idx] = value;
    }

    void AddMany(
            int[] codes,
            @MagicConstant(flagsFromClass = State.class) int state,
            @MagicConstant(flagsFromClass = Action.class) int action,
            @MagicConstant(flagsFromClass = State.class) int next
    ) {
        for (var code : codes) {
            AddOne(code, state, action, next);
        }
    }

    void AddRange(
            int start,
            int end,
            @MagicConstant(flagsFromClass = State.class) int state,
            @MagicConstant(flagsFromClass = Action.class) int action,
            @MagicConstant(flagsFromClass = State.class) int next
    ) {
        for (int i = start; i <= end; i++) {
            AddOne(i, state, action, next);
        }
    }

    @SuppressWarnings("MagicConstant")
    Transition Transition(
            @MagicConstant(flagsFromClass = State.class) int state,
            int code
    ) {
        var index = (state << IndexStateShift) | code;
        var value = t[index];
        // Rely on math to get right constant values
        return new Transition(
                value & TransitionStateMask,
                value >> TransitionActionShift
        );
    }

    static int[] r(int start, int end) {
        var a = new int[end - start + 1];
        for (int i = 0; i < (end - start + 1); i++) {
            a[i] = start + i;
        }
        return a;
    }

    static final TransitionTable table = generate();

    static TransitionTable generate() {
        var table = new TransitionTable(DefaultTableSize);
        table.setDefault(NONE, GROUND);

        // Anywhere
        for (var state : r(GROUND, UTF8)) {
            // Anywhere -> Ground
            table.AddMany(new int[]{0x18, 0x1a, 0x99, 0x9a}, state, EXECUTE, GROUND);
            table.AddRange(0x80, 0x8F, state, EXECUTE, GROUND);
            table.AddRange(0x90, 0x97, state, EXECUTE, GROUND);
            table.AddOne(0x9C, state, EXECUTE, GROUND);
            // Anywhere -> Escape
            table.AddOne(0x1B, state, CLEAR, ESCAPE);
            // Anywhere -> SosStringState
            table.AddOne(0x98, state, START, SOS_STRING);
            // Anywhere -> PmStringState
            table.AddOne(0x9E, state, START, PM_STRING);
            // Anywhere -> ApcStringState
            table.AddOne(0x9F, state, START, APC_STRING);
            // Anywhere -> CsiEntry
            table.AddOne(0x9B, state, CLEAR, CSI_ENTRY);
            // Anywhere -> DcsEntry
            table.AddOne(0x90, state, CLEAR, DCS_ENTRY);
            // Anywhere -> OscString
            table.AddOne(0x9D, state, START, OSC_STRING);
            // Anywhere -> Utf8
            table.AddRange(0xC2, 0xDF, state, COLLECT, UTF8); // UTF8 2 byte sequence
            table.AddRange(0xE0, 0xEF, state, COLLECT, UTF8); // UTF8 3 byte sequence
            table.AddRange(0xF0, 0xF4, state, COLLECT, UTF8); // UTF8 4 byte sequence
        }

        // Ground
        table.AddRange(0x00, 0x17, GROUND, EXECUTE, GROUND);
        table.AddOne(0x19, GROUND, EXECUTE, GROUND);
        table.AddRange(0x1C, 0x1F, GROUND, EXECUTE, GROUND);
        table.AddRange(0x20, 0x7E, GROUND, PRINT, GROUND);
        table.AddOne(0x7F, GROUND, EXECUTE, GROUND);

        // EscapeIntermediate
        table.AddRange(0x00, 0x17, ESCAPE_INTERMEDIATE, EXECUTE, ESCAPE_INTERMEDIATE);
        table.AddOne(0x19, ESCAPE_INTERMEDIATE, EXECUTE, ESCAPE_INTERMEDIATE);
        table.AddRange(0x1C, 0x1F, ESCAPE_INTERMEDIATE, EXECUTE, ESCAPE_INTERMEDIATE);
        table.AddRange(0x20, 0x2F, ESCAPE_INTERMEDIATE, COLLECT, ESCAPE_INTERMEDIATE);
        table.AddOne(0x7F, ESCAPE_INTERMEDIATE, IGNORE, ESCAPE_INTERMEDIATE);
        // EscapeIntermediate -> Ground
        table.AddRange(0x30, 0x7E, ESCAPE_INTERMEDIATE, DISPATCH, GROUND);

        // Escape
        table.AddRange(0x00, 0x17, ESCAPE, EXECUTE, ESCAPE);
        table.AddOne(0x19, ESCAPE, EXECUTE, ESCAPE);
        table.AddRange(0x1C, 0x1F, ESCAPE, EXECUTE, ESCAPE);
        table.AddOne(0x7F, ESCAPE, IGNORE, ESCAPE);
        // Escape -> Ground
        table.AddRange(0x30, 0x4F, ESCAPE, DISPATCH, GROUND);
        table.AddRange(0x51, 0x57, ESCAPE, DISPATCH, GROUND);
        table.AddOne(0x59, ESCAPE, DISPATCH, GROUND);
        table.AddOne(0x5A, ESCAPE, DISPATCH, GROUND);
        table.AddOne(0x5C, ESCAPE, DISPATCH, GROUND);
        table.AddRange(0x60, 0x7E, ESCAPE, DISPATCH, GROUND);
        // Escape -> Escape_intermediate
        table.AddRange(0x20, 0x2F, ESCAPE, COLLECT, ESCAPE_INTERMEDIATE);
        // Escape -> Sos_pm_apc_string
        table.AddOne('X', ESCAPE, START, SOS_STRING); // SOS
        table.AddOne('^', ESCAPE, START, PM_STRING);  // PM
        table.AddOne('_', ESCAPE, START, APC_STRING); // APC
        // Escape -> Dcs_entry
        table.AddOne('P', ESCAPE, CLEAR, DCS_ENTRY);
        // Escape -> Csi_entry
        table.AddOne('[', ESCAPE, CLEAR, CSI_ENTRY);
        // Escape -> Osc_string
        table.AddOne(']', ESCAPE, START, OSC_STRING);

        // Sos_pm_apc_string
        for (var state : r(SOS_STRING, APC_STRING)) {
            table.AddRange(0x00, 0x17, state, PUT, state);
            table.AddOne(0x19, state, PUT, state);
            table.AddRange(0x1C, 0x1F, state, PUT, state);
            table.AddRange(0x20, 0x7F, state, PUT, state);
            // ESC, ST, CAN, and SUB terminate the sequence
            table.AddOne(0x1B, state, DISPATCH, ESCAPE);
            table.AddOne(0x9C, state, DISPATCH, GROUND);
            table.AddMany(new int[]{0x18, 0x1A}, state, IGNORE, GROUND);
        }

        // Dcs_entry
        table.AddRange(0x00, 0x07, DCS_ENTRY, IGNORE, DCS_ENTRY);
        table.AddRange(0x0E, 0x17, DCS_ENTRY, IGNORE, DCS_ENTRY);
        table.AddOne(0x19, DCS_ENTRY, IGNORE, DCS_ENTRY);
        table.AddRange(0x1C, 0x1F, DCS_ENTRY, IGNORE, DCS_ENTRY);
        table.AddOne(0x7F, DCS_ENTRY, IGNORE, DCS_ENTRY);
        // Dcs_entry -> Dcs_intermediate
        table.AddRange(0x20, 0x2F, DCS_ENTRY, COLLECT, DCS_INTERMEDIATE);
        // Dcs_entry -> Dcs_param
        table.AddRange(0x30, 0x3B, DCS_ENTRY, PARAM, DCS_PARAM);
        table.AddRange(0x3C, 0x3F, DCS_ENTRY, MARKER, DCS_PARAM);
        // Dcs_entry -> Dcs_passthrough
        table.AddRange(0x08, 0x0D, DCS_ENTRY, PUT, DCS_STRING); // Follows ECMA-48 § 8.3.27
        // XXX: allows passing ESC (not a ECMA-48 standard) this to allow for
        // passthrough of ANSI sequences like in Screen or Tmux passthrough mode.
        table.AddOne(0x1B, DCS_ENTRY, PUT, DCS_STRING);
        table.AddRange(0x40, 0x7E, DCS_ENTRY, START, DCS_STRING);

        // Dcs_intermediate
        table.AddRange(0x00, 0x17, DCS_INTERMEDIATE, IGNORE, DCS_INTERMEDIATE);
        table.AddOne(0x19, DCS_INTERMEDIATE, IGNORE, DCS_INTERMEDIATE);
        table.AddRange(0x1C, 0x1F, DCS_INTERMEDIATE, IGNORE, DCS_INTERMEDIATE);
        table.AddRange(0x20, 0x2F, DCS_INTERMEDIATE, COLLECT, DCS_INTERMEDIATE);
        table.AddOne(0x7F, DCS_INTERMEDIATE, IGNORE, DCS_INTERMEDIATE);
        // Dcs_intermediate -> Dcs_passthrough
        table.AddRange(0x30, 0x3F, DCS_INTERMEDIATE, START, DCS_STRING);
        table.AddRange(0x40, 0x7E, DCS_INTERMEDIATE, START, DCS_STRING);

        // Dcs_param
        table.AddRange(0x00, 0x17, DCS_PARAM, IGNORE, DCS_PARAM);
        table.AddOne(0x19, DCS_PARAM, IGNORE, DCS_PARAM);
        table.AddRange(0x1C, 0x1F, DCS_PARAM, IGNORE, DCS_PARAM);
        table.AddRange(0x30, 0x3B, DCS_PARAM, PARAM, DCS_PARAM);
        table.AddOne(0x7F, DCS_PARAM, IGNORE, DCS_PARAM);
        table.AddRange(0x3C, 0x3F, DCS_PARAM, IGNORE, DCS_PARAM);
        // Dcs_param -> Dcs_intermediate
        table.AddRange(0x20, 0x2F, DCS_PARAM, COLLECT, DCS_INTERMEDIATE);
        // Dcs_param -> Dcs_passthrough
        table.AddRange(0x40, 0x7E, DCS_PARAM, START, DCS_STRING);

        // Dcs_passthrough
        table.AddRange(0x00, 0x17, DCS_STRING, PUT, DCS_STRING);
        table.AddOne(0x19, DCS_STRING, PUT, DCS_STRING);
        table.AddRange(0x1C, 0x1F, DCS_STRING, PUT, DCS_STRING);
        table.AddRange(0x20, 0x7E, DCS_STRING, PUT, DCS_STRING);
        table.AddOne(0x7F, DCS_STRING, PUT, DCS_STRING);
        table.AddRange(0x80, 0xFF, DCS_STRING, PUT, DCS_STRING); // Allow Utf8 characters by extending the printable range from 0x7F to 0xFF
        // ST, CAN, SUB, and ESC terminate the sequence
        table.AddOne(0x1B, DCS_STRING, DISPATCH, ESCAPE);
        table.AddOne(0x9C, DCS_STRING, DISPATCH, GROUND);
        table.AddMany(new int[]{0x18, 0x1A}, DCS_STRING, IGNORE, GROUND);

        // Csi_param
        table.AddRange(0x00, 0x17, CSI_PARAM, EXECUTE, CSI_PARAM);
        table.AddOne(0x19, CSI_PARAM, EXECUTE, CSI_PARAM);
        table.AddRange(0x1C, 0x1F, CSI_PARAM, EXECUTE, CSI_PARAM);
        table.AddRange(0x30, 0x3B, CSI_PARAM, PARAM, CSI_PARAM);
        table.AddOne(0x7F, CSI_PARAM, IGNORE, CSI_PARAM);
        table.AddRange(0x3C, 0x3F, CSI_PARAM, IGNORE, CSI_PARAM);
        // Csi_param -> Ground
        table.AddRange(0x40, 0x7E, CSI_PARAM, DISPATCH, GROUND);
        // Csi_param -> Csi_intermediate
        table.AddRange(0x20, 0x2F, CSI_PARAM, COLLECT, CSI_INTERMEDIATE);

        // Csi_intermediate
        table.AddRange(0x00, 0x17, CSI_INTERMEDIATE, EXECUTE, CSI_INTERMEDIATE);
        table.AddOne(0x19, CSI_INTERMEDIATE, EXECUTE, CSI_INTERMEDIATE);
        table.AddRange(0x1C, 0x1F, CSI_INTERMEDIATE, EXECUTE, CSI_INTERMEDIATE);
        table.AddRange(0x20, 0x2F, CSI_INTERMEDIATE, COLLECT, CSI_INTERMEDIATE);
        table.AddOne(0x7F, CSI_INTERMEDIATE, IGNORE, CSI_INTERMEDIATE);
        // Csi_intermediate -> Ground
        table.AddRange(0x40, 0x7E, CSI_INTERMEDIATE, DISPATCH, GROUND);
        // Csi_intermediate -> Csi_ignore
        table.AddRange(0x30, 0x3F, CSI_INTERMEDIATE, IGNORE, GROUND);

        // Csi_entry
        table.AddRange(0x00, 0x17, CSI_ENTRY, EXECUTE, CSI_ENTRY);
        table.AddOne(0x19, CSI_ENTRY, EXECUTE, CSI_ENTRY);
        table.AddRange(0x1C, 0x1F, CSI_ENTRY, EXECUTE, CSI_ENTRY);
        table.AddOne(0x7F, CSI_ENTRY, IGNORE, CSI_ENTRY);
        // Csi_entry -> Ground
        table.AddRange(0x40, 0x7E, CSI_ENTRY, DISPATCH, GROUND);
        // Csi_entry -> Csi_intermediate
        table.AddRange(0x20, 0x2F, CSI_ENTRY, COLLECT, CSI_INTERMEDIATE);
        // Csi_entry -> Csi_param
        table.AddRange(0x30, 0x3B, CSI_ENTRY, PARAM, CSI_PARAM);
        table.AddRange(0x3C, 0x3F, CSI_ENTRY, MARKER, CSI_PARAM);

        // Osc_string
        table.AddRange(0x00, 0x06, OSC_STRING, IGNORE, OSC_STRING);
        table.AddRange(0x08, 0x17, OSC_STRING, IGNORE, OSC_STRING);
        table.AddOne(0x19, OSC_STRING, IGNORE, OSC_STRING);
        table.AddRange(0x1C, 0x1F, OSC_STRING, IGNORE, OSC_STRING);
        table.AddRange(0x20, 0xFF, OSC_STRING, PUT, OSC_STRING); // Allow Utf8 characters by extending the printable range from 0x7F to 0xFF

        // ST, CAN, SUB, ESC, and BEL terminate the sequence
        table.AddOne(0x1B, OSC_STRING, DISPATCH, ESCAPE);
        table.AddOne(0x07, OSC_STRING, DISPATCH, GROUND);
        table.AddOne(0x9C, OSC_STRING, DISPATCH, GROUND);
        table.AddMany(new int[]{0x18, 0x1A}, OSC_STRING, IGNORE, GROUND);

        return table;
    }
}
