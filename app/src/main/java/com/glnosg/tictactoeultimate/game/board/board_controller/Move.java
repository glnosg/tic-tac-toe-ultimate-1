package com.glnosg.tictactoeultimate.game.board.board_controller;

public class Move {

    private int mBoardId;
    private int mColumn, mRow;

    public Move(int boardId, int column, int row) {
        mBoardId = boardId;
        mColumn = column;
        mRow = row;
    }

    public int getBoardId() {
        return mBoardId;
    }

    public int getColumn() {
        return mColumn;
    }

    public int getRow() {
        return mRow;
    }
}
