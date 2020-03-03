package com.glnosg.tictactoeultimate.game.board.board_view;

public interface OnBoardEventListener {

    void onColumnScreenRatioMeasured();
    void onCellClick(int column, int row);
    void onBoardScale(float scaleFactor);
}
