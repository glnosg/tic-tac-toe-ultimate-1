package com.glnosg.tictactoeultimate.game.board.cell.bitmap_helper;

import com.glnosg.tictactoeultimate.game.board.figure.Figure;

public interface BitmapFetcher {
    int getBitmapId(Figure figure, int figureIndex, boolean isActive, boolean isWinning);
}
