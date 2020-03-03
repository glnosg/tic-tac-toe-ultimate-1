package com.glnosg.tictactoeultimate.game.view_managers;

import android.view.View;

import com.glnosg.TicTacToeUltimate;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGamePlayer;
import com.glnosg.tictactoeultimate.game.view_managers.JoinerCandidatesAdapter.CandidateButtonsOnClickListener;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class JoinerCandidatesDisplayManager {

    private RecyclerView mDisplay;
    private JoinerCandidatesAdapter mAdapter;

    public JoinerCandidatesDisplayManager(RecyclerView candidatesDisplay) {
        mDisplay = candidatesDisplay;
        initDisplay();
    }

    private void initDisplay() {
        mDisplay.setLayoutManager(new LinearLayoutManager(TicTacToeUltimate.getAppContext()));
        mAdapter = new JoinerCandidatesAdapter();
        mDisplay.setAdapter(mAdapter);
    }

    public void show() {
        mDisplay.setVisibility(View.VISIBLE);
    }

    public void hide() {
        mAdapter.clear();
        mDisplay.setVisibility(View.GONE);
    }

    public void update(List<NetworkGamePlayer> candidates) {
        mAdapter.setData(candidates);
        mAdapter.notifyDataSetChanged();
    }

    public void attachCandidateButtonsOnClickListener(CandidateButtonsOnClickListener listener) {
        mAdapter.attachOnClickListener(listener);
    }
}
