package com.glnosg.tictactoeultimate.game.board.figure;

import static com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.BoardInitData.EMPTY_CELL;

public class FigureFactory {

    public static Figure getShape(int shapeNumber) {
        switch (shapeNumber) {
            case EMPTY_CELL:
                return Blank.getInstance();
            case 0:
                return Cross.getInstance();
            case 1:
                return Nought.getInstance();
            case 2:
                return Triangle.getInstance();
            case 3:
                return Square.getInstance();
            default:
                    return null;
        }
    }
}
