package com.glnosg.tictactoeultimate.game.player.player_action_controller;

import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_state.BoardState;
import com.glnosg.tictactoeultimate.game.board.board_controller.CellChoiceObserver;
import com.glnosg.tictactoeultimate.game.board.board_controller.Move;
import com.glnosg.tictactoeultimate.game.player.Player;

public abstract class PlayerActionController {

    protected Player mPlayer;

    public PlayerActionController(Player player) {
        this.mPlayer = player;
    }

    public abstract void activate(BoardState boardState);

    protected void notifyCellChoiceObserver(Move move) {
        mPlayer.notifyCellChoiceObserver(move);
    }
}
