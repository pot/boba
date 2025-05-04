package dev.weisz.boba.counter;

import dev.weisz.boba.tea.ProgramOpts;
import dev.weisz.boba.terminal.WinSize;

public class Main {
    public static void main(String[] args) {
        var program = new Counter();
        var model = new Model();

        ProgramOpts opts = ProgramOpts.builder()
                .startupTitle("Counter")
                .build();

        program.run(model, opts);
    }
}
