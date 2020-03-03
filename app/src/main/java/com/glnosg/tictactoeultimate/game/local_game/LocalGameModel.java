package com.glnosg.tictactoeultimate.game.local_game;

import android.content.Context;
import android.os.Bundle;

import com.glnosg.TicTacToeUltimate;
import com.glnosg.tictactoeultimate.AppExecutors;
import com.glnosg.tictactoeultimate.game.GamePresenter;
import com.glnosg.tictactoeultimate.game.board.board_controller.BoardController;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.BoardInitData;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_state.BoardState;
import com.glnosg.tictactoeultimate.game.board.board_controller.BoardControllerFactory;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.GameInitData;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.PlayerInitData;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_state.GameState;
import com.glnosg.tictactoeultimate.data.database.AppDatabase;
import com.glnosg.tictactoeultimate.data.database.GameSave;
import com.glnosg.tictactoeultimate.data.database.PlayerState;
import com.glnosg.tictactoeultimate.game.player.Player;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.app.LoaderManager.LoaderCallbacks;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

public class LocalGameModel {

    private final String LOG_TAG = LocalGameModel.class.getSimpleName();
    private final int DB_LOADER_ID = 1011;

    private GamePresenter mGamePresenter;

    private Gson mGson;
    private ExecutorService mDbExecutor;
    private AppDatabase mDb;
    private boolean mIsGameSaveUpdated;

    private LoaderManager mLoaderManager;
    private LoaderCallbacks<GameState> mDbLoaderCallbacks;
    private long mGameSaveId;

    public LocalGameModel(LocalGamePresenter presenter) {
        mGamePresenter = presenter;
        initVariables();
    }

    private void initVariables() {
        mGson = new Gson();
        mDbExecutor = AppExecutors.getInstance().dbIO();
        mDb = AppDatabase.getInstance(TicTacToeUltimate.getAppContext());
        mDbLoaderCallbacks = new GameStateLoaderCallbacks();
        mGameSaveId = AppDatabase.NOT_SAVED;
        mIsGameSaveUpdated = true;
    }

    public void initDefaultGame() {
        GameInitData initData = GameInitData.getDefaultGameInitData();
        initPlayers(initData.getPlayersInitData());
        initBoardPresenter(initData.getBoardInitData());
    }

    public void initGame(String gameInitDataJson) {
        GameInitData initData = mGson.fromJson(gameInitDataJson, GameInitData.class);
        initPlayers(initData.getPlayersInitData());
        initBoardPresenter(initData.getBoardInitData());
    }

    private void initPlayers(List<PlayerInitData> playersInitData) {
        List<Player> players = new ArrayList<>();
        int i = 0;
        for(PlayerInitData playerInitData : playersInitData) {
            Player player = new Player(i++, playerInitData.getName());
            player.setDifficulty(playerInitData.getDifficulty());
            players.add(player);
        }
        mGamePresenter.onPlayersInitialized(players);
        mGamePresenter.setPlayerStartingRoundId(0);
    }

    private void initBoardPresenter(BoardInitData initData) {
        BoardController boardController =
                BoardControllerFactory.getBoardController(initData.getBoardType());
        mGamePresenter.onBoardControllerInitialized(boardController);
        boardController.registerBoardStateObserver(mGamePresenter.getBoardStateObserver());
        boardController.initBoard(initData);
        mGamePresenter.onBoardControllerInitialized(boardController);

    }

    public void loadGame(long gameSaveId, LoaderManager manager) {
        mGameSaveId = gameSaveId;
        mLoaderManager = manager;
        initLoader();
    }

    private void initLoader() {
        mLoaderManager.initLoader(DB_LOADER_ID, null, mDbLoaderCallbacks).forceLoad();
    }

    private void initGame(GameState gameState) {
        initPlayers(gameState);
        initBoardPresenter(gameState.getBoardState());
    }

    private void initPlayers(GameState gameState) {
        mGamePresenter.onPlayersInitialized(gameState.getPlayers());
        mGamePresenter.setPlayerStartingRoundId(gameState.getPlayerStartingRoundId());
    }

    private void initBoardPresenter(BoardState boardState) {
        BoardController boardController = BoardControllerFactory
                .getBoardController(boardState.getBoardType());
        mGamePresenter.onBoardControllerInitialized(boardController);
        boardController.registerBoardStateObserver(mGamePresenter.getBoardStateObserver());
        boardController.loadBoard(boardState);
        mGamePresenter.onBoardControllerInitialized(boardController);
    }

    public void onPlayerMoved() {
        mIsGameSaveUpdated = false;
    }

    public long saveGameStateToDatabase(
            BoardState boardState, List<Player> players, int playerStartingRoundId) {
        String boardStateJson = mGson.toJson(boardState);
        GameSave gameSave = new GameSave(boardStateJson, new Date());
        gameSave.setPlayerStartingTheRoundId(playerStartingRoundId);
        mGameSaveId = insertGameStateToDb(gameSave);
        savePlayerStateToDataBase(players);
        mIsGameSaveUpdated = true;
        return mGameSaveId;
    }

    private Long insertGameStateToDb(final GameSave gameSave) {
        Future<Long> future;
        long gameSaveId = AppDatabase.NOT_SAVED;
        future = mDbExecutor.submit(() -> {
            if (mGameSaveId == AppDatabase.NOT_SAVED) {
                return mDb.getGameSaveDao().insertGameSave(gameSave);
            } else {
                gameSave.setId(mGameSaveId);
                mDb.getGameSaveDao().updateGameSave(gameSave);
                return mGameSaveId;
            }
        });
        try {
            gameSaveId = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return gameSaveId;
    }

    private void savePlayerStateToDataBase(List<Player> players) {
        for(Player player : players) {
            if (player.getPlayerStateId() == -1) {
                insertPlayerStateToDb(player.getPlayerState(mGameSaveId));
            } else {
                PlayerState playerState = player.getPlayerState(mGameSaveId);
                playerState.setPlayerStateId(player.getPlayerStateId());
                updatePlayerStateInDb(playerState);
            }
        }
    }

    private void insertPlayerStateToDb(final PlayerState playerState) {
        mDbExecutor.execute(() -> mDb.getPlayerStateDao().insertPlayerState(playerState));
    }

    private void updatePlayerStateInDb(final PlayerState playerState) {
        mDbExecutor.execute(() -> mDb.getPlayerStateDao().updatePlayerState(playerState));
    }

    private class GameStateLoaderCallbacks implements LoaderCallbacks<GameState> {
        @NonNull
        @Override
        public Loader<GameState> onCreateLoader(int id, @Nullable Bundle args) {
            return new GameStateLoader(TicTacToeUltimate.getAppContext(), mGameSaveId);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<GameState> loader, GameState data) {
                initGame(data);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<GameState> loader) { }
    }

    private static class GameStateLoader extends AsyncTaskLoader<GameState> {

        private AppDatabase mDb;
        private Gson mGson;

        private long mGameSaveId;

        public GameStateLoader(Context context, long gameSaveId) {
            super(context);
            mDb = AppDatabase.getInstance(context);
            mGson = new Gson();
            mGameSaveId = gameSaveId;
        }

        @Nullable
        @Override
        public GameState loadInBackground() {
            GameSave gameSave = mDb.getGameSaveDao().getGameSave(mGameSaveId);
            int playerStartingTheRoundId = gameSave.getPlayerStartingTheRoundId();
            BoardState boardState = mGson.fromJson(gameSave.getBoardStateJson(), BoardState.class);
            GameState gameState = new GameState(playerStartingTheRoundId, boardState, getPlayers());
            return gameState;
        }

        private List<Player> getPlayers() {
            List<PlayerState> playerStates = mDb.getPlayerStateDao().getAllPlayerStates(mGameSaveId);
            List<Player> players = new ArrayList<>();
            for (PlayerState playerState : playerStates) {
                players.add(new Player(playerState));
            }
            return players;
        }
    }

    public boolean isGameSaveUpdated() {
        return mIsGameSaveUpdated;
    }
}
