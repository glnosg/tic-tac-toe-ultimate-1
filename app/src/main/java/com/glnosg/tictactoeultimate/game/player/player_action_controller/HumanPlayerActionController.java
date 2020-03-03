package com.glnosg.tictactoeultimate.game.player.player_action_controller;

import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_state.BoardState;
import com.glnosg.tictactoeultimate.game.player.Player;

public class HumanPlayerActionController extends PlayerActionController {

    public HumanPlayerActionController(Player player) {
        super(player);
    }

    @Override
    public void activate(BoardState boardState) { }
}
