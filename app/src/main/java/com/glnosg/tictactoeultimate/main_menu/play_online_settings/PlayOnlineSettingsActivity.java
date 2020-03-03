package com.glnosg.tictactoeultimate.main_menu.play_online_settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.firebase.ui.auth.AuthUI;
import com.glnosg.tictactoeultimate.R;
import com.glnosg.tictactoeultimate.game.GameActivity;
import com.glnosg.tictactoeultimate.main_menu.MainMenuActivity;
import com.glnosg.tictactoeultimate.main_menu.play_online_settings.PlayOnlineMVP.SettingsView;
import com.glnosg.tictactoeultimate.main_menu.play_online_settings.PlayOnlineMVP.SettingsPresenter;
import com.glnosg.tictactoeultimate.settings.PreferencesActivity;
import com.glnosg.tictactoeultimate.settings.PreferencesFragment;
import com.glnosg.tictactoeultimate.settings.PreferencesUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class PlayOnlineSettingsActivity extends AppCompatActivity
        implements SettingsView, SharedPreferences.OnSharedPreferenceChangeListener {

    private final String LOG_TAG = PlayOnlineSettingsActivity.class.getSimpleName();
    private final int RC_SIGN_IN = 1;

    private SettingsPresenter mSettingsPresenter;
    private AlertDialog mWaitingForResponseDialog;
    private OpenGamesDisplayManager mOpenGamesDisplayManager;
    private Toast mToast;
    private Locale mLastLocale;

    @BindView(R.id.rv_open_games_display) RecyclerView openGamesDisplay;
    @BindView(R.id.pb_loading_indicator) ProgressBar loadingIndicator;
    @BindView(R.id.tv_empty_database) TextView emptyDatabaseMessageDisplay;
    @BindView(R.id.button_start) Button startButton;
    @BindView(R.id.tb_ultimate_switch) ToggleButton ultimateModeSwitch;
    @BindView(R.id.et_name) EditText nameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_online_settings);
        ButterKnife.bind(this);
        initViews();
        mSettingsPresenter = new PlayOnlinePresenter(this);
        mLastLocale = getResources().getConfiguration().locale;
        setupSharedPreferences();
        initWaitingForResponseDialog();
    }

    private void initViews() {
        initOpenGamesDisplayManager();
        initButtons();
    }

    private void initOpenGamesDisplayManager() {
        mOpenGamesDisplayManager = new OpenGamesDisplayManager(openGamesDisplay);
    }

    private void initButtons() {
        startButton.setEnabled(false);
        startButton.setOnClickListener((View v) -> mSettingsPresenter.onStartClicked());
        ultimateModeSwitch.setOnCheckedChangeListener(
                (CompoundButton buttonView, boolean isChecked)
                        -> mSettingsPresenter.onUltimateSwitched(isChecked));
    }

    private void setupSharedPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(this);
        PreferencesUtils.updateLocale();
        restartActivityIfLocaleChanged();
    }

    private void restartActivityIfLocaleChanged() {
        Locale currentLocale = getResources().getConfiguration().locale;
        if (!currentLocale.equals(mLastLocale)) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

    private void initWaitingForResponseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_waiting, null);
        Button button = dialogView.findViewById(R.id.button_cancel);
        button.setOnClickListener((View v) -> mSettingsPresenter.onWaitingForResponseCancelled());
        builder.setView(dialogView);
        mWaitingForResponseDialog = builder.create();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_play_online, menu);
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
                AuthUI.getInstance().signOut(this);
                Toast.makeText(this, getString(R.string.message_signed_out), Toast.LENGTH_SHORT).show();
                return true;
            case (R.id.action_exit):
                exitTheApp();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void exitTheApp() {
        Intent intent = new Intent(PlayOnlineSettingsActivity.this, MainMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(getString(R.string.intent_key_exit_app), true);
        startActivity(intent);
    }

    @Override
    public void setStartButtonEnabled(boolean isEnabled) {
        startButton.setEnabled(isEnabled);
    }

    @Override
    public void showLoggingScreen() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setAvailableProviders(getAuthProviders())
                        .setLogo(R.mipmap.ic_tictactoe)
                        .setTheme(R.style.LoginTheme)
                        .build(),
                RC_SIGN_IN);
    }

    private List<AuthUI.IdpConfig> getAuthProviders() {
        return Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.AnonymousBuilder().build());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                showToast(getString(R.string.message_signed_in));
            } else if (resultCode == RESULT_CANCELED) {
                showToast(getString(R.string.message_signing_in_cancelled));
                finish();
            }
        }
    }

    @Override
    public void showLoadingIndicator() {
        loadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingIndicator() {
        loadingIndicator.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showEmptyDatabaseMessage() {
        emptyDatabaseMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyDatabaseMessage() {
        emptyDatabaseMessageDisplay.setVisibility(View.GONE);
    }

    @Override
    public void setNameInputHint(String nameHint) {
        nameInput.setText(nameHint);
    }

    @Override
    public void openGame(String gameId) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(getString(R.string.intent_key_game_online_id), gameId);
        startActivity(intent);
    }

    @Override
    public void showWaitingForResponseDialog() {
        mWaitingForResponseDialog.show();
    }

    @Override
    public void cancelWaitingForResponseDialog() {
        mWaitingForResponseDialog.cancel();
    }

    @Override
    public String getDisplayName() {
        return nameInput.getText().toString();
    }

    @Override
    public OpenGamesDisplayManager getOpenGamesDisplayManager() {
        return mOpenGamesDisplayManager;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSettingsPresenter.onActivityPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSettingsPresenter.onActivityResume();
    }

    private void showToast(String text) {
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        mToast.show();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.preferences_language_key))) {
            restartActivityIfLocaleChanged();
        }
    }
}
