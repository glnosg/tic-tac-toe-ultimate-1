package com.glnosg.tictactoeultimate.game.board.board_controller.victory_check;

import android.util.Log;

import java.util.LinkedList;
import java.util.List;

public class VictoryCheckerAllDirections implements VictoryChecker {

    private List<VictoryChecker> mWinningCheckers;
    private List<int[]> mWinningCells;

    public VictoryCheckerAllDirections() {
        mWinningCells = new LinkedList<>();
        mWinningCheckers = new LinkedList<>();
        mWinningCheckers.add(new VictoryCheckerVertical());
        mWinningCheckers.add(new VictoryCheckerHorizontal());
        mWinningCheckers.add(new VictoryCheckerDiagonal());
    }

    @Override
    public List<int[]> getWinningCells() {
        return mWinningCells;
    }

    @Override
    public boolean isWinner(List<int[]> moves, int howManyInLineToWin) {
        if (moves.size() < howManyInLineToWin) {
            return false;
        } else {
            for (VictoryChecker currentChecker : mWinningCheckers) {
                if (currentChecker.isWinner(moves, howManyInLineToWin)) {
                    mWinningCells = currentChecker.getWinningCells();
                    return true;
                }
            }
        }
        return false;
    }
}
