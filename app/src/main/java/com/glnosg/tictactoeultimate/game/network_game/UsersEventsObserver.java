package com.glnosg.tictactoeultimate.game.network_game;

public interface UsersEventsObserver {
    void onUserDeactivated(String userId);
    void onUserActivated(String userId);
}
