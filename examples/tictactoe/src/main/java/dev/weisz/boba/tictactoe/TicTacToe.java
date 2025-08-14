package dev.weisz.boba.tictactoe;

import dev.weisz.boba.tea.Cmd;
import dev.weisz.boba.tea.Msg;
import dev.weisz.boba.tea.Program;
import dev.mccue.color.terminal.ANSIColor;
import dev.mccue.color.terminal.TerminalStyle;

public class TicTacToe extends Program {
    private final TicTacToeModel model;

    public TicTacToe() {
        this.model = new TicTacToeModel();
    }

    @Override
    public Cmd update(Msg msg) {
        switch (msg) {
            case Msg.KeyClickMsg(char key) -> {
                if (!model.active && key != 'q') {
                    return null;
                }

                switch (key) {
                    case 'w' -> {
                        if (model.selectedSlot[0] != 0) {
                            model.selectedSlot[0] = model.selectedSlot[0] - 1;
                        }
                    }
                    case 'a' -> {
                        if (model.selectedSlot[1] != 0) {
                            model.selectedSlot[1] = model.selectedSlot[1] - 1;
                        }
                    }
                    case 's' -> {
                        if (model.selectedSlot[0] != 2) {
                            model.selectedSlot[0] = model.selectedSlot[0] + 1;
                        }
                    }
                    case 'd' -> {
                        if (model.selectedSlot[1] != 2) {
                            model.selectedSlot[1] = model.selectedSlot[1] + 1;
                        }
                    }
                    case 'f' -> {
                        int selector = model.currentPlayer.toSlot();
                        if (model.board[model.selectedSlot[0]][model.selectedSlot[1]] == 0) {
                            model.board[model.selectedSlot[0]][model.selectedSlot[1]] = selector;
                            model.currentPlayer = model.currentPlayer == Selector.X ? Selector.O : Selector.X;
                            model.checkForWin();
                        }
                    }
                    case 'q' -> System.exit(0);
                }
            }
            default -> {}
        }

        return null;
    }

    @Override
    protected String view() {
        StringBuilder builder = new StringBuilder();
        builder.append("Welcome to Boba Tic Tac Toe! Use WASD to navigate cells and F to select.\n\n\n");

        builder.append("Current Turn: ")
                .append(model.currentPlayer != null ? model.currentPlayer : "None")
                .append("\n");

        int colIndex = 0;
        for (int[] col : model.board) {
            int rowIndex = 0;
            for (int row : col) {
                Selector slot = Selector.fromSlot(row);

                String cellValue = slot != null ? slot.toString() : " ";
                if (model.winner != null) {
                    int finalColIndex = colIndex;
                    int finalRowIndex = rowIndex;
                    if (model.winner.winSlots().stream().anyMatch(s -> s[0] == finalColIndex && s[1] == finalRowIndex)) {
                        builder.append(TerminalStyle.builder().backgroundColor(ANSIColor.BRIGHT_GREEN).apply(cellValue));
                    } else {
                        builder.append(cellValue);
                    }
                } else if (model.selectedSlot[0] == colIndex && model.selectedSlot[1] == rowIndex) {
                    builder.append(TerminalStyle.builder().backgroundColor(ANSIColor.BRIGHT_YELLOW).apply(cellValue));
                } else {
                    builder.append(cellValue);
                }

                if (rowIndex != 2) {
                    builder.append(" | ");
                }
                rowIndex++;
            }

            builder.append("\n");
            if (colIndex != 2) {
                builder.append("-- ".repeat(3));
                builder.append("\n");
            }
            colIndex++;
        }

        if (model.winner != null) {
            builder.append("\n\n")
                    .append(TerminalStyle.builder().backgroundColor(ANSIColor.BRIGHT_GREEN).apply("Winner: " + model.winner.winner()));
        }

        return builder.toString();
    }
}
