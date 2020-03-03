package com.glnosg.tictactoeultimate.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {GameSave.class, PlayerState.class}, version = 3, exportSchema = false)
@TypeConverters({DateConverter.class, DifficultyConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    public static final long NOT_SAVED = -1;
    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "TicTacToeDatabase.db";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return sInstance;
    }

    public abstract GameSaveDao getGameSaveDao();
    public abstract PlayerStateDao getPlayerStateDao();
}
