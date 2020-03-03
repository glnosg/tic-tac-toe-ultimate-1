package com.glnosg.tictactoeultimate.main_menu.load_game;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.glnosg.tictactoeultimate.R;
import com.glnosg.tictactoeultimate.main_menu.load_game.GameSavePreview.GameSavePreview;
import com.glnosg.tictactoeultimate.main_menu.load_game.GameSavePreview.PlayerPreview;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GameSaveAdapter extends RecyclerView.Adapter<GameSaveAdapter.GameSaveViewHolder> {

    private final String PLAYER_SCORE_SEPARATOR = ": ", BOT_LABEL = " (BOT)", NO_PLAYER = " - ";

    private List<GameSavePreview> mGameSavePreviews;
    private ItemClickListener mItemClickListener;
    private Context mContext;

    public GameSaveAdapter(Context context, ItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
        mContext = context;
    }

    @NonNull
    @Override
    public GameSaveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View gameSaveView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.list__item_game_saves, parent, false);
        return new GameSaveViewHolder(gameSaveView);
    }

    @Override
    public void onBindViewHolder(@NonNull GameSaveViewHolder holder, int position) {
        GameSavePreview gameSavePreview = mGameSavePreviews.get(position);
        holder.dateDisplay.setText(gameSavePreview.getFormattedDate());
        if (gameSavePreview.isBoardUltimate()) {
            holder.boardTypeDisplay.setText(mContext.getString(R.string.label_multi_board));
        } else {
            holder.boardTypeDisplay.setText(mContext.getString(R.string.label_single_board));
        }
        initPlayerDisplay(gameSavePreview.getPlayerDisplayData(0), holder.player0Display);
        initPlayerDisplay(gameSavePreview.getPlayerDisplayData(1), holder.player1Display);
        initPlayerDisplay(gameSavePreview.getPlayerDisplayData(2), holder.player2Display);
        initPlayerDisplay(gameSavePreview.getPlayerDisplayData(3), holder.player3Display);
    }

    private void initPlayerDisplay(PlayerPreview displayData, TextView display) {
        if (displayData == null) {
            display.setText(NO_PLAYER);
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append(displayData.getName());
            builder.append(PLAYER_SCORE_SEPARATOR);
            builder.append(displayData.getScore());
            if (!displayData.isHuman())
                builder.append(BOT_LABEL);
            display.setText(builder.toString());
        }
    }

    @Override
    public int getItemCount() {
        if (mGameSavePreviews == null) return 0;
        else return mGameSavePreviews.size();
    }

    public List<GameSavePreview> getGameSavePreviews() {
        return mGameSavePreviews;
    }

    public void setGameSavePreviews(List<GameSavePreview> gameSavePreviews) {
        mGameSavePreviews = gameSavePreviews;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClicked(long itemPosition);
    }

    class GameSaveViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView dateDisplay, boardTypeDisplay;
        TextView player0Display, player1Display, player2Display, player3Display;

        public GameSaveViewHolder(View gameSaveView) {
            super(gameSaveView);
            dateDisplay = gameSaveView.findViewById(R.id.tv_date);
            boardTypeDisplay = gameSaveView.findViewById(R.id.tv_board_type);
            player0Display = gameSaveView.findViewById(R.id.tv_player0);
            player1Display = gameSaveView.findViewById(R.id.tv_player1);
            player2Display = gameSaveView.findViewById(R.id.tv_player2);
            player3Display = gameSaveView.findViewById(R.id.tv_player3);
            gameSaveView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            long elementId = mGameSavePreviews.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClicked(elementId);
        }
    }
}
