package com.glnosg.tictactoeultimate.data.database;

import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.PlayerInitData;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "players",
        indices = {@Index("game_save_id")},
        foreignKeys = @ForeignKey(entity = GameSave.class,
                                    parentColumns = "id",
                                    childColumns = "game_save_id",
                                    onDelete = CASCADE ))
public class PlayerState {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "player_state_id")
    private long mPlayerStateId;
    @ColumnInfo(name = "name")
    private String mName;
    @ColumnInfo(name = "player_id")
    private int mPlayerId;
    @ColumnInfo(name = "score")
    private int mScore;
    @ColumnInfo(name = "is_active")
    private boolean mIsActive;
    @ColumnInfo(name = "difficulty")
    private PlayerInitData.Difficulty mDifficulty;
    @ColumnInfo(name = "game_save_id")
    private long mGameSaveId;

    @Ignore
    public PlayerState(
            String name,
            int playerId,
            int score,
            boolean isActive,
            PlayerInitData.Difficulty difficulty,
            long gameSaveId) {

        mName = name;
        mPlayerId = playerId;
        mScore = score;
        mIsActive = isActive;
        mDifficulty = difficulty;
        mGameSaveId = gameSaveId;
    }

    public PlayerState(
            long playerStateId,
            String name,
            int playerId,
            int score,
            boolean isActive,
            PlayerInitData.Difficulty difficulty,
            long gameSaveId) {

        mPlayerStateId = playerStateId;
        mName = name;
        mPlayerId = playerId;
        mScore = score;
        mIsActive = isActive;
        mDifficulty = difficulty;
        mGameSaveId = gameSaveId;
    }

    public void setPlayerStateId(long playerStateId) {
        mPlayerStateId = playerStateId;
    }

    public long getPlayerStateId() {
        return mPlayerStateId;
    }

    public void setGameSaveId(long id) {
        mGameSaveId = id;
    }

    public long getGameSaveId() {
        return mGameSaveId;
    }

    public int getPlayerId() {
        return mPlayerId;
    }

    public String getName() {
        return mName;
    }

    public int getScore() {
        return mScore;
    }

    public boolean isActive() {
        return mIsActive;
    }

    public boolean isHuman() {
        return mDifficulty == PlayerInitData.Difficulty.HUMAN;
    }

    public PlayerInitData.Difficulty getDifficulty() {
        return mDifficulty;
    }
}
