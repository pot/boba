package dev.weisz.boba.tea;

/**
 * Marker type for messages sent to a program which trigger
 * the {@link Program#update(Object, Msg)} method.
 */
public interface Msg {
    record QuitMsg() implements Msg {}
    record InteruptMsg() implements Msg {}
    record SetTitleMsg(String title) implements Msg {}
    record WindowSizeMsg(int height, int width) implements Msg {}
    record RepaintMsg() implements Msg {}
    record ClearScrollAreaMsg() implements Msg {}
    record ScrollUpMsg(String[] lines, int topBoundary, int bottomBoundary) implements Msg {}
    record SyncScrollAreaMsg() implements Msg {}
    record ScrollDownMsg(String[] lines, int topBoundary, int bottomBoundary) implements Msg {}
    record PrintLineMsg(String line) implements Msg {}
    record ClearScreenMsg() implements Msg {}
    record KeyClickMsg(char key) implements Msg {}
}
