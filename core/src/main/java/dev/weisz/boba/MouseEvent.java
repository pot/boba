package dev.weisz.boba;

import java.util.EnumSet;
import java.util.Objects;

public record MouseEvent(
        int x,
        int y,
        boolean shift,
        boolean alt,
        boolean ctrl,
        MouseAction action,
        MouseButton button
) {
    public MouseEvent {
        Objects.requireNonNull(action, "action");
        Objects.requireNonNull(button, "button");
    }

    public boolean isWheel() {
        return EnumSet.of(
                MouseButton.WHEEL_UP,
                MouseButton.WHEEL_DOWN,
                MouseButton.WHEEL_LEFT,
                MouseButton.WHEEL_RIGHT
        ).contains(button);
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        if (ctrl) {
            sb.append("ctrl+");
        }
        if (alt) {
            sb.append("alt+");
        }
        if (shift) {
            sb.append("shift+");
        }

        if (button == MouseButton.NONE) {
            if (action == MouseAction.MOTION || action == MouseAction.RELEASE) {
                sb.append(action);
            }
            else {
                sb.append("unknown");
            }
        }
        else if (isWheel()) {
            sb.append(button);
        }
        else {
            sb.append(button);
            sb.append(" ").append(action);
            /*
            else {
                btn := mouseButtons[m.Button]
                if btn != "" {
                    s += btn
                }
                act := mouseActions[m.Action]
                if act != "" {
                    s += " " + act
                }
            }
             */
        }

        return sb.toString();
    }
}
