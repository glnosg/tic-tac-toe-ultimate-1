package com.glnosg.tictactoeultimate.main_menu.standard_game_settings;

import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.BoardInitData;
import com.glnosg.tictactoeultimate.main_menu.standard_game_settings.StandardGameMvp.SettingsView;
import com.glnosg.tictactoeultimate.main_menu.standard_game_settings.StandardGameMvp.SettingsPresenter;
import com.glnosg.tictactoeultimate.main_menu.standard_game_settings.StandardGameMvp.SettingsModel;

public class StandardGameSettingsPresenter implements SettingsPresenter {

    private SettingsView mSettingsView;
    private SettingsModel mSettingsModel;

    private boolean mIsUltimate;

    public StandardGameSettingsPresenter(SettingsView settingsView) {
        mSettingsView = settingsView;
        mSettingsModel = new StandardGameSettingsModel(this);
        mIsUltimate = false;
    }

    @Override
    public void onUltimateSwitched(boolean isUltimate) {
        mIsUltimate = isUltimate;
        if (mIsUltimate) {
            mSettingsView.lockDifficultySwitch();
            mSettingsView.showUltimateInstructions();
        } else {
            mSettingsView.unlockDifficultySwitch();
            mSettingsView.showSingleInstructions();
        }
    }

    @Override
    public void onStartClicked() {
        int boardType = mIsUltimate ?
                BoardInitData.BOARD_TYPE_CLASSICAL_ULTIMATE : BoardInitData.BOARD_TYPE_SINGLE;
        String gameInitDataJson = mSettingsModel.getGameInitDataJson(boardType, mSettingsView.getPlayerInitData());
        mSettingsView.startGame(gameInitDataJson);
    }
}
