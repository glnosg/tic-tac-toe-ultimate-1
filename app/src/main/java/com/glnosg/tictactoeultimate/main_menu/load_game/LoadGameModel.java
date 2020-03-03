package com.glnosg.tictactoeultimate.main_menu.load_game;

import android.content.Context;
import android.os.Bundle;

import com.glnosg.tictactoeultimate.main_menu.load_game.GameSavePreview.GameSavePreview;
import com.glnosg.tictactoeultimate.main_menu.load_game.GameSavePreview.PlayerPreview;
import com.glnosg.tictactoeultimate.main_menu.load_game.GameSavePreview.RemovedPreview;
import com.glnosg.tictactoeultimate.main_menu.load_game.LoadGameMVP.Presenter;
import com.glnosg.tictactoeultimate.main_menu.load_game.LoadGameMVP.Model;

import com.glnosg.TicTacToeUltimate;
import com.glnosg.tictactoeultimate.AppExecutors;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_state.BoardState;
import com.glnosg.tictactoeultimate.data.database.AppDatabase;
import com.glnosg.tictactoeultimate.data.database.GameSave;
import com.glnosg.tictactoeultimate.data.database.PlayerState;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ExecutorService;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.app.LoaderManager.LoaderCallbacks;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

public class LoadGameModel implements Model {

    private final String LOG_TAG = LoadGameModel.class.getSimpleName();
    private final int DB_LOADER_ID = 1001;

    private Presenter mPresenter;

    private AppDatabase mDb;
    private ExecutorService mDbExecutor;

    private LoaderManager mLoaderManager;
    private LoaderCallbacks<List<GameSavePreview>> mDbLoaderCallbacks;

    private List<GameSavePreview> mGameSavePreviews;
    private Stack<RemovedPreview> mRemovedPreviews;

    public LoadGameModel(Presenter loadGamePresenter, LoaderManager loaderManager) {
        mPresenter = loadGamePresenter;
        mLoaderManager = loaderManager;
        initVariables();
        initLoader();
    }

    private void initVariables() {
        mDb = AppDatabase.getInstance(TicTacToeUltimate.getAppContext());
        mDbExecutor = AppExecutors.getInstance().dbIO();
        mDbLoaderCallbacks = new DbLoaderCallbacks();
        mRemovedPreviews = new Stack<>();
    }

    private void initLoader() {
        mLoaderManager.initLoader(DB_LOADER_ID, null, mDbLoaderCallbacks).forceLoad();
    }

    @Override
    public void reloadGameSaves() {
        mLoaderManager.restartLoader(DB_LOADER_ID, null, mDbLoaderCallbacks).forceLoad();
    }

    @Override
    public void removePreview(int position) {
        mRemovedPreviews.push(
                new RemovedPreview(mGameSavePreviews.get(position), position));
        mGameSavePreviews.remove(position);
        mPresenter.gameSavePreviewsPrepared(mGameSavePreviews);
    }

    @Override
    public void retrieveLastRemovedPreview() {
        if (!mRemovedPreviews.empty()) {
            RemovedPreview preview = mRemovedPreviews.peek();
            mGameSavePreviews.add(preview.getPosition(), preview.getPreview());
            mRemovedPreviews.pop();
            mPresenter.gameSavePreviewsPrepared(mGameSavePreviews);
            if (mRemovedPreviews.empty())
                mPresenter.onRemovedPreviewsStackEmpty();
        }
    }

    @Override
    public void deleteRemovedPreviewsAndCorrespondingGameSaves() {
        mDbExecutor.execute(() -> {
            for (RemovedPreview preview : mRemovedPreviews) {
                long gameSaveId = preview.getPreview().getId();
                mDb.getGameSaveDao().deleteGameSave(gameSaveId);
            }
            mRemovedPreviews.clear();
        });
    }

    private class DbLoaderCallbacks implements LoaderCallbacks<List<GameSavePreview>> {
        @NonNull
        @Override
        public Loader<List<GameSavePreview>> onCreateLoader(int id, @Nullable Bundle args) {
            return new GameSavePreviewsLoader(TicTacToeUltimate.getAppContext());
        }

        @Override
        public void onLoadFinished(@NonNull Loader<List<GameSavePreview>> loader, List<GameSavePreview> data) {
            mGameSavePreviews = data;
            mPresenter.gameSavePreviewsPrepared(mGameSavePreviews);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<List<GameSavePreview>> loader) { }
    }

    private static class GameSavePreviewsLoader extends AsyncTaskLoader<List<GameSavePreview>> {

        private AppDatabase mDb;
        private Gson mGson;

        private List<GameSave> mGameSaves;
        private List<GameSavePreview> mGameSavePreviews;

        public GameSavePreviewsLoader(Context context) {
            super(context);
            mDb = AppDatabase.getInstance(context);
            mGson = new Gson();
        }

        @Nullable
        @Override
        public List<GameSavePreview> loadInBackground() {
            mGameSaves = mDb.getGameSaveDao().getAllGameSaves();
            prepareGameSavePreviews();
            return mGameSavePreviews;
        }

        private void prepareGameSavePreviews() {
            mGameSavePreviews = new ArrayList<>();
            for (GameSave gameSave : mGameSaves) {
                BoardState boardState = mGson.fromJson(gameSave.getBoardStateJson(), BoardState.class);
                mGameSavePreviews.add(
                        new GameSavePreview(
                                gameSave.getId(),
                                gameSave.getUpdatedAt(),
                                boardState.isUltimate(), getPlayerPreviews(gameSave)));
            }
        }

        private LinkedList<PlayerPreview> getPlayerPreviews(GameSave gameSave) {
            LinkedList<PlayerPreview> playerPreviews = new LinkedList<>();
            List<PlayerState> playerStateObjects
                    = mDb.getPlayerStateDao().getAllPlayerStates(gameSave.getId());
            for (PlayerState playerState : playerStateObjects) {
                playerPreviews.add(
                        new PlayerPreview(
                                playerState.getScore(),
                                playerState.getName(),
                                playerState.isHuman()));
            }
            return playerPreviews;
        }
    }
}
