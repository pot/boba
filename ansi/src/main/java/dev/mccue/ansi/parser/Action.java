package dev.mccue.ansi.parser;

import org.intellij.lang.annotations.MagicConstant;

final class Action {
    private Action() {}

    static final int NONE = 0;
    static final int CLEAR = 1;
    static final int COLLECT = 2;
    static final int MARKER = 3;
    static final int DISPATCH = 4;
    static final int EXECUTE = 5;
    static final int START = 6; // Start of a data string
    static final int PUT = 7;   // Put into the data string
    static final int PARAM = 8;
    static final int PRINT = 9;
    static final int IGNORE = NONE;

    static final String[] names = {
            "NoneAction",
            "ClearAction",
            "CollectAction",
            "MarkerAction",
            "DispatchAction",
            "ExecuteAction",
            "StartAction",
            "PutAction",
            "ParamAction",
            "PrintAction",
    };

    static String name(
            @MagicConstant(flagsFromClass = Action.class) int value
    ) {
        return names[value];
    }
}
