package dev.mccue.boba.simpletea;

import dev.mccue.ansi.Screen;
import dev.mccue.boba.tea.Cmd;
import dev.mccue.boba.tea.Msg;
import dev.mccue.boba.tea.Program;
import dev.mccue.boba.tea.UpdateResult;

public class Counter extends Program<Model, String> {
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
        return "Count: " + model.count + "\n'w' to increment, 's' to decrement, 'q' to quit\n";
    }
}
