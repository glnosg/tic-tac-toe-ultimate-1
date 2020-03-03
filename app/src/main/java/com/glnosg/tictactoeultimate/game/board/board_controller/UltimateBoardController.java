package com.glnosg.tictactoeultimate.game.board.board_controller;

import android.util.Log;

import com.glnosg.tictactoeultimate.game.board.cell.Cell;
import com.glnosg.tictactoeultimate.game.board.cell.CellStateChangeObserver;
import com.glnosg.tictactoeultimate.game.board.board_controller.cells_deactivator.CellsDeactivator;
import com.glnosg.tictactoeultimate.game.board.board_controller.cells_deactivator.CellsDeactivatorFactory;
import com.glnosg.tictactoeultimate.game.board.board_controller.victory_check.VictoryChecker;
import com.glnosg.tictactoeultimate.game.board.board_controller.victory_check.VictoryCheckerAllDirections;
import com.glnosg.tictactoeultimate.game.board.board_view.BoardView;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_state.BoardState;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.BoardInitData;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_state.CellState;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.BoardInitData.COLUMN_INDEX;
import static com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.BoardInitData.ROW_INDEX;


public class UltimateBoardController implements BoardController, BoardStateObserver, CellStateChangeObserver {

    public final String LOG_TAG = UltimateBoardController.class.getSimpleName();

    private BoardStateObserver mBoardStateObserver;
    private VictoryChecker mVictoryChecker;
    private CellsDeactivator mCellsDeactivator;

    private int mBoardId;
    private Cell[][] mCells;
    private HashMap<Integer, LinkedList<int[]>> mMoves;
    private BoardController[][] mChildBoardControllers;
    private boolean[][] mIsChildBoardInitialized;

    private int mNumberOfColumns, mNumberOfRows;
    private int mNumberOfCells, mTakenCellsCounter;
    private int mHowManyInLineToWin;
    private boolean mIsActive, mIsLocked, mIsGameFinished;

    public UltimateBoardController() {
        initVariables();
    }

    private void initVariables() {
        setBoardId(BoardInitData.MAIN_BOARD_ID);
        mNumberOfColumns = BoardInitData.BOARD_SIZE_DEFAULT;
        mNumberOfRows = BoardInitData.BOARD_SIZE_DEFAULT;
        mNumberOfCells = mNumberOfColumns * mNumberOfRows;
        mTakenCellsCounter = 0;
        mHowManyInLineToWin = BoardInitData.HOW_MANY_TO_WIN_DEFAULT;
        mVictoryChecker = new VictoryCheckerAllDirections();
        mCells = new Cell[mNumberOfColumns][mNumberOfRows];
        mChildBoardControllers = new BoardController[mNumberOfColumns][mNumberOfRows];
        mIsChildBoardInitialized = new boolean[mNumberOfColumns][mNumberOfRows];
        initChildBoardControllers();
        mMoves = new HashMap<>();
        mIsActive = true;
        mIsLocked = false;
        mIsGameFinished = false;
    }

    private void initChildBoardControllers() {
        for (int row = 0; row < mNumberOfRows; row++) {
            for (int column = 0; column < mNumberOfColumns; column++) {
                BoardController childBoardController = new SingleBoardController();
                int childBoardId = (row * mNumberOfColumns) + column;
                childBoardController.setBoardId(childBoardId);
                childBoardController.registerBoardStateObserver(this);
                mChildBoardControllers[column][row] = childBoardController;
            }
        }
    }

    @Override
    public void setBoardId(int boardId) {
        mBoardId = boardId;
    }

    @Override
    public void initBoard(BoardInitData initData) {
        initEmptyCells();
        mCellsDeactivator = CellsDeactivatorFactory.getCellsDeactivator(initData.getBoardType(), this);
        initChildBoards(initData);
        mBoardStateObserver.onBoardInitialized(mBoardId);
    }

    private void initEmptyCells() {
        for (int row = 0; row < mNumberOfRows; row++) {
            for (int column = 0; column < mNumberOfColumns; column++) {
                mCells[column][row] = new Cell(column, row);
                mCells[column][row].registerObserver(this);
            }
        }
    }

    private void initChildBoards(BoardInitData initData) {
        for (int row = 0; row < mNumberOfRows; row++) {
            for (int column = 0; column < mNumberOfColumns; column++) {
                mChildBoardControllers[column][row].initBoard(initData);
            }
        }
    }

    @Override
    public void loadBoard(BoardState state) {
        loadChildBoards(state);
        loadCells(state);
        mCellsDeactivator = CellsDeactivatorFactory.getCellsDeactivator(state.getBoardType(), this);
        checkIfAnybodyWon();
        mBoardStateObserver.onBoardInitialized(mBoardId);
    }

    private void loadChildBoards(BoardState state) {
        for (int row = 0; row < mNumberOfRows; row++) {
            for (int column = 0; column < mNumberOfColumns; column++) {
                mChildBoardControllers[column][row].loadBoard(state.getChildBoard(column, row));
            }
        }
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
                mBoardStateObserver.onPlayerHasWon(mBoardId, playerId);
            }
        }
    }

    @Override
    public void clearBoard() {
        resetVariables();
        clearCells();
        clearChildBoards();
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

    private void clearChildBoards() {
        for (int row = 0; row < mNumberOfRows; row++) {
            for (int column = 0; column < mNumberOfColumns; column++) {
                mChildBoardControllers[column][row].clearBoard();
            }
        }
    }

    @Override
    public void lockBoard() {
        Log.d(LOG_TAG, "[lockBoard]");
        mIsLocked = true;
        for (int row = 0; row < mNumberOfRows; row++)
            for (int column = 0; column < mNumberOfColumns; column++) {
                mChildBoardControllers[column][row].lockBoard();
            }
    }

    @Override
    public void unlockBoard() {
        Log.d(LOG_TAG, "[unlockBoard]");
        if (!mIsGameFinished) {
            for (int row = 0; row < mNumberOfRows; row++) {
                for (int column = 0; column < mNumberOfColumns; column++) {
                    mChildBoardControllers[column][row].unlockBoard();
                }
            }
        }
    }

    @Override
    public void activateBoard() {
        if (!mIsGameFinished) {
            mIsActive = true;
            for (int row = 0; row < mNumberOfRows; row++) {
                for (int column = 0; column < mNumberOfColumns; column++) {
                    mChildBoardControllers[column][row].activateBoard();
                }
            }
        }
    }

    @Override
    public void deactivateBoard() {
        mIsActive = false;
        for (int row = 0; row < mNumberOfRows; row++) {
            for (int column = 0; column < mNumberOfColumns; column++) {
                mChildBoardControllers[column][row].deactivateBoard();
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
        mChildBoardControllers[column][row].activateBoard();
    }

    @Override
    public void deactivateCell(int cellIndex) {
        int column = getColumnBasedOnIndex(cellIndex);
        int row = getRowBasedOnIndex(cellIndex);
        mChildBoardControllers[column][row].deactivateBoard();
    }

    @Override
    public boolean isCellActive(int cellIndex) {
        int column = getColumnBasedOnIndex(cellIndex);
        int row = getRowBasedOnIndex(cellIndex);
        return mChildBoardControllers[column][row].isBoardActive();
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
        return mChildBoardControllers[0][0].getNumberOfSingleBoardColumns();
    }

    @Override
    public int getNumberOfSingleBoardRows() {
        return mChildBoardControllers[0][0].getNumberOfSingleBoardRows();
    }

    @Override
    public void assignPlayerToCell(int boardId, int column, int row, int playerId) {
        BoardController childBoardController
                = mChildBoardControllers[getColumnBasedOnIndex(boardId)][getRowBasedOnIndex(boardId)];
        childBoardController.assignPlayerToCell(boardId, column, row, playerId);
        activateBoard();
        mCellsDeactivator.deactivate(new int[] {column, row});
    }

    private void assignPlayerToCell(int column, int row, int playerId) {
        mCells[column][row].assignPlayer(playerId);
        addMoveToList(column, row, playerId);
        if (isWinner(playerId)) {
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
        for (int row = 0; row < mNumberOfRows; row++) {
            for (int column = 0; column < mNumberOfColumns; column++) {
                mChildBoardControllers[column][row].zoomOut();
            }
        }
    }

    @Override
    public void drawImageOnTopOfBoard(int bitmapId) { }

    @Override
    public BoardState getBoardState() {
        return new BoardState(
                mIsActive,
                mIsLocked,
                mHowManyInLineToWin,
                mCellsDeactivator.getHowManyRemainActive(),
                getCellStateObjects(),
                getChildBoardStateObjects());
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

    private BoardState[][] getChildBoardStateObjects() {
        BoardState[][] childBoardState = new BoardState[mNumberOfColumns][mNumberOfRows];
        for (int row = 0; row < mNumberOfRows; row++) {
            for (int column = 0; column < mNumberOfColumns; column++) {
                childBoardState[column][row] = mChildBoardControllers[column][row].getBoardState();
            }
        }
        return childBoardState;
    }

    @Override
    public List<BoardView> getBoardViews() {
        LinkedList<BoardView> childViews = new LinkedList<>();
        for (int row = 0; row < mNumberOfRows; row++) {
            for (int column = 0; column < mNumberOfColumns; column++) {
                BoardView childView = mChildBoardControllers[column][row].getBoardViews().get(0);
                childViews.add(childView);
            }
        }
        return childViews;
    }

    @Override
    public void registerCellChoiceObserver(CellChoiceObserver observer) {
        for (int row = 0; row < mNumberOfRows; row++) {
            for (int column = 0; column < mNumberOfColumns; column++) {
                if (mChildBoardControllers[column][row] != null) {
                    mChildBoardControllers[column][row].registerCellChoiceObserver(observer);
                }
            }
        }
    }

    @Override
    public void registerBoardStateObserver(BoardStateObserver observer) {
        mBoardStateObserver = observer;
    }

    @Override
    public void onBoardInitialized(int boardId) {
        int column = getColumnBasedOnIndex(boardId);
        int row = getRowBasedOnIndex(boardId);
        mIsChildBoardInitialized[column][row] = true;
        if (isBoardFullyInitialized()) {
            mBoardStateObserver.onBoardInitialized(mBoardId);
        }
    }

    private boolean isBoardFullyInitialized() {
        for (int row = 0; row < mNumberOfRows; row++) {
            for (int column = 0; column < mNumberOfColumns; column++) {
                if (!mIsChildBoardInitialized[column][row]) return false;
            }
        }
        return true;
    }

    @Override
    public void onBoardFull(int boardId) {
        int column = getColumnBasedOnIndex(boardId);
        int row = getRowBasedOnIndex(boardId);
        mChildBoardControllers[column][row].deactivateBoard();
        incrementTakenCellsCounter();
    }

    @Override
    public void onPlayerHasWon(int boardId, int winnerId) {
        int column = getColumnBasedOnIndex(boardId);
        int row = getRowBasedOnIndex(boardId);
        assignPlayerToCell(column, row, winnerId);
        mChildBoardControllers[column][row].deactivateBoard();
        mCells[column][row].assignPlayer(winnerId);
    }

    @Override
    public void onCellStateChange(int column, int row, int newBitmapId) {
        mChildBoardControllers[column][row].drawImageOnTopOfBoard(newBitmapId);
    }
}
