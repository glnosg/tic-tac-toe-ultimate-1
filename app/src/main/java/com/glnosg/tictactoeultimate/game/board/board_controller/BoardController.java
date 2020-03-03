package com.glnosg.tictactoeultimate.game.board.board_controller;

import com.glnosg.tictactoeultimate.game.board.board_view.BoardView;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.BoardInitData;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_state.BoardState;

import java.util.List;

public interface BoardController {
    void setBoardId(int id);
    void initBoard(BoardInitData initData);
    void loadBoard(BoardState state);
    void clearBoard();
    void lockBoard();
    void unlockBoard();
    void activateBoard();
    void deactivateBoard();
    boolean isBoardActive();
    void activateCell(int cellIndex);
    void deactivateCell(int cellIndex);
    boolean isCellActive(int cellIndex);
    boolean isGameFinished();
    int getNumberOfCells();
    int getNumberOfSingleBoardColumns();
    int getNumberOfSingleBoardRows();
    void assignPlayerToCell(int boardId, int column, int row, int playerId);
    void zoomOut();
    void drawImageOnTopOfBoard(int bitmapId);
    List<BoardView> getBoardViews();
    BoardState getBoardState();
    void registerCellChoiceObserver(CellChoiceObserver observer);
    void registerBoardStateObserver(BoardStateObserver observer);
}
