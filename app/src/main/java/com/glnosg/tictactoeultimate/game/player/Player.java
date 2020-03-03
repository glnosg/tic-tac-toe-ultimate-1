package com.glnosg.tictactoeultimate.game.player;

import android.util.Log;

import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_state.BoardState;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.PlayerInitData;
import com.glnosg.tictactoeultimate.data.database.AppDatabase;
import com.glnosg.tictactoeultimate.data.database.PlayerState;
import com.glnosg.tictactoeultimate.game.board.board_controller.CellChoiceObserver;
import com.glnosg.tictactoeultimate.game.board.board_controller.Move;
import com.glnosg.tictactoeultimate.game.player.player_action_controller.BotPlayerActionController;
import com.glnosg.tictactoeultimate.game.player.player_action_controller.HumanPlayerActionController;
import com.glnosg.tictactoeultimate.game.player.player_action_controller.PlayerActionController;
import com.google.gson.annotations.SerializedName;

public class Player {

    private final String LOG_TAG = Player.class.getSimpleName();

    @SerializedName("id")
    private int mID;
    @SerializedName("name")
    private String mName;
    @SerializedName("score")
    private int mScore;
    @SerializedName("isActive")
    private boolean mIsActive;
    @SerializedName("difficulty")
    private PlayerInitData.Difficulty mDifficulty;
    @SerializedName("playerStateId")
    private long mPlayerStateId;

    private transient PlayerActionController mActionController;
    private transient CellChoiceObserver mCellChoiceObserver;

    public Player(PlayerState playerState) {
        mID = playerState.getPlayerId();
        mName = playerState.getName();
        mScore = playerState.getScore();
        mIsActive = playerState.isActive();
        mDifficulty = playerState.getDifficulty();
        mPlayerStateId = playerState.getPlayerStateId();
        setActionController();
    }

    public Player(int id, String name) {
        mID = id;
        mName = name;
        mScore = 0;
        mDifficulty = PlayerInitData.Difficulty.HUMAN;
        mPlayerStateId = AppDatabase.NOT_SAVED;
        setActionController();
    }

    private void setActionController() {
        if (isHuman()) mActionController = new HumanPlayerActionController(this);
        else mActionController = new BotPlayerActionController(this);
    }

    public void registerCellChoiceObserver(CellChoiceObserver observer) {
        mCellChoiceObserver = observer;
    }

    public void notifyCellChoiceObserver(Move move) {
        if (mCellChoiceObserver != null) {
            mCellChoiceObserver.playerHasChosenCell(move);
        }
    }

    public void activate(BoardState boardState) {
        mIsActive = true;
        if (mActionController == null) setActionController();
        mActionController.activate(boardState);
    }

    public void deactivate() {
        mIsActive = false;
    }

    public void incrementScore() {
        mScore++;
    }

    public void resetScore() {
        mScore = 0;
    }

    public void setDifficulty(PlayerInitData.Difficulty difficulty) {
        mDifficulty = difficulty;
        setActionController();
    }

    public void switchDifficulty() {
        mDifficulty = mDifficulty.next();
        setActionController();
    }

    public PlayerInitData.Difficulty getDifficulty() {
        return mDifficulty;
    }

    public int getId() {
        return mID;
    }

    public String getName() {
        return mName;
    }

    public int getScore() {
        return mScore;
    }

    public boolean isHuman() {
        return mDifficulty == PlayerInitData.Difficulty.HUMAN;
    }

    public boolean isActive() {
        return mIsActive;
    }

    public long getPlayerStateId() {
        return mPlayerStateId;
    }

    public PlayerState getPlayerState(long gameSaveId) {
        return new PlayerState(
                mName, mID, mScore, mIsActive, mDifficulty, gameSaveId);
    }
}
