package com.glnosg.tictactoeultimate.game.network_game.network_game_state;

import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGame;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGamePlayer;
import com.glnosg.tictactoeultimate.game.GameView;
import com.glnosg.tictactoeultimate.game.network_game.NetworkGameModel;
import com.glnosg.tictactoeultimate.game.network_game.NetworkGamePresenter;

public class GameFinishedState extends NetworkGameState {

    private final String LOG_TAG = GameFinishedState.class.getSimpleName();
    private final int EXIT_APP = 1, EXIT_GAME = 2;

    private int mLeaveOption;

    public GameFinishedState(
            GameView gameView,
            NetworkGamePresenter networkGamePresenter,
            NetworkGameModel networkGameModel,
            int winnerId) {
        super(gameView, networkGamePresenter, networkGameModel);
        if (winnerId == NetworkGame.DRAW) {
            mGameView.getMessageDisplayManager().showGameFinishedDrawMessage();
        } else {
            String winnerName = mNetworkGamePresenter.getPlayerName(winnerId);
            if (winnerName != null) {
                mGameView.getMessageDisplayManager().showGameFinishedWinnerMessage(winnerName);
            }
        }
        mGameView.getButtonsManager().hideAllButtons();
        mGameView.getButtonsManager().showLeaveButton();
        mGameView.getButtonsManager().showNextGameButton();
    }

    @Override
    public void onExitClicked() {
        mLeaveOption = EXIT_APP;
        leave();
    }

    private void leave() {
        mNetworkGameModel.removeThisPlayerFromGame();
        if (mLeaveOption == EXIT_GAME) mGameView.exitGameActivity();
        else if (mLeaveOption == EXIT_APP) mGameView.exitTheApp();
    }

    @Override
    public void onNextGameClicked() {
        mNetworkGameModel.setThisPlayerState(NetworkGame.READY);
        mNetworkGamePresenter.setState(
                new GameplayState(mGameView, mNetworkGamePresenter, mNetworkGameModel));
    }

    @Override
    public void onLeaveClicked() {
        mLeaveOption = EXIT_GAME;
        leave();
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
        mGameView.getNetworkPlayersDisplayManager().hide();
        mNetworkGameModel.setGameState(NetworkGame.STATE_OPEN);
        mNetworkGamePresenter.setState(
                new NoOpponentState(mGameView, mNetworkGamePresenter, mNetworkGameModel));
    }
}
