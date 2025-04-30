package dev.mccue.boba.terminal;

@FunctionalInterface
public interface Callback extends AutoCloseable {
    @Override
    void close() throws Exception;
}
