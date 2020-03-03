package com.glnosg.tictactoeultimate.game.player.player_action_controller.ai;

import android.util.Log;

import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.BoardInitData;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_state.BoardState;
import com.glnosg.tictactoeultimate.game.board.board_controller.Move;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomMoveDecisionMaker implements MoveDecisionMaker{

    private BoardState mBoardState;

    @Override
    public Move getNextMove(BoardState boardState) {
        List<Integer> activeCellIndexes = new ArrayList<>();

        mBoardState = boardState;

        for (int i = 0; i < mBoardState.getNumberOfCells(); i++) {
            if (!mBoardState.isCellTaken(i)) {
                Log.d("MAKER", "[getNextMove] added index = " + i);
                activeCellIndexes.add(i);
            }
        }

        if (!activeCellIndexes.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(activeCellIndexes.size());
            int randomCellIndex = activeCellIndexes.get(randomIndex);

            return new Move(
                    BoardInitData.MAIN_BOARD_ID,
                    getColumnBasedOnIndex(randomCellIndex),
                    getRowBasedOnIndex(randomCellIndex));
        }

        return null;
    }

    private int getColumnBasedOnIndex(int cellIndex) {
        return cellIndex % mBoardState.getNumberOfColumns();
    }

    private int getRowBasedOnIndex(int cellIndex) {
        return cellIndex / mBoardState.getNumberOfRows();
    }
}
