package com.glnosg.tictactoeultimate.data.database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface PlayerStateDao {

    @Query("SELECT * FROM players WHERE game_save_id = :gameSaveId")
    List<PlayerState> getAllPlayerStates(long gameSaveId);

    @Insert
    void insertPlayerState(PlayerState playerState);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updatePlayerState(PlayerState playerState);

    @Delete
    void deletePlayerState(PlayerState... playerStates);
}
