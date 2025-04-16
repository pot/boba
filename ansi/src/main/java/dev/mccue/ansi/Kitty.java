package dev.mccue.ansi;

import java.lang.annotation.*;

/// Kitty keyboard protocol progressive enhancement flags.
/// @see <a href="https://sw.kovidgoyal.net/kitty/keyboard-protocol/#progressive-enhancement">https://sw.kovidgoyal.net/kitty/keyboard-protocol/#progressive-enhancement</a>
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Kitty {
}
