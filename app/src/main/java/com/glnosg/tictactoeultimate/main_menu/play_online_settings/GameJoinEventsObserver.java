package com.glnosg.tictactoeultimate.main_menu.play_online_settings;

public interface GameJoinEventsObserver {
    void onJoinerAccepted(String gameId);
    void onJoinerDeclined();
}
