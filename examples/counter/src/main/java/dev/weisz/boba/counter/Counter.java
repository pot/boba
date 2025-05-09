package dev.weisz.boba.counter;

import dev.weisz.boba.tea.Msg;
import dev.weisz.boba.tea.Program;
import dev.weisz.boba.tea.UpdateResult;

public class Counter extends Program<Model> {
    @Override
    public UpdateResult<Model> update(Model model, Msg msg) {
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

        return new UpdateResult<>(model, null);
    }

    @Override
    public String view(Model model) {
        return "Count: " + model.count + "\n'w' to increment, 's' to decrement, 'q' to quit";
    }
}
