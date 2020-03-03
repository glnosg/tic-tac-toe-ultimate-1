package com.glnosg.tictactoeultimate.game.view_managers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.glnosg.tictactoeultimate.R;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGame;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGamePlayer;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class JoinerCandidatesAdapter
        extends RecyclerView.Adapter<JoinerCandidatesAdapter.JoinerCandidateVH> {

    private final String LOG_TAG = JoinerCandidatesAdapter.class.getSimpleName();

    private List<NetworkGamePlayer> mCandidates;
    private CandidateButtonsOnClickListener mOnClickListener;

    public JoinerCandidatesAdapter() { }

    public void setData(List<NetworkGamePlayer> candidates) {
        mCandidates = candidates;
    }

    public void clear() {
        mCandidates.clear();
    }

    public void attachOnClickListener(CandidateButtonsOnClickListener listener) {
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public JoinerCandidateVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View candidateView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.list_item_joiner_candidate, parent, false);
        return new JoinerCandidateVH(candidateView);
    }

    @Override
    public void onBindViewHolder(@NonNull JoinerCandidateVH holder, int position) {
        NetworkGamePlayer candidate = mCandidates.get(position);
        holder.nameDisplay.setText(candidate.getName());
        displayUserStats(holder.statsDisplay, candidate);
        setButtons(holder, candidate);
    }

    private void displayUserStats(TextView statsDisplay, NetworkGamePlayer candidate) {
        String stats = "("
                + candidate.getGamesPlayed() + ", "
                + candidate.getGamesWon() + ", "
                + candidate.getGamesLeft() + ")";
        statsDisplay.setText(stats);
    }

    private void setButtons(JoinerCandidateVH holder, NetworkGamePlayer candidate) {
        if (candidate.getState() == NetworkGame.ACCEPTED) {
            holder.acceptButton.setVisibility(View.GONE);
            holder.declineButton.setVisibility(View.GONE);
            holder.acceptedDisplay.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mCandidates.size();
    }

    public interface CandidateButtonsOnClickListener {
        void onAcceptJoinerClicked(String userId);
        void onDeclineJoinerClicked(String userId);
    }

    class JoinerCandidateVH extends RecyclerView.ViewHolder {

        TextView nameDisplay, statsDisplay, acceptedDisplay;
        Button acceptButton, declineButton;

        public JoinerCandidateVH(View candidateView) {
            super(candidateView);
            nameDisplay = candidateView.findViewById(R.id.tv_name);
            statsDisplay = candidateView.findViewById(R.id.tv_stats);
            acceptedDisplay = candidateView.findViewById(R.id.tv_accepted);
            acceptButton = candidateView.findViewById(R.id.button_accept);
            declineButton = candidateView.findViewById(R.id.button_decline);
            initButtons();
        }

        private void initButtons() {
            acceptButton.setOnClickListener((View v) -> {
                if (getAdapterPosition() != RecyclerView.NO_POSITION && mCandidates.size() > 0) {
                    String candidateUid = mCandidates.get(getAdapterPosition()).getUserId();
                    mOnClickListener.onAcceptJoinerClicked(candidateUid);
                }
                    });
            declineButton.setOnClickListener((View v) -> {
                if (getAdapterPosition() != RecyclerView.NO_POSITION && mCandidates.size() > 0) {
                    String userId = mCandidates.get(getAdapterPosition()).getUserId();
                    mOnClickListener.onDeclineJoinerClicked(userId);
                }
            });
        }
    }
}
