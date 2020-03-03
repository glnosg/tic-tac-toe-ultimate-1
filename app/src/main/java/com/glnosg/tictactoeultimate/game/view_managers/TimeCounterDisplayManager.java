package com.glnosg.tictactoeultimate.game.view_managers;

import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeCounterDisplayManager {

    private TextView mTimeCounterDisplay;

    public TimeCounterDisplayManager(TextView timeCounterDisplay) {
        mTimeCounterDisplay = timeCounterDisplay;
    }

    public void show() {
        mTimeCounterDisplay.setVisibility(View.VISIBLE);
    }

    public void hide() {
        mTimeCounterDisplay.setVisibility(View.GONE);
    }

    public void displayTimeLeft(long timeInMillis) {
        final String timeFormat = "mm:ss";
        DateFormat dateFormat = new SimpleDateFormat(timeFormat);
        Date date = new Date(timeInMillis);
        mTimeCounterDisplay.setText(dateFormat.format(date));
    }
}
