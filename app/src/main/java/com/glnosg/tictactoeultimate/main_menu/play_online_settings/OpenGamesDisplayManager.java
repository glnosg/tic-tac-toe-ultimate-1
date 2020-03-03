package com.glnosg.tictactoeultimate.main_menu.play_online_settings;

import android.view.View;

import com.glnosg.TicTacToeUltimate;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGame;
import com.glnosg.tictactoeultimate.main_menu.play_online_settings.OpenGamesAdapter.OpenGamesListEventListener;


import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OpenGamesDisplayManager {

    private RecyclerView mDisplay;
    private OpenGamesAdapter mAdapter;

    public OpenGamesDisplayManager(RecyclerView openGamesDisplay) {
        mDisplay = openGamesDisplay;
        initDisplay();
    }

    private void initDisplay() {
        mDisplay.setLayoutManager(new LinearLayoutManager(TicTacToeUltimate.getAppContext()));
        mAdapter = new OpenGamesAdapter();
        mDisplay.setAdapter(mAdapter);
    }

    public void attachOpenGamesListEventListener(OpenGamesListEventListener listener) {
        mAdapter.attachOpenGamesListEventListener(listener);
    }

    public void show() {
        mDisplay.setVisibility(View.VISIBLE);
    }

    public void hide() {
        mDisplay.setVisibility(View.GONE);
    }

    public void setData(List<OpenNetworkGame> openGames) {
        mAdapter.setData(openGames);
        mAdapter.notifyDataSetChanged();
    }

    public void addGame(OpenNetworkGame game) {
        mAdapter.addGame(game);
        mAdapter.notifyDataSetChanged();
    }

    public void removeGame(String gameId) {
        mAdapter.removeGame(gameId);
        mAdapter.notifyDataSetChanged();
    }

    public void clearData() {
        mAdapter.clear();
        mAdapter.notifyDataSetChanged();
    }
}
