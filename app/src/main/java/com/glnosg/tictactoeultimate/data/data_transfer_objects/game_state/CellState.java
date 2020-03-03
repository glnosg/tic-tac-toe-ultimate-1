package com.glnosg.tictactoeultimate.data.data_transfer_objects.game_state;

import com.google.gson.annotations.SerializedName;

import static com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.BoardInitData.EMPTY_CELL;

public class CellState {

    @SerializedName("column")
    private int mColumn;
    @SerializedName("row")
    private int mRow;
    @SerializedName("playerId")
    private int mPlayerId;
    @SerializedName("isActive")
    private boolean mIsActive;
    @SerializedName("isLocked")
    private boolean mIsLocked;
    @SerializedName("isWinning")
    private boolean mIsWinning;

    public CellState(int column, int row) {
        mColumn = column;
        mRow = row;
        mPlayerId = EMPTY_CELL;
        mIsActive = true;
        mIsLocked = false;
        mIsWinning = false;
    }

    public CellState (
            int column, int row, int playerId, boolean isActive, boolean isLocked, boolean isWinning) {
        mColumn = column;
        mRow = row;
        mPlayerId = playerId;
        mIsActive = isActive;
        mIsLocked = isLocked;
        mIsWinning = isWinning;
    }

    public int getColumn() {
        return mColumn;
    }

    public int getRow() {
        return mRow;
    }

    public int getPlayerId() {
        return mPlayerId;
    }

    public boolean isActive() {
        return mIsActive;
    }

    public boolean isLocked() {
        return mIsLocked;
    }

    public boolean isWinning() {
        return mIsWinning;
    }
}
