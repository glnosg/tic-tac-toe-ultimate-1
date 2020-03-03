package com.glnosg.tictactoeultimate.main_menu.standard_game_settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;

import com.firebase.ui.auth.AuthUI;
import com.glnosg.tictactoeultimate.R;
import com.glnosg.tictactoeultimate.game.GameActivity;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.PlayerInitData;
import com.glnosg.tictactoeultimate.main_menu.MainMenuActivity;
import com.glnosg.tictactoeultimate.main_menu.custom_game_settings.PlayerSettingsAdapter;
import com.glnosg.tictactoeultimate.main_menu.standard_game_settings.StandardGameMvp.SettingsPresenter;
import com.glnosg.tictactoeultimate.main_menu.standard_game_settings.instructions.InstructionsPageAdapter;
import com.glnosg.tictactoeultimate.main_menu.standard_game_settings.instructions.SingleInstructionsPageAdapter;
import com.glnosg.tictactoeultimate.main_menu.standard_game_settings.instructions.UltimateInstructionsPageAdapter;
import com.glnosg.tictactoeultimate.settings.PreferencesActivity;
import com.glnosg.tictactoeultimate.settings.PreferencesUtils;

import java.util.List;
import java.util.Locale;

public class StandardGameSettingsActivity extends AppCompatActivity implements
        StandardGameMvp.SettingsView, SharedPreferences.OnSharedPreferenceChangeListener {

    private final String LOG_TAG = StandardGameSettingsActivity.class.getSimpleName();

    private SettingsPresenter mSettingsPresenter;
    private Toast mToast;
    private Locale mLastLocale;

    private PlayerSettingsAdapter mPlayerSettingsAdapter;
    @BindView(R.id.rv_local_players) RecyclerView playersDisplay;
    @BindView(R.id.view_pager_single) ViewPager mSingleInstructionsViewPager;
    @BindView(R.id.view_pager_ultimate) ViewPager mUltimateInstructionsViewPager;
    @BindView(R.id.tb_ultimate_switch) ToggleButton ultimateModeSwitch;
    @BindView(R.id.button_start) Button startButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_game_settings);
        ButterKnife.bind(this);
        initVariables();
        setupSharedPreferences();
        initViews();
    }

    private void initVariables() {
        mLastLocale = getResources().getConfiguration().locale;
        mSettingsPresenter = new StandardGameSettingsPresenter(this);
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

    private void initViews() {
        initPlayersDisplay();
        initButtons();
        initInstructions();
    }

    private void initPlayersDisplay() {
        playersDisplay.setLayoutManager(new LinearLayoutManager(this));
        mPlayerSettingsAdapter = new PlayerSettingsAdapter(this);
        playersDisplay.setAdapter(mPlayerSettingsAdapter);
        playersDisplay.invalidate();
    }

    private void initButtons() {
        startButton.setOnClickListener((View v) -> mSettingsPresenter.onStartClicked());
        ultimateModeSwitch.setOnCheckedChangeListener(
                (CompoundButton buttonView, boolean isChecked)
                        -> mSettingsPresenter.onUltimateSwitched(isChecked));
    }

    private void initInstructions() {
        InstructionsPageAdapter singleInstructionsAdapter =
                new SingleInstructionsPageAdapter(getSupportFragmentManager(),this);
        mSingleInstructionsViewPager.setAdapter(singleInstructionsAdapter);
        InstructionsPageAdapter ultimateInstructionsAdapter =
                new UltimateInstructionsPageAdapter(getSupportFragmentManager(), this);
        mUltimateInstructionsViewPager.setAdapter(ultimateInstructionsAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_standard_settings, menu);
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
                exitTheApp();
        }
        return super.onOptionsItemSelected(item);
    }

    private void exitTheApp() {
        Intent intent =
                new Intent(StandardGameSettingsActivity.this, MainMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(getString(R.string.intent_key_exit_app), true);
        startActivity(intent);
    }

    @Override
    public List<PlayerInitData> getPlayerInitData() {
        return mPlayerSettingsAdapter.getPlayerInitData();
    }

    @Override
    public void startGame(String gameInitDataJson) {
        Intent newGameIntent = new Intent(this, GameActivity.class);
        newGameIntent.putExtra(
                getResources().getString(R.string.intent_key_game_init_data), gameInitDataJson);
        newGameIntent.putExtra(
                getString(R.string.intent_key_is_bot_supported),
                !mPlayerSettingsAdapter.isDifficultySwitchLocked());
        startActivity(newGameIntent);
    }

    @Override
    public void showSingleInstructions() {
        mUltimateInstructionsViewPager.setVisibility(View.GONE);
        mSingleInstructionsViewPager.setVisibility(View.VISIBLE);
        mUltimateInstructionsViewPager.setCurrentItem(0);
    }

    @Override
    public void showUltimateInstructions() {
        mSingleInstructionsViewPager.setVisibility(View.GONE);
        mUltimateInstructionsViewPager.setVisibility(View.VISIBLE);
        mSingleInstructionsViewPager.setCurrentItem(0);
    }

    @Override
    public void lockDifficultySwitch() {
        mPlayerSettingsAdapter.lockDifficultySwitch();
        mPlayerSettingsAdapter.notifyDataSetChanged();
    }

    @Override
    public void unlockDifficultySwitch() {
        mPlayerSettingsAdapter.unlockDifficultySwitch();
    }

    @Override
    public void showErrorMessage() {
        showToast(getString(R.string.message_something_went_wrong));
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