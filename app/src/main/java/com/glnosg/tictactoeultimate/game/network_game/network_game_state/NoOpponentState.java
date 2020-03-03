package com.glnosg.tictactoeultimate.game.network_game.network_game_state;

import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGame;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGamePlayer;
import com.glnosg.tictactoeultimate.game.GameView;
import com.glnosg.tictactoeultimate.game.network_game.NetworkGameModel;
import com.glnosg.tictactoeultimate.game.network_game.NetworkGamePresenter;

public class NoOpponentState extends NetworkGameState {

    private final String LOG_TAG = NoOpponentState.class.getSimpleName();

    public NoOpponentState(
            GameView gameView,
            NetworkGamePresenter networkGamePresenter,
            NetworkGameModel networkGameModel) {
        super(gameView, networkGamePresenter, networkGameModel);
        mNetworkGamePresenter.resetGame();
        mGameView.getMessageDisplayManager().showWaitingForOpponentMessage();
        mGameView.getButtonsManager().hideAllButtons();
        mGameView.getButtonsManager().showLeaveButton();
        mNetworkGameModel.setThisPlayerState(NetworkGame.READY);
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
    public void onNewOpponentClicked() { }

    @Override
    public void onDeclineClicked() { }

    @Override
    public void onAcceptClicked() { }

    @Override
    public void onActivityDestroy() {
        mNetworkGameModel.detachListeners();
        mNetworkGameModel.removeThisPlayerFromGame();
    }

    @Override
    public void onPlayerJoined(NetworkGamePlayer joiner) {
        mNetworkGamePresenter.setState(
                new OpponentFoundState(mGameView, mNetworkGamePresenter, mNetworkGameModel, joiner));
    }

    @Override
    public void onPlayerLeft(String userId) { }
}
