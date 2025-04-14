package dev.mccue.boba;

import java.util.Optional;

public enum MouseButton {
    NONE,
    LEFT,
    MIDDLE,
    RIGHT,
    WHEEL_UP,
    WHEEL_DOWN,
    WHEEL_LEFT,
    WHEEL_RIGHT,
    BACKWARD,
    FORWARD,
    _10,
    _11;

    static Optional<MouseButton> fromInt(int i) {
        try {
            return Optional.of(MouseButton.values()[i]);
        } catch (IndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }

    @Override
    public String toString() {
        return switch (this) {
            case NONE -> "none";
            case LEFT -> "left";
            case MIDDLE -> "middle";
            case RIGHT -> "right";
            case WHEEL_UP -> "wheel up";
            case WHEEL_DOWN -> "wheel down";
            case WHEEL_LEFT -> "wheel left";
            case WHEEL_RIGHT -> "wheel right";
            case BACKWARD -> "backward";
            case FORWARD -> "forward";
            case _10 -> "button 10";
            case _11 -> "button 11";
        };
    }
}