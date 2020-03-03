package com.glnosg.tictactoeultimate.game.local_game;

import android.util.Log;

import com.glnosg.tictactoeultimate.game.board.board_view.StandardBoardView;
import com.glnosg.tictactoeultimate.game.view_managers.GameButtonsOnClickListener;
import com.glnosg.tictactoeultimate.game.GamePresenter;
import com.glnosg.tictactoeultimate.game.GameView;
import com.glnosg.tictactoeultimate.game.board.board_controller.BoardStateObserver;
import com.glnosg.tictactoeultimate.game.board.board_controller.CellChoiceObserver;
import com.glnosg.tictactoeultimate.game.board.board_controller.BoardController;
import com.glnosg.tictactoeultimate.game.board.board_controller.Move;
import com.glnosg.tictactoeultimate.game.board.board_view.BoardView;
import com.glnosg.tictactoeultimate.data.database.AppDatabase;
import com.glnosg.tictactoeultimate.game.player.Player;
import com.glnosg.tictactoeultimate.game.view_managers.GameButtonsManager;
import com.glnosg.tictactoeultimate.game.view_managers.GameMessageDisplayManager;
import com.glnosg.tictactoeultimate.game.view_managers.LocalPlayersAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocalGamePresenter implements GamePresenter, BoardStateObserver, CellChoiceObserver,
        GameButtonsOnClickListener, LocalPlayersAdapter.DifficultyOnClickListener {

    private final String LOG_TAG = LocalGamePresenter.class.getSimpleName();
    private final int NO_ACTIVE_PLAYER = -1;

    private GameView mGameView;
    private GameButtonsManager mButtonsManager;
    private GameMessageDisplayManager mMessageDisplayManager;
    private LocalGameModel mGameModel;
    private BoardController mBoardController;

    private List<Player> mPlayers;
    private int mActivePlayerId, mPlayerStartingRoundId;

    private boolean mIsAutoSaveOn;

    public LocalGamePresenter(GameView gameView, String gameInitDataJson) {
        mGameView = gameView;
        initVariables();
        mGameModel.initGame(gameInitDataJson);
    }

    public LocalGamePresenter(GameView gameView, long gameSaveId) {
        mGameView = gameView;
        initVariables();
        mGameModel.loadGame(gameSaveId, mGameView.getActivityLoaderManager());
    }

    public LocalGamePresenter(GameView gameView) {
        mGameView = gameView;
        initVariables();
        mGameModel.initDefaultGame();
    }

    private void initVariables() {
        mPlayers = new ArrayList<>();
        mActivePlayerId = NO_ACTIVE_PLAYER;
        mPlayerStartingRoundId = 0;
        mButtonsManager = mGameView.getButtonsManager();
        mButtonsManager.setOnClickListener(this);
        mMessageDisplayManager = mGameView.getMessageDisplayManager();
        mGameModel = new LocalGameModel(this);
    }

    @Override
    public void setIsBotSupported(boolean isBotSupported) {
        if (isBotSupported) {
            mGameView.getLocalPlayersDisplayManager().unlockDifficultySwitch();
            mGameView.getLocalPlayersDisplayManager().attachDifficultyOnClickListener(this);
        }
    }

    @Override
    public void setIsAutoSaveOn(boolean isAutoSaveOn) {
        mIsAutoSaveOn = isAutoSaveOn;
    }

    @Override
    public void onPlayersInitialized(List<Player> players) {
        if (players != null) {
            mPlayers = players;
            for (Player player : mPlayers) {
                player.registerCellChoiceObserver(this);
                if (player.isActive()) mActivePlayerId = player.getId();
            }
            mGameView.getLocalPlayersDisplayManager().update(mPlayers);
            mGameView.getLocalPlayersDisplayManager().show();
        } else {
            Log.e(LOG_TAG, "[onPlayersInitialized] Method's received a null parameter");
            mGameView.showErrorMessage();
        }
    }

    @Override
    public void setPlayerStartingRoundId(int id) {
        mPlayerStartingRoundId = id;
    }

    private void switchPlayer() {
        if (mActivePlayerId == mPlayers.size() - 1) activatePlayer(0);
        else activatePlayer(++mActivePlayerId);
    }

    private void activatePlayer(int playerId) {
        if (mBoardController.isGameFinished()) return;
        deactivateAllPlayers();
        mActivePlayerId = playerId;
        Player player = mPlayers.get(mActivePlayerId);
        if (player.isHuman()) {
            mBoardController.unlockBoard();
        } else {
            mBoardController.lockBoard();
        }
        player.activate(mBoardController.getBoardState());
    }

    private void deactivateAllPlayers() {
        for (Player player : mPlayers) {
            player.deactivate();
        }
        mActivePlayerId = NO_ACTIVE_PLAYER;
    }

    private void switchPlayerStartingRound() {
        if (mPlayerStartingRoundId == mPlayers.size() - 1) mPlayerStartingRoundId = 0;
        else mPlayerStartingRoundId++;
    }

    private void activatePlayerStartingTheRound() {
        activatePlayer(mPlayerStartingRoundId);
    }

    @Override
    public void onBoardControllerInitialized(BoardController boardController) {
        mBoardController = boardController;
    }

    @Override
    public void onNextGameClicked() {
        mButtonsManager.hideNextGameButton();
        mMessageDisplayManager.hide();
        mBoardController.clearBoard();
        switchPlayerStartingRound();
        activatePlayerStartingTheRound();
        mGameView.getLocalPlayersDisplayManager().show();
        mGameView.getLocalPlayersDisplayManager().update(mPlayers);
    }

    @Override
    public void onZoomOutClicked() {
        mBoardController.zoomOut();
    }

    @Override
    public void onLeaveClicked() {
       mGameView.exitGameActivity();
    }

    @Override
    public void onNewOpponentClicked() {
        Log.e(LOG_TAG, "[onNewOpponentClicked]");
    }

    @Override
    public void onDeclineClicked() {
        Log.e(LOG_TAG, "[onDeclineClicked]");
    }

    @Override
    public void onAcceptClicked() {
        Log.e(LOG_TAG, "[onAcceptClicked]");
    }

    @Override
    public void onSaveGameClicked() {
        long gameSaveId = mGameModel.saveGameStateToDatabase(
                mBoardController.getBoardState(),
                mPlayers,
                mPlayerStartingRoundId);
        if (gameSaveId == AppDatabase.NOT_SAVED) mGameView.showErrorMessage();
        else mGameView.showGameSavedMessage();
    }

    @Override
    public void onSignOutClicked() {
    }

    @Override
    public void onExitClicked() {
        // TODO Check if game state's saved, if not ask user
        mGameView.exitTheApp();
    }

    @Override
    public void onDifficultyClicked(int position) {
        Player player = mPlayers.get(position);
        player.switchDifficulty();
        mGameView.getLocalPlayersDisplayManager().update(mPlayers);
        if (player.isActive() && !player.isHuman() && !mBoardController.isGameFinished()) {
            player.activate(mBoardController.getBoardState());
        }
    }

    @Override
    public void onBackPressed() {
        mGameView.exitGameActivity();
    }

    @Override
    public void onActivityPause() {
        if (mIsAutoSaveOn && !mGameModel.isGameSaveUpdated()) {
            mGameModel.saveGameStateToDatabase(
                    mBoardController.getBoardState(),
                    mPlayers,
                    mPlayerStartingRoundId);
        }
    }

    @Override
    public void onActivityResume() { }

    @Override
    public void onActivityDestroy() { }

    @Override
    public BoardStateObserver getBoardStateObserver() {
        return this;
    }

    @Override
    public void onBoardInitialized(int boardId) {
        mBoardController.registerCellChoiceObserver(this);
        mGameView.hideLoadingIndicator();
        mButtonsManager.showLeaveButton();
        activateZoomOutIfNeeded();
        if (mActivePlayerId == NO_ACTIVE_PLAYER) activatePlayerStartingTheRound();
        mGameView.getLocalPlayersDisplayManager().update(mPlayers);
        initBoardView();
    }

    private void activateZoomOutIfNeeded() {
        int numOfColumns = mBoardController.getNumberOfSingleBoardColumns();
        int numOfRows = mBoardController.getNumberOfSingleBoardRows();
        boolean isNeeded = numOfColumns > StandardBoardView.COLUMNS_ON_SCREEN_AT_MAX_ZOOM
                || numOfRows > StandardBoardView.COLUMNS_ON_SCREEN_AT_MAX_ZOOM;
        if (isNeeded) {
            mButtonsManager.showZoomOutButton();
        }
    }

    private void initBoardView() {
        List<BoardView> boardViews = mBoardController.getBoardViews();
        if (boardViews.size() == 1) mGameView.setSingleBoardView(boardViews.get(0));
        else mGameView.setUltimateBoardView(boardViews);
    }

    @Override
    public void onBoardFull(int boardId) {
        mButtonsManager.showNextGameButton();
        mGameView.getLocalPlayersDisplayManager().hide();
        mMessageDisplayManager.showGameFinishedDrawMessage();
    }

    @Override
    public void onPlayerHasWon(int boardId, int winnerId) {
        mBoardController.lockBoard();
        mPlayers.get(winnerId).incrementScore();
        mButtonsManager.showNextGameButton();
        mGameView.getLocalPlayersDisplayManager().hide();
        mMessageDisplayManager.showGameFinishedWinnerMessage(mPlayers.get(winnerId).getName());
    }

    @Override
    public void playerHasChosenCell(Move move) {
        if (move != null) {
            mBoardController.assignPlayerToCell(
                    move.getBoardId(),
                    move.getColumn(),
                    move.getRow(),
                    mActivePlayerId);
            mGameModel.onPlayerMoved();
        } else {
            Log.e(LOG_TAG, "[playerHasChosenCell] Method's received a null parameter");
        }
        switchPlayer();
        mGameView.getLocalPlayersDisplayManager().update(mPlayers);
    }
}
