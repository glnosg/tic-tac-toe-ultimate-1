package com.glnosg.tictactoeultimate.game.board.board_controller.cells_deactivator;

import com.glnosg.tictactoeultimate.game.board.board_controller.BoardController;

public abstract class CellsDeactivator {
    protected int mHowManyRemainActive;
    protected BoardController mBoardController;

    public CellsDeactivator(int howManyCellsRemainActive, BoardController boardController) {

        mHowManyRemainActive = howManyCellsRemainActive;
        mBoardController = boardController;
    }

    /**
     *
     * @param cellClickedDuringLastMove - int[0] = column, int[1] = row
     */
    public abstract void deactivate (int[] cellClickedDuringLastMove);

    public int getHowManyRemainActive() {
        return mHowManyRemainActive;
    }
}
