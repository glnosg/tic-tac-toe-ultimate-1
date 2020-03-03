package com.glnosg.tictactoeultimate.game.network_game;

import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGamePlayer;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkMove;

import java.util.List;

public interface GameEventsObserver {
    void setInitialData(String creatorId, int boardType);
    void onPlayerJoined(NetworkGamePlayer joiner);
    void onPlayerLeft(String userId);
    void onAllPlayersReady(List<NetworkGamePlayer> players);
    void onCreatorChanged(String userId);
    void onActivePlayerChanged(int playerId);
    void onMoveAdded(NetworkMove move);
    void onGameFinished(int winnerId);
    void onMovesRemoved();
    void onPlayerOutOfTime(String userId);
}
