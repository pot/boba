package dev.mccue.ansi;

import java.lang.annotation.*;

/// C1 control characters.
///
/// These range from (0x80-0x9F) as defined in ISO 6429 (ECMA-48).
/// @see <a href="https://en.wikipedia.org/wiki/C0_and_C1_control_codes">https://en.wikipedia.org/wiki/C0_and_C1_control_codes</a>
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface C1 {
    char START = ControlCharacters.PAD;
    char END = ControlCharacters.APC;
}
