package com.glnosg.tictactoeultimate.main_menu.standard_game_settings;

import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.PlayerInitData;

import java.util.List;

public interface StandardGameMvp {

    interface SettingsView {
        List<PlayerInitData> getPlayerInitData();
        void startGame(String gameInitDataJson);
        void showUltimateInstructions();
        void showSingleInstructions();
        void lockDifficultySwitch();
        void unlockDifficultySwitch();
        void showErrorMessage();
    }

    interface SettingsPresenter {
        void onUltimateSwitched(boolean isUltimate);
        void onStartClicked();
    }

    interface SettingsModel {
        String getGameInitDataJson(int boardType, List<PlayerInitData> playerInitData);
    }
}
