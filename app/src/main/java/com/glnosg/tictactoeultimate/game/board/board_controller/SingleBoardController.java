package com.glnosg.tictactoeultimate.game.board.board_controller;

import android.util.Log;

import com.glnosg.TicTacToeUltimate;
import com.glnosg.tictactoeultimate.game.board.cell.Cell;
import com.glnosg.tictactoeultimate.game.board.board_controller.victory_check.VictoryChecker;
import com.glnosg.tictactoeultimate.game.board.board_controller.victory_check.VictoryCheckerAllDirections;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.BoardInitData;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_state.BoardState;
import com.glnosg.tictactoeultimate.game.board.cell.CellStateChangeObserver;
import com.glnosg.tictactoeultimate.game.board.board_view.BoardView;
import com.glnosg.tictactoeultimate.game.board.board_view.OnBoardEventListener;
import com.glnosg.tictactoeultimate.game.board.board_view.StandardBoardView;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_state.CellState;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;

import static com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.BoardInitData.COLUMN_INDEX;
import static com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.BoardInitData.ROW_INDEX;


public class SingleBoardController
        implements BoardController, OnBoardEventListener, CellStateChangeObserver {

    public final String LOG_TAG = SingleBoardController.class.getSimpleName();

    private BoardView mBoardView;
    private float mLastColumnScreenRatio;

    private VictoryChecker mVictoryChecker;
    private CellChoiceObserver mCellChoiceObserver;
    private BoardStateObserver mBoardStateObserver;

    private int mBoardId;
    private Cell[][] mCells;
    private HashMap<Integer, LinkedList<int[]>> mMoves;

    private int mNumberOfColumns, mNumberOfRows;
    private int mNumberOfCells, mTakenCellsCounter;
    private int mHowManyInLineToWin;
    private boolean mIsActive, mIsLocked, mIsGameFinished;

    public SingleBoardController() {}

    @Override
    public void setBoardId(int id) {
        mBoardId = id;
    }

    @Override
    public void initBoard(BoardInitData initData) {
        mNumberOfColumns = initData.getNumberOfColumns();
        mNumberOfRows = initData.getNumberOfRows();
        mHowManyInLineToWin = initData.getHowManyInLineToWin();
        mIsActive = true;
        mIsLocked = false;
        mIsGameFinished = false;
        initView();
        initVariables();
        initEmptyCells();
        mBoardStateObserver.onBoardInitialized(mBoardId);
    }

    private void initVariables() {
        mNumberOfCells = mNumberOfColumns * mNumberOfRows;
        mTakenCellsCounter = 0;
        mCells = new Cell[mNumberOfColumns][mNumberOfRows];
        mMoves = new HashMap<>();
        mVictoryChecker = new VictoryCheckerAllDirections();
    }

    private void initEmptyCells() {
        for (int row = 0; row < mNumberOfRows; row++) {
            for (int column = 0; column < mNumberOfColumns; column++) {
                mCells[column][row] = new Cell(column, row);
                mCells[column][row].registerObserver(this);
            }
        }
    }

    private void initView() {
        mBoardView = new StandardBoardView(
                TicTacToeUltimate.getAppContext(), mNumberOfColumns, mNumberOfRows);
        mBoardView.registerOnBoardEventListener(this);
        if(!mIsActive) mBoardView.makeGridTransparent();
    }

    @Override
    public void loadBoard(BoardState state) {
        mNumberOfColumns = state.getNumberOfColumns();
        mNumberOfRows = state.getNumberOfRows();
        mHowManyInLineToWin = state.getHowManyInLineToWin();
        mIsActive = state.isActive();
        mIsLocked = state.isLocked();
        initView();
        initVariables();
        loadCells(state);
        checkIfAnybodyWon();
        mBoardStateObserver.onBoardInitialized(mBoardId);
    }

    private void loadCells(BoardState state) {
        for (int row = 0; row < mNumberOfRows; row++) {
            for (int column = 0; column < mNumberOfColumns; column++) {
                Cell cell = new Cell(state.getCellState(column, row));
                if (cell.isTaken()) addMoveToList(column, row, cell.getPlayerId());
                cell.registerObserver(this);
                mCells[column][row] = cell;
            }
        }
    }

    private void addMoveToList(int column, int row, int playerId) {
        int[] move = new int[] {column, row};
        LinkedList<int[]> moves;
        if (mMoves.containsKey(playerId)) moves = mMoves.get(playerId);
        else moves = new LinkedList<>();
        moves.add(move);
        mMoves.put(playerId, moves);
        incrementTakenCellsCounter();
    }

    private void incrementTakenCellsCounter() {
        if (++mTakenCellsCounter == mNumberOfCells) {
            mIsGameFinished = true;
            mBoardStateObserver.onBoardFull(mBoardId);
        }
    }

    private void checkIfAnybodyWon() {
        for(int playerId : mMoves.keySet()) {
            if (isWinner(playerId)) {
                mIsGameFinished = true;
                if (mIsActive) {
                    mBoardStateObserver.onPlayerHasWon(mBoardId, playerId);
                }
            }
        }
    }

    @Override
    public void clearBoard() {
        mBoardView.removeImageOnTopOfBoard();
        mBoardView.makeGridSolid();
        mBoardView.zoomOut();
        resetVariables();
        clearCells();
    }

    private void resetVariables() {
        mIsActive = true;
        mIsLocked = false;
        mIsGameFinished = false;
        mTakenCellsCounter = 0;
        mMoves = new HashMap<>();
    }

    private void clearCells() {
        for (int row = 0; row < mNumberOfRows; row++) {
            for (int column = 0; column < mNumberOfColumns; column++) {
                mCells[column][row].clear();
            }
        }
    }

    @Override
    public void lockBoard() {
        mIsLocked = true;
        for (int row = 0; row < mNumberOfRows; row++) {
            for (int column = 0; column < mNumberOfColumns; column++) {
                mCells[column][row].lock();
            }
        }
    }

    @Override
    public void unlockBoard() {
        if (!mIsGameFinished) {
            for (int row = 0; row < mNumberOfRows; row++) {
                for (int column = 0; column < mNumberOfColumns; column++) {
                    mCells[column][row].unlock();
                }
            }
        }
    }

    @Override
    public void activateBoard() {
        if (!isBoardActive() && !mIsGameFinished) {
            mBoardView.makeGridSolid();
            mIsActive = true;
            for (int row = 0; row < mNumberOfRows; row++) {
                for (int column = 0; column < mNumberOfColumns; column++) {
                    mCells[column][row].activate();
                }
            }
        }
    }

    @Override
    public void deactivateBoard() {
        if (isBoardActive()) {
            mBoardView.makeGridTransparent();
            mIsActive = false;
            for (int row = 0; row < mNumberOfRows; row++) {
                for (int column = 0; column < mNumberOfColumns; column++) {
                    mCells[column][row].deactivate();
                }
            }
        }
    }

    @Override
    public boolean isBoardActive() {
        return mIsActive;
    }

    @Override
    public void activateCell(int cellIndex) {
        int column = getColumnBasedOnIndex(cellIndex);
        int row = getRowBasedOnIndex(cellIndex);
        mCells[column][row].activate();
    }

    @Override
    public void deactivateCell(int cellIndex) {
        int column = getColumnBasedOnIndex(cellIndex);
        int row = getRowBasedOnIndex(cellIndex);
        mCells[column][row].deactivate();
    }

    @Override
    public boolean isCellActive(int cellIndex) {
        int column = getColumnBasedOnIndex(cellIndex);
        int row = getRowBasedOnIndex(cellIndex);
        return mCells[column][row].isActive();
    }

    @Override
    public boolean isGameFinished() {
        return mIsGameFinished;
    }

    private int getColumnBasedOnIndex(int cellIndex) {
        return cellIndex % mNumberOfColumns;
    }

    private int getRowBasedOnIndex(int cellIndex) {
        return cellIndex / mNumberOfColumns;
    }

    @Override
    public int getNumberOfCells() {
        return mNumberOfCells;
    }

    @Override
    public int getNumberOfSingleBoardColumns() {
        return mNumberOfColumns;
    }

    @Override
    public int getNumberOfSingleBoardRows() {
        return mNumberOfRows;
    }

    @Override
    public void assignPlayerToCell(int boardId, int column, int row, int playerId) {
        mCells[column][row].assignPlayer(playerId);
        addMoveToList(column, row, playerId);
        if (isWinner(playerId)) {
            lockBoard();
            mIsGameFinished = true;
            markWinningCells();
            mBoardStateObserver.onPlayerHasWon(mBoardId, playerId);
        }
    }

    private boolean isWinner(int playerId) {
        return mVictoryChecker.isWinner(mMoves.get(playerId), mHowManyInLineToWin);
    }

    private void markWinningCells() {
        List<int[]> winningCells = mVictoryChecker.getWinningCells();
        for (int[] cell : winningCells) {
            mCells[cell[COLUMN_INDEX]][cell[ROW_INDEX]].markAsWinning();
        }
    }

    @Override
    public void zoomOut() {
        mBoardView.zoomOut();
    }

    @Override
    public void drawImageOnTopOfBoard(int imageId) {
        mBoardView.drawImageOnTopOfBoard(imageId);
    }

    @Override
    public List<BoardView> getBoardViews() {
        List<BoardView> boardViews = new LinkedList<>();
        boardViews.add(mBoardView);
        return boardViews;
    }

    @Override
    public BoardState getBoardState() {
        return new BoardState(
                mIsActive,
                mIsLocked,
                mHowManyInLineToWin,
                BoardInitData.BOARD_TYPE_SINGLE,
                getCellStateObjects(),
                null);
    }

    private CellState[][] getCellStateObjects() {
        CellState[][] cellStateObjects = new CellState[mNumberOfColumns][mNumberOfRows];
        for (int row = 0; row < mNumberOfRows; row++) {
            for (int column = 0; column < mNumberOfColumns; column++) {
                Cell cell = mCells[column][row];
                cellStateObjects[column][row] = new CellState(
                        column,
                        row,
                        cell.getPlayerId(),
                        cell.isActive(),
                        cell.isLocked(),
                        cell.isWinning());
            }
        }
        return cellStateObjects;
    }

    @Override
    public void onColumnScreenRatioMeasured() {
        setColumnScreenRatio(mBoardView.getBaseColumnScreenRatio());
    }

    private void setColumnScreenRatio(float columnScreenRatio) {
        if (!(mLastColumnScreenRatio > 0 || mLastColumnScreenRatio < 0)) {
            mLastColumnScreenRatio = columnScreenRatio;
            updateBitmaps();
        }
    }

    @Override
    public void onCellClick(int column, int row) {
        if (mCells[column][row].isClickable()) {
            Move move = new Move(mBoardId, column, row);
            mCellChoiceObserver.playerHasChosenCell(move);
        }
    }

    @Override
    public void onBoardScale(float scaleFactor) {
        if (isViewUpdateNecessary(mBoardView.getBaseColumnScreenRatio(), scaleFactor)) {
            updateBitmaps();
        }
    }

    public boolean isViewUpdateNecessary(float baseColumnScreenRatio, float scaleFactor) {
        float currentColumnScreenRatio = baseColumnScreenRatio * scaleFactor;
        if (isTransition(currentColumnScreenRatio)) {
            mLastColumnScreenRatio = currentColumnScreenRatio;
            return true;
        } else {
            mLastColumnScreenRatio = currentColumnScreenRatio;
            return false;
        }
    }

    private boolean isTransition(float currentColumnScreenRatio) {
        if (mLastColumnScreenRatio < .1 && currentColumnScreenRatio >= .1) return true;
        else if (mLastColumnScreenRatio >= .1 && currentColumnScreenRatio < .1) return true;
        else if (mLastColumnScreenRatio < .035 && currentColumnScreenRatio >= .035) return true;
        else if (mLastColumnScreenRatio >= .035 && currentColumnScreenRatio < .035) return true;
        else return false;
    }

    private void updateBitmaps() {
        if (mLastColumnScreenRatio > .1) {
            useBigBitmaps();
        } else if (mLastColumnScreenRatio > .035) {
            useMediumBitmaps();
        } else {
            useSmallBitmaps();
        }
    }

    private void useBigBitmaps() {
        for (int row = 0; row < mNumberOfRows; row++) {
            for (int column = 0; column < mNumberOfColumns; column++) {
                mCells[column][row].useBigBitmap();
            }
        }
    }

    private void useMediumBitmaps() {
        for (int row = 0; row < mNumberOfRows; row++) {
            for (int column = 0; column < mNumberOfColumns; column++) {
                mCells[column][row].useMediumBitmap();
            }
        }
    }

    private void useSmallBitmaps() {
        for (int row = 0; row < mNumberOfRows; row++) {
            for (int column = 0; column < mNumberOfColumns; column++) {
                mCells[column][row].useSmallBitmap();
            }
        }
    }

    @Override
    public void onCellStateChange(int column, int row, int newBitmapId) {
        updateCell(column, row, newBitmapId);
    }

    private void updateCell(int column, int row, int newBitmapId) {
        mBoardView.updateCell(column, row, newBitmapId);
        mBoardView.invalidate();
    }

    @Override
    public void registerCellChoiceObserver(@NonNull CellChoiceObserver observer) {
        mCellChoiceObserver = observer;
    }

    @Override
    public void registerBoardStateObserver(@NonNull BoardStateObserver observer) {
        mBoardStateObserver = observer;
    }
}
