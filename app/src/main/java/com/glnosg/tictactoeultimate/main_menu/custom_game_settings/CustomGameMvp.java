package com.glnosg.tictactoeultimate.main_menu.custom_game_settings;

import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.PlayerInitData;

import java.util.List;

public interface CustomGameMvp {

    interface SettingsView {
        void showUltimateSettings();
        void hideUltimateSettings();
        void showRemoveButton();
        void hideRemoveButton();
        void showAddButton();
        void hideAddButton();
        void addRowOnPlayersDisplay();
        void removeRowFromPlayersDisplay();
        void setHowManyInRowMaxValue(int value);
        int getNumberOfColumns();
        int getNumberOfRows();
        int getHowManyInLineToWin();
        int getHowManyRemainActive();
        List<PlayerInitData> getPlayersInitData();
        void startGame(String gameInitDataJson);
        void showErrorMessage();
        void showBoardToSmallMessage();
    }

    interface SettingsPresenter {
        void onAddClicked();
        void onRemoveClicked();
        void setMaxNumberOfPlayers(int number);
        void onColumnsPickerScrolled(int currentValue);
        void onRowsPickerScrolled(int currentValue);
        void onUltimateSwitched(boolean isUltimate);
        void onStartClicked();
    }

    interface SettingsModel {
        String getGameInitDataJson(
                int numberOfColumns,
                int numberOfRows,
                int howManyInLineToWin,
                int boardType,
                List<PlayerInitData> playerInitData);
    }
}
