package dev.mccue.ansi;

public final class ControlCharacters {
    private ControlCharacters() {}

    /// SP is the space character (Char: \x20).
    public static final char SP = 0x20;
    /// DEL is the delete character (Caret: ^?, Char: \x7f).
    public static final char DEL = 0x7F;

    /// NUL is the null character (Caret: ^@, Char: \0).
    @C0 public static final char NUL = 0x00;
    /// SOH is the start of heading character (Caret: ^A).
    @C0 public static final char SOH = 0x01;
    /// STX is the start of text character (Caret: ^B).
    @C0 public static final char STX = 0x02;
    /// ETX is the end of text character (Caret: ^C).
    @C0 public static final char ETX = 0x03;
    /// EOT is the end of transmission character (Caret: ^D).
    @C0 public static final char EOT = 0x04;
    /// ENQ is the enquiry character (Caret: ^E).
    @C0 public static final char ENQ = 0x05;
    /// ACK is the acknowledge character (Caret: ^F).
    @C0 public static final char ACK = 0x06;
    /// BEL is the bell character (Caret: ^G, Char: \a).
    @C0 public static final char BEL = 0x07;
    /// BS is the backspace character (Caret: ^H, Char: \b).
    @C0 public static final char BS = 0x08;
    /// HT is the horizontal tab character (Caret: ^I, Char: \t).
    @C0 public static final char HT = 0x09;
    /// LF is the line feed character (Caret: ^J, Char: \n).
    @C0 public static final char LF = 0x0A;
    /// VT is the vertical tab character (Caret: ^K, Char: \v).
    @C0 public static final char VT = 0x0B;
    /// FF is the form feed character (Caret: ^L, Char: \f).
    @C0 public static final char FF = 0x0C;
    /// CR is the carriage return character (Caret: ^M, Char: \r).
    @C0 public static final char CR = 0x0D;
    /// SO is the shift out character (Caret: ^N).
    @C0 public static final char SO = 0x0E;
    /// SI is the shift in character (Caret: ^O).
    @C0 public static final char SI = 0x0F;
    /// DLE is the data link escape character (Caret: ^P).
    @C0 public static final char DLE = 0x10;
    /// DC1 is the device control 1 character (Caret: ^Q).
    @C0 public static final char DC1 = 0x11;
    /// DC2 is the device control 2 character (Caret: ^R).
    @C0 public static final char DC2 = 0x12;
    /// DC3 is the device control 3 character (Caret: ^S).
    @C0 public static final char DC3 = 0x13;
    /// DC4 is the device control 4 character (Caret: ^T).
    @C0 public static final char DC4 = 0x14;
    /// NAK is the negative acknowledge character (Caret: ^U).
    @C0 public static final char NAK = 0x15;
    /// SYN is the synchronous idle character (Caret: ^V).
    @C0 public static final char SYN = 0x16;
    /// ETB is the end of transmission block character (Caret: ^W).
    @C0 public static final char ETB = 0x17;
    /// CAN is the cancel character (Caret: ^X).
    @C0 public static final char CAN = 0x18;
    /// EM is the end of medium character (Caret: ^Y).
    @C0 public static final char EM = 0x19;
    /// SUB is the substitute character (Caret: ^Z).
    @C0 public static final char SUB = 0x1A;
    /// ESC is the escape character (Caret: ^[, Char: \e).
    @C0 public static final char ESC = 0x1B;
    /// FS is the file separator character (Caret: ^\).
    @C0 public static final char FS = 0x1C;
    /// GS is the group separator character (Caret: ^]).
    @C0 public static final char GS = 0x1D;
    /// RS is the record separator character (Caret: ^^).
    @C0 public static final char RS = 0x1E;
    /// US is the unit separator character (Caret: ^_).
    @C0 public static final char US = 0x1F;

    /// PAD is the padding character.
    @C1 public static final char PAD = 0x80;
    /// HOP is the high octet preset character.
    @C1 public static final char HOP = 0x81;
    /// BPH is the break permitted here character.
    @C1 public static final char BPH = 0x82;
    /// NBH is the no break here character.
    @C1 public static final char NBH = 0x83;
    /// IND is the index character.
    @C1 public static final char IND = 0x84;
    /// NEL is the next line character.
    @C1 public static final char NEL = 0x85;
    /// SSA is the start of selected area character.
    @C1 public static final char SSA = 0x86;
    /// ESA is the end of selected area character.
    @C1 public static final char ESA = 0x87;
    /// HTS is the horizontal tab set character.
    @C1 public static final char HTS = 0x88;
    /// HTJ is the horizontal tab with justification character.
    @C1 public static final char HTJ = 0x89;
    /// VTS is the vertical tab set character.
    @C1 public static final char VTS = 0x8A;
    /// PLD is the partial line forward character.
    @C1 public static final char PLD = 0x8B;
    /// PLU is the partial line backward character.
    @C1 public static final char PLU = 0x8C;
    /// RI is the reverse index character.
    @C1 public static final char RI = 0x8D;
    /// SS2 is the single shift 2 character.
    @C1 public static final char SS2 = 0x8E;
    /// SS3 is the single shift 3 character.
    @C1 public static final char SS3 = 0x8F;
    /// DCS is the device control string character.
    @C1 public static final char DCS = 0x90;
    /// PU1 is the private use 1 character.
    @C1 public static final char PU1 = 0x91;
    /// PU2 is the private use 2 character.
    @C1 public static final char PU2 = 0x92;
    /// STS is the set transmit state character.
    @C1 public static final char STS = 0x93;
    /// CCH is the cancel character.
    @C1 public static final char CCH = 0x94;
    /// MW is the message waiting character.
    @C1 public static final char MW = 0x95;
    /// SPA is the start of guarded area character.
    @C1 public static final char SPA = 0x96;
    /// EPA is the end of guarded area character.
    @C1 public static final char EPA = 0x97;
    /// SOS is the start of string character.
    @C1 public static final char SOS = 0x98;
    /// SGCI is the single graphic character introducer character.
    @C1 public static final char SGCI = 0x99;
    /// SCI is the single character introducer character.
    @C1 public static final char SCI = 0x9A;
    /// CSI is the control sequence introducer character.
    @C1 public static final char CSI = 0x9B;
    /// ST is the string terminator character.
    @C1 public static final char ST = 0x9C;
    /// OSC is the operating system command character.
    @C1 public static final char OSC = 0x9D;
    /// PM is the privacy message character.
    @C1 public static final char PM = 0x9E;
    /// APC is the application program command character.
    @C1 public static final char APC = 0x9F;
}
