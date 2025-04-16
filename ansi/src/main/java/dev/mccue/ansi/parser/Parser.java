package dev.mccue.ansi.parser;

import dev.mccue.ansi.ControlCharacters;
import org.intellij.lang.annotations.MagicConstant;

import java.util.Arrays;

import static dev.mccue.ansi.parser.Width.utf8ByteLen;

public final class Parser {
    // Params contains the raw parameters of the sequence.
    // These parameters used when constructing CSI and DCS sequences.
    int[] Params;

    // Data contains the raw data of the sequence.
    // These data used when constructing OSC, DCS, SOS, PM, and APC sequences.
    byte[] Data;

    // DataLen keeps track of the length of the data buffer.
    // If DataLen is -1, the data buffer is unlimited and will grow as needed.
    // Otherwise, DataLen is limited by the size of the Data buffer.
    int DataLen;

    // ParamsLen keeps track of the number of parameters.
    // This is limited by the size of the Params buffer.
    //
    // This is also used when collecting UTF-8 runes to keep track of the
    // number of rune bytes collected.
    int ParamsLen;

    // Cmd contains the raw command along with the private marker and
    // intermediate bytes of the sequence.
    // The first lower byte contains the command byte, the next byte contains
    // the private marker, and the next byte contains the intermediate byte.
    //
    // This is also used when collecting UTF-8 runes treating it as a slice of
    // 4 bytes.
    int Cmd;

    // State is the current state of the parser.
    @MagicConstant(flagsFromClass = State.class) int state;

    public Parser(int paramsSize, int dataSize) {
        if (dataSize <= 0) {
            dataSize = 0;
            this.DataLen = -1;
        }
        this.Params = new int[paramsSize];
        this.Data = new byte[dataSize];
    }

    public void reset() {
        clear();
        this.state = State.GROUND;
    }

    public void clear() {
        if (Params.length > 0) {
            Params[0] = Seq.MissingParam;
        }
        ParamsLen = 0;
        Cmd = 0;
    }

    public String stateName() {
        return State.name(state);
    }

    public void parse(ParserDispatcher dispatcher, byte[] b) {
        for (int i = 0; i < b.length; i++) {
            Advance(dispatcher, b[i], i < b.length - 1);
        }
    }

    // Advance advances the parser with the given dispatcher and byte.
    public @MagicConstant(flagsFromClass = Action.class) int Advance(ParserDispatcher dispatcher, byte b, boolean more) {
        switch (state) {
            case State.UTF8:
                // We handle UTF-8 here.
                return advanceUtf8(dispatcher, b);
            default:
                return advance(dispatcher, b, more);
        }
    }

    // maximum number of bytes of a UTF-8 encoded Unicode character.
    static final int UTFMax = 4;

    public void collectRune(byte b) {
        if (ParamsLen > UTFMax) {
            return;
        }

        var shift = ParamsLen * 8;
        Cmd = Cmd & ~(0xff << shift);;
        Cmd |= (b << shift);
        ParamsLen++;
    }

    public @MagicConstant(flagsFromClass = Action.class) int advanceUtf8(ParserDispatcher dispatcher, byte b) {
        // Collect UTF-8 rune bytes.
        collectRune(b);

        var rw = utf8ByteLen(Cmd & 0xff);
        if (rw == -1) {
            // We panic here because the first byte comes from the state machine,
            // if this panics, it means there is a bug in the state machine!
            throw new IllegalStateException("invalid rune"); // unreachable
        }

        if (ParamsLen < rw) {
            return Action.COLLECT;
        }

        // We have enough bytes to decode the rune using unsafe
        new String(Character.toChars(Cmd));
        dispatcher.accept(Rune.of(Cmd));

        state = State.GROUND;
        ParamsLen = 0;

        return Action.PRINT;
    }

    public @MagicConstant(flagsFromClass = Action.class) int advance(ParserDispatcher d, byte b, boolean more) {
        var transition = TransitionTable.table.Transition(state, b);
        var state = transition.state();
        var action = transition.action();

        // We need to clear the parser state if the state changes from EscapeState.
        // This is because when we enter the EscapeState, we don't get a chance to
        // clear the parser state. For example, when a sequence terminates with a
        // ST (\x1b\\ or \x9c), we dispatch the current sequence and transition to
        // EscapeState. However, the parser state is not cleared in this case and
        // we need to clear it here before dispatching the esc sequence.
        if (this.state != state) {
            if (this.state == State.ESCAPE) {
                this.performAction(d, Action.CLEAR, state, b);
            }
            if (action == Action.PUT &&
                    this.state == State.DCS_ENTRY && state == State.DCS_STRING) {
                // XXX: This is a special case where we need to start collecting
                // non-string parameterized data i.e. doesn't follow the ECMA-48 §
                // 5.4.1 string parameters format.
                this.performAction(d, Action.START, state, (byte) 0);
            }
        }

        // Handle special cases
        if (b == ControlCharacters.ESC && this.state == State.ESCAPE) {
            // Two ESCs in a row
            performAction(d, Action.EXECUTE, state, b);
            if (!more) {
                // Two ESCs at the end of the buffer
                performAction(d, Action.EXECUTE, state, b);
            }
        }
        else if (b == ControlCharacters.ESC && !more) {
            // Last byte is an ESC
            performAction(d, Action.EXECUTE, state, b);
        }
        else if (this.state == State.ESCAPE && b == 'P' && !more) {
            // ESC P (DCS) at the end of the buffer
            performAction(d, Action.DISPATCH, state, b);
        }
        else if (this.state == State.ESCAPE && b == 'X' && !more) {
            // ESC X (SOS) at the end of the buffer
            performAction(d, Action.DISPATCH, state, b);
        }
        else if (
                this.state == State.ESCAPE
                        && (b == 'P' || b == 'X' || b == '[' || b == ']' || b == '^' || b == '_')
                        && !more
        ) {
            // ESC P (DCS) at the end of the buffer
            // ESC X (SOS) at the end of the buffer
            // ESC [ (CSI) at the end of the buffer
            // ESC ] (OSC) at the end of the buffer
            // ESC ^ (PM) at the end of the buffer
            // ESC _ (APC) at the end of the buffer
            performAction(d, Action.DISPATCH, state, b);
        }
        else {
            performAction(d, action, state, b);
        }
        this.state = state;

        return action;
    }

    private static byte[] append(byte[] bytes, byte b) {
        byte[] newArray = new byte[bytes.length + 1];
        int i;
        for (i = 0; i < bytes.length; i++) {
            newArray[i] = bytes[i];
        }
        newArray[i] = b;
        return newArray;
    }

    private static int[] slice(int[] ints, int newLen) {
        return Arrays.stream(ints).limit(newLen).toArray();
    }

    private static byte[] slice(byte[] bytes, int newLen) {
        byte[] newArr = new byte[newLen];
        for (int i = 0; i < newLen; i++) {
            newArr[i] = bytes[i];
        }
        return newArr;
    }

    void performAction(
            ParserDispatcher dispatcher,
            @MagicConstant(flagsFromClass = Action.class) int action,
            @MagicConstant(flagsFromClass = State.class) int state,
            byte b
    ) {
        switch (action) {
            case Action.IGNORE -> {
            }

            case Action.CLEAR -> {
                this.clear();
            }

            case Action.PRINT -> {
                dispatcher.accept(Rune.of(b));
            }

            case Action.EXECUTE -> {
                dispatcher.accept(ControlCode.of(b));
            }

            case Action.MARKER -> {
                // Collect private marker
                // we only store the last marker
                this.Cmd = Cmd & ~(0xff << Seq.MarkerShift);
                this.Cmd |= Byte.toUnsignedInt(b) << Seq.MarkerShift;

            }

            case Action.COLLECT -> {
                if (state == State.UTF8) {
                    // Reset the UTF-8 counter
                    ParamsLen = 0;
                    collectRune(b);
                } else {
                    // Collect intermediate bytes
                    // we only store the last intermediate byte
                    Cmd = Cmd & ~(0xff << Seq.IntermedShift);
                    Cmd |= Byte.toUnsignedInt(b) << Seq.IntermedShift;
                }
            }


            case Action.PARAM -> {
                // Collect parameters
                if (ParamsLen >= Params.length) {
                    break;
                }

                if (b >= '0' && b <= '9') {
                    if (Params[ParamsLen] == Seq.MissingParam) {
                        Params[ParamsLen] = 0;
                    }

                    Params[ParamsLen] *= 10;
                    Params[ParamsLen] += (b - '0');
                }

                if (b == ':') {
                    Params[ParamsLen] |= Seq.HasMoreFlag;
                }

                if (b == ';' || b == ':') {
                    ParamsLen++;
                    if (ParamsLen < Params.length) {
                        Params[ParamsLen] = Seq.MissingParam;
                    }
                }
            }

            case Action.START -> {
                if (DataLen < 0 && Data != null) {
                    Data = new byte[] {};
                } else {
                    DataLen = 0;
                }
                if (this.state >= State.DCS_ENTRY && this.state <= State.DCS_STRING) {
                    // Collect the command byte for DCS
                    Cmd |= Byte.toUnsignedInt(b);
                } else {
                    Cmd = Seq.MissingCommand;
                }

            }

            case Action.PUT -> {
                if (this.state == State.OSC_STRING) {
                    if (b == ';' && Cmd == Seq.MissingCommand) {
                        // Try to parse the command
                        var datalen = this.Data.length;
                        if (this.DataLen >= 0) {
                            datalen = this.DataLen;
                        }
                        for (int i = 0; i < datalen; i++) {
                            var d = Byte.toUnsignedInt(this.Data[i]);
                            if (d < '0' || d > '9') {
                                break;
                            }
                            if (Cmd == Seq.MissingCommand) {
                                Cmd = 0;
                            }
                            Cmd *= 10;
                            Cmd += d - '0';
                        }
                    }
                }

                if (DataLen < 0) {
                    Data = append(Data, b);
                }
                else {
                    if (DataLen < Data.length) {
                        Data[DataLen] = b;
                        DataLen++;
                    }
                }

            }

            case Action.DISPATCH -> {
                // Increment the last parameter
                if (this.ParamsLen > 0 && this.ParamsLen < (this.Params.length) - 1 ||
                        this.ParamsLen == 0 && Params.length > 0 && this.Params[0] != Seq.MissingParam) {
                    this.ParamsLen++;
                }


                TerminalSequence seq = null;
                var data = this.Data;
                if (this.DataLen >= 0) {
                    data = slice(data, DataLen);
                }
                switch (this.state) {
                    case State.CSI_ENTRY, State.CSI_PARAM, State.CSI_INTERMEDIATE -> {
                        Cmd |= Byte.toUnsignedInt(b);
                        seq = CsiSequence.of(Cmd, slice(Params, ParamsLen));
                    }

                    case State.ESCAPE, State.ESCAPE_INTERMEDIATE -> {
                        Cmd |= Byte.toUnsignedInt(b);
                        seq = EscSequence.of(Cmd);
                    }
                    case State.DCS_ENTRY, State.DCS_PARAM, State.DCS_INTERMEDIATE, State.DCS_STRING -> {
                        seq = DcsSequence.of(slice(Params, ParamsLen), data, Cmd);
                    }
                    case State.OSC_STRING -> {
                        seq = OscSequence.of(data, Cmd);
                    }
                    case State.SOS_STRING -> {
                        seq = SosSequence.of(data);
                    }
                    case State.PM_STRING -> {
                        seq = PmSequence.of(data);
                    }
                    case State.APC_STRING -> {
                        seq = ApcSequence.of(data);
                    }
                }

                if (seq == null) {
                    throw new IllegalStateException("Invalid state: " + this.state);
                }

                dispatcher.accept(seq);
            }
        }
    }
}
