package com.glnosg.tictactoeultimate.game.view_managers;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.glnosg.TicTacToeUltimate;
import com.glnosg.tictactoeultimate.R;

public class GameMessageDisplayManager {

    private TextView mGameMessageDisplay;
    private Context mContext;

    public GameMessageDisplayManager(TextView gameMessageDisplay) {
        mGameMessageDisplay = gameMessageDisplay;
        mContext = TicTacToeUltimate.getAppContext();
    }

    public void hide() {
        mGameMessageDisplay.setVisibility(View.GONE);
    }

    public void showGameFinishedWinnerMessage(String winnerName) {
        String winnerMessage =
                winnerName + mContext.getString(R.string.game_message_finished_winner_suffix);
        mGameMessageDisplay.setText(winnerMessage);
        mGameMessageDisplay.setVisibility(View.VISIBLE);
    }

    public void showGameFinishedDrawMessage() {
        mGameMessageDisplay.setText(mContext.getString(R.string.game_message_finished_draw));
        mGameMessageDisplay.setVisibility(View.VISIBLE);
    }

    public void showWaitingForOpponentMessage() {
        mGameMessageDisplay.setText(mContext.getString(R.string.game_message_waiting_for_opponent));
        mGameMessageDisplay.setVisibility(View.VISIBLE);
    }

    public void showOpponentWantsToJoinMessage(String opponentName) {
        String message =
                opponentName + mContext.getString(R.string.game_message_opponent_wants_join_suffix);
        mGameMessageDisplay.setText(message);
        mGameMessageDisplay.setVisibility(View.VISIBLE);
    }

    public void showWaitingForInactiveOpponentMessage() {
        mGameMessageDisplay.setText(mContext.getString(R.string.game_message_waiting_for_inactive_opponent));
        mGameMessageDisplay.setVisibility(View.VISIBLE);
    }

    public void showOpponentLeftMessage() {
        mGameMessageDisplay.setText(mContext.getString(R.string.game_message_opponent_left));
        mGameMessageDisplay.setVisibility(View.VISIBLE);
    }

    public void showWaitingForOpponentDecisionMessage() {
        mGameMessageDisplay.setText(mContext.getString(R.string.game_message_waiting_for_opponent_decision));
        mGameMessageDisplay.setVisibility(View.VISIBLE);
    }

    public void showOpponentWantsPlayAgainMessage() {
        mGameMessageDisplay.setText(mContext.getString(R.string.game_message_opponent_wants_play_again));
        mGameMessageDisplay.setVisibility(View.VISIBLE);
    }

    public void showTimeUpMessage() {
        mGameMessageDisplay.setText(mContext.getString(R.string.game_message_you_out_of_time));
        mGameMessageDisplay.setVisibility(View.VISIBLE);
    }
}
