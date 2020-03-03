package com.glnosg.tictactoeultimate.main_menu.custom_game_settings;

import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.BoardInitData;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.GameInitData;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.PlayerInitData;
import com.glnosg.tictactoeultimate.main_menu.custom_game_settings.CustomGameMvp.SettingsPresenter;
import com.glnosg.tictactoeultimate.main_menu.custom_game_settings.CustomGameMvp.SettingsModel;
import com.google.gson.Gson;

import java.util.List;


public class CustomGameSettingsModel implements SettingsModel {

    private SettingsPresenter mSettingsPresenter;
    private Gson mGson;

    public CustomGameSettingsModel(SettingsPresenter settingsPresenter) {
        mSettingsPresenter = settingsPresenter;
        mGson = new Gson();
    }

    @Override
    public String getGameInitDataJson(
            int numberOfColumns,
            int numberOfRows,
            int howManyInLineToWin,
            int boardType,
            List<PlayerInitData> playersInitData) {
        BoardInitData boardInitData =
                new BoardInitData(numberOfColumns, numberOfRows, howManyInLineToWin, boardType);
        GameInitData gameInitData = new GameInitData(playersInitData, boardInitData);
        return mGson.toJson(gameInitData);
    }
}
