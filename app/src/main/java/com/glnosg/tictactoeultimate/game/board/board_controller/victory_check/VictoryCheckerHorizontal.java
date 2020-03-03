package com.glnosg.tictactoeultimate.game.board.board_controller.victory_check;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.BoardInitData.COLUMN_INDEX;
import static com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.BoardInitData.ROW_INDEX;

public class VictoryCheckerHorizontal implements VictoryChecker {

    private List<int[]> mReceivedMoves;
    private int mHowManyInLineToWin;
    private List<int[]> mWinningCells;

    public VictoryCheckerHorizontal() {
        this.mWinningCells = new LinkedList<>();
    }

    @Override
    public List<int[]> getWinningCells() {
        return mWinningCells;
    }

    @Override
    public boolean isWinner(List<int[]> moves, int howManyInLineToWin) {
        mWinningCells.clear();
        mReceivedMoves = moves;
        mHowManyInLineToWin = howManyInLineToWin;
        ArrayList<Integer> potentiallyWinningRows = getPotentiallyWinningRows();
        if (potentiallyWinningRows.isEmpty()) return false;
        for (int currentRow : potentiallyWinningRows) {
            if (hasEnoughConsecutiveColumns(currentRow)) return true;
        }
        return false;
    }

    /** When number of moves in the same row is equal to or
     * larger than number of cells in line needed to win,
     * then the row is considered potentially winning */
    private ArrayList<Integer> getPotentiallyWinningRows() {
        ArrayList<Integer> sortedIndexes = getSortedColumnIndexes();
        ArrayList<Integer> potentiallyWinningRows = new ArrayList<>();
        int sameIndexCounter = 0;
        for (int i = 1; i < sortedIndexes.size(); i++) {
            boolean isSameLikePrevious = sortedIndexes.get(i) == sortedIndexes.get(i - 1);
            boolean isAlreadyAdded = potentiallyWinningRows.contains(sortedIndexes.get(i));
            if (isSameLikePrevious && !isAlreadyAdded) {
                sameIndexCounter++;
                if (sameIndexCounter == (mHowManyInLineToWin - 1)) {
                    potentiallyWinningRows.add(sortedIndexes.get(i));
                    sameIndexCounter = 0;
                }
            } else {
                sameIndexCounter = 0;
            }
        }
        return potentiallyWinningRows;
    }

    private ArrayList<Integer> getSortedColumnIndexes() {
        ArrayList<Integer> rowIndexes = new ArrayList<>();
        for (int[] currentCell : mReceivedMoves) {
            rowIndexes.add(currentCell[ROW_INDEX]);
        }
        Collections.sort(rowIndexes);
        return rowIndexes;
    }

    /** When number of consecutive column indexes is equal to or
     * larger than number of cells in line needed to win,
     * then winning condition is satisfied */
    private boolean hasEnoughConsecutiveColumns(int row) {
        ArrayList<Integer> sortedColumnIndexes = getSortedColumnIndexes(row);
        int consecutiveColumnsCounter = 0;
        for (int i = 1; i < sortedColumnIndexes.size(); i++) {
            int previousColumn = sortedColumnIndexes.get(i - 1);
            int currentColumn = sortedColumnIndexes.get(i);
            if (currentColumn == (previousColumn + 1)) {
                consecutiveColumnsCounter++;
                addMoveToWinningCells(previousColumn, row);
                if (consecutiveColumnsCounter == mHowManyInLineToWin - 1) {
                    addMoveToWinningCells(currentColumn, row);
                    return true;
                }
            } else {
                consecutiveColumnsCounter = 0;
                resetWinningCells();
            }
        }
        resetWinningCells();
        return false;
    }

    private ArrayList<Integer> getSortedColumnIndexes(int rowIndex) {
        ArrayList<Integer> columnIndexes = new ArrayList<>();
        for (int[] currentCell : mReceivedMoves) {
            if (currentCell[ROW_INDEX] == rowIndex)
                columnIndexes.add(currentCell[COLUMN_INDEX]);
        }
        Collections.sort(columnIndexes);
        return columnIndexes;
    }

    private void addMoveToWinningCells(int column, int row) {
        int[] move = {column, row};
        mWinningCells.add(move);
    }

    private void resetWinningCells() {
        mWinningCells.clear();
    }
}
