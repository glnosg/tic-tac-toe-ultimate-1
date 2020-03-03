package com.glnosg.tictactoeultimate.game.board.board_view;


public interface BoardView {

    void updateCell(int column, int row, int shapeId);
    void clearCell(int column, int row);

    void drawImageOnTopOfBoard(int imageId);
    void removeImageOnTopOfBoard();

    void makeGridTransparent();
    void makeGridSolid();
    void zoomOut();

    void setBoardBackgroundColor(int color);
    void setGridColorSolid(int color);
    void setGridColorTransparent(int color);

    float getBaseColumnScreenRatio();
    void invalidate();

    void registerOnBoardEventListener(OnBoardEventListener onBoardEventListener);
    void removeOnBoardEventListener(OnBoardEventListener onBoardEventListener);
}
