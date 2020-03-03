package com.glnosg.tictactoeultimate.game.view_managers;

import android.view.View;
import android.widget.Button;

import java.util.Arrays;
import java.util.List;

public class GameButtonsManager {

    private GameButtonsOnClickListener mButtonsOnClickListener;
    private Button mZoomOut, mNextGame, mLeave, mNewOpponent, mAccept, mDecline;
    private List<Button> mButtons;

    public GameButtonsManager (
            Button zoomOut,
            Button nextGame,
            Button leave,
            Button newOpponent,
            Button accept,
            Button decline) {
        mZoomOut = zoomOut;
        mNextGame = nextGame;
        mLeave = leave;
        mNewOpponent = newOpponent;
        mAccept = accept;
        mDecline = decline;
        mButtons = Arrays.asList(mZoomOut, mNextGame, mLeave, mNewOpponent, mAccept, mDecline);
    }

    public void setOnClickListener(GameButtonsOnClickListener buttonsOnClickListener) {
        mButtonsOnClickListener = buttonsOnClickListener;
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        mZoomOut.setOnClickListener((View v) -> mButtonsOnClickListener.onZoomOutClicked());
        mNextGame.setOnClickListener((View v) -> mButtonsOnClickListener.onNextGameClicked());
        mLeave.setOnClickListener((View v) -> mButtonsOnClickListener.onLeaveClicked());
        mNewOpponent.setOnClickListener((View v) -> mButtonsOnClickListener.onNewOpponentClicked());
        mAccept.setOnClickListener((View v) -> mButtonsOnClickListener.onAcceptClicked());
        mDecline.setOnClickListener((View) -> mButtonsOnClickListener.onDeclineClicked());
    }

    public void hideAllButtons() {
        for (Button button : mButtons) {
            button.setVisibility(View.GONE);
        }
    }

    public void showZoomOutButton() {
        mZoomOut.setVisibility(View.VISIBLE);
    }

    public void hideZoomOutButton() {
        mZoomOut.setVisibility(View.GONE);
    }

    public void showNextGameButton() {
        mNextGame.setVisibility(View.VISIBLE);
    }

    public void hideNextGameButton() {
        mNextGame.setVisibility(View.GONE);
    }

    public void showLeaveButton() {
        mLeave.setVisibility(View.VISIBLE);
    }

    public void hideLeaveButton() {
        mLeave.setVisibility(View.GONE);
    }

    public void showNewOpponentButton() {
        mNewOpponent.setVisibility(View.VISIBLE);
    }

    public void hideNewOpponentButton() {
        mNewOpponent.setVisibility(View.GONE);
    }

    public void showAcceptButton() {
        mAccept.setVisibility(View.VISIBLE);
    }

    public void hideAcceptButton() {
        mAccept.setVisibility(View.GONE);
    }

    public void showDeclineButton() {
        mDecline.setVisibility(View.VISIBLE);
    }

    public void hideDeclineButton() {
        mDecline.setVisibility(View.GONE);
    }
}
