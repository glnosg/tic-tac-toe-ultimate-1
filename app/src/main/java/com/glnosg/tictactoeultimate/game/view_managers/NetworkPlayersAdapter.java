package com.glnosg.tictactoeultimate.game.view_managers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glnosg.TicTacToeUltimate;
import com.glnosg.tictactoeultimate.R;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.GameInitData;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGamePlayer;
import com.glnosg.tictactoeultimate.game.board.figure.Figure;
import com.glnosg.tictactoeultimate.game.board.figure.FigureFactory;
import com.glnosg.tictactoeultimate.settings.PreferencesUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NetworkPlayersAdapter extends RecyclerView.Adapter<NetworkPlayersAdapter.PlayerDisplayVH> {

    private final String LOG_TAG = NetworkGamePlayer.class.getSimpleName();

    private List<NetworkGamePlayer> mPlayers;
    private Context mContext;
    private int mActivePlayerId;

    public NetworkPlayersAdapter() {
        mPlayers = new ArrayList<>();
        mContext = TicTacToeUltimate.getAppContext();
        mActivePlayerId = GameInitData.NO_ACTIVE_PLAYER;
        PreferencesUtils.updateLocale();
    }

    public void setData(List<NetworkGamePlayer> players) {
        mPlayers.clear();
        mPlayers = players;
        Collections.sort(mPlayers);
    }

    public void setActivePlayerId(int playerId) {
        mActivePlayerId = playerId;
    }

    public void deactivatePlayers() {
        mActivePlayerId = GameInitData.NO_ACTIVE_PLAYER;
    }

    @NonNull
    @Override
    public PlayerDisplayVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View playerView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.list_item_network_player, parent, false);
        return new PlayerDisplayVH(playerView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerDisplayVH holder, int position) {
        String labelValueSeparator = ": ";
        NetworkGamePlayer player = mPlayers.get(position);
        displayShape(holder.shapeDisplay, player.getPlayerId());
        holder.nameDisplay.setText(player.getName());
        String gamesPlayed = mContext.getString(R.string.net_player_label_played)
                        + labelValueSeparator
                        + player.getGamesPlayed();
        holder.gamesPlayedDisplay.setText(gamesPlayed);
        String gamesWon = mContext.getString(R.string.net_player_label_won)
                + labelValueSeparator
                + player.getGamesWon();
        holder.gamesWonDisplay.setText(gamesWon);
        String gamesLeft = mContext.getString(R.string.net_player_label_left)
                + labelValueSeparator
                + player.getGamesLeft();
        holder.gamesLeftDisplay.setText(gamesLeft);
    }

    private void displayShape(ImageView display, int playerId) {
        Figure figure = FigureFactory.getShape(playerId);
        int bitmapId;
        if (figure != null) {
            if (playerId == mActivePlayerId) bitmapId = figure.getBigWinningBitmapId(0);
            else bitmapId = figure.getBigSolidBitmapId(0);
            display.setImageResource(bitmapId);
        } else {
            Log.e(LOG_TAG, "[displayShape] FigureFactory returned null Figure");
        }
    }

    @Override
    public int getItemCount() {
        return mPlayers.size();
    }

    class PlayerDisplayVH extends RecyclerView.ViewHolder {

        ImageView shapeDisplay;
        TextView nameDisplay, gamesPlayedDisplay, gamesWonDisplay, gamesLeftDisplay;

        public PlayerDisplayVH(View playerView) {
            super(playerView);
            shapeDisplay = playerView.findViewById(R.id.iv_player_shape);
            nameDisplay = playerView.findViewById(R.id.tv_name);
            gamesPlayedDisplay = playerView.findViewById(R.id.tv_games_played);
            gamesWonDisplay = playerView.findViewById(R.id.tv_games_won);
            gamesLeftDisplay = playerView.findViewById(R.id.tv_games_left);
        }
    }
}
