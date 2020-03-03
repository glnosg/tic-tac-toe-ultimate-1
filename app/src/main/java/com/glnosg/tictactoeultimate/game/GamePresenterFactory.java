package com.glnosg.tictactoeultimate.game;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.glnosg.TicTacToeUltimate;
import com.glnosg.tictactoeultimate.R;
import com.glnosg.tictactoeultimate.data.database.AppDatabase;
import com.glnosg.tictactoeultimate.game.local_game.LocalGamePresenter;
import com.glnosg.tictactoeultimate.game.network_game.NetworkGamePresenter;

public class GamePresenterFactory {

    private static final String LOG_TAG = GamePresenterFactory.class.getSimpleName();

    public static GamePresenter getGamePresenter(GameView gameView, Intent intent) {
        Context context = TicTacToeUltimate.getAppContext();
        String isBotSupportedKey = context.getString(R.string.intent_key_is_bot_supported);
        String gameInitDataKey = context.getString(R.string.intent_key_game_init_data);
        String gameSaveKey = context.getString(R.string.intent_key_game_save_id);
        String gameOnlineKey = context.getString(R.string.intent_key_game_online_id);
        GamePresenter gamePresenter;
        if (intent.hasExtra(gameOnlineKey)) {
            gamePresenter = new NetworkGamePresenter(gameView, intent.getStringExtra(gameOnlineKey));
        } else if (intent.hasExtra(gameSaveKey)) {
            gamePresenter = new LocalGamePresenter(gameView, intent.getLongExtra(gameSaveKey, AppDatabase.NOT_SAVED));
        } else if (intent.hasExtra(gameInitDataKey)) {
            gamePresenter = new LocalGamePresenter(gameView, intent.getStringExtra(gameInitDataKey));
        } else {
            Log.e(LOG_TAG, "Something went wrong. Returning default presenter.");
            gamePresenter = new LocalGamePresenter(gameView);
        }
        gamePresenter.setIsBotSupported(intent.getBooleanExtra(isBotSupportedKey, false));
        return gamePresenter;
    }
}
