package com.glnosg.tictactoeultimate.game.view_managers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.glnosg.TicTacToeUltimate;
import com.glnosg.tictactoeultimate.R;
import com.glnosg.tictactoeultimate.game.board.figure.Figure;
import com.glnosg.tictactoeultimate.game.board.figure.FigureFactory;
import com.glnosg.tictactoeultimate.game.player.Player;
import com.glnosg.tictactoeultimate.settings.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LocalPlayersAdapter extends RecyclerView.Adapter<LocalPlayersAdapter.PlayerDisplayVH> {

    private final String LOG_TAG = LocalPlayersAdapter.class.getSimpleName();

    private Context mContext;
    private List<Player> mPlayers;
    private DifficultyOnClickListener mDifficultyOnClickListener;
    private boolean mIsDifficultySwitchLocked;

    public LocalPlayersAdapter() {
        mContext = TicTacToeUltimate.getAppContext();
        mIsDifficultySwitchLocked = true;
        mPlayers = new ArrayList<>();
        PreferencesUtils.updateLocale();
    }

    public void attachDifficultyOnClickListener(DifficultyOnClickListener listener) {
        mDifficultyOnClickListener = listener;
    }

    public void lockDifficultySwitch() {
        mIsDifficultySwitchLocked = true;
    }

    public void unlockDifficultySwitch() {
        mIsDifficultySwitchLocked = false;
    }

    public void setData(List<Player> players) {
        mPlayers = players;
    }

    @NonNull
    @Override
    public PlayerDisplayVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View playerView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.list_item_local_player, parent, false);
        return new PlayerDisplayVH(playerView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerDisplayVH holder, int position) {
        Player player = mPlayers.get(position);
        displayShape(holder.shapeDisplay, player);
        displayName(holder.nameDisplay, player);
        holder.scoreDisplay.setText(Integer.toString(player.getScore()));
        displayDifficulty(holder.difficultyDisplay, player);
    }

    private void displayShape(ImageView display, Player player) {
        Figure figure = FigureFactory.getShape(player.getId());
        int bitmapId;
        if (figure != null) {
            if (player.isActive()) bitmapId = figure.getBigWinningBitmapId(0);
            else bitmapId = figure.getBigSolidBitmapId(0);
            display.setImageResource(bitmapId);
        } else {
            Log.e(LOG_TAG, "[displayShape] FigureFactory returned null Figure");
        }
    }

    private void displayName(TextView display, Player player) {
        final String NAME_SCORE_SEPARATOR = ": ";
        String nameLabel = player.getName() + NAME_SCORE_SEPARATOR;
        display.setText(nameLabel);
    }

    private void displayDifficulty(TextView display, Player player) {
        switch (player.getDifficulty()) {
            case HUMAN:
                display.setText(mContext.getString(R.string.player_control_label_human));
                break;
            case EASY:
                display.setText(mContext.getString(R.string.player_control_label_bot_easy));
                break;
            case MEDIUM:
                display.setText(mContext.getString(R.string.player_control_label_bot_medium));
                break;
            case HARD:
                display.setText(mContext.getString(R.string.player_control_label_bot_hard));
        }
    }

    @Override
    public int getItemCount() {
        return mPlayers.size();
    }

    public interface DifficultyOnClickListener {
        void onDifficultyClicked(int position);
    }

    class PlayerDisplayVH extends RecyclerView.ViewHolder {

        private Toast mToast;
        ImageView shapeDisplay;
        TextView nameDisplay, scoreDisplay, difficultyDisplay;

        public PlayerDisplayVH(View playerView) {
            super(playerView);
            shapeDisplay = playerView.findViewById(R.id.iv_player_shape);
            nameDisplay = playerView.findViewById(R.id.tv_name);
            scoreDisplay = playerView.findViewById(R.id.tv_score);
            difficultyDisplay = playerView.findViewById(R.id.tv_difficulty);
            difficultyDisplay.setOnClickListener((View v) -> switchDifficulty());
        }

        private void switchDifficulty() {
            Log.d(LOG_TAG, "[switchDifficulty] adapterPosition = " + getAdapterPosition());

            if (mIsDifficultySwitchLocked) {
                showToast(mContext.getString(R.string.message_bot_not_supported));
            } else {
                mDifficultyOnClickListener.onDifficultyClicked(getAdapterPosition());
            }
        }

        private void showToast(String text) {
            if (mToast != null) mToast.cancel();
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
            mToast.show();
        }
    }
}
