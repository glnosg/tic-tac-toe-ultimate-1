package com.glnosg.tictactoeultimate.game.board.cell.bitmap_helper;

import com.glnosg.tictactoeultimate.game.board.figure.Figure;

public class SmallBitmapFetcher implements BitmapFetcher {

    @Override
    public int getBitmapId(Figure figure, int figureIndex, boolean isActive, boolean isWinning) {
        return figure.getSmallBitmapId(figureIndex, isActive, isWinning);
    }
}
