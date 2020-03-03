package com.glnosg.tictactoeultimate.game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.glnosg.tictactoeultimate.R;
import com.glnosg.tictactoeultimate.game.board.board_view.BoardView;
import com.glnosg.tictactoeultimate.game.board.board_view.UltimateBoardLayout;
import com.glnosg.tictactoeultimate.game.view_managers.JoinerCandidatesDisplayManager;
import com.glnosg.tictactoeultimate.game.view_managers.GameButtonsManager;
import com.glnosg.tictactoeultimate.game.view_managers.GameMessageDisplayManager;
import com.glnosg.tictactoeultimate.game.view_managers.LocalPlayersDisplayManager;
import com.glnosg.tictactoeultimate.game.view_managers.NetworkPlayersDisplayManager;
import com.glnosg.tictactoeultimate.game.view_managers.TimeCounterDisplayManager;
import com.glnosg.tictactoeultimate.main_menu.MainMenuActivity;
import com.glnosg.tictactoeultimate.settings.PreferencesActivity;
import com.glnosg.tictactoeultimate.settings.PreferencesUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class GameActivity extends AppCompatActivity
        implements GameView, SharedPreferences.OnSharedPreferenceChangeListener {

    private final String LOG_TAG = GameActivity.class.getSimpleName();

    private GamePresenter mGamePresenter;
    private AlertDialog mLeaveLoseDialog;
    private Toast mToast;
    private Locale mLastLocale;

    private LocalPlayersDisplayManager mLocalPlayersDisplayManager;
    private NetworkPlayersDisplayManager mNetworkPlayersDisplayManager;
    private JoinerCandidatesDisplayManager mJoinerCandidatesDisplayManager;
    private GameMessageDisplayManager mGameMessageDisplayManager;
    private GameButtonsManager mGameButtonsManager;
    private TimeCounterDisplayManager mTimeCounterDisplayManager;

    @BindView(R.id.rv_local_players) RecyclerView localPlayersDisplay;
    @BindView(R.id.rv_network_players) RecyclerView networkPlayersDisplay;
    @BindView(R.id.rv_joiner_candidates) RecyclerView joinerCandidatesDisplay;
    @BindView(R.id.tv_time_counter) TextView timeCounterDisplay;
    @BindView(R.id.fl_game_board) FrameLayout boardDisplay;
    @BindView(R.id.pb_loading_indicator) ProgressBar loadingIndicator;
    @BindView(R.id.button_zoom_out) Button zoomOutButton;
    @BindView(R.id.button_leave) Button leaveButton;
    @BindView(R.id.button_new_opponent) Button newOpponentButton;
    @BindView(R.id.button_accept) Button acceptButton;
    @BindView(R.id.button_decline) Button declineButton;
    @BindView(R.id.button_next_game) Button nextGameButton;
    @BindView(R.id.tv_game_message) TextView gameMessageDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
        initViewManagers();
        initPresenter();
        mLastLocale = getResources().getConfiguration().locale;
        setupSharedPreferences();
    }

    private void setupSharedPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mGamePresenter.setIsAutoSaveOn(
                preferences.getBoolean(
                        getString(R.string.preferences_auto_save_key), true));
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

    private void initViewManagers() {
        mJoinerCandidatesDisplayManager = new JoinerCandidatesDisplayManager(joinerCandidatesDisplay);
        mLocalPlayersDisplayManager = new LocalPlayersDisplayManager(localPlayersDisplay);
        mNetworkPlayersDisplayManager = new NetworkPlayersDisplayManager(networkPlayersDisplay);
        mGameMessageDisplayManager = new GameMessageDisplayManager(gameMessageDisplay);
        mGameButtonsManager = new GameButtonsManager(
                zoomOutButton,
                nextGameButton,
                leaveButton,
                newOpponentButton,
                acceptButton,
                declineButton);
        mTimeCounterDisplayManager = new TimeCounterDisplayManager(timeCounterDisplay);
    }

    private void initPresenter() {
        mGamePresenter = GamePresenterFactory.getGamePresenter(this, getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_game_activity, menu);
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
            case (R.id.action_save):
                mGamePresenter.onSaveGameClicked();
                return true;
            case (R.id.action_sign_out):
                mGamePresenter.onSignOutClicked();
                return true;
            case (R.id.action_exit):
                mGamePresenter.onExitClicked();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showBoardDisplay() {
        boardDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBoardDisplay() {
        boardDisplay.setVisibility(View.GONE);
    }

    @Override
    public void setSingleBoardView(BoardView boardView) {
        initBoardLayout((View) boardView);
    }

    @Override
    public void setUltimateBoardView(List<BoardView> singleBoardViews) {
        List<View> singleViews = new LinkedList<>();
        for (BoardView boardView : singleBoardViews) {
            singleViews.add((View) boardView);
        }
        initBoardLayout(new UltimateBoardLayout(this, singleViews));
    }

    private void initBoardLayout(View view) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        boardDisplay.addView(view, params);
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
    public void showGameSavedMessage() {
        showToast(getString(R.string.message_game_save_success));
    }

    @Override
    public void showErrorMessage() {
        showToast(getString(R.string.message_something_went_wrong));
    }

    @Override
    public void showMessageCantSave() {
        showToast(getString(R.string.message_cant_save_online));
    }

    @Override
    public void signOut() {
        Toast.makeText(this, getString(R.string.message_signed_out), Toast.LENGTH_SHORT).show();
        AuthUI.getInstance().signOut(this);
    }

    @Override
    public void showMessageCantSignOut() {
        showToast(getString(R.string.message_cant_sign_out));
    }

    private void showToast(String text) {
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        mToast.show();
    }

    @Override
    public void showLeaveLoseDialog(LeaveLoseDialogOnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_leave_lose, null);
        Button cancelButton = dialogView.findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener((View v) -> listener.onCancelClicked());
        Button okButton = dialogView.findViewById(R.id.button_ok);
        okButton.setOnClickListener((View v) -> listener.onOkClicked());
        builder.setView(dialogView);
        mLeaveLoseDialog = builder.create();
        mLeaveLoseDialog.show();
    }

    @Override
    public void hideLeaveLoseDialog() {
        mLeaveLoseDialog.cancel();
    }

    @Override
    public void exitGameActivity() {
        finishAndRemoveTask();
    }

    @Override
    public void exitTheApp() {
        Intent intent = new Intent(GameActivity.this, MainMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(getString(R.string.intent_key_exit_app), true);
        startActivity(intent);
    }

    @Override
    public GameMessageDisplayManager getMessageDisplayManager() {
        return mGameMessageDisplayManager;
    }

    @Override
    public LocalPlayersDisplayManager getLocalPlayersDisplayManager() {
        return mLocalPlayersDisplayManager;
    }

    @Override
    public NetworkPlayersDisplayManager getNetworkPlayersDisplayManager() {
        return mNetworkPlayersDisplayManager;
    }

    @Override
    public JoinerCandidatesDisplayManager getJoinerCandidatesDisplayManager() {
        return mJoinerCandidatesDisplayManager;
    }

    @Override
    public GameButtonsManager getButtonsManager() {
        return mGameButtonsManager;
    }

    @Override
    public TimeCounterDisplayManager getTimeCounterDisplayManager() {
        return mTimeCounterDisplayManager;
    }

    @Override
    public LoaderManager getActivityLoaderManager() {
        return LoaderManager.getInstance(this);
    }

    @Override
    public void onBackPressed() {
        mGamePresenter.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGamePresenter.onActivityPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGamePresenter.onActivityResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mGamePresenter.onActivityDestroy();
        super.onDestroy();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.preferences_auto_save_key))) {
            boolean isAutoSaveOn = sharedPreferences.getBoolean(key, true);
            mGamePresenter.setIsAutoSaveOn(isAutoSaveOn);
        }
    }
}
