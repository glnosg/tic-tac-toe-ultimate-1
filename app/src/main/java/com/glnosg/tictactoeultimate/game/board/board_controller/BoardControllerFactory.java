package com.glnosg.tictactoeultimate.game.board.board_controller;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.BoardInitData;

public class BoardControllerFactory {

    public static BoardController getBoardController(int boardType) {
        if (boardType == BoardInitData.BOARD_TYPE_SINGLE) {
            return new SingleBoardController();
        } else {
            return new UltimateBoardController();
        }
    }
}
