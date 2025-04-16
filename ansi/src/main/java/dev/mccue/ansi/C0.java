package dev.mccue.ansi;

import java.lang.annotation.*;

/// C0 control characters.
///
/// These range from (0x00-0x1F) as defined in ISO 646 (ASCII).
/// @see <a href="https://en.wikipedia.org/wiki/C0_and_C1_control_codes">
///     https://en.wikipedia.org/wiki/C0_and_C1_control_codes</a>
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface C0 {
    char START = ControlCharacters.NUL;
    char END = ControlCharacters.US;
}

