package com.glnosg.tictactoeultimate.main_menu.load_game;


import com.glnosg.tictactoeultimate.main_menu.load_game.GameSavePreview.GameSavePreview;
import com.glnosg.tictactoeultimate.main_menu.load_game.LoadGameMVP.View;
import com.glnosg.tictactoeultimate.main_menu.load_game.LoadGameMVP.Presenter;
import com.glnosg.tictactoeultimate.main_menu.load_game.LoadGameMVP.Model;

import java.util.List;

import androidx.loader.app.LoaderManager;

public class LoadGamePresenter implements Presenter {

    private final String LOG_TAG = LoadGamePresenter.class.getSimpleName();

    private View mView;
    private Model mModel;

    public LoadGamePresenter (View loadGameView, LoaderManager loaderManager) {
        mView = loadGameView;
        mModel = new LoadGameModel(this, loaderManager);
    }

    @Override
    public void gameSavePreviewsPrepared(List<GameSavePreview> previews) {
        displayData(previews);

    }

    private void displayData(List<GameSavePreview> previews) {
        mView.hideLoadingIndicator();
        if (previews.isEmpty()) {
            mView.showEmptyDatabaseMessage();
        } else {
            mView.hideEmptyDatabaseMessage();
            mView.displayGameSavePreviews(previews);
        }
    }

    @Override
    public void onGameSavePreviewSwiped(int gamePreviewPosition) {
        mView.activateUndoButton();
        mModel.removePreview(gamePreviewPosition);
    }

    @Override
    public void onSwipeRefresh() {
        mModel.deleteRemovedPreviewsAndCorrespondingGameSaves();
        mView.deactivateUndoButton();
        mModel.reloadGameSaves();
        mView.hideRefreshAnimation();
    }

    @Override
    public void onUndoClicked() {
        mModel.retrieveLastRemovedPreview();
    }

    @Override
    public void onRemovedPreviewsStackEmpty() {
        mView.deactivateUndoButton();
    }

    @Override
    public void onActivityDestroy() {
        mModel.deleteRemovedPreviewsAndCorrespondingGameSaves();
    }
}
