package com.glnosg.tictactoeultimate.main_menu.custom_game_settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.glnosg.tictactoeultimate.R;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.PlayerInitData;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class PlayerSettingsAdapter extends RecyclerView.Adapter<PlayerSettingsAdapter.PlayerDisplayVH> {

    private final int ROWS_MIN = 2;

    private Context mContext;
    private Toast mToast;
    private Stack<PlayerInitData> mPlayerInitData;
    private List<EditText> mNameInputs;
    private boolean mIsDifficultySwitchLocked;

    public PlayerSettingsAdapter(Context context) {
        mContext = context;
        mNameInputs = new ArrayList<>();
        setInitialData();
    }

    private void setInitialData() {
        mPlayerInitData = new Stack<>();
        for (int i = 0; i < ROWS_MIN; i++) {
            mPlayerInitData.push(new PlayerInitData());
        }
    }

    public void lockDifficultySwitch() {
        mIsDifficultySwitchLocked = true;
        for(PlayerInitData playerData : mPlayerInitData) {
            playerData.setDifficulty(PlayerInitData.Difficulty.HUMAN);
        }
    }

    public void unlockDifficultySwitch() {
        mIsDifficultySwitchLocked = false;
    }

    public boolean isDifficultySwitchLocked() {
        return mIsDifficultySwitchLocked;
    }

    public void addRow() {
        mPlayerInitData.push(new PlayerInitData());
    }

    public void removeRow() {
        if (mPlayerInitData.size() > ROWS_MIN) mPlayerInitData.pop();
    }

    public List<PlayerInitData> getPlayerInitData() {
        List<PlayerInitData> playerInitData = new LinkedList<>();
        for (int i = 0; i < mPlayerInitData.size(); i++) {
            PlayerInitData singlePlayerInitData = mPlayerInitData.get(i);
            singlePlayerInitData.setName(getValidName(i));
            playerInitData.add(singlePlayerInitData);
        }
        return playerInitData;
    }

    private String getValidName(int position) {
        String input = mNameInputs.get(position).getText().toString();
        if (input.trim().equals("")) return getPlayerName(position);
        else return input;
    }

    private String getPlayerName(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append(mContext.getString(R.string.player_label));
        builder.append(" ");
        builder.append(position + 1);
        return builder.toString();
    }

    @NonNull
    @Override
    public PlayerDisplayVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View playerSettingsView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.list_item_player_settings, parent, false);

        return new PlayerDisplayVH(playerSettingsView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerDisplayVH holder, int position) {
        holder.playerLabelDisplay.setText(getPlayerLabel(position));
        displayDifficultyLevel(holder.difficultyDisplay, position);
        mNameInputs.add(position, holder.nameInput);
    }

    private String getPlayerLabel(int adapterPosition) {
        StringBuilder builder = new StringBuilder();
        builder.append(mContext.getString(R.string.player_label));
        builder.append(" ");
        builder.append(adapterPosition + 1);
        builder.append(": ");
        return builder.toString();
    }

    private void displayDifficultyLevel(TextView display, int adapterPosition) {
        PlayerInitData playerInitData = mPlayerInitData.get(adapterPosition);
        PlayerInitData.Difficulty difficulty = playerInitData.getDifficulty();
        switch (difficulty) {
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
        return mPlayerInitData.size();
    }

    class PlayerDisplayVH extends RecyclerView.ViewHolder {

        TextView playerLabelDisplay, difficultyDisplay;
        EditText nameInput;

        public PlayerDisplayVH(View playerSettingsView) {
            super(playerSettingsView);
            playerLabelDisplay = playerSettingsView.findViewById(R.id.tv_player_label);
            difficultyDisplay = playerSettingsView.findViewById(R.id.tv_difficulty);
            difficultyDisplay.setOnClickListener((View v) -> {
                switchDifficulty(getAdapterPosition());
                displayDifficultyLevel(difficultyDisplay, getAdapterPosition());
            });
            nameInput = playerSettingsView.findViewById(R.id.et_name);
        }
    }

    private void switchDifficulty(int adapterPosition) {
        if(mIsDifficultySwitchLocked) {
            showToast(mContext.getString(R.string.message_bot_not_supported));
        } else {
            PlayerInitData playerInitData = mPlayerInitData.get(adapterPosition);
            playerInitData.switchDifficulty();
        }
    }

    private void showToast(String text) {
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        mToast.show();
    }
}
