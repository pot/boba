package dev.mccue.boba.counter;

import dev.mccue.boba.tea.ProgramOpts;

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
