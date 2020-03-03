package com.glnosg.tictactoeultimate.game.network_game.network_game_state;

import android.util.Log;

import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGamePlayer;
import com.glnosg.tictactoeultimate.game.GameView;
import com.glnosg.tictactoeultimate.game.LeaveLoseDialogOnClickListener;
import com.glnosg.tictactoeultimate.game.network_game.NetworkGameModel;
import com.glnosg.tictactoeultimate.game.network_game.NetworkGamePresenter;

public class GameplayState extends NetworkGameState implements LeaveLoseDialogOnClickListener {

    private final String LOG_TAG = GameplayState.class.getSimpleName();
    private final int EXIT_APP = 1, EXIT_GAME = 2;

    private int mLeaveOption;

    public GameplayState(
            GameView gameView,
            NetworkGamePresenter networkGamePresenter,
            NetworkGameModel networkGameModel) {
        super(gameView, networkGamePresenter, networkGameModel);
        gameView.getMessageDisplayManager().showWaitingForOpponentDecisionMessage();
        gameView.getTimeCounterDisplayManager().show();
        gameView.getButtonsManager().hideAllButtons();
        gameView.getButtonsManager().showLeaveButton();
    }

    @Override
    public void onExitClicked() {
        mLeaveOption = EXIT_APP;
        mGameView.showLeaveLoseDialog(this);
    }

    @Override
    public void onNextGameClicked() {
        Log.e(LOG_TAG, "onNextGameClicked");
    }

    @Override
    public void onLeaveClicked() {
        mLeaveOption = EXIT_GAME;
        mGameView.showLeaveLoseDialog(this);
    }

    @Override
    public void onNewOpponentClicked() {
        Log.e(LOG_TAG, "[onNewOpponentClicked]");
    }

    @Override
    public void onDeclineClicked() {
        Log.e(LOG_TAG, "[onDeclineClicked]");
    }

    @Override
    public void onAcceptClicked() {
        Log.e(LOG_TAG, "[onAcceptClicked]");
    }

    @Override
    public void onActivityDestroy() {
        Log.d(LOG_TAG, "[onActivityDestroy]");
        mNetworkGameModel.detachListeners();
        mNetworkGameModel.incrementThisUserGamesLeft();
        mNetworkGameModel.removeThisPlayerFromGame();
    }

    @Override
    public void onPlayerJoined(NetworkGamePlayer joiner) {
        Log.e(LOG_TAG, "[onPlayerJoined]");
    }

    @Override
    public void onPlayerLeft(String userId) {
        mNetworkGameModel.incrementUserGamesWon(mNetworkGameModel.getThisUserId());
        mGameView.getNetworkPlayersDisplayManager().hide();
        mNetworkGameModel.setThisPlayerState(0);
        mNetworkGamePresenter.setState(
                new OpponentLeftState(mGameView, mNetworkGamePresenter, mNetworkGameModel));
    }

    @Override
    public void onOkClicked() {
        leave();
    }


    private void leave() {
        if (mLeaveOption == EXIT_GAME) mGameView.exitGameActivity();
        else if (mLeaveOption == EXIT_APP) mGameView.exitTheApp();
    }

    @Override
    public void onCancelClicked() {
        mGameView.hideLeaveLoseDialog();
    }
}
