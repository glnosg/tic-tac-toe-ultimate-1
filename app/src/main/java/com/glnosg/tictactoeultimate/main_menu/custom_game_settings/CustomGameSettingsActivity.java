package com.glnosg.tictactoeultimate.main_menu.custom_game_settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.firebase.ui.auth.AuthUI;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.PlayerInitData;
import com.glnosg.tictactoeultimate.game.GameActivity;
import com.glnosg.tictactoeultimate.main_menu.MainMenuActivity;
import com.glnosg.tictactoeultimate.main_menu.custom_game_settings.CustomGameMvp.SettingsPresenter;

import com.glnosg.tictactoeultimate.R;
import com.glnosg.tictactoeultimate.settings.PreferencesActivity;
import com.glnosg.tictactoeultimate.settings.PreferencesFragment;
import com.glnosg.tictactoeultimate.settings.PreferencesUtils;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

import static com.glnosg.tictactoeultimate.main_menu.custom_game_settings.BoardSanityController.ACTIVE_BOARDS_MAX;
import static com.glnosg.tictactoeultimate.main_menu.custom_game_settings.BoardSanityController.ACTIVE_BOARDS_MIN;
import static com.glnosg.tictactoeultimate.main_menu.custom_game_settings.BoardSanityController.COLUMNS_MAX;
import static com.glnosg.tictactoeultimate.main_menu.custom_game_settings.BoardSanityController.COLUMNS_MIN;
import static com.glnosg.tictactoeultimate.main_menu.custom_game_settings.BoardSanityController.FIGURES_IN_ROW_MIN;
import static com.glnosg.tictactoeultimate.main_menu.custom_game_settings.BoardSanityController.ROWS_MAX;
import static com.glnosg.tictactoeultimate.main_menu.custom_game_settings.BoardSanityController.ROWS_MIN;

public class CustomGameSettingsActivity extends AppCompatActivity
        implements CustomGameMvp.SettingsView, SharedPreferences.OnSharedPreferenceChangeListener {

    private final String LOG_TAG = CustomGameSettingsActivity.class.getSimpleName();

    private SettingsPresenter mPresenter;
    private Toast mToast;
    private Locale mLastLocale;

    private PlayerSettingsAdapter mPlayerSettingsAdapter;
    @BindView(R.id.rv_local_players) RecyclerView playersDisplay;

    @BindView(R.id.button_remove) Button removePlayerButton;
    @BindView(R.id.button_add) Button addPlayerButton;
    @BindView(R.id.button_start) Button startButton;

    @BindView(R.id.np_figures_in_row) NumberPicker figuresInRowNumberPicker;
    @BindView(R.id.np_columns) NumberPicker columnsNumberPicker;
    @BindView(R.id.np_rows) NumberPicker rowsNumberPicker;

    @BindView(R.id.np_boards_activated) NumberPicker activeBoardsNumberPicker;
    @BindView(R.id.tv_boards_activated_description) TextView boardDeactivateDescription;
    @BindView(R.id.rl_boards_activated) RelativeLayout activeBoardsLayout;
    @BindView(R.id.tb_ultimate_switch) ToggleButton ultimateModeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_game_settings);
        ButterKnife.bind(this);
        initVariables();
        setupSharedPreferences();
        initViews();
    }

    private void initVariables() {
        mPresenter = new CustomGameSettingsPresenter(this);
        mLastLocale = getResources().getConfiguration().locale;
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
        initUltimateModeSwitch();
        initNumberPickers();
    }

    private void initPlayersDisplay() {
        playersDisplay.setLayoutManager(new LinearLayoutManager(this));
        mPlayerSettingsAdapter = new PlayerSettingsAdapter(this);
        mPlayerSettingsAdapter.lockDifficultySwitch();
        playersDisplay.setAdapter(mPlayerSettingsAdapter);
        playersDisplay.invalidate();
        mLastLocale = getResources().getConfiguration().locale;
    }

    private void initButtons() {
        removePlayerButton.setOnClickListener((View v) -> mPresenter.onRemoveClicked());
        addPlayerButton.setOnClickListener((View v) -> mPresenter.onAddClicked());
        startButton.setOnClickListener((View v) -> mPresenter.onStartClicked());
    }

    private void initUltimateModeSwitch() {
        ultimateModeSwitch.setOnCheckedChangeListener(
                (CompoundButton buttonView, boolean isChecked)
                        -> mPresenter.onUltimateSwitched(isChecked));
    }

    private void initNumberPickers() {
        initHowManyFiguresInRowPicker();
        initNumberOfColumnsPicker();
        initNumberOfRowsPicker();
        initNumberOfDeactivatedBoardsPicker();
    }

    private void initHowManyFiguresInRowPicker() {
        figuresInRowNumberPicker.setMinValue(FIGURES_IN_ROW_MIN);
        figuresInRowNumberPicker.setMaxValue(FIGURES_IN_ROW_MIN);
        setNumberPickerTextColor(
                figuresInRowNumberPicker, ContextCompat.getColor(this, R.color.colorChalk));
    }

    private void initNumberOfColumnsPicker() {
        columnsNumberPicker.setMinValue(COLUMNS_MIN);
        columnsNumberPicker.setMaxValue(COLUMNS_MAX);
        setNumberPickerTextColor(
                columnsNumberPicker, ContextCompat.getColor(this, R.color.colorChalk));
        columnsNumberPicker.setOnScrollListener((NumberPicker v, int state) ->
                mPresenter.onColumnsPickerScrolled(columnsNumberPicker.getValue()));
    }

    private void initNumberOfRowsPicker() {
        rowsNumberPicker.setMinValue(ROWS_MIN);
        rowsNumberPicker.setMaxValue(ROWS_MAX);
        setNumberPickerTextColor(
                rowsNumberPicker, ContextCompat.getColor(this, R.color.colorChalk));
        rowsNumberPicker.setOnScrollListener((NumberPicker v, int state) ->
                mPresenter.onRowsPickerScrolled(rowsNumberPicker.getValue()));
    }

    private void initNumberOfDeactivatedBoardsPicker() {
        activeBoardsNumberPicker.setMinValue(ACTIVE_BOARDS_MIN);
        activeBoardsNumberPicker.setMaxValue(ACTIVE_BOARDS_MAX);
        activeBoardsNumberPicker.setValue(ACTIVE_BOARDS_MAX);
        setNumberPickerTextColor(
                activeBoardsNumberPicker, ContextCompat.getColor(this, R.color.colorChalk));
    }

    private void setNumberPickerTextColor(NumberPicker numberPicker, int color) {
        try{
            Field selectorWheelPaintField = numberPicker.getClass()
                    .getDeclaredField("mSelectorWheelPaint");
            selectorWheelPaintField.setAccessible(true);
            ((Paint)selectorWheelPaintField.get(numberPicker)).setColor(color);
        }
        catch(NoSuchFieldException | IllegalArgumentException | IllegalAccessException e){
            Log.w("setNumberPickerTextCol", e);
        }
        final int count = numberPicker.getChildCount();
        for(int i = 0; i < count; i++){
            View child = numberPicker.getChildAt(i);
            if(child instanceof EditText)
                ((EditText)child).setTextColor(color);
        }
        numberPicker.invalidate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_custom_settings, menu);
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
        Intent intent = new Intent(CustomGameSettingsActivity.this, MainMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(getString(R.string.intent_key_exit_app), true);
        startActivity(intent);
    }

    @Override
    public void showUltimateSettings() {
        activeBoardsLayout.setVisibility(View.VISIBLE);
        boardDeactivateDescription.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideUltimateSettings() {
        activeBoardsLayout.setVisibility(View.GONE);
        boardDeactivateDescription.setVisibility(View.GONE);
    }

    @Override
    public void showRemoveButton() {
        removePlayerButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRemoveButton() {
        removePlayerButton.setVisibility(View.GONE);
    }

    @Override
    public void showAddButton() {
        addPlayerButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideAddButton() {
        addPlayerButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void addRowOnPlayersDisplay() {
        mPlayerSettingsAdapter.addRow();
        playersDisplay.requestLayout();
    }

    @Override
    public void removeRowFromPlayersDisplay() {
        mPlayerSettingsAdapter.removeRow();
        playersDisplay.requestLayout();
    }

    @Override
    public void setHowManyInRowMaxValue(int value) {
        figuresInRowNumberPicker.setMaxValue(value);
        figuresInRowNumberPicker.invalidate();
    }

    @Override
    public int getNumberOfColumns() {
        return columnsNumberPicker.getValue();
    }

    @Override
    public int getNumberOfRows() {
        return rowsNumberPicker.getValue();
    }

    @Override
    public int getHowManyInLineToWin() {
        return figuresInRowNumberPicker.getValue();
    }

    @Override
    public int getHowManyRemainActive() {
        return activeBoardsNumberPicker.getValue();
    }

    @Override
    public List<PlayerInitData> getPlayersInitData() {
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
    public void showErrorMessage() {
        showToast(getString(R.string.message_something_went_wrong));
    }

    @Override
    public void showBoardToSmallMessage() {
        showToast(getString(R.string.message_board_to_small));
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
