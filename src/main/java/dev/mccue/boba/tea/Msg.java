package dev.mccue.boba.tea;

/**
 * Marker type for messages sent to a program which trigger
 * the {@link Program#update(Object, Msg)} method.
 */
public interface Msg {
    record SetTitleMsg(String title) implements Msg {}
    record WindowSizeMsg(int width, int height) implements Msg {}
}
