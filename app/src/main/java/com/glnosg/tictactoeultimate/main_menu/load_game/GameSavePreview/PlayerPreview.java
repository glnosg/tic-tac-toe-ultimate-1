package com.glnosg.tictactoeultimate.main_menu.load_game.GameSavePreview;


public class PlayerPreview {

    private int mScore;
    private String mName;
    private boolean mIsHuman;

    public PlayerPreview(int score, String name, boolean isHuman) {
        mScore = score;
        mName = name;
        mIsHuman = isHuman;
    }

    public int getScore() {
        return mScore;
    }

    public String getName() {
        return mName;
    }

    public boolean isHuman() {
        return mIsHuman;
    }
}
