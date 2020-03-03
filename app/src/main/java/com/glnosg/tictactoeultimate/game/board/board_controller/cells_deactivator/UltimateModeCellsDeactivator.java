package com.glnosg.tictactoeultimate.game.board.board_controller.cells_deactivator;

import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.BoardInitData;
import com.glnosg.tictactoeultimate.game.board.board_controller.BoardController;

public class UltimateModeCellsDeactivator extends CellsDeactivator {

    public UltimateModeCellsDeactivator(int numberOfCellsToDeactivate, BoardController boardController) {
        super(numberOfCellsToDeactivate, boardController);
    }

    @Override
    public void deactivate(int[] cellClickedDuringLastMove) {
        int clickedCellIndex = getCellIndex(cellClickedDuringLastMove);
        if (mBoardController.isCellActive(clickedCellIndex)) {
            activateCell(clickedCellIndex);
            deactivateOtherCells(clickedCellIndex);
        } else {
            mBoardController.activateBoard();
        }
    }

    private int getCellIndex(int[] cell) {
        int column = cell[BoardInitData.COLUMN_INDEX];
        int row = cell[BoardInitData.ROW_INDEX];
        return  (BoardInitData.BOARD_SIZE_DEFAULT * row) + column;
    }

    private void activateCell(int cellThatWillStayActive) {
        mBoardController.activateCell(cellThatWillStayActive);
    }

    private void deactivateOtherCells(int cellThatWillStayActiveIndex) {
        int currentIndex;
        for (int row = 0; row < BoardInitData.BOARD_SIZE_DEFAULT; row++) {
            for (int col = 0; col < BoardInitData.BOARD_SIZE_DEFAULT; col++) {
                currentIndex = (BoardInitData.BOARD_SIZE_DEFAULT * row) + col;
                if (currentIndex != cellThatWillStayActiveIndex)
                    mBoardController.deactivateCell(currentIndex);
            }
        }
    }
}
