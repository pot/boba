package dev.weisz.boba.tictactoe;

import java.util.List;

public record WinResult(
        Selector winner,
        List<int[]> winSlots
) {
}
