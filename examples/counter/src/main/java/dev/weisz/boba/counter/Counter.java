package dev.weisz.boba.counter;

import dev.weisz.boba.tea.Cmd;
import dev.weisz.boba.tea.Msg;
import dev.weisz.boba.tea.Program;

public class Counter extends Program {
    private final Model model = new Model();

    @Override
    public Cmd update(Msg msg) {
        switch (msg) {
            case Increment _ -> {
                model.count++;
            }
            case Decrement _ -> {
                model.count--;
            }
            case Msg.KeyClickMsg(char key) -> {
                if (key == 'w') {
                    model.count++;
                } else if (key == 's') {
                    model.count--;
                } else if (key == 'q') {
                    System.exit(0);
                }
            }
            default -> {}
        }

        return null;
    }

    @Override
    public String view() {
        return "Count: " + model.count + "\n'w' to increment, 's' to decrement, 'q' to quit";
    }
}
