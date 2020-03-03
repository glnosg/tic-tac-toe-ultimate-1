package com.glnosg.tictactoeultimate.game;

import com.glnosg.tictactoeultimate.game.board.board_controller.BoardController;
import com.glnosg.tictactoeultimate.game.board.board_controller.BoardStateObserver;
import com.glnosg.tictactoeultimate.game.player.Player;

import java.util.List;

public interface GamePresenter {
    void setIsBotSupported(boolean isBotSupported);
    void setIsAutoSaveOn(boolean isAutoSaveOn);
    void onPlayersInitialized(List<Player> players);
    void setPlayerStartingRoundId(int id);
    void onBoardControllerInitialized(BoardController boardController);
    void onSaveGameClicked();
    void onSignOutClicked();
    void onExitClicked();
    void onBackPressed();
    void onActivityPause();
    void onActivityResume();
    void onActivityDestroy();
    BoardStateObserver getBoardStateObserver();
}
