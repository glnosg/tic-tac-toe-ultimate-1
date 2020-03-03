package com.glnosg.tictactoeultimate.main_menu.play_online_settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.glnosg.TicTacToeUltimate;
import com.glnosg.tictactoeultimate.R;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGame;
import com.glnosg.tictactoeultimate.settings.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OpenGamesAdapter extends RecyclerView.Adapter<OpenGamesAdapter.GameWaitingVH> {

    private final String LOG_TAG = OpenGamesAdapter.class.getSimpleName();
    private final String LABEL_VALUE_SEPARATOR = ": ";

    private List<OpenNetworkGame> mOpenNetworkGames;
    private OpenGamesListEventListener mOpenGamesListEventListener;
    private Context mContext;

    public OpenGamesAdapter() {
        mOpenNetworkGames = new ArrayList<>();
        mContext = TicTacToeUltimate.getAppContext();
        PreferencesUtils.updateLocale();
    }

    public void attachOpenGamesListEventListener(OpenGamesListEventListener listener) {
        mOpenGamesListEventListener = listener;
    }

    public void setData(List<OpenNetworkGame> openGames) {
        mOpenNetworkGames = openGames;
        mOpenGamesListEventListener.onFirstOpenGameAdded();
    }

    public void addGame(OpenNetworkGame game) {
       if (!isAlreadyAdded(game.getGameId())) {
           mOpenNetworkGames.add(game);
           if (mOpenNetworkGames.size() == 1) mOpenGamesListEventListener.onFirstOpenGameAdded();
       }
    }

    private boolean isAlreadyAdded(String gameId) {
        for (OpenNetworkGame currentGame : mOpenNetworkGames) {
            if (currentGame.getGameId().equals(gameId)) return true;
        }
        return false;
    }

    public void removeGame(String gameId) {
        for (int i = mOpenNetworkGames.size() - 1; i >=0; i--) {
            if (mOpenNetworkGames.get(i).getGameId().equals(gameId)) {
                mOpenNetworkGames.remove(i);
            }
        }
        if (mOpenNetworkGames.size() == 0) mOpenGamesListEventListener.onOpenGamesListEmpty();
    }

    public void clear() {
        mOpenNetworkGames.clear();
    }

    @NonNull
    @Override
    public GameWaitingVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View gameWaitingView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.list_item_open_game, parent, false);
        return new GameWaitingVH(gameWaitingView);
    }

    @Override
    public void onBindViewHolder(@NonNull GameWaitingVH holder, int position) {
        OpenNetworkGame game = mOpenNetworkGames.get(position);
        holder.nameDisplay.setText(game.getCreatorName());
        String gamesPlayedFormatted = mContext.getString(R.string.net_player_label_played)
                + LABEL_VALUE_SEPARATOR
                + game.getCreatorGamesPlayed();
        holder.gamesPlayedDisplay.setText(gamesPlayedFormatted);
        String gamesWonFormatted = mContext.getString(R.string.net_player_label_won)
                + LABEL_VALUE_SEPARATOR
                + game.getCreatorGamesWon();
        holder.gamesWonDisplay.setText(gamesWonFormatted);
        String gamesLeftFormatted = mContext.getString(R.string.net_player_label_left)
                + LABEL_VALUE_SEPARATOR
                + game.getCreatorGamesLeft();
        holder.gamesLeftDisplay.setText(gamesLeftFormatted);
        displayBoardType(holder.boardTypeDisplay, game.getBoardType());
        setTopDivider(holder.topDivider, position);
    }

    private void displayBoardType(TextView boardTypeDisplay, int boardType) {
        if (boardType == NetworkGame.ULTIMATE) {
            boardTypeDisplay.setText(mContext.getString(R.string.label_ultimate));
        } else if (boardType == NetworkGame.SINGLE) {
            boardTypeDisplay.setText(mContext.getString(R.string.label_single_board));
        }
    }

    private void setTopDivider(View view, int position) {
        if (position == 0) view.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return mOpenNetworkGames.size();
    }

    public interface OpenGamesListEventListener {
        void onFirstOpenGameAdded();
        void onOpenGameClicked(String clickedGameId);
        void onOpenGamesListEmpty();
    }

    class GameWaitingVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nameDisplay, gamesPlayedDisplay, gamesWonDisplay, gamesLeftDisplay;
        TextView boardTypeDisplay;
        View topDivider;

        public GameWaitingVH(View openGameView) {
            super(openGameView);
            nameDisplay = openGameView.findViewById(R.id.tv_author_name);
            gamesPlayedDisplay = openGameView.findViewById(R.id.tv_author_games_played);
            gamesWonDisplay = openGameView.findViewById(R.id.tv_author_games_won);
            gamesLeftDisplay = openGameView.findViewById(R.id.tv_author_games_left);
            boardTypeDisplay = openGameView.findViewById(R.id.tv_board_type);
            topDivider = openGameView.findViewById(R.id.view_top_divider);
            openGameView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String clickedGameId = mOpenNetworkGames.get(getAdapterPosition()).getGameId();
            mOpenGamesListEventListener.onOpenGameClicked(clickedGameId);
        }
    }
}
