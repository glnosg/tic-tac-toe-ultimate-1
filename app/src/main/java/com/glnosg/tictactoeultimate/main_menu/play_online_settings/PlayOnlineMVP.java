package com.glnosg.tictactoeultimate.main_menu.play_online_settings;

import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGame;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGamePlayer;

import java.util.Map;

public interface PlayOnlineMVP {

    interface SettingsView {
        void setStartButtonEnabled(boolean isEnabled);
        void showLoggingScreen();
        void showLoadingIndicator();
        void hideLoadingIndicator();
        void showEmptyDatabaseMessage();
        void hideEmptyDatabaseMessage();
        void setNameInputHint(String nameHint);
        void openGame(String gameId);
        void showWaitingForResponseDialog();
        void cancelWaitingForResponseDialog();
        String getDisplayName();
        OpenGamesDisplayManager getOpenGamesDisplayManager();
    }

    interface  SettingsPresenter {
        void onUserSignedOut();
        void onUserSignedIn(String userName, int gamesPlayed, int gamesWon, int gamesLeft);
        void onHasUnfinishedGame(String gameId);
        void onUltimateSwitched(boolean isUltimate);
        void onStartClicked();
        void onGameCreated(String gameId);
        void onWaitingForResponseCancelled();
        void onActivityPause();
        void onActivityResume();
    }

    interface SettingsModel {
        void attachAuthListener();
        void initGamesDbHelpers(GamesDbEventsObserver observer);
        void attachGamesDbListeners();
        void detachListeners();
        void createNewGame(Map<String, Object> gameMap, NetworkGamePlayer creator);
        void joinGame(NetworkGamePlayer joiner, String gameId, GameJoinEventsObserver observer);
        void cancelJoining();
        String getNewGameId();
        String getCurrentUserId();
    }
}
