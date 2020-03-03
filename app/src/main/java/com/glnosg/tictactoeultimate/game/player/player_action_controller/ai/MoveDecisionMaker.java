package com.glnosg.tictactoeultimate.game.player.player_action_controller.ai;

import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_state.BoardState;
import com.glnosg.tictactoeultimate.game.board.board_controller.Move;

public interface MoveDecisionMaker {

    Move getNextMove(BoardState boardState);
}
