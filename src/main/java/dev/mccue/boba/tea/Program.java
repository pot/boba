package dev.mccue.boba.tea;

import dev.mccue.boba.Renderer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// TODO: this needs to be stateful and should likely be an abstract class instead
public abstract class Program<Model, View extends String> {
    private final BlockingQueue<Msg> msgQueue = new LinkedBlockingQueue<>();
    private Renderer renderer;

    protected abstract UpdateResult<Model> update(Model model, Msg msg);
    protected abstract View view(Model model);

    public Model run(ProgramOpts opts) {
        return null;
    }

    private Model eventLoop(Model model) throws InterruptedException {
        while (true) {
            Msg msg = msgQueue.take();

            switch (msg) {
                case Msg.SetTitleMsg(String title) ->
                    renderer.setWindowTitle(title);

                default -> { /* ignore */ }
            }

            UpdateResult<Model> updateResult = update(model, msg);
            if (updateResult.cmd() != null) {
                // handle command async
                processCmd(updateResult.cmd());
            }

            renderer.write(view(model));
        }
    }

    private void processCmd(Cmd cmd) {
        Thread.startVirtualThread(() -> {
            Msg msg = msgQueue.poll();
            if (msg != null) {
                msgQueue.add(msg);
            }
        });
    }
}
