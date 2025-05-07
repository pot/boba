# Boba
![Maven Central Version](https://img.shields.io/maven-central/v/dev.weisz/boba)


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

## Logging
We use the slf4j logging framework. You will need to include a logging implementation like logback-classic as seen
below. There is a default logback.xml provided so the actual program isn't interrupted by unexpected logging. By default,
it logs to a file called app.log, but it is highly recommended to either disable logging completely or log to your own file.

```xml
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
        </dependency>
```

## Roadmap

- [ ] Non char inputs (arrow keys, control, combinations, ect..)
- [ ] Mouse scrolling support
- [ ] Layout API
- [ ] Min win size option. Can stop movement with native code by settings win size
- [ ] Look at MacTerminal#makeCooked (figure out why its not working)

## Versioning
We follow the [Calendar Versioning](https://calver.org) schema (YYYY.M.D[.MODIFIER])
