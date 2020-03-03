package com.glnosg.tictactoeultimate.main_menu.load_game.GameSavePreview;

public class RemovedPreview {

    private GameSavePreview mGameSavePreview;
    private int mPosition;

    public RemovedPreview(GameSavePreview gameSavePreview, int position) {

        mGameSavePreview = gameSavePreview;
        mPosition = position;
    }

    public GameSavePreview getPreview() {
        return mGameSavePreview;
    }

    public int getPosition() {
        return mPosition;
    }
}
