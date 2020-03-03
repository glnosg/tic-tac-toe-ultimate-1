package com.glnosg.tictactoeultimate.game.board.cell;

import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_state.CellState;
import com.glnosg.tictactoeultimate.game.board.cell.bitmap_helper.BitmapHelper;

import static com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.BoardInitData.EMPTY_CELL;

public class Cell implements CellStateChangeObservable {

    private int mColumn, mRow;
    private CellStateChangeObserver mStateChangeObserver;

    private int mPlayerId;
    private boolean mIsActive, mIsLocked, mIsWinning;

    private BitmapHelper mBitmapHelper;

    public Cell(int column, int row) {
        mColumn = column;
        mRow = row;
        mPlayerId = EMPTY_CELL;
        mIsActive = true;
        mIsLocked = false;
        mIsWinning = false;
        mBitmapHelper = new BitmapHelper(this);
    }

    public Cell(CellState cellState) {
        mColumn = cellState.getColumn();
        mRow = cellState.getRow();
        mPlayerId = cellState.getPlayerId();
        mIsActive = cellState.isActive();
        mIsLocked = cellState.isLocked();
        mIsWinning = cellState.isWinning();
        mBitmapHelper = new BitmapHelper(this);
        mBitmapHelper.setPlayerId(mPlayerId);
        notifyObserver();
    }

    public void clear() {
        if (isTaken()) {
            mBitmapHelper.setPlayerId(EMPTY_CELL);
            notifyObserver();
            mPlayerId = EMPTY_CELL;
        }
        mIsActive = true;
        mIsLocked = false;
        mIsWinning = false;
    }

    public void activate() {
        mIsActive = true;
        mBitmapHelper.updateBitmap();
        notifyObserver();
    }

    public void deactivate() {
        mIsActive = false;
        mBitmapHelper.updateBitmap();
        notifyObserver();
    }

    public boolean isActive() {
        return mIsActive;
    }

    public void lock() {
        mIsLocked = true;
    }

    public void unlock() {
        mIsLocked = false;
    }

    public boolean isLocked() {
        return mIsLocked;
    }

    public boolean isTaken() {
        return mPlayerId != -1;
    }

    public boolean isClickable() {
        return mIsActive && !mIsLocked && !isTaken();
    }

    public void assignPlayer(int playerId) {
            mPlayerId = playerId;
            mBitmapHelper.setPlayerId(mPlayerId);
            mIsLocked = true;
            notifyObserver();
    }

    public void markAsWinning() {
       mIsWinning = true;
        mBitmapHelper.updateBitmap();
       notifyObserver();
    }

    public boolean isWinning() {
        return mIsWinning;
    }

    public void useSmallBitmap() {
        mBitmapHelper.useSmallBitmap();
        notifyObserver();
    }

    public void useMediumBitmap() {
        mBitmapHelper.useMediumBitmap();
        notifyObserver();
    }

    public void useBigBitmap() {
        mBitmapHelper.useBigBitmap();
        notifyObserver();
    }

    public int getPlayerId() {
        return mPlayerId;
    }

    @Override
    public void registerObserver(CellStateChangeObserver observer) {
        mStateChangeObserver = observer;
        notifyObserver();
    }

    @Override
    public void unregisterObserver() {
        mStateChangeObserver = null;
    }

    @Override
    public void notifyObserver() {
        if (mStateChangeObserver != null && isTaken()) {
            mStateChangeObserver.onCellStateChange(mColumn, mRow, mBitmapHelper.getBitmapId());
        }
    }
}
