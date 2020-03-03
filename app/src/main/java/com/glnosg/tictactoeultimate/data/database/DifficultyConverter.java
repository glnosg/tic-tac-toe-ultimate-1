package com.glnosg.tictactoeultimate.data.database;

import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.PlayerInitData;

import androidx.room.TypeConverter;

public class DifficultyConverter {

    @TypeConverter
    public static PlayerInitData.Difficulty toDifficulty(int difficultyLevel) {
        return PlayerInitData.Difficulty.getDifficulty(difficultyLevel);
    }

    @TypeConverter
    public static int toInt(PlayerInitData.Difficulty difficulty) {
        return difficulty == null ? null : difficulty.ordinal();
    }
}
