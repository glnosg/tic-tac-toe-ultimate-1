package com.glnosg.tictactoeultimate.game.board.board_controller.cells_deactivator;

import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.BoardInitData;
import com.glnosg.tictactoeultimate.game.board.board_controller.BoardController;

public class CellsDeactivatorFactory {
    public static CellsDeactivator getCellsDeactivator(
            int numberOfCellsLeftActive, BoardController boardController) {

        if (numberOfCellsLeftActive == BoardInitData.BOARD_TYPE_CLASSICAL_ULTIMATE) {
            return new UltimateModeCellsDeactivator(numberOfCellsLeftActive, boardController);
        } else {
            return new RandomCellsDeactivator(numberOfCellsLeftActive, boardController);
        }
    }
}
