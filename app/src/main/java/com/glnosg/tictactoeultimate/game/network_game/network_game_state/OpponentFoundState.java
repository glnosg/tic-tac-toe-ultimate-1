package com.glnosg.tictactoeultimate.game.network_game.network_game_state;

import android.util.Log;

import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.GameInitData;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGame;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGamePlayer;
import com.glnosg.tictactoeultimate.game.GameView;
import com.glnosg.tictactoeultimate.game.view_managers.JoinerCandidatesAdapter.CandidateButtonsOnClickListener;
import com.glnosg.tictactoeultimate.game.network_game.NetworkGameModel;
import com.glnosg.tictactoeultimate.game.network_game.NetworkGamePresenter;

import java.util.LinkedList;
import java.util.List;

public class OpponentFoundState extends NetworkGameState implements CandidateButtonsOnClickListener {

    private final String LOG_TAG = OpponentFoundState.class.getSimpleName();

    private List<NetworkGamePlayer> mCandidates;
    private List<String> mAcceptedUsersIds;

    public OpponentFoundState(
            GameView gameView,
            NetworkGamePresenter networkGamePresenter,
            NetworkGameModel networkGameModel,
            NetworkGamePlayer candidate) {
        super(gameView, networkGamePresenter, networkGameModel);
        mCandidates = new LinkedList<>();
        mAcceptedUsersIds = new LinkedList<>();
        mCandidates.add(candidate);
        setGameView();
    }

    private void setGameView() {
        mGameView.getMessageDisplayManager().hide();
        mGameView.getJoinerCandidatesDisplayManager().show();
        mGameView.getJoinerCandidatesDisplayManager().attachCandidateButtonsOnClickListener(this);
        mGameView.getJoinerCandidatesDisplayManager().update(mCandidates);
        mGameView.getButtonsManager().hideAllButtons();
        mGameView.getButtonsManager().showLeaveButton();
    }

    @Override
    public void onExitClicked() {
        mGameView.exitTheApp();
    }

    @Override
    public void onNextGameClicked() {
        Log.e(LOG_TAG, "[onNextGameClicked]");
    }

    @Override
    public void onLeaveClicked() {
        mGameView.exitGameActivity();
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
        mNetworkGameModel.detachListeners();
        mNetworkGameModel.removeThisPlayerFromGame();
        mNetworkGameModel.removeThisGameFromDb();
    }

    @Override
    public void onPlayerJoined(NetworkGamePlayer joiner) {
        if (!isAlreadyAdded(joiner)) mCandidates.add(joiner);
        mGameView.getJoinerCandidatesDisplayManager().update(mCandidates);
    }

    private boolean isAlreadyAdded(NetworkGamePlayer candidate) {
        for (NetworkGamePlayer player : mCandidates) {
            if (player.getUserId().equals(candidate.getUserId())) return true;
        }
        return false;
    }

    @Override
    public void onPlayerLeft(String userId) {
        Log.d(LOG_TAG, "[onPlayerLeft]");
        removeCandidate(userId);
        if (mCandidates.size() == 0) {
            mGameView.getJoinerCandidatesDisplayManager().hide();
            mNetworkGamePresenter.setState(
                    new NoOpponentState(mGameView, mNetworkGamePresenter, mNetworkGameModel));
        } else {
            mGameView.getJoinerCandidatesDisplayManager().update(mCandidates);
        }
    }

    @Override
    public void onAcceptJoinerClicked(String userId) {
        if (!isAlreadyAccepted(userId)) mAcceptedUsersIds.add(userId);
        int playerId = mAcceptedUsersIds.size();
        mNetworkGameModel.acceptPlayer(userId, playerId);
        for (NetworkGamePlayer candidate : mCandidates) {
            if (candidate.getUserId().equals(userId)) candidate.setState(NetworkGame.ACCEPTED);
        }
        mGameView.getJoinerCandidatesDisplayManager().update(mCandidates);
        if (mAcceptedUsersIds.size() == GameInitData.PLAYERS_MAX_STANDARD - 1) {
            mNetworkGameModel.setGameState(NetworkGame.STATE_CLOSED);
            declineAllWaitingPlayers();
            mGameView.getJoinerCandidatesDisplayManager().hide();
            mCandidates.clear();
            mAcceptedUsersIds.clear();
            mNetworkGamePresenter.setState(
                    new GameplayState(mGameView, mNetworkGamePresenter, mNetworkGameModel)
            );
        }
    }

    private boolean isAlreadyAccepted(String checkedUserId) {
        for (String currentUserId : mAcceptedUsersIds) {
            if (checkedUserId.equals(currentUserId)) return true;
        }
        return false;
    }

    private void declineAllWaitingPlayers() {
        for(NetworkGamePlayer candidate : mCandidates) {
            if (candidate.getState() != NetworkGame.ACCEPTED) {
                onDeclineJoinerClicked(candidate.getUserId());
            }
        }
    }

    @Override
    public void onDeclineJoinerClicked(String userId) {
        mNetworkGameModel.declinePlayer(userId);
        removeCandidate(userId);
        if (mCandidates.size() == 0) {
            mGameView.getJoinerCandidatesDisplayManager().hide();
            mNetworkGamePresenter.setState(
                    new NoOpponentState(mGameView, mNetworkGamePresenter, mNetworkGameModel));
        } else {
            mGameView.getJoinerCandidatesDisplayManager().update(mCandidates);
        }
    }

    private void removeCandidate(String userId) {
        for (int i = mCandidates.size() - 1; i >= 0; i--) {
            if (mCandidates.get(i).getUserId().equals(userId)) {
                mCandidates.remove(i);
            }
        }
        for (int i = mAcceptedUsersIds.size() - 1; i >= 0; i--) {
            if (mAcceptedUsersIds.get(i).equals(userId)) {
                mAcceptedUsersIds.remove(i);
            }
        }
    }
}
