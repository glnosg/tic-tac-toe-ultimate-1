package com.glnosg.tictactoeultimate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.glnosg.tictactoeultimate.main_menu.MainMenuActivity;

import androidx.annotation.Nullable;

import static java.lang.Thread.sleep;

public class SplashScreenActivity extends Activity {

    private final Long SPLASH_TIME_IN_MILLIS = 2000L;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        displaySplash(SPLASH_TIME_IN_MILLIS);
    }

    private void displaySplash(long millis) {
        new Thread(() -> {
            try {
                sleep(millis);
                Intent intent = new Intent(this, MainMenuActivity.class);
                startActivity(intent);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                finish();
            }
        }).start();
    }
}
