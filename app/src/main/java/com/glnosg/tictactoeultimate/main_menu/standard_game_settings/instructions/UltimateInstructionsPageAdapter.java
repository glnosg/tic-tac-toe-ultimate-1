package com.glnosg.tictactoeultimate.main_menu.standard_game_settings.instructions;

import android.content.Context;
import com.glnosg.tictactoeultimate.R;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

public class UltimateInstructionsPageAdapter extends InstructionsPageAdapter {

    public UltimateInstructionsPageAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm, context);
    }

    @Override
    protected void initInstructionsData() {
        mInstructionsData = new ArrayList<>();
        mInstructionsData.add(new InstructionsPageData(
                mContext.getString(R.string.instructions_ultimate_empty_board),
                R.drawable.instructions_ultimate_empty_board));
        mInstructionsData.add(new InstructionsPageData(
                mContext.getString(R.string.instructions_ultimate_one_figure),
                R.drawable.instructions_ultimate_one_figure));
        mInstructionsData.add(new InstructionsPageData(
                mContext.getString(R.string.instructions_ultimate_one_figure_corresponding_cell),
                R.drawable.instructions_ultimate_one_figure_corresponding_marked));
        mInstructionsData.add(new InstructionsPageData(
                mContext.getString(R.string.instructions_ultimate_two_figures),
                R.drawable.instructions_ultimate_two_figures));
        mInstructionsData.add(new InstructionsPageData(
                mContext.getString(R.string.instructions_ultimate_one_winner),
                R.drawable.instructions_ultimate_one_winner));
        mInstructionsData.add(new InstructionsPageData(
                mContext.getString(R.string.instructions_ultimate_whole_board_active),
                R.drawable.instructions_ultimate_whole_board_active));
        mInstructionsData.add(new InstructionsPageData(
                mContext.getString(R.string.instructions_ultimate_game_won),
                R.drawable.instructions_ultimate_game_won));
        mInstructionsData.add(new InstructionsPageData(
                mContext.getString(R.string.instructions_ultimate_board_full),
                R.drawable.instructions_ultimate_board_full));
    }
}
