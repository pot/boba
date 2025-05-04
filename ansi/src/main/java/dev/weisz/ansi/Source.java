package dev.weisz.ansi;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Repeatable(Sources.class)
@interface Source {
    String value();
}
