package com.glnosg.tictactoeultimate.game;

import com.glnosg.tictactoeultimate.game.board.board_view.BoardView;
import com.glnosg.tictactoeultimate.game.view_managers.GameButtonsManager;
import com.glnosg.tictactoeultimate.game.view_managers.GameMessageDisplayManager;

import java.util.List;

import androidx.loader.app.LoaderManager;
import com.glnosg.tictactoeultimate.game.view_managers.JoinerCandidatesDisplayManager;
import com.glnosg.tictactoeultimate.game.view_managers.LocalPlayersDisplayManager;
import com.glnosg.tictactoeultimate.game.view_managers.NetworkPlayersDisplayManager;
import com.glnosg.tictactoeultimate.game.view_managers.TimeCounterDisplayManager;

public interface GameView {
    void showBoardDisplay();
    void hideBoardDisplay();
    void setSingleBoardView(BoardView boardView);
    void setUltimateBoardView(List<BoardView> singleBoardViews);

    void showLoadingIndicator();
    void hideLoadingIndicator();
    void showGameSavedMessage();
    void showErrorMessage();
    void showMessageCantSave();
    void showLeaveLoseDialog(LeaveLoseDialogOnClickListener listener);
    void hideLeaveLoseDialog();
    void showMessageCantSignOut();
    void signOut();
    void exitGameActivity();
    void exitTheApp();

    LocalPlayersDisplayManager getLocalPlayersDisplayManager();
    NetworkPlayersDisplayManager getNetworkPlayersDisplayManager();
    JoinerCandidatesDisplayManager getJoinerCandidatesDisplayManager();
    GameMessageDisplayManager getMessageDisplayManager();
    GameButtonsManager getButtonsManager();
    TimeCounterDisplayManager getTimeCounterDisplayManager();
    LoaderManager getActivityLoaderManager();
}
