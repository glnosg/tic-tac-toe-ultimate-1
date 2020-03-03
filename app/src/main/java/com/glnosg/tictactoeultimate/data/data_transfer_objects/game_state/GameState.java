package com.glnosg.tictactoeultimate.data.data_transfer_objects.game_state;

import com.glnosg.tictactoeultimate.game.player.Player;

import java.util.List;

public class GameState {

    private int mPlayerStartingRoundId;
    private BoardState mBoardState;
    private List<Player> mPlayers;

    public GameState(int playerStartingTheRoundId, BoardState boardState, List<Player> players) {
        mPlayerStartingRoundId = playerStartingTheRoundId;
        mBoardState = boardState;
        mPlayers = players;
    }

    public int getPlayerStartingRoundId() {
        return mPlayerStartingRoundId;
    }

    public BoardState getBoardState() {
        return mBoardState;
    }

    public List<Player> getPlayers() {
        return mPlayers;
    }
}
