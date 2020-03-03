package com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameInitData {

    public static final int NO_ACTIVE_PLAYER = -1;
    public static final int PLAYERS_MIN = 2, PLAYERS_MAX_STANDARD = 2, PLAYERS_MAX_CUSTOM = 4;

    @SerializedName("players")
    private List<PlayerInitData> mPlayersInitData;
    @SerializedName("board")
    private BoardInitData mBoardInitData;

    public GameInitData(List<PlayerInitData> playersInitData, BoardInitData boardInitData) {
        mPlayersInitData = playersInitData;
        mBoardInitData = boardInitData;
    }

    public List<PlayerInitData> getPlayersInitData() {
        return mPlayersInitData;
    }

    public BoardInitData getBoardInitData() {
        return mBoardInitData;
    }

    public static GameInitData getDefaultGameInitData() {
        List<PlayerInitData> playersInitData = new ArrayList<>();
        playersInitData.add(new PlayerInitData());
        playersInitData.add(new PlayerInitData());
        BoardInitData boardInitData = new BoardInitData(BoardInitData.BOARD_TYPE_SINGLE);
        return new GameInitData(playersInitData, boardInitData);
    }
}
