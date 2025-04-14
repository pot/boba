package dev.mccue.boba.tictactoe;

public enum Selector {
    X,
    O;

    public static Selector fromSlot(int row) {
        if (row == 1) {
            return X;
        }

        if (row == 2) {
            return O;
        }

        return null;
    }

    public int toSlot() {
        return this == X ? 1 : 2;
    }
}
