package com.glnosg.tictactoeultimate.game.board.board_controller.cells_deactivator;

import com.glnosg.tictactoeultimate.game.board.board_controller.BoardController;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class RandomCellsDeactivator extends CellsDeactivator {
    private final String LOG_TAG = RandomCellsDeactivator.class.getSimpleName();

    private ArrayList<Integer> mClickableCellIndexes, mRandomIndexesOfClickableCells;
    private Random random;

    public RandomCellsDeactivator(int numberOfCellsLeftActive, BoardController boardController) {
        super(numberOfCellsLeftActive, boardController);
        mClickableCellIndexes = new ArrayList<>();
        mRandomIndexesOfClickableCells = new ArrayList<>();
        random = new Random();
    }

    @Override
    public void deactivate(int[] cellClickedDuringLastMove) {
        mClickableCellIndexes.clear();
        mRandomIndexesOfClickableCells.clear();
        getAllClickableCells();
        if (mClickableCellIndexes.size() <= mHowManyRemainActive) {
            activateAllClickableCells();
            deactivateAllCellsExcluding(mClickableCellIndexes);
        } else {
            getRandomIndexesOfClickableCells();
            activateRandomCells();
            deactivateAllCellsExcluding(mRandomIndexesOfClickableCells);
        }

    }

    private void getAllClickableCells() {
        for (int i = 0; i < mBoardController.getNumberOfCells(); i++) {
            if (mBoardController.isCellActive(i)) {
                mClickableCellIndexes.add(i);
            }
        }
    }

    private void activateAllClickableCells() {
        for (int clickableIndex : mClickableCellIndexes) {
            mBoardController.activateCell(clickableIndex);
        }
    }

    private void deactivateAllCellsExcluding(ArrayList<Integer> excludedIndexes) {
        for (int i = 0; i < mBoardController.getNumberOfCells(); i++) {
            if (!excludedIndexes.contains(i)) mBoardController.deactivateCell(i);
        }
    }

    private void getRandomIndexesOfClickableCells() {
        LinkedList<Integer> randomIndexes = new LinkedList<>();
        do {
            int randomIndex = random.nextInt(mClickableCellIndexes.size());
            if (!randomIndexes.contains(randomIndex)) {
                randomIndexes.add(randomIndex);
                mRandomIndexesOfClickableCells.add(mClickableCellIndexes.get(randomIndex));
            }
        } while (randomIndexes.size() < mHowManyRemainActive);
    }

    private void activateRandomCells() {
        for (int randomIndex : mRandomIndexesOfClickableCells) {
            mBoardController.activateCell(randomIndex);
        }
    }
}
