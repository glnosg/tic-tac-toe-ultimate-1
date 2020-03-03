package com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data;

import com.google.gson.annotations.SerializedName;

public class PlayerInitData {

    public enum Difficulty {
        HUMAN, EASY, MEDIUM, HARD;
        private static Difficulty[] values = values();

        public static Difficulty getDifficulty(int difficultyLevel) {
            if (difficultyLevel < 0 || difficultyLevel >= values.length) return HUMAN;
            else return values[difficultyLevel];
        }

        public Difficulty next() {
            return values[(this.ordinal() + 1) % values.length];
        }
    }

    @SerializedName("name")
    private String mName;
    @SerializedName("difficulty")
    private Difficulty mDifficulty;

    public PlayerInitData() {
        mName = "Player";
        mDifficulty = Difficulty.HUMAN;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setDifficulty(Difficulty difficulty) {
        mDifficulty = difficulty;
    }

    public void switchDifficulty() {
        mDifficulty = mDifficulty.next();
    }

    public String getName() {
        return mName;
    }

    public Difficulty getDifficulty() {
        return mDifficulty;
    }
}
