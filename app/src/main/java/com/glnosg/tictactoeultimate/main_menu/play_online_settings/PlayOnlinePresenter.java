package com.glnosg.tictactoeultimate.main_menu.play_online_settings;

import android.util.Log;

import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.GameInitData;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGame;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGamePlayer;
import com.glnosg.tictactoeultimate.main_menu.play_online_settings.PlayOnlineMVP.SettingsView;
import com.glnosg.tictactoeultimate.main_menu.play_online_settings.PlayOnlineMVP.SettingsPresenter;
import com.glnosg.tictactoeultimate.main_menu.play_online_settings.PlayOnlineMVP.SettingsModel;
import com.glnosg.tictactoeultimate.main_menu.play_online_settings.OpenGamesAdapter.OpenGamesListEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayOnlinePresenter implements
        SettingsPresenter, GamesDbEventsObserver, OpenGamesListEventListener, GameJoinEventsObserver {

    private final String LOG_TAG = PlayOnlinePresenter.class.getSimpleName();
    private final String ANONYMOUS = "Anonymous";

    private SettingsView mSettingsView;
    private SettingsModel mSettingsModel;

    private int mBoardType;
    private String mUserName;
    private int mGamesPlayed, mGamesWon, mGamesLeft;

    public PlayOnlinePresenter(SettingsView settingsView) {
        mSettingsView = settingsView;
        mSettingsModel = new PlayOnlineModel(this);
        mUserName = ANONYMOUS;
        mBoardType = NetworkGame.SINGLE;
    }

    @Override
    public void onUserSignedOut() {
        mSettingsView.showLoggingScreen();
        mSettingsView.getOpenGamesDisplayManager().clearData();
        mUserName = ANONYMOUS;
        mSettingsModel.detachListeners();
    }

    @Override
    public void onUserSignedIn(String userName, int gamesPlayed, int gamesWon, int gamesLeft) {
        mUserName = getFormattedName(userName);
        mSettingsView.setNameInputHint(mUserName);
        mGamesPlayed = gamesPlayed;
        mGamesWon = gamesWon;
        mGamesLeft = gamesLeft;
        mSettingsModel.initGamesDbHelpers(this);
    }

    private String getFormattedName(String userName) {
        if (userName != null && !userName.trim().matches("")) {
            String words[] = userName.split(" ", 2);
            return words[0];
        } else {
            return ANONYMOUS;
        }
    }

    @Override
    public void onHasUnfinishedGame(String gameId) {
        mSettingsView.openGame(gameId);
    }

    @Override
    public void onUltimateSwitched(boolean isUltimate) {
        if (isUltimate) mBoardType = NetworkGame.ULTIMATE;
        else mBoardType = NetworkGame.SINGLE;
    }

    @Override
    public void onStartClicked() {
        updateUserName();
        Map<String, Object> newGameMap = getGameMap(mSettingsModel.getNewGameId());
        updateUserName();
        NetworkGamePlayer creator =
                new NetworkGamePlayer(
                        mUserName,
                        mSettingsModel.getCurrentUserId(),
                        mGamesPlayed,
                        mGamesWon,
                        mGamesLeft);
        mSettingsModel.createNewGame(newGameMap, creator);
    }

    private void updateUserName() {
        String newName = mSettingsView.getDisplayName().trim();
        if (!newName.equals("")) mUserName = newName;
    }

    private Map<String, Object> getGameMap(String nodeId) {
        Map<String, Object> gameMap = new HashMap<>();
        gameMap.put(NetworkGame.CREATOR_ID, mSettingsModel.getCurrentUserId());
        gameMap.put(NetworkGame.STATE, NetworkGame.STATE_OPEN);
        gameMap.put(NetworkGame.BOARD_TYPE, mBoardType);
        gameMap.put(NetworkGame.ACTIVE_PLAYER_ID, GameInitData.NO_ACTIVE_PLAYER);
        gameMap.put(NetworkGame.GAME_ID, nodeId);
        return gameMap;
    }

    @Override
    public void onGameCreated(String gameId) {
        mSettingsView.openGame(gameId);
    }

    @Override
    public void onWaitingForResponseCancelled() {
        mSettingsModel.cancelJoining();
        mSettingsView.cancelWaitingForResponseDialog();
    }

    @Override
    public void onActivityPause() {
        mSettingsModel.detachListeners();
        mSettingsView.getOpenGamesDisplayManager().clearData();
    }

    @Override
    public void onActivityResume() {
        mSettingsModel.attachAuthListener();
        mSettingsModel.initGamesDbHelpers(this);
    }

    @Override
    public void setInitialData(List<OpenNetworkGame> openGames) {
        mSettingsView.getOpenGamesDisplayManager().attachOpenGamesListEventListener(this);
        mSettingsView.hideLoadingIndicator();
        if (openGames.isEmpty()) mSettingsView.showEmptyDatabaseMessage();
        else mSettingsView.getOpenGamesDisplayManager().setData(openGames);
        mSettingsModel.attachGamesDbListeners();
        mSettingsView.setStartButtonEnabled(true);
    }

    @Override
    public void onGameOpened(OpenNetworkGame game) {
        mSettingsView.hideEmptyDatabaseMessage();
        mSettingsView.getOpenGamesDisplayManager().addGame(game);
    }

    @Override
    public void onGameClosed(String gameId) {
        mSettingsView.getOpenGamesDisplayManager().removeGame(gameId);
    }

    @Override
    public void onFirstOpenGameAdded() {
        mSettingsView.hideLoadingIndicator();
        mSettingsView.hideEmptyDatabaseMessage();
        mSettingsView.getOpenGamesDisplayManager().show();
    }

    @Override
    public void onOpenGameClicked(String gameId) {
        mSettingsView.showWaitingForResponseDialog();
        updateUserName();
        NetworkGamePlayer joiner =
                new NetworkGamePlayer(
                        mUserName,
                        mSettingsModel.getCurrentUserId(),
                        mGamesPlayed,
                        mGamesWon,
                        mGamesLeft);
        mSettingsModel.joinGame(joiner, gameId, this);
    }

    @Override
    public void onOpenGamesListEmpty() {
        mSettingsView.getOpenGamesDisplayManager().hide();
        mSettingsView.showEmptyDatabaseMessage();
    }

    @Override
    public void onJoinerAccepted(String gameId) {
        mSettingsView.cancelWaitingForResponseDialog();
        mSettingsView.openGame(gameId);
    }

    @Override
    public void onJoinerDeclined() {
        mSettingsView.cancelWaitingForResponseDialog();
    }
}
