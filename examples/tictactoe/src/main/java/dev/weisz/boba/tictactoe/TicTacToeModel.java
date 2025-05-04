package dev.weisz.boba.tictactoe;

import java.util.List;
import java.util.Random;

public class TicTacToeModel {
    private static final Random RANDOM = new Random();

    // 0 represents an empty space
    // 1 represents an x
    // 2 represents an o
    public int[][] board;
    public Selector currentPlayer;

    // col row
    public final int[] selectedSlot = new int[]{0, 0};

    boolean active = true;
    WinResult winner;

    public TicTacToeModel() {
        this.board = new int[3][3];
        currentPlayer = RANDOM.nextInt(2) == 0 ? Selector.X : Selector.O;
    }


    public void checkForWin() {
        WinResult result = null;
        WinResult horizontal = checkHorizontal();
        WinResult vertical = checkVertical();
        WinResult diagonal = checkDiagonal();

        if (horizontal != null) {
            result = horizontal;
        }
        else if (vertical != null) {
            result = vertical;
        }
        else if (diagonal != null) {
            result = diagonal;
        }

        if (result != null) {
            active = false;
            winner = result;
            selectedSlot[0] = -1;
            selectedSlot[1] = -1;
        }
    }

    private WinResult checkHorizontal() {
        for (int row = 0; row < 3; row++) {
            int first = board[0][row];
            if (first == 0) {
                continue;
            }

            if (board[1][row] == first && board[2][row] == first) {
                List<int[]> slots = List.of(
                        new int[]{0, row},
                        new int[]{1, row},
                        new int[]{2, row}
                );
                return new WinResult(Selector.fromSlot(first), slots);
            }
        }

        return null;
    }

    private WinResult checkVertical() {
        for (int col = 0; col < 3; col++) {
            int first = board[col][0];
            if (first == 0) {
                continue;
            }

            if (board[col][1] == first && board[col][2] == first) {
                List<int[]> slots = List.of(
                        new int[]{col, 0},
                        new int[]{col, 1},
                        new int[]{col, 2}
                );
                return new WinResult(Selector.fromSlot(first), slots);
            }
        }

        return null;
    }
    private WinResult checkDiagonal() {
        int first = board[0][0];
        if (first != 0 && board[1][1] == first && board[2][2] == first) {
            return new WinResult(
                    Selector.fromSlot(first),
                    List.of(new int[] {0, 0}, new int[] {1, 1}, new int[] {2, 2})
            );
        }

        first = board[0][2];
        if (first != 0 && board[1][1] == first && board[2][0] == first) {
            return new WinResult(
                    Selector.fromSlot(first),
                    List.of(new int[] {0, 2}, new int[] {1, 1}, new int[] {2, 0})
            );
        }

        return null;
    }

}
