package com.glnosg.tictactoeultimate.main_menu.custom_game_settings;

import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.BoardInitData;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.GameInitData;
import com.glnosg.tictactoeultimate.main_menu.custom_game_settings.CustomGameMvp.SettingsView;
import com.glnosg.tictactoeultimate.main_menu.custom_game_settings.CustomGameMvp.SettingsPresenter;
import com.glnosg.tictactoeultimate.main_menu.custom_game_settings.CustomGameMvp.SettingsModel;


public class CustomGameSettingsPresenter implements SettingsPresenter {

    private SettingsView mView;
    private SettingsModel mSettingsModel;
    private BoardSanityController mSanityController;

    private int mPlayersMin, mPlayersMax;
    private int mPlayersCounter;
    private boolean mIsUltimate;

    public CustomGameSettingsPresenter(SettingsView settingsView) {
        mView = settingsView;
        mSettingsModel = new CustomGameSettingsModel(this);
        mPlayersMin = GameInitData.PLAYERS_MIN;
        mSanityController = new BoardSanityController(this);
        mSanityController.attachSettingsView(mView);
        mPlayersCounter = mPlayersMin;
        mIsUltimate = false;
    }

    @Override
    public void onAddClicked() {
        if (mPlayersCounter == mPlayersMax) {
            mView.showBoardToSmallMessage();
            return;
        }
        if (mPlayersCounter == mPlayersMin) {
            mView.showRemoveButton();
            mView.addRowOnPlayersDisplay();
            mPlayersCounter++;
        } else if (mPlayersCounter > mPlayersMin && mPlayersCounter < GameInitData.PLAYERS_MAX_CUSTOM - 1) {
            mView.addRowOnPlayersDisplay();
            mPlayersCounter++;
        } else if (mPlayersCounter == GameInitData.PLAYERS_MAX_CUSTOM - 1) {
            mView.hideAddButton();
            mView.addRowOnPlayersDisplay();
            mPlayersCounter++;
        }
    }

    @Override
    public void onRemoveClicked() {
        if (mPlayersCounter == GameInitData.PLAYERS_MAX_CUSTOM) {
            mView.showAddButton();
            mView.removeRowFromPlayersDisplay();
            mPlayersCounter--;
        } else if (mPlayersCounter < GameInitData.PLAYERS_MAX_CUSTOM && mPlayersCounter > mPlayersMin + 1) {
            mView.removeRowFromPlayersDisplay();
            mPlayersCounter--;
        } else if (mPlayersCounter == mPlayersMin + 1) {
            mView.hideRemoveButton();
            mView.removeRowFromPlayersDisplay();
            mPlayersCounter--;
        }
    }

    @Override
    public void setMaxNumberOfPlayers(int number) {
        mPlayersMax = number;
        while (mPlayersCounter > mPlayersMax) {
            onRemoveClicked();
        }
    }

    @Override
    public void onColumnsPickerScrolled(int currentValue) {
        mSanityController.onColumnsPickerScrolled(currentValue);
    }

    @Override
    public void onRowsPickerScrolled(int currentValue) {
        mSanityController.onRowsPickerScrolled(currentValue);
    }

    @Override
    public void onUltimateSwitched(boolean isUltimate) {
        mIsUltimate = isUltimate;
        if (isUltimate) mView.showUltimateSettings();
        else mView.hideUltimateSettings();
    }

    @Override
    public void onStartClicked() {
        int boardType = mIsUltimate ? mView.getHowManyRemainActive() : BoardInitData.BOARD_TYPE_SINGLE;
        String gameInitDataJson = mSettingsModel.getGameInitDataJson(
                mView.getNumberOfColumns(),
                mView.getNumberOfRows(),
                mView.getHowManyInLineToWin(),
                boardType,
                mView.getPlayersInitData());
        mView.startGame(gameInitDataJson);
    }
}
