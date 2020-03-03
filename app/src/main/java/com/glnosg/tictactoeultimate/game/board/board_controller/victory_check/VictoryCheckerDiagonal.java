package com.glnosg.tictactoeultimate.game.board.board_controller.victory_check;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.BoardInitData.COLUMN_INDEX;
import static com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.BoardInitData.ROW_INDEX;

public class VictoryCheckerDiagonal implements VictoryChecker {

    private List<int[]> mReceivedMoves;
    private int mHowManyInLineToWin;
    private List<int[]> mWinningCells;

    public VictoryCheckerDiagonal() {
        this.mWinningCells = new LinkedList<>();
    }

    @Override
    public List<int[]> getWinningCells() {
        return mWinningCells;
    }

    @Override
    public boolean isWinner(List<int[]> movesList, int howManyInLineToWin) {
        final int oneRowAscending = 1;
        final int oneRowDescending = -1;
        mWinningCells.clear();
        mReceivedMoves = movesList;
        mHowManyInLineToWin = howManyInLineToWin;
        LinkedList<LinkedList<Integer>> potentiallyWinningColumnSequences
                = getPotentiallyWinningColumnSequences();
        if (potentiallyWinningColumnSequences.isEmpty()) return false;
        for (LinkedList<Integer> currentColumnSequence : potentiallyWinningColumnSequences) {
            if (hasEnoughRowsInSpecifiedOrder(currentColumnSequence, oneRowAscending)) return true;
            if (hasEnoughRowsInSpecifiedOrder(currentColumnSequence, oneRowDescending)) return true;
        }
        return false;
    }

    /** When number of moves with consecutive column indexes is equal to or
     * larger than a number of cells in line needed to win,
     * then the sequence is considered potentially winning */
    private LinkedList<LinkedList<Integer>> getPotentiallyWinningColumnSequences() {
        LinkedList<LinkedList<Integer>> potentiallyWinningColumnSequences = new LinkedList<>();
        ArrayList<Integer> individualColumns = getAllSortedIndividualColumns();
        LinkedList<Integer> potentiallyWinningSequence = new LinkedList<>();
        potentiallyWinningSequence.add(individualColumns.get(0));
        for (int i = 1; i < individualColumns.size(); i++) {
            int previousColumn = individualColumns.get(i - 1);
            int currentColumn = individualColumns.get(i);
            if (currentColumn == previousColumn + 1) {
                potentiallyWinningSequence.add(currentColumn);
            } else {
                if (potentiallyWinningSequence.size() >= mHowManyInLineToWin) {
                    potentiallyWinningColumnSequences.add(potentiallyWinningSequence);
                }
                potentiallyWinningSequence = new LinkedList<>();
                potentiallyWinningSequence.add(currentColumn);
            }
        }
        if (potentiallyWinningSequence.size() >= mHowManyInLineToWin) {
            potentiallyWinningColumnSequences.add(potentiallyWinningSequence);
        }
        return potentiallyWinningColumnSequences;
    }

    private ArrayList<Integer> getAllSortedIndividualColumns() {
        ArrayList<Integer> columnIndexes = getAllColumnIndexes();
        Collections.sort(columnIndexes);
        return getListWithoutRepeatedElements(columnIndexes);
    }

    private ArrayList getAllColumnIndexes() {
        ArrayList<Integer> columnIndexes = new ArrayList<>();
        for (int[] currentCell : mReceivedMoves) {
            columnIndexes.add(currentCell[COLUMN_INDEX]);
        }
        return columnIndexes;
    }

    private ArrayList getListWithoutRepeatedElements(ArrayList<Integer> list) {
        for (int i = list.size() - 2; i >= 0; i--) {
            if (list.get(i) == list.get(i + 1)) list.remove(i + 1);
        }
        return list;
    }

    /** When number of rows in specified order is equal to or
     * larger than number of cells in line needed to win,
     * then winning condition is satisfied
     *
     * @param rowsOrder - defines a dependency between row indexes
     *                  of cells with consecutive column indexes
     *                   1 - one by one ascending
     *                  -1 - one by one descending
     *
     * */
    private boolean hasEnoughRowsInSpecifiedOrder(
            LinkedList<Integer> consecutiveColumnSequence, int rowsOrder) {
        for (int currentColumn : consecutiveColumnSequence) {
            LinkedList<Integer> rowIndexes = getSortedRowIndexes(currentColumn);

            for (int currentRow : rowIndexes) {
                int[] initialMove = {currentColumn, currentRow};
                if (isSequenceWinning(initialMove, 1, rowsOrder)) {
                    return true;
                }
            }
        }

        return false;
    }

    private LinkedList<Integer> getSortedRowIndexes(int columnIndex) {
        LinkedList<Integer> rowIndexes = new LinkedList<>();
        for (int[] currentCell : mReceivedMoves) {
            if (currentCell[COLUMN_INDEX] == columnIndex) rowIndexes.add(currentCell[ROW_INDEX]);
        }
        Collections.sort(rowIndexes);
        return rowIndexes;
    }

    private boolean isSequenceWinning(int[] previousMove,
                                      int movesInLineCounter,
                                      int rowsOrder) {
        int[] currentMove = {previousMove[COLUMN_INDEX] + 1, previousMove[ROW_INDEX] + rowsOrder};
        if (isMoveInReceivedMoves(currentMove)) {
            movesInLineCounter++;
            if (movesInLineCounter == mHowManyInLineToWin) {
                setWinningCells(currentMove[COLUMN_INDEX], currentMove[ROW_INDEX], rowsOrder);
                return true;
            } else {
                return isSequenceWinning(currentMove, movesInLineCounter, rowsOrder);
            }
        }
        return false;
    }

    private boolean isMoveInReceivedMoves(int[] move) {
        for (int[] currentMove : mReceivedMoves) {
            boolean isColumnSame = currentMove[COLUMN_INDEX] == move[COLUMN_INDEX];
            boolean isRowSame = currentMove[ROW_INDEX] == move[ROW_INDEX];
            if (isColumnSame && isRowSame) return true;
        }
        return false;
    }

    private void setWinningCells (int lastCellColumn, int lastCellRow, int rowsOrder) {
        int firstColumn = lastCellColumn - (mHowManyInLineToWin - 1);
        int firstRow = lastCellRow - ((mHowManyInLineToWin - 1) * rowsOrder);
        for (int i = 0; i < mHowManyInLineToWin; i++) {
            int[] currentMove = {firstColumn + i, firstRow + (i * rowsOrder)};
            mWinningCells.add(currentMove);
        }
    }
}
