package dev.mccue.boba.tea;

import java.util.List;
import java.util.function.Consumer;

/**
 * A group of messages which are executed together, there is no specific order
 * for them.
 *
 * @param cmdList
 */
record BatchCmd(List<Cmd> cmdList) implements Cmd {

    record BatchMsg(List<Cmd> cmdList) implements Msg {}

    public BatchCmd(Cmd... cmds) {
        this(List.of(cmds));
    }

    @Override
    public Msg execute() {
        return new BatchMsg(cmdList);
    }
}
