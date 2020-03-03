package com.glnosg.tictactoeultimate.game.player.player_action_controller.ai;

import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.PlayerInitData;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_state.BoardState;
import com.glnosg.tictactoeultimate.game.board.board_controller.Move;
import com.glnosg.tictactoeultimate.game.board.board_controller.victory_check.VictoryChecker;
import com.glnosg.tictactoeultimate.game.board.board_controller.victory_check.VictoryCheckerAllDirections;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.BoardInitData.COLUMN_INDEX;
import static com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.BoardInitData.MAIN_BOARD_ID;
import static com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.BoardInitData.ROW_INDEX;

public class MiniMaxOneBoardTwoPlayers implements MoveDecisionMaker {

    private final String LOG_TAG = MiniMaxOneBoardTwoPlayers.class.getSimpleName();
    private final int LOSS = 0, DRAW = 10, VICTORY = 100;

    private VictoryChecker mVictoryChecker;
    private MoveDecisionMaker mRandomMoveDecisionMaker;

    private int mPlayerId, mOpponentId;
    private int mHowManyInLineToWin;
    private int mRandomMoveFactor;
    private Random mRandom;

    public MiniMaxOneBoardTwoPlayers(int playerId, PlayerInitData.Difficulty difficulty) {
        mPlayerId = playerId;
        mOpponentId = playerId == 0 ? 1 : 0;
        mVictoryChecker = new VictoryCheckerAllDirections();
        mRandomMoveDecisionMaker = new RandomMoveDecisionMaker();
        mRandom = new Random();
        setRandomFactor(difficulty);
    }

    private void setRandomFactor(PlayerInitData.Difficulty difficulty) {
        switch (difficulty) {
            case HARD:
                mRandomMoveFactor = 51;
                break;
            case MEDIUM:
                mRandomMoveFactor = 25;
                break;
            case EASY:
                mRandomMoveFactor = 6;
        }
    }

    @Override
    public Move getNextMove(BoardState boardState) {
        int random = mRandom.nextInt(101);
        if (boardState.isBoardEmpty() || random % mRandomMoveFactor == 0) {
            return mRandomMoveDecisionMaker.getNextMove(boardState);
        }
        mHowManyInLineToWin = boardState.getHowManyInLineToWin();
        int[] move = getBestMove(
                getEmptyCells(boardState),
                getPlayerMoves(boardState, mPlayerId),
                getPlayerMoves(boardState, mOpponentId));
        return new Move(MAIN_BOARD_ID, move[COLUMN_INDEX], move[ROW_INDEX]);
    }

    private int[] getBestMove(List<int[]> emptyCells, List<int[]> movesPlayer, List<int[]> movesOpponent) {

        MiniMaxMove alpha = new MiniMaxMove(emptyCells.get(0), Integer.MIN_VALUE);
        MiniMaxMove beta = new MiniMaxMove(emptyCells.get(0), Integer.MAX_VALUE);
        MiniMaxMove move = playerMove(emptyCells, movesPlayer, movesOpponent, 0, alpha, beta);
        return move.getMove();
    }

    private MiniMaxMove playerMove(
            List<int[]> emptyCells,
            List<int[]> movesPlayer,
            List<int[]> movesOpponent,
            int depth,
            MiniMaxMove alpha,
            MiniMaxMove beta) {
        MiniMaxMove alphaNew = alpha;
        MiniMaxMove value = alpha;
        for (int[] cell : emptyCells) {
            List<int[]> emptyCellsNew = new LinkedList<>(emptyCells);
            Collections.copy(emptyCellsNew, emptyCells);
            List<int[]> movesPlayerNew = new LinkedList<>(movesPlayer);
            Collections.copy(movesPlayerNew, movesPlayer);
            movesPlayerNew.add(cell);
            emptyCellsNew.remove(cell);
            if (mVictoryChecker.isWinner(movesPlayerNew, mHowManyInLineToWin)) {
                return new MiniMaxMove(cell, VICTORY - depth);
            } else if (emptyCellsNew.isEmpty()) {
                return new MiniMaxMove(cell, DRAW);
            }
            MiniMaxMove valueNew =
                    opponentMove(emptyCellsNew, movesPlayerNew, movesOpponent, depth + 1, alphaNew, beta);
            if (valueNew.getValue() > value.getValue()) value = new MiniMaxMove(cell, valueNew.getValue());
            if (valueNew.getValue() >= beta.getValue()) return value;
            if (valueNew.getValue() > alphaNew.getValue()) alphaNew = valueNew;
        }
        return value;
    }

    private MiniMaxMove opponentMove(
            List<int[]> emptyCells,
            List<int[]> movesPlayer,
            List<int[]> movesOpponent,
            int depth,
            MiniMaxMove alpha,
            MiniMaxMove beta) {
        MiniMaxMove betaNew = beta;
        MiniMaxMove value = beta;
        for (int[] cell : emptyCells) {
            List<int[]> emptyCellsNew = new LinkedList<>(emptyCells);
            Collections.copy(emptyCellsNew, emptyCells);
            List<int[]> movesOpponentNew = new LinkedList<>(movesOpponent);
            Collections.copy(movesOpponentNew, movesOpponent);
            movesOpponentNew.add(cell);
            emptyCellsNew.remove(cell);
            if (mVictoryChecker.isWinner(movesOpponentNew, mHowManyInLineToWin)) {
                return new MiniMaxMove(cell, LOSS);
            } else if (emptyCellsNew.isEmpty()) {
                return new MiniMaxMove(cell, DRAW);
            }
            MiniMaxMove valueNew =
                    playerMove(emptyCellsNew, movesPlayer, movesOpponentNew, depth + 1, alpha, betaNew);
            if (valueNew.getValue() < value.getValue()) value = new MiniMaxMove(cell, valueNew.getValue());
            if (valueNew.getValue() <= alpha.getValue()) return value;
            if (valueNew.getValue() < betaNew.getValue()) betaNew = valueNew;
        }
        return value;
    }

    private List<int[]> getEmptyCells(BoardState boardState) {
        List<int[]> emptyCells = new LinkedList<>();
        for (int i = 0; i < boardState.getNumberOfCells(); i++) {
            if (!boardState.isCellTaken(i)) {
                int[] cell = new int[] {
                        boardState.getColumnBasedOnIndex(i),
                        boardState.getRowBasedOnIndex(i)};
                emptyCells.add(cell);
            }
        }
        return emptyCells;
    }

    private List<int[]> getPlayerMoves(BoardState boardState, int playerId) {
        List<int[]> playerCells = new LinkedList<>();
        for (int i = 0; i < boardState.getNumberOfCells(); i++) {
            if (boardState.getPlayerFromCell(i) == playerId) {
                int[] cell = new int[] {
                        boardState.getColumnBasedOnIndex(i),
                        boardState.getRowBasedOnIndex(i)};
                playerCells.add(cell);
            }
        }
        return playerCells;
    }
}
