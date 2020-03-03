package com.glnosg.tictactoeultimate.game.board.cell;

public interface CellStateChangeObserver {

    void onCellStateChange(int column, int row, int newBitmapId);
}
