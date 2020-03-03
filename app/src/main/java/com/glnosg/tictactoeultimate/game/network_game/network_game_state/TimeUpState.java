package com.glnosg.tictactoeultimate.game.network_game.network_game_state;

import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGamePlayer;
import com.glnosg.tictactoeultimate.game.GameView;
import com.glnosg.tictactoeultimate.game.network_game.NetworkGameModel;
import com.glnosg.tictactoeultimate.game.network_game.NetworkGamePresenter;

public class TimeUpState extends NetworkGameState {

    public TimeUpState(
            GameView gameView,
            NetworkGamePresenter networkGamePresenter,
            NetworkGameModel networkGameModel) {
        super(gameView, networkGamePresenter, networkGameModel);
        mGameView.getNetworkPlayersDisplayManager().hide();
        mGameView.getMessageDisplayManager().showTimeUpMessage();
        mNetworkGameModel.detachListeners();
        mNetworkGameModel.removeThisPlayerFromGame();
        mGameView.getButtonsManager().hideAllButtons();
        mGameView.getButtonsManager().showLeaveButton();
    }

    @Override
    public void onExitClicked() {
        mGameView.exitTheApp();
    }

    @Override
    public void onNextGameClicked() { }

    @Override
    public void onLeaveClicked() {
        mGameView.exitGameActivity();
    }

    @Override
    public void onNewOpponentClicked() {

    }

    @Override
    public void onDeclineClicked() {

    }

    @Override
    public void onAcceptClicked() {

    }

    @Override
    public void onActivityDestroy() {

    }

    @Override
    public void onPlayerJoined(NetworkGamePlayer joiner) {

    }

    @Override
    public void onPlayerLeft(String userId) {

    }
}
