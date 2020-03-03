package com.glnosg.tictactoeultimate.game.board.board_controller;

public interface BoardStateObserver {
    void onBoardInitialized(int boardId);
    void onBoardFull(int boardId);
    void onPlayerHasWon(int boardId, int winnerId);
}
