# Boba

A Java port of [Bubble Tea](https://github.com/charmbracelet/bubbletea), a powerful terminal UI framework. Boba enables the creation of rich terminal user interfaces including games using a functional, message-passing architecture.

## Requirements

- Java 22+
- Terminal with ANSI support

## Examples

### Simple Counter
A basic counter application demonstrating core concepts:

```java
public class Counter extends Program<Model> {
    @Override
    public UpdateResult<Model> update(Model model, Msg msg) {
        switch (msg) {
            case Msg.KeyClickMsg(char key) -> {
                if (key == 'w') model.count++;
                else if (key == 's') model.count--;
                else if (key == 'q') System.exit(0);
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
```

## Roadmap

- [ ] Non char inputs (arrow keys, control, combinations, ect..)
- [ ] Mouse scrolling support
- [ ] Layout API
- [ ] Min win size option. Can stop movement with native code by settings win size
- [ ] Look at MacTerminal#makeCooked (figure out why its not working)