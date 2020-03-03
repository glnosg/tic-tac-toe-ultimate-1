package com.glnosg.tictactoeultimate.data.database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface GameSaveDao {

    @Query("SELECT * FROM game_saves ORDER BY updated_at DESC")
    List<GameSave> getAllGameSaves();

    @Query("SELECT * FROM game_saves WHERE id = :id")
    GameSave getGameSave(long id);

    @Insert
    long insertGameSave(GameSave gameSave);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateGameSave(GameSave gameSave);

    @Delete
    void deleteGameSave(GameSave... gameSaves);

    @Query("DELETE FROM game_saves WHERE id = :id")
    void deleteGameSave(long id);

    @Query("DELETE FROM game_saves")
    void deleteAllGameSaves();
}
