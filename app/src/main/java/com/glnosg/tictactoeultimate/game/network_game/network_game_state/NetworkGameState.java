package com.glnosg.tictactoeultimate.game.network_game.network_game_state;

import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGamePlayer;
import com.glnosg.tictactoeultimate.game.GameView;
import com.glnosg.tictactoeultimate.game.network_game.NetworkGameModel;
import com.glnosg.tictactoeultimate.game.network_game.NetworkGamePresenter;

public abstract class NetworkGameState {

    protected GameView mGameView;
    protected NetworkGamePresenter mNetworkGamePresenter;
    protected NetworkGameModel mNetworkGameModel;

    public NetworkGameState(
            GameView gameView,
            NetworkGamePresenter networkGamePresenter,
            NetworkGameModel networkGameModel){
        mGameView = gameView;
        mNetworkGamePresenter = networkGamePresenter;
        mNetworkGameModel = networkGameModel;
    }

    public abstract void onExitClicked();
    public abstract void onNextGameClicked();
    public abstract void onLeaveClicked();
    public abstract void onNewOpponentClicked();
    public abstract void onDeclineClicked();
    public abstract void onAcceptClicked();

    public abstract void onActivityDestroy();
    public abstract void onPlayerJoined(NetworkGamePlayer joiner);
    public abstract void onPlayerLeft(String userId);
}
