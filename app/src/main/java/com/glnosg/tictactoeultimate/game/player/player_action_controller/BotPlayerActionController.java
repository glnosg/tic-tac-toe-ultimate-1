package com.glnosg.tictactoeultimate.game.player.player_action_controller;

import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_state.BoardState;
import com.glnosg.tictactoeultimate.game.board.board_controller.Move;
import com.glnosg.tictactoeultimate.game.player.Player;
import com.glnosg.tictactoeultimate.game.player.player_action_controller.ai.MiniMaxOneBoardTwoPlayers;
import com.glnosg.tictactoeultimate.game.player.player_action_controller.ai.MoveDecisionMaker;

public class BotPlayerActionController extends PlayerActionController {

    private MoveDecisionMaker mMoveDecisionMaker;

    public BotPlayerActionController(Player player) {
        super(player);
        mMoveDecisionMaker = new MiniMaxOneBoardTwoPlayers(mPlayer.getId(), mPlayer.getDifficulty());
    }

    @Override
    public void activate(BoardState boardState) {
        Move move = mMoveDecisionMaker.getNextMove(boardState);
        if (move != null)
        notifyCellChoiceObserver(move);
    }
}
