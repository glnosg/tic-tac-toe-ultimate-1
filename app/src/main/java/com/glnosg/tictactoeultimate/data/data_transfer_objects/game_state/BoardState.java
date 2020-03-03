package com.glnosg.tictactoeultimate.data.data_transfer_objects.game_state;

import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.BoardInitData;
import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;

import static com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.BoardInitData.EMPTY_CELL;


public class BoardState {

    private int mNumberOfColumns, mNumberOfRows;

    @SerializedName("isActive")
    private boolean mIsActive;
    @SerializedName("isLocked")
    private boolean mIsLocked;
    @SerializedName("howManyInLineToWin")
    private int mHowManyInLineToWin;
    @SerializedName("boardType")
    private int mBoardType;
    @SerializedName("cells")
    private CellState[][] mCells;
    @SerializedName("childBoards")
    private BoardState[][] mChildBoards;

    public BoardState(){}

    /**
     *
     * @param childBoards - if null it's not an ultimate board
     */
    public BoardState (
            boolean isActive,
            boolean isLocked,
            int howManyInLineToWin,
            int boardType,
            @NonNull CellState[][] cells,
            BoardState[][] childBoards) {
        mIsActive = isActive;
        mIsLocked = isLocked;
        mBoardType = boardType;
        mCells = cells;
        if (mBoardType != BoardInitData.BOARD_TYPE_SINGLE) {
            mHowManyInLineToWin = BoardInitData.HOW_MANY_TO_WIN_DEFAULT;
            mChildBoards = childBoards;
        } else {
            mHowManyInLineToWin = howManyInLineToWin;
        }
        setBoardSizeVariables();
    }

    private void setBoardSizeVariables() {
        mNumberOfColumns = mCells.length;
        mNumberOfRows = mCells[0].length;
    }

    public int getNumberOfColumns() {
        return mNumberOfColumns;
    }

    public int getNumberOfRows() {
        return mNumberOfRows;
    }

    public int getNumberOfCells() {
        return mNumberOfColumns * mNumberOfRows;
    }

    public int getBoardType() {
        return mBoardType;
    }

    public boolean isActive() {
        return mIsActive;
    }

    public boolean isLocked() {
        return mIsLocked;
    }

    public boolean isUltimate() {
        return mBoardType != BoardInitData.BOARD_TYPE_SINGLE;
    }

    public boolean isCellTaken(int cellIndex) {
        int column = getColumnBasedOnIndex(cellIndex);
        int row = getRowBasedOnIndex(cellIndex);
        return mCells[column][row].getPlayerId() != EMPTY_CELL;
    }

    public boolean isBoardEmpty() {
        for (int i = 0; i < getNumberOfCells(); i++) {
            if (isCellTaken(i)) return false;
        }
        return true;
    }

    public int getPlayerFromCell(int cellIndex) {
        int column = getColumnBasedOnIndex(cellIndex);
        int row = getRowBasedOnIndex(cellIndex);
        return mCells[column][row].getPlayerId();
    }

    public int getColumnBasedOnIndex(int cellIndex) {
        return cellIndex % mNumberOfColumns;
    }

    public int getRowBasedOnIndex(int cellIndex) {
        return cellIndex / mNumberOfColumns;
    }

    public int getHowManyInLineToWin() {
        return mHowManyInLineToWin;
    }

    public CellState getCellState(int column, int row) {
        if (column < 0 || column >= mNumberOfColumns)
            throw new IndexOutOfBoundsException("Column " + column + " is out of bounds!");
        else if (row < 0 || row >= mNumberOfRows)
            throw new IndexOutOfBoundsException("Row " + row + " is out of bounds!");
        else
            return mCells[column][row];
    }

    public BoardState getChildBoard(int column, int row) {
        if (column < 0 || column >= mNumberOfColumns)
            throw new IndexOutOfBoundsException("Column " + column + " is out of bounds!");
        else if (row < 0 || row >= mNumberOfRows)
            throw new IndexOutOfBoundsException("Row " + row + " is out of bounds!");
        else
            return mChildBoards[column][row];
    }
}
