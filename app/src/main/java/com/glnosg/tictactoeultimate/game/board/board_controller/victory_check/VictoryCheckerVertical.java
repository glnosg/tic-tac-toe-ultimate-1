package com.glnosg.tictactoeultimate.game.board.board_controller.victory_check;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.BoardInitData.COLUMN_INDEX;
import static com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.BoardInitData.ROW_INDEX;

public class VictoryCheckerVertical implements VictoryChecker {

    private List<int[]> mReceivedMoves;
    private int mHowManyInLineToWin;
    private List<int[]> mWinningCells;

    public VictoryCheckerVertical() {
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
        ArrayList<Integer> potentiallyWinningColumns = getPotentiallyWinningColumns();
        if (potentiallyWinningColumns.isEmpty()) return false;
        for (int currentColumn : potentiallyWinningColumns) {
            if (hasEnoughConsecutiveRows(currentColumn)) return true;
        }
        return false;
    }

    /** When number of moves in the same column is equal to or
     * larger than number of cells in line needed to win,
     * then the column is considered potentially winning */
    private ArrayList<Integer> getPotentiallyWinningColumns() {
        ArrayList<Integer> sortedIndexes = getSortedColumnIndexes();
        ArrayList<Integer> potentiallyWinningColumns = new ArrayList<>();
        int sameIndexCounter = 0;
        for (int i = 1; i < sortedIndexes.size(); i++) {
            boolean isSameLikePrevious = sortedIndexes.get(i) == sortedIndexes.get(i - 1);
            boolean isAlreadyAdded = potentiallyWinningColumns.contains(sortedIndexes.get(i));
            if (isSameLikePrevious && !isAlreadyAdded) {
                sameIndexCounter++;
                if (sameIndexCounter == (mHowManyInLineToWin - 1)) {
                    potentiallyWinningColumns.add(sortedIndexes.get(i));
                    sameIndexCounter = 0;
                }
            } else {
                sameIndexCounter = 0;
            }
        }
        return potentiallyWinningColumns;
    }

    private ArrayList<Integer> getSortedColumnIndexes() {
        ArrayList<Integer> columnIndexes = new ArrayList<>();
        for (int[] currentCell : mReceivedMoves)
            columnIndexes.add(currentCell[COLUMN_INDEX]);
        Collections.sort(columnIndexes);
        return columnIndexes;
    }

    /** When number of consecutive row indexes is equal to or
     * larger than number of cells in line needed to win,
     * then winning condition is satisfied */
    private boolean hasEnoughConsecutiveRows(int column) {
        ArrayList<Integer> sortedRowIndexes = getSortedRowIndexes(column);
        int consecutiveRowsCounter = 0;
        for (int i = 1; i < sortedRowIndexes.size(); i++) {
            int previousRow = sortedRowIndexes.get(i - 1);
            int currentRow = sortedRowIndexes.get(i);
            if (currentRow == (previousRow + 1)) {
                consecutiveRowsCounter++;
                addMoveToWinningCells(column, previousRow);
                if (consecutiveRowsCounter == mHowManyInLineToWin - 1) {
                    addMoveToWinningCells(column, currentRow);
                    return true;
                }
            } else {
                consecutiveRowsCounter = 0;
                resetWinningCells();
            }
        }
        resetWinningCells();
        return false;
    }

    private ArrayList<Integer> getSortedRowIndexes(int columnIndex) {
        ArrayList<Integer> rowIndexes = new ArrayList<>();
        for (int[] currentCell : mReceivedMoves) {
            if (currentCell[COLUMN_INDEX] == columnIndex)
                rowIndexes.add(currentCell[ROW_INDEX]);
        }
        Collections.sort(rowIndexes);
        return rowIndexes;
    }

    private void addMoveToWinningCells(int column, int row) {
        int[] move = {column, row};
        mWinningCells.add(move);
    }
    private void resetWinningCells() {
        mWinningCells.clear();
    }
}
