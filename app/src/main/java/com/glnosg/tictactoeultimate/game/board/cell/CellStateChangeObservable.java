package com.glnosg.tictactoeultimate.game.board.cell;

public interface CellStateChangeObservable {

    void registerObserver(CellStateChangeObserver observer);
    void unregisterObserver();
    void notifyObserver();
}
