package com.glnosg.tictactoeultimate.main_menu.custom_game_settings;

import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.GameInitData;
import com.glnosg.tictactoeultimate.main_menu.custom_game_settings.CustomGameMvp.SettingsPresenter;
import com.glnosg.tictactoeultimate.main_menu.custom_game_settings.CustomGameMvp.SettingsView;

public class BoardSanityController {

    public static final int FIGURES_IN_ROW_MIN = 3, FIGURES_IN_ROW_MAX = 5;
    public static final int COLUMNS_MIN = 3, COLUMNS_MAX = 20;
    public static final int ROWS_MIN = 3, ROWS_MAX = 50;
    public static final int ACTIVE_BOARDS_MIN = 1, ACTIVE_BOARDS_MAX = 9;

    private SettingsView mSettingsView;
    private SettingsPresenter mSettingsPresenter;

    private int mColumnsPickerCurrentValue, mRowsPickerCurrentValue;

    public BoardSanityController(SettingsPresenter settingsPresenter) {
        mSettingsPresenter = settingsPresenter;
        mSettingsPresenter.setMaxNumberOfPlayers(GameInitData.PLAYERS_MIN);
    }

    public void attachSettingsView(SettingsView settingsView) {
        mSettingsView = settingsView;
    }

    public void onColumnsPickerScrolled(int currentValue) {
        mColumnsPickerCurrentValue = currentValue;
        setHowManyInRowMaxValue();
        setMaxNumberOfPlayers();
    }

    public void onRowsPickerScrolled(int currentValue) {
        mRowsPickerCurrentValue = currentValue;
        setHowManyInRowMaxValue();
        setMaxNumberOfPlayers();
    }

    private void setHowManyInRowMaxValue() {
        if (mColumnsPickerCurrentValue > 4 && mRowsPickerCurrentValue > 4) {
            mSettingsView.setHowManyInRowMaxValue(FIGURES_IN_ROW_MAX);
        } else if (mColumnsPickerCurrentValue > 3 && mRowsPickerCurrentValue > 3) {
            mSettingsView.setHowManyInRowMaxValue(FIGURES_IN_ROW_MAX - 1);
        } else {
            mSettingsView.setHowManyInRowMaxValue(FIGURES_IN_ROW_MIN);
        }
    }

    private void setMaxNumberOfPlayers() {
        if (mColumnsPickerCurrentValue > 6 && mRowsPickerCurrentValue > 6) {
            mSettingsPresenter.setMaxNumberOfPlayers(GameInitData.PLAYERS_MAX_CUSTOM);
        } else if (mColumnsPickerCurrentValue > 4 && mRowsPickerCurrentValue > 4) {
            mSettingsPresenter.setMaxNumberOfPlayers(GameInitData.PLAYERS_MIN + 1);
        } else {
            mSettingsPresenter.setMaxNumberOfPlayers(GameInitData.PLAYERS_MIN);
        }
    }
}
