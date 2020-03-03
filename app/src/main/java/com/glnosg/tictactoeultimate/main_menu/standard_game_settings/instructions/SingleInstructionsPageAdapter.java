package com.glnosg.tictactoeultimate.main_menu.standard_game_settings.instructions;

import android.content.Context;
import com.glnosg.tictactoeultimate.R;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

public class SingleInstructionsPageAdapter extends InstructionsPageAdapter {

    public SingleInstructionsPageAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm, context);
    }

    @Override
    protected void initInstructionsData() {
        mInstructionsData = new ArrayList<>();
        mInstructionsData.add(new InstructionsPageData(
                mContext.getString(R.string.instructions_single_empty_board),
                R.drawable.instructions_single_empty_board));
        mInstructionsData.add(new InstructionsPageData(
                mContext.getString(R.string.instructions_single_one_figure),
                R.drawable.instructions_single_one_figure));
        mInstructionsData.add(new InstructionsPageData(
                mContext.getString(R.string.instructions_single_two_figures),
                R.drawable.instructions_single_two_figures));
        mInstructionsData.add(new InstructionsPageData(
                mContext.getString(R.string.instructions_single_three_figures),
                R.drawable.instructions_single_three_figures));
        mInstructionsData.add(new InstructionsPageData(
                mContext.getString(R.string.instructions_single_winner),
                R.drawable.instructions_single_winner));
        mInstructionsData.add(new InstructionsPageData(
                mContext.getString(R.string.instructions_single_board_full),
                R.drawable.instructions_single_full_board));
    }
}
