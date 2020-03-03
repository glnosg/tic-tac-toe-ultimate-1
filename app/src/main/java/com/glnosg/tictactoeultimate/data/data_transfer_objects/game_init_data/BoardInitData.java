package com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data;

import com.google.gson.annotations.SerializedName;

public class BoardInitData {

    public static final int COLUMN_INDEX = 0, ROW_INDEX = 1;
    public static final int MAIN_BOARD_ID = 0;
    public static final int BOARD_TYPE_CLASSICAL_ULTIMATE = -1, BOARD_TYPE_SINGLE = 0;
    public static final int BOARD_SIZE_DEFAULT = 3, HOW_MANY_TO_WIN_DEFAULT = 3;
    public static final int EMPTY_CELL = -1;

    @SerializedName("columns")
    private int mNumberOfColumns;
    @SerializedName("rows")
    private int mNumberOfRows;
    @SerializedName("how_many_to_win")
    private int mHowManyInLineToWin;
    @SerializedName("board_type")
    private int mBoardType;

    /**
     *
     * @param boardType:
     *                 -1 - classical ultimate,
     *                  0 - single board,
     *                1-9 - custom ultimate, value determines number of randomly chosen child boards,
     *                      that remain active in each round
     */
    public BoardInitData(int boardType) {
        mNumberOfColumns = BOARD_SIZE_DEFAULT;
        mNumberOfRows = BOARD_SIZE_DEFAULT;
        mHowManyInLineToWin = HOW_MANY_TO_WIN_DEFAULT;
        mBoardType = boardType;
    }

    /**
     *
     * @param numberOfColumns - number of columns in a single board (in an ultimate mode the value applies to child boards)
     * @param numberOfRows - number of rows in single board (in an ultimate mode the value applies to child boards)
     */
    public BoardInitData(
            int numberOfColumns, int numberOfRows, int howManyInLineToWin, int boardType) {
        mNumberOfColumns = numberOfColumns;
        mNumberOfRows = numberOfRows;
        mHowManyInLineToWin = howManyInLineToWin;
        mBoardType = boardType;
    }

    public int getNumberOfColumns() {
        return mNumberOfColumns;
    }

    public int getNumberOfRows() {
        return mNumberOfRows;
    }

    public int getHowManyInLineToWin() {
        return mHowManyInLineToWin;
    }

    public int getBoardType() {
        return mBoardType;
    }
}
