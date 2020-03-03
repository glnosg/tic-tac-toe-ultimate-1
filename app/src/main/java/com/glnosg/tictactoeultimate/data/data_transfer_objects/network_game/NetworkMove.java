package com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game;

public class NetworkMove {
    private int playerId, boardId;
    private int column, row;

    public NetworkMove() {}

    public NetworkMove(int playerId, int boardId, int column, int row) {
        this.playerId = playerId;
        this.boardId = boardId;
        this.column = column;
        this.row = row;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getBoardId() {
        return boardId;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }
}
