package com.glnosg.tictactoeultimate.main_menu.standard_game_settings.instructions;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public abstract class InstructionsPageAdapter extends FragmentPagerAdapter {

    protected Context mContext;
    protected List<InstructionsPageData> mInstructionsData;

    public InstructionsPageAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
        mInstructionsData = new ArrayList<>();
        initInstructionsData();
    }

    protected abstract void initInstructionsData();

    @NonNull
    @Override
    public Fragment getItem(int position) {
        String formattedPageNumber = getFormattedPageNumber(position);
        InstructionsPageData data = mInstructionsData.get(position);
        return new InstructionsPageFragment(
                data.getInstructions(), formattedPageNumber, data.getImageResourceId());
    }

    protected String getFormattedPageNumber(int adapterPosition) {
        int pageNumber = adapterPosition + 1;
        return pageNumber + " / " + getCount();
    }

    @Override
    public int getCount() {
        return mInstructionsData.size();
    }
}
