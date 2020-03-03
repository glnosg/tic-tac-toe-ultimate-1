package com.glnosg.tictactoeultimate.game.board.cell.bitmap_helper;

import com.glnosg.tictactoeultimate.game.board.figure.Figure;

public class BigBitmapFetcher implements BitmapFetcher {

    @Override
    public int getBitmapId(Figure figure, int figureIndex, boolean isActive, boolean isWinning) {
        return figure.getBigBitmapId(figureIndex, isActive, isWinning);
    }
}
