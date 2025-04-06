package dev.mccue.boba.tea;

import java.util.List;
import java.util.function.Consumer;

record BatchCmd(List<Cmd> cmdList) implements Cmd {
    BatchCmd(List<Cmd> cmdList) {
        this.cmdList = List.copyOf(cmdList);
    }

    // TODO: re-implement

    @Override
    public Msg execute() throws Exception {
        return null;
    }
}
