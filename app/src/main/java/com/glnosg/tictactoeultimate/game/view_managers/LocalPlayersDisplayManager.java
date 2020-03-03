package com.glnosg.tictactoeultimate.game.view_managers;

import android.view.View;

import com.glnosg.TicTacToeUltimate;
import com.glnosg.tictactoeultimate.game.player.Player;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LocalPlayersDisplayManager {

    private RecyclerView mDisplay;
    private LocalPlayersAdapter mAdapter;

    public LocalPlayersDisplayManager(RecyclerView playersDisplay) {
        mDisplay = playersDisplay;
        initDisplay();
    }

    private void initDisplay() {
        mDisplay.setLayoutManager(new LinearLayoutManager(TicTacToeUltimate.getAppContext()));
        mAdapter = new LocalPlayersAdapter();
        mDisplay.setAdapter(mAdapter);
    }

    public void show() {
        mDisplay.setVisibility(View.VISIBLE);
    }

    public void hide() {
        mDisplay.setVisibility(View.GONE);
    }

    public void lockDifficultySwitch() {
        mAdapter.lockDifficultySwitch();
    }

    public void unlockDifficultySwitch() {
        mAdapter.unlockDifficultySwitch();
    }

    public void update(List<Player> players) {
        mAdapter.setData(players);
        mAdapter.notifyDataSetChanged();
    }

    public void attachDifficultyOnClickListener(LocalPlayersAdapter.DifficultyOnClickListener listener) {
        mAdapter.attachDifficultyOnClickListener(listener);
    }
}
