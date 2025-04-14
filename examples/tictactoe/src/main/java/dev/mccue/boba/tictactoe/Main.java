package dev.mccue.boba.tictactoe;

import dev.mccue.boba.tea.ProgramOpts;

public class Main {
    public static void main(String[] args) {
        var program = new TicTacToe();
        var model = new TicTacToeModel();

        program.run(model, ProgramOpts.builder()
                .startupTitle("Tic Tac Toe")
                .build());
    }
}
