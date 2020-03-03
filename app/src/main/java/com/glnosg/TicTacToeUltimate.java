package com.glnosg;

import android.app.Application;
import android.content.Context;

public class TicTacToeUltimate extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        TicTacToeUltimate.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return TicTacToeUltimate.context;
    }
}
