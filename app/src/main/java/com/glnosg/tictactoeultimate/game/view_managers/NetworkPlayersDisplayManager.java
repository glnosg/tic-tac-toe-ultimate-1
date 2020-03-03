package com.glnosg.tictactoeultimate.game.view_managers;

import android.util.Log;
import android.view.View;

import com.glnosg.TicTacToeUltimate;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGamePlayer;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NetworkPlayersDisplayManager {

    private final String LOG_TAG = NetworkPlayersDisplayManager.class.getSimpleName();

    private RecyclerView mDisplay;
    private NetworkPlayersAdapter mAdapter;

    public NetworkPlayersDisplayManager(RecyclerView playersDisplay) {
        mDisplay = playersDisplay;
        initDisplay();
    }

    private void initDisplay() {
        mDisplay.setLayoutManager(new LinearLayoutManager(TicTacToeUltimate.getAppContext()));
        mAdapter = new NetworkPlayersAdapter();
        mDisplay.setAdapter(mAdapter);
    }

    public void show() {
        mDisplay.setVisibility(View.VISIBLE);
    }

    public void hide() {
        mDisplay.setVisibility(View.GONE);
    }

    public void updateDisplay(List<NetworkGamePlayer> players) {
        mAdapter.setData(players);
        mAdapter.notifyDataSetChanged();
    }

    public void updateActivePlayer(int activePlayerId) {
        mAdapter.setActivePlayerId(activePlayerId);
        mAdapter.notifyDataSetChanged();
    }

    public void deactivatePlayers() {
        mAdapter.deactivatePlayers();
    }
}
