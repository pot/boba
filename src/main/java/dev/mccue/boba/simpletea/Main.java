package dev.mccue.boba.simpletea;

import dev.mccue.ansi.Screen;
import dev.mccue.boba.tea.ProgramOpts;
import sun.misc.Signal;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        var program = new Counter();
        var model = new Model();

        ProgramOpts opts = ProgramOpts.builder()
                .startupTitle("hey")
                .build();

        program.run(model, opts);
        System.out.println("Program finished.");
    }
}
