package com.glnosg.tictactoeultimate.main_menu.load_game.GameSavePreview;

import java.text.DateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

public class GameSavePreview {

    private long mId;

    private String mFormattedDate;
    private boolean mIsBoardUltimate;
    private LinkedList<PlayerPreview> mPlayerDisplayDataObjects;

    public GameSavePreview(
            long id,
            Date date,
            boolean isBoardUltimate,
            LinkedList<PlayerPreview> playerDisplayDataObjects) {

        mId = id;
        mIsBoardUltimate = isBoardUltimate;
        mPlayerDisplayDataObjects = playerDisplayDataObjects;

        formatDate(date);
    }

    private void formatDate(Date date) {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
        mFormattedDate = dateFormat.format(date);
    }

    public long getId() {
        return mId;
    }

    public String getFormattedDate() {
        return mFormattedDate;
    }

    public boolean isBoardUltimate() {
        return mIsBoardUltimate;
    }

    public PlayerPreview getPlayerDisplayData(int playerId) {

        if (playerId < 0 || playerId > mPlayerDisplayDataObjects.size() - 1)
            return null;
        else
            return mPlayerDisplayDataObjects.get(playerId);
    }
}
