package dev.weisz.boba;

public enum MouseAction {
    PRESS,
    RELEASE,
    MOTION;

    static MouseAction fromInt(int i) {
        try {
            return MouseAction.values()[i];
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException(String.valueOf(i));
        }
    }

    @Override
    public String toString() {
        return switch (this) {
            case PRESS -> "press";
            case RELEASE -> "release";
            case MOTION -> "motion";
        };
    }
}