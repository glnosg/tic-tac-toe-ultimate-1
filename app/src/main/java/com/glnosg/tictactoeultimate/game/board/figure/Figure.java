package com.glnosg.tictactoeultimate.game.board.figure;



public interface Figure {
    int getHowManyAvailableShapes();
    int getRandomShapeIndex();
    int getSmallBitmapId(int shapeIndex, boolean isSolid, boolean isWinning);
    int getMediumBitmapId(int shapeIndex, boolean isSolid, boolean isWinning);
    int getBigBitmapId(int shapeIndex, boolean isSolid, boolean isWinning);
    int getBigSolidBitmapId(int shapeIndex);
    int getBigWinningBitmapId(int shapeIndex);
}


