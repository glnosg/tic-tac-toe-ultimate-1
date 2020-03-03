package com.glnosg.tictactoeultimate.game.network_game;

import android.os.CountDownTimer;
import android.util.Log;

import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.BoardInitData;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGame;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGamePlayer;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkMove;
import com.glnosg.tictactoeultimate.game.GamePresenter;
import com.glnosg.tictactoeultimate.game.GameView;
import com.glnosg.tictactoeultimate.game.board.board_controller.BoardController;
import com.glnosg.tictactoeultimate.game.board.board_controller.BoardControllerFactory;
import com.glnosg.tictactoeultimate.game.board.board_controller.BoardStateObserver;
import com.glnosg.tictactoeultimate.game.board.board_controller.CellChoiceObserver;
import com.glnosg.tictactoeultimate.game.board.board_controller.Move;
import com.glnosg.tictactoeultimate.game.board.board_view.BoardView;
import com.glnosg.tictactoeultimate.game.network_game.network_game_state.GameFinishedState;
import com.glnosg.tictactoeultimate.game.network_game.network_game_state.GameplayState;
import com.glnosg.tictactoeultimate.game.network_game.network_game_state.NetworkGameState;
import com.glnosg.tictactoeultimate.game.network_game.network_game_state.NoOpponentState;
import com.glnosg.tictactoeultimate.game.network_game.network_game_state.TimeUpState;
import com.glnosg.tictactoeultimate.game.player.Player;
import com.glnosg.tictactoeultimate.game.view_managers.GameButtonsOnClickListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.GameInitData.NO_ACTIVE_PLAYER;

public class NetworkGamePresenter implements
        GamePresenter, BoardStateObserver, CellChoiceObserver, GameButtonsOnClickListener,
        UsersEventsObserver, GameEventsObserver {

    private final String LOG_TAG = NetworkGamePresenter.class.getSimpleName();
    private final long TIME_FOR_MOVE = 30000, TIME_SINGLE_TICK = 1000;

    private GameView mGameView;
    private NetworkGameModel mGameModel;
    private BoardController mBoardController;
    private NetworkGameState mCurrentState;

    private String mCreatorId;
    private List<NetworkGamePlayer> mPlayers;
    private NetworkGamePlayer mThisNetworkGamePlayer;
    private List<NetworkMove> mMoves;
    private CountDownTimer mTimeForMoveCounter;

    private int mActivePlayerId, mPlayerStartingRoundId;

    public NetworkGamePresenter(GameView gameView, String gameId) {
        mGameView = gameView;
        initVariables();
        initGameModel(gameId);
    }

    private void initVariables() {
        mPlayers = new LinkedList<>();
        mMoves = new LinkedList<>();
        mActivePlayerId = NO_ACTIVE_PLAYER;
        mPlayerStartingRoundId = 0;
        initTimeForMoveCounter();
        mGameView.getButtonsManager().setOnClickListener(this);
    }

    private void initTimeForMoveCounter() {
        mTimeForMoveCounter = new CountDownTimer(TIME_FOR_MOVE, TIME_SINGLE_TICK) {
            @Override
            public void onTick(long millisUntilFinished) {
                mGameView.getTimeCounterDisplayManager().displayTimeLeft(millisUntilFinished);
            }
            @Override
            public void onFinish() {
                for (NetworkGamePlayer player : mPlayers) {
                    if (mActivePlayerId == player.getPlayerId()) {
                        mGameModel.setPlayerOutOfTime(player.getUserId());
                    }
                }
            }
        };
    }

    private void initGameModel(String gameId) {
        mGameModel = new NetworkGameModel(this, gameId);
        mGameModel.attachAuthListener();
    }

    public void resetGame() {
        mGameModel.activatePlayer(NO_ACTIVE_PLAYER);
        mPlayerStartingRoundId = 0;
        mGameModel.removeMoves();
    }

    public void setState(NetworkGameState state) {
        mCurrentState = state;
    }

    public boolean isThisCreator() {
        return mGameModel.getThisUserId().equals(mCreatorId);
    }

    public boolean isCreator(String userId) {
        return userId.equals(mCreatorId);
    }

    public NetworkGamePlayer getThisNetworkGamePlayer() {
        return mThisNetworkGamePlayer;
    }

    public List<NetworkGamePlayer> getPlayers() {
        return mPlayers;
    }

    public String getPlayerName(int playerId) {
        for (NetworkGamePlayer player : mPlayers) {
            if (player.getPlayerId() == playerId) return player.getName();
        }
        return null;
    }

    @Override
    public void setInitialData(
            String creatorId,
            int boardType) {
        mCreatorId = creatorId;
        setInitialState();
        initBoard(boardType);
        if (isThisCreator()) mGameModel.setThisUserGamePlayed();
    }

    private void setInitialState() {
        if (isThisCreator()) {
            mCurrentState = new NoOpponentState(mGameView, this, mGameModel);
        } else {
            mCurrentState = new GameplayState(mGameView, this, mGameModel);
            mGameView.getMessageDisplayManager().showWaitingForOpponentMessage();
        }
    }

    private void initBoard(int boardType) {
        if (boardType == NetworkGame.ULTIMATE) initUltimateBoard();
        else initSingleBoard();
    }

    private void initUltimateBoard() {
        mBoardController = BoardControllerFactory
                .getBoardController(BoardInitData.BOARD_TYPE_CLASSICAL_ULTIMATE);
        mBoardController.registerBoardStateObserver(this);
        mBoardController.initBoard(new BoardInitData(BoardInitData.BOARD_TYPE_CLASSICAL_ULTIMATE));
    }

    private void initSingleBoard() {
        mBoardController = BoardControllerFactory
                .getBoardController(BoardInitData.BOARD_TYPE_SINGLE);
        mBoardController.registerBoardStateObserver(this);
        mBoardController.initBoard(new BoardInitData(BoardInitData.BOARD_TYPE_SINGLE));
    }

    @Override
    public void onPlayerJoined(NetworkGamePlayer joiner) {
        mCurrentState.onPlayerJoined(joiner);
    }

    @Override
    public void onPlayerLeft(String userId) {
        mTimeForMoveCounter.cancel();
        mGameView.getTimeCounterDisplayManager().hide();
        mGameModel.activatePlayer(NO_ACTIVE_PLAYER);
        mCurrentState.onPlayerLeft(userId);
    }

    @Override
    public void onAllPlayersReady(List<NetworkGamePlayer> players) {
        mPlayers = new ArrayList<>();
        for (NetworkGamePlayer currentPlayer : players) {
            mPlayers.add(currentPlayer);
            if (mGameModel.getThisUserId().equals(currentPlayer.getUserId())) {
                mThisNetworkGamePlayer = currentPlayer;
            }
        }
        mGameModel.incrementThisUserGamesPlayed();
        mGameView.getMessageDisplayManager().hide();
        mGameView.getNetworkPlayersDisplayManager().show();
        mGameView.getNetworkPlayersDisplayManager().updateDisplay(mPlayers);
        mGameModel.setThisUserGamePlayed();
        if (isThisCreator()) {
            mGameModel.removeMoves();
            mGameModel.activatePlayer(mPlayerStartingRoundId);
            mGameModel.setWinnerId(NetworkGame.GAME_NOT_FINISHED);
        }
    }

    @Override
    public void onCreatorChanged(String userId) {
        mCreatorId = userId;
    }

    @Override
    public void onActivePlayerChanged(int playerId) {
        mActivePlayerId = playerId;
        mGameView.getNetworkPlayersDisplayManager().updateActivePlayer(mActivePlayerId);
        mTimeForMoveCounter.start();
        if (mThisNetworkGamePlayer != null && mThisNetworkGamePlayer.getPlayerId() == playerId) {
            mBoardController.unlockBoard();
        }
    }

    @Override
    public void onMoveAdded(NetworkMove move) {
        if (mBoardController != null && !isMoveAlreadyAdded(move)) {
            mMoves.add(move);
            mBoardController.assignPlayerToCell(
                    move.getBoardId(),
                    move.getColumn(),
                    move.getRow(),
                    move.getPlayerId());
            if (isThisCreator()) {
                switchPlayer();
            }
        }
    }

    private boolean isMoveAlreadyAdded(NetworkMove moveToCheck) {
        for (NetworkMove currentMove : mMoves) {
            boolean isBoardSame = moveToCheck.getBoardId() == currentMove.getBoardId();
            boolean isColumnSame = moveToCheck.getColumn() == currentMove.getColumn();
            boolean isRowSame = moveToCheck.getRow() == currentMove.getRow();
            if (isBoardSame && isColumnSame && isRowSame) return true;
        }
        return false;
    }

    private void switchPlayer() {
        if (mActivePlayerId == mPlayers.size() - 1) {
            mGameModel.activatePlayer(0);
        } else {
            mGameModel.activatePlayer(++mActivePlayerId);
        }
    }

    @Override
    public void onGameFinished(int winnerId) {
        mTimeForMoveCounter.cancel();
        mGameView.getTimeCounterDisplayManager().hide();
        mGameView.getNetworkPlayersDisplayManager().hide();
        if (isThisCreator()) {
            mGameModel.setAllPlayersState(0);
            mGameModel.activatePlayer(NO_ACTIVE_PLAYER);
            switchPlayerStartingRound();
            incrementPlayerScore(winnerId);
        }
        mCurrentState =
                new GameFinishedState(mGameView, this, mGameModel, winnerId);
    }

    private void incrementPlayerScore(int playerId) {
        for (NetworkGamePlayer player : mPlayers) {
            if (player.getPlayerId() == playerId) {
                mGameModel.incrementUserGamesWon(player.getUserId());
            }
        }
    }

    private void switchPlayerStartingRound() {
        if (mPlayerStartingRoundId == mPlayers.size() - 1) mPlayerStartingRoundId = 0;
        else mPlayerStartingRoundId++;
    }

    @Override
    public void onMovesRemoved() {
        mMoves = new LinkedList<>();
        mBoardController.clearBoard();
        mBoardController.lockBoard();
    }

    @Override
    public void onPlayerOutOfTime(String userId) {
        mTimeForMoveCounter.cancel();
        mGameView.getTimeCounterDisplayManager().hide();
        if (userId.equals(mGameModel.getThisUserId())) {
            mCurrentState =
                    new TimeUpState(mGameView, NetworkGamePresenter.this, mGameModel);
        } else {
            mGameModel.removePlayerFromGame(userId);
        }
    }

    public GameEventsObserver getGameEventsObserver() {
        return this;
    }

    @Override
    public void onBoardInitialized(int boardId) {
        mBoardController.lockBoard();
        mBoardController.registerCellChoiceObserver(this);
        mGameView.hideLoadingIndicator();
        initBoardView();
        mGameModel.attachDbListeners();
        mGameModel.setThisPlayerState(NetworkGame.READY);
    }

    private void initBoardView() {
        List<BoardView> boardViews = mBoardController.getBoardViews();
        if (boardViews.size() == 1) mGameView.setSingleBoardView(boardViews.get(0));
        else mGameView.setUltimateBoardView(boardViews);
    }

    @Override
    public void onBoardFull(int boardId) {
        mGameModel.setWinnerId(NetworkGame.DRAW);
    }

    @Override
    public void onPlayerHasWon(int boardId, int winnerId) {
        mGameModel.setWinnerId(winnerId);
    }

    @Override
    public void playerHasChosenCell(Move move) {
        mTimeForMoveCounter.cancel();
        mBoardController.lockBoard();
        NetworkMove netMove = new NetworkMove(
                mThisNetworkGamePlayer.getPlayerId(),
                move.getBoardId(),
                move.getColumn(),
                move.getRow());
        mGameModel.addMove(netMove);
    }

    @Override
    public void onUserDeactivated(String userId) {

    }

    @Override
    public void onUserActivated(String userId) {

    }

    @Override
    public void setIsBotSupported(boolean isBotSupported) { }

    @Override
    public void setIsAutoSaveOn(boolean isAutoSaveOn) {

    }

    @Override
    public void onPlayersInitialized(List<Player> players) { }
    @Override
    public void setPlayerStartingRoundId(int id) { }
    @Override
    public void onBoardControllerInitialized(BoardController boardController) { }

    @Override
    public void onSaveGameClicked() {
        mGameView.showMessageCantSave();
    }

    @Override
    public void onSignOutClicked() {
        mGameView.showMessageCantSignOut();
    }

    @Override
    public void onExitClicked() {
        mCurrentState.onExitClicked();
    }

    @Override
    public void onBackPressed() {
        onLeaveClicked();
    }

    @Override
    public void onActivityPause() {
        // TODO set user not active, receive info from user monitor, make opponent's nick transparent;
    }

    @Override
    public void onActivityResume() {

    }

    @Override
    public void onActivityDestroy() {
        Log.d(LOG_TAG, "[onActivityDestroy]");
        mCurrentState.onActivityDestroy();
    }

    @Override
    public void onZoomOutClicked() { }

    @Override
    public void onNextGameClicked() {
        mCurrentState.onNextGameClicked();
    }

    @Override
    public void onLeaveClicked() {
        mCurrentState.onLeaveClicked();
    }

    @Override
    public void onNewOpponentClicked() {
        mCurrentState.onNewOpponentClicked();
    }

    @Override
    public void onAcceptClicked() {
        mCurrentState.onAcceptClicked();
    }

    @Override
    public void onDeclineClicked() {
        mCurrentState.onDeclineClicked();
    }

    @Override
    public BoardStateObserver getBoardStateObserver() {
        return this;
    }
}
