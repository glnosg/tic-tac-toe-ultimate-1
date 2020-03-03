package com.glnosg.tictactoeultimate.game.board.figure;

import com.glnosg.tictactoeultimate.R;

public class Blank implements Figure {

    private static final Object LOCK = new Object();
    private static Blank sInstance;

    private Blank(){}

    public static Blank getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new Blank();
            }
        }
        return sInstance;
    }

    @Override
    public int getHowManyAvailableShapes() {
        return 1;
    }

    @Override
    public int getRandomShapeIndex() {
        return 0;
    }

    @Override
    public int getSmallBitmapId(int shapeIndex, boolean isSolid, boolean isWinning) {
        return R.drawable.shape_blank;
    }

    @Override
    public int getMediumBitmapId(int shapeIndex, boolean isSolid, boolean isWinning) {
        return R.drawable.shape_blank;
    }

    @Override
    public int getBigBitmapId(int shapeIndex, boolean isSolid, boolean isWinning) {
        return R.drawable.shape_blank;
    }

    @Override
    public int getBigSolidBitmapId(int shapeIndex) {
        return R.drawable.shape_blank;
    }

    @Override
    public int getBigWinningBitmapId(int shapeIndex) {
        return R.drawable.shape_blank;
    }
}
