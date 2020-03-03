package com.glnosg.tictactoeultimate.main_menu.load_game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.firebase.ui.auth.AuthUI;
import com.glnosg.tictactoeultimate.game.GameActivity;
import com.glnosg.tictactoeultimate.main_menu.MainMenuActivity;
import com.glnosg.tictactoeultimate.main_menu.load_game.GameSavePreview.GameSavePreview;
import com.glnosg.tictactoeultimate.main_menu.load_game.LoadGameMVP.Presenter;

import com.glnosg.tictactoeultimate.main_menu.load_game.GameSaveAdapter.ItemClickListener;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.glnosg.tictactoeultimate.R;
import com.glnosg.tictactoeultimate.settings.PreferencesActivity;
import com.glnosg.tictactoeultimate.settings.PreferencesUtils;

import java.util.List;
import java.util.Locale;

public class LoadGameActivity extends AppCompatActivity
        implements LoadGameMVP.View, ItemClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private final String LOG_TAG = LoadGameActivity.class.getSimpleName();

    private Presenter mPresenter;
    private MenuItem mUndoButton;
    private Locale mLastLocale;

    private GameSaveAdapter mGameSaveAdapter;
    @BindView(R.id.rv_game_saves_display) RecyclerView gameSavesDisplay;

    @BindView(R.id.pb_loading_indicator) ProgressBar loadingIndicator;
    @BindView(R.id.tv_empty_database) TextView emptyDatabaseMessageDisplay;
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_game);
        ButterKnife.bind(this);
        initViews();
        initVariables();
        setupSharedPreferences();
    }

    private void initVariables() {
        LoaderManager loaderManager =  LoaderManager.getInstance(this);
        mPresenter = new LoadGamePresenter(this, loaderManager);
        mLastLocale = getResources().getConfiguration().locale;
    }

    private void initViews() {
        initSwipeRefresh();
        initGameSavesDisplay();
    }

    private void initSwipeRefresh() {
        swipeRefresh.setOnRefreshListener(() -> mPresenter.onSwipeRefresh());
        swipeRefresh.setColorSchemeResources(R.color.colorBoard);
    }

    private void initGameSavesDisplay() {
        gameSavesDisplay.setLayoutManager(new LinearLayoutManager(this));
        mGameSaveAdapter = new GameSaveAdapter(this, this);
        gameSavesDisplay.setAdapter(mGameSaveAdapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(
                    RecyclerView recyclerView,
                    RecyclerView.ViewHolder viewHolder,
                    RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                mPresenter.onGameSavePreviewSwiped(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(gameSavesDisplay);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_load_game, menu);
        mUndoButton = menu.findItem(R.id.action_undo);
        deactivateUndoButton();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case (R.id.action_undo):
                mPresenter.onUndoClicked();
                return true;
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
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void exitTheApp() {
        Intent intent = new Intent(LoadGameActivity.this, MainMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(getString(R.string.intent_key_exit_app), true);
        startActivity(intent);
    }

    @Override
    public void showLoadingIndicator() {
        loadingIndicator.setVisibility(android.view.View.VISIBLE);
    }

    @Override
    public void hideLoadingIndicator() {
        loadingIndicator.setVisibility(android.view.View.GONE);
    }

    @Override
    public void showEmptyDatabaseMessage() {
        emptyDatabaseMessageDisplay.setVisibility(android.view.View.VISIBLE);
    }

    @Override
    public void hideEmptyDatabaseMessage() {
        emptyDatabaseMessageDisplay.setVisibility(android.view.View.GONE);
    }

    @Override
    public void hideRefreshAnimation() {
        final Handler handler = new Handler();
        handler.postDelayed(() -> swipeRefresh.setRefreshing(false), 1000);
    }

    @Override
    public void displayGameSavePreviews(List<GameSavePreview> previews) {
        gameSavesDisplay.setVisibility(android.view.View.VISIBLE);
        mGameSaveAdapter.setGameSavePreviews(previews);
        gameSavesDisplay.invalidate();
    }

    @Override
    public void onItemClicked(long gameSaveId) {
        Intent startGameIntent = new Intent(this, GameActivity.class);
        startGameIntent.putExtra(
                getResources().getString(R.string.intent_key_game_save_id), gameSaveId);
        mPresenter.onActivityDestroy();
        startActivity(startGameIntent);
    }

    @Override
    public void activateUndoButton() {
        mUndoButton.setIcon(R.drawable.ic_undo_enabled);
        mUndoButton.setEnabled(true);
    }

    @Override
    public void deactivateUndoButton() {
        mUndoButton.setIcon(R.drawable.ic_undo_disabled);
        mUndoButton.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        mPresenter.onActivityDestroy();
        super.onDestroy();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.preferences_language_key))) {
            restartActivityIfLocaleChanged();
        }
    }
}



