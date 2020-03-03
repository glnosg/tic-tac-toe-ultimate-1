package com.glnosg.tictactoeultimate.main_menu.standard_game_settings.instructions;

public class InstructionsPageData {

    private String mInstructions;
    private int mImageResourceId;

    public InstructionsPageData (String instructions, int imageResourceId) {
        mInstructions = instructions;
        mImageResourceId = imageResourceId;
    }

    public String getInstructions() {
        return mInstructions;
    }

    public int getImageResourceId() {
        return mImageResourceId;
    }
}
