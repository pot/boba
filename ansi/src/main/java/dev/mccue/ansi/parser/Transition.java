package dev.mccue.ansi.parser;

import org.intellij.lang.annotations.MagicConstant;

record Transition(
        @MagicConstant(flagsFromClass = State.class) int state,
        @MagicConstant(flagsFromClass = Action.class) int action
) {
}
