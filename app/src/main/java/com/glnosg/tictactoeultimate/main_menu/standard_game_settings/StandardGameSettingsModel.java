package com.glnosg.tictactoeultimate.main_menu.standard_game_settings;

import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.BoardInitData;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.GameInitData;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.PlayerInitData;
import com.glnosg.tictactoeultimate.main_menu.standard_game_settings.StandardGameMvp.SettingsPresenter;
import com.glnosg.tictactoeultimate.main_menu.standard_game_settings.StandardGameMvp.SettingsModel;
import com.google.gson.Gson;

import java.util.List;

public class StandardGameSettingsModel implements SettingsModel {

    private SettingsPresenter mSettingsPresenter;
    private Gson mGson;

    public StandardGameSettingsModel(SettingsPresenter settingsPresenter) {
        mSettingsPresenter = settingsPresenter;
        mGson = new Gson();
    }

    @Override
    public String getGameInitDataJson(int boardType, List<PlayerInitData> playersInitData) {
        BoardInitData boardInitData = new BoardInitData(boardType);
        GameInitData gameInitData = new GameInitData(playersInitData, boardInitData);
        return mGson.toJson(gameInitData);
    }
}
