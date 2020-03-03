package com.glnosg.tictactoeultimate.main_menu.play_online_settings;

import java.util.List;

public interface GamesDbEventsObserver {
    void setInitialData(List<OpenNetworkGame> openGames);
    void onGameOpened(OpenNetworkGame game);
    void onGameClosed(String gameId);
}
