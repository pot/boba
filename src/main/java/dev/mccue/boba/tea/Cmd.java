package dev.mccue.boba.tea;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@FunctionalInterface
public interface Cmd {
    /**
     * Executes the command.
     */
    Msg execute() throws Exception;

    static Cmd batch(Cmd... cmds) {
        var cmdsArrayList = new ArrayList<Cmd>();
        for (var cmd : cmds) {
            if (cmd instanceof BatchCmd(List<Cmd> cmdList)) {
                cmdsArrayList.addAll(cmdList);
            }
            else {
                cmdsArrayList.add(cmd);
            }
        }

        return new BatchCmd(cmdsArrayList);
    }
}
