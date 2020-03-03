package com.glnosg.tictactoeultimate.game.network_game.network_game_state;

import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGame;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGamePlayer;
import com.glnosg.tictactoeultimate.game.GameView;
import com.glnosg.tictactoeultimate.game.network_game.NetworkGameModel;
import com.glnosg.tictactoeultimate.game.network_game.NetworkGamePresenter;

public class OpponentLeftState extends NetworkGameState {

    private final String LOG_TAG = GameFinishedState.class.getSimpleName();
    private final int EXIT_APP = 1, EXIT_GAME = 2;

    private int mLeaveOption;

    public OpponentLeftState(
            GameView gameView,
            NetworkGamePresenter networkGamePresenter,
            NetworkGameModel networkGameModel) {
        super(gameView, networkGamePresenter, networkGameModel);
        mGameView.getMessageDisplayManager().showOpponentLeftMessage();
        mGameView.getButtonsManager().hideAllButtons();
        mGameView.getButtonsManager().showLeaveButton();
        mGameView.getButtonsManager().showNewOpponentButton();
    }

    @Override
    public void onExitClicked() {
        mLeaveOption = EXIT_APP;
        leave();
    }

    private void leave() {
        if (mLeaveOption == EXIT_GAME) mGameView.exitGameActivity();
        else if (mLeaveOption == EXIT_APP) mGameView.exitTheApp();
    }

    @Override
    public void onNextGameClicked() { }
    @Override
    public void onLeaveClicked() {
        mLeaveOption = EXIT_GAME;
        leave();
    }
    @Override
    public void onNewOpponentClicked() {
        mNetworkGameModel.setGameState(NetworkGame.STATE_OPEN);
        mNetworkGamePresenter.setState(
                new NoOpponentState(mGameView, mNetworkGamePresenter, mNetworkGameModel));
    }
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

    public void onPlayerJoined(NetworkGamePlayer joiner) { }
    @Override
    public void onPlayerLeft(String userId) { }
}
