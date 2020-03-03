package com.glnosg.tictactoeultimate.main_menu.load_game;


import com.glnosg.tictactoeultimate.main_menu.load_game.GameSavePreview.GameSavePreview;

import java.util.List;

public interface LoadGameMVP {

    interface View {
        void showLoadingIndicator();
        void hideLoadingIndicator();
        void showEmptyDatabaseMessage();
        void hideEmptyDatabaseMessage();
        void hideRefreshAnimation();
        void displayGameSavePreviews(List<GameSavePreview> gameSavePreviews);
        void activateUndoButton();
        void deactivateUndoButton();
    }

    interface Presenter {
        void gameSavePreviewsPrepared(List<GameSavePreview> previews);
        void onGameSavePreviewSwiped(int gamePreviewPosition);
        void onSwipeRefresh();
        void onUndoClicked();
        void onRemovedPreviewsStackEmpty();
        void onActivityDestroy();
    }

    interface Model {
        void reloadGameSaves();
        void removePreview(int position);
        void retrieveLastRemovedPreview();
        void deleteRemovedPreviewsAndCorrespondingGameSaves();
    }
}
