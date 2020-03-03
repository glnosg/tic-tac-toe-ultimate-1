package com.glnosg.tictactoeultimate.data.database;

import java.util.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "game_saves")
public class GameSave {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;
    @ColumnInfo(name = "player_starting_round")
    private int mPlayerStartingTheRoundId;
    @ColumnInfo(name = "board_state")
    private String mBoardStateJson;
    @ColumnInfo(name = "updated_at")
    private Date mUpdatedAt;

    @Ignore
    public GameSave(String boardStateJson, Date updatedAt) {
        mPlayerStartingTheRoundId = 0;
        mBoardStateJson = boardStateJson;
        mUpdatedAt = updatedAt;
    }

    public GameSave(long id, String boardStateJson, Date updatedAt) {
        mId = id;
        mPlayerStartingTheRoundId = 0;
        mBoardStateJson = boardStateJson;
        mUpdatedAt = updatedAt;
    }

    public void setId(long id) {
        mId = id;
    }

    public void setPlayerStartingTheRoundId(int playerId) {
        mPlayerStartingTheRoundId = playerId;
    }

    public long getId() {
        return mId;
    }

    public int getPlayerStartingTheRoundId() {
        return mPlayerStartingTheRoundId;
    }

    public String getBoardStateJson() {
        return mBoardStateJson;
    }

    public Date getUpdatedAt() {
        return mUpdatedAt;
    }
}
