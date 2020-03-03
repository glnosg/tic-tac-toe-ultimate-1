package com.glnosg.tictactoeultimate.game.board.cell.bitmap_helper;


import com.glnosg.tictactoeultimate.game.board.cell.Cell;
import com.glnosg.tictactoeultimate.game.board.figure.Figure;
import com.glnosg.tictactoeultimate.game.board.figure.FigureFactory;

public class BitmapHelper {

    private final int EMPTY_CELL = -1;

    private Cell mCell;

    private Figure mFigure;
    private int mFigureIndex, mBitmapId;

    private BitmapFetcher mBitmapFetcher;


    public BitmapHelper(Cell cell) {
        mCell = cell;
        mBitmapFetcher = new BigBitmapFetcher();
        setPlayerId(EMPTY_CELL);
    }

    public void updateBitmap() {
        mBitmapId = mBitmapFetcher
                .getBitmapId(mFigure, mFigureIndex, mCell.isActive(), mCell.isWinning());
    }

    public void setPlayerId(int playerId) {
        mFigure = FigureFactory.getShape(playerId);
        mFigureIndex = mFigure.getRandomShapeIndex();
        updateBitmap();
    }

    public void useBigBitmap() {
        mBitmapFetcher = new BigBitmapFetcher();
        updateBitmap();
    }

    public void useMediumBitmap() {
        mBitmapFetcher = new MediumBitmapFetcher();
        updateBitmap();
    }

    public void useSmallBitmap() {
        mBitmapFetcher = new SmallBitmapFetcher();
        updateBitmap();
    }

    public int getBitmapId() {
        return mBitmapId;
    }


}
