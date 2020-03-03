package com.glnosg.tictactoeultimate.game.board.board_controller.victory_check;

import java.util.List;

public interface VictoryChecker {

    boolean isWinner(List<int[]> movesList, int howManyInLineToWin);
    List<int[]> getWinningCells();
}
