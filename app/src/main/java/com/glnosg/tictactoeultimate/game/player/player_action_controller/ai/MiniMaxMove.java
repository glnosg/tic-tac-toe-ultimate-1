package com.glnosg.tictactoeultimate.game.player.player_action_controller.ai;

public class MiniMaxMove {

    private int[] mMove;
    private int mValue;

    public MiniMaxMove(int[] move, int value) {
        mMove = move;
        mValue = value;
    }

    public int[] getMove() {
        return mMove;
    }

    public int getValue() {
        return mValue;
    }
}
