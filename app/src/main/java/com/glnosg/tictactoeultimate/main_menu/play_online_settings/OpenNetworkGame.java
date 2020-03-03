package com.glnosg.tictactoeultimate.main_menu.play_online_settings;

public class OpenNetworkGame {

    private int mBoardType;
    private String mGameId;
    private String mCreatorId, mCreatorName;
    private int mCreatorGamesPlayed, mCreatorGamesWon, mCreatorGamesLeft;

    public OpenNetworkGame(int boardType, String gameId, String creatorId) {
        mBoardType = boardType;
        mGameId = gameId;
        mCreatorId = creatorId;
    }

    public void setCreatorName(String creatorName) {
        mCreatorName = creatorName;
    }

    public void setCreatorGamesPlayed(int creatorGamesPlayed) {
        mCreatorGamesPlayed = creatorGamesPlayed;
    }

    public void setCreatorGamesWon(int creatorGamesWon) {
        mCreatorGamesWon = creatorGamesWon;
    }

    public void setCreatorGamesLeft(int creatorGamesLeft) {
        mCreatorGamesLeft = creatorGamesLeft;
    }

    public int getBoardType() {
        return mBoardType;
    }

    public String getGameId() {
        return mGameId;
    }

    public String getCreatorName() {
        if (mCreatorName != null) return mCreatorName;
        else return "ANONYMOUS";
    }

    public int getCreatorGamesPlayed() {
        return mCreatorGamesPlayed;
    }

    public int getCreatorGamesWon() {
        return mCreatorGamesWon;
    }

    public int getCreatorGamesLeft() {
        return mCreatorGamesLeft;
    }
}
