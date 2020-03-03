package com.glnosg.tictactoeultimate.main_menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.glnosg.tictactoeultimate.R;
import com.glnosg.tictactoeultimate.main_menu.custom_game_settings.CustomGameSettingsActivity;
import com.glnosg.tictactoeultimate.main_menu.load_game.LoadGameActivity;
import com.glnosg.tictactoeultimate.main_menu.play_online_settings.PlayOnlineSettingsActivity;
import com.glnosg.tictactoeultimate.main_menu.standard_game_settings.StandardGameSettingsActivity;
import com.glnosg.tictactoeultimate.settings.PreferencesActivity;
import com.glnosg.tictactoeultimate.settings.PreferencesFragment;

import java.util.Locale;


public class MainMenuActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final String LOG_TAG = MainMenuActivity.class.getSimpleName();

    private Locale mLastLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getBooleanExtra(getString(R.string.intent_key_exit_app), false)) {
            finish();
        }
        setContentView(R.layout.activity_main_menu);
        mLastLocale = getResources().getConfiguration().locale;
        setupSharedPreferences();
    }

    private void setupSharedPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        choseLocale(preferences.getString(getString(R.string.preferences_language_key), ""));
        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void choseLocale(String languagePreferenceValue) {
        if (getString(R.string.preferences_language_value_polish).equals(languagePreferenceValue)) {
            setLocale(new Locale(PreferencesFragment.POLISH));
        } else {
            setLocale(Locale.ENGLISH);
        }
        restartActivityIfLocaleChanged();
    }

    private void setLocale(Locale locale) {
        Locale.setDefault(locale);
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, displayMetrics);
    }

    private void restartActivityIfLocaleChanged() {
        Locale currentLocale = getResources().getConfiguration().locale;
        if (!currentLocale.equals(mLastLocale)) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

    public void mainMenuButtonClicked(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.ib_standard_game:
                Intent standardGameIntent = new Intent(
                        MainMenuActivity.this, StandardGameSettingsActivity.class);
                startActivity(standardGameIntent);
                break;
            case R.id.ib_custom_game:
                Intent customGameIntent = new Intent(
                        MainMenuActivity.this, CustomGameSettingsActivity.class);
                startActivity(customGameIntent);
                break;
            case R.id.ib_load_game:
                Intent loadGameIntent = new Intent(
                        MainMenuActivity.this, LoadGameActivity.class);
                startActivity(loadGameIntent);
                break;
            case R.id.ib_play_online:
                Intent playOnlineIntent = new Intent(
                        MainMenuActivity.this, PlayOnlineSettingsActivity.class);
                startActivity(playOnlineIntent);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case (R.id.action_settings):
                Intent startSettingsActivity = new Intent(this, PreferencesActivity.class);
                startActivity(startSettingsActivity);
                return true;
            case (R.id.action_sign_out):
                Toast.makeText(this, getString(R.string.message_signed_out), Toast.LENGTH_SHORT).show();
                AuthUI.getInstance().signOut(this);
                return true;
            case (R.id.action_exit):
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.preferences_language_key))) {
            restartActivityIfLocaleChanged();
        }
    }
}
