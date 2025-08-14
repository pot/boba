package dev.weisz.boba.tictactoe;

import dev.weisz.boba.tea.ProgramOpts;

public class Main {
    public static void main(String[] args) {
        var program = new TicTacToe();

        program.run(ProgramOpts.builder()
                .startupTitle("Tic Tac Toe")
                .build());
    }
}
