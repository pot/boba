package dev.mccue.boba.simpletea;

import dev.mccue.boba.tea.ProgramOpts;
import sun.misc.Signal;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var program = new Counter();
        var model = new Model();

        ProgramOpts opts = ProgramOpts.builder()
                        .build();

        program.run(opts);


        program.update(model, new Increment());
        program.update(model, new Increment());
        program.update(model, new Increment());

        var scanner = new Scanner(System.in);
        do {
            System.out.print("\u001b[2J\u001b[H");
            System.out.println(
                    program.view(model)
            );
            System.out.println();
            if ("d".equals(scanner.nextLine())) {
                program.update(model, new Decrement());
                System.out.println("abc");
            }
            else if ("i".equals(scanner.nextLine())) {
                program.update(model, new Increment());
            }
        } while (scanner.hasNextLine());
    }
}
