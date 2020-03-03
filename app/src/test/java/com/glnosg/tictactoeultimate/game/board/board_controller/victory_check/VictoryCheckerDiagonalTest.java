package com.glnosg.tictactoeultimate.game.board.board_controller.victory_check;

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertArrayEquals;

public class VictoryCheckerDiagonalTest {

    private VictoryChecker testedVictoryChecker;
    private LinkedList<int[]> testedMoves;

    @Before
    public void init() {
        testedVictoryChecker = new VictoryCheckerDiagonal();
        testedMoves = new LinkedList<>();
    }

    /** 0 1 2 3 4 5 6 7 8 9
     0 - - - - - - - - - -
     1 - - - - - - - - - -
     2 - - - - - - - - - -
     3 - - - X - - - - - -
     4 - - - - X - - - - -
     5 - - - - - X - - - -
     6 - - - - - - X - - -
     7 - - - - - - - X - -
     8 - - - - - - - - - -
     9 - - - - - - - - - - */
    @Test(timeout = 10)
    public void checkDiagonalTest_FiveInLineDiagonalForward_FiveNeededToWin() {
        for (int i = 3; i < 8; i++)
            testedMoves.add(new int[] {i, i});

        boolean isWinner = testedVictoryChecker.isWinner(testedMoves, 5);
        assertTrue(isWinner);

        List<int[]> winningCells = testedVictoryChecker.getWinningCells();
        assertArrayEquals(winningCells.toArray(), testedMoves.toArray());
    }

    /** 0 1 2 3 4 5 6 7 8 9
     0 - - - - - - - - - -
     1 - - - - - - - - - -
     2 - - - - - - - - - -
     3 - - - X - - - - - -
     4 - - - - X - - - - -
     5 - - - - - X - - - -
     6 - - - - - - X - - -
     7 - - - - - - - X - -
     8 - - - - - - - - - -
     9 - - - - - - - - - - */
    @Test(timeout = 10)
    public void checkDiagonalTest_FiveInLineDiagonalForward_SixNeededToWin() {
        for (int i = 3; i < 8; i++)
            testedMoves.add(new int[] {i, i});

        boolean isWinner = testedVictoryChecker.isWinner(testedMoves, 6);
        assertFalse(isWinner);

        List<int[]> winningCells = testedVictoryChecker.getWinningCells();
        assertTrue(winningCells.isEmpty());
    }

    /** 0 1 2 3 4 5 6 7 8 9
     0 - - - - - - - - - -
     1 - - - - - - - - - -
     2 - - - - - - - - - -
     3 - - - X - - - - - -
     4 - - - - X - - - - -
     5 - - - - - X - - - -
     6 - - - - - - X - - -
     7 - - - - - - X - - -
     8 - - - - - - - - - -
     9 - - - - - - - - - - */
    @Test(timeout = 10)
    public void checkDiagonalTest_FourInLineDiagonalForward_FiveNeededToWin() {
        for (int i = 3; i < 7; i++)
            testedMoves.add(new int[] {i, i});

        testedMoves.add(new int[] {6, 7});

        boolean isWinner = testedVictoryChecker.isWinner(testedMoves, 5);
        assertFalse(isWinner);

        List<int[]> winningCells = testedVictoryChecker.getWinningCells();
        assertTrue(winningCells.isEmpty());
    }

    /** 0 1 2 3 4 5 6 7 8 9
     0 - - - - - - - X - -
     1 - - - - - - X - - -
     2 - - - - - X X - - -
     3 - - X X X - - X - -
     4 - - X - X - - - X -
     5 - - X - - X - - - X
     6 - - X - - - X - - -
     7 - - - - - X - X - -
     8 - - - - X - - - - -
     9 - - - X - - - - - - */
    @Test(timeout = 10)
    public void checkDiagonalTest_FiveInLineDiagonalForward_MoreNoiseMoves_FiveNeededToWin() {
        for (int i = 3; i < 8; i++)
            testedMoves.add(new int[] {i, i});

        for (int i = 3; i < 7; i++)
            testedMoves.add(new int[] {2, i});

        for (int i = 2; i < 6; i++)
            testedMoves.add(new int[] {i + 4, i});

        for (int i = 4; i < 8; i++)
            testedMoves.add(new int[] {i, 7 - i});

        for (int i = 3; i < 7; i++)
            testedMoves.add(new int[] {i, 12 - i});

        boolean isWinner = testedVictoryChecker.isWinner(testedMoves, 5);
        assertTrue(isWinner);

        List<int[]> winningCells = testedVictoryChecker.getWinningCells();
        LinkedList expectedWinningCells = new LinkedList();
        for (int i = 3; i < 8; i++)
            expectedWinningCells.add(new int[] {i, i});
        assertArrayEquals(winningCells.toArray(), expectedWinningCells.toArray());
    }

    /** 0 1 2 3 4 5 6 7 8 9
     0 - - - - - - - - - -
     1 X - - - - - - - - -
     2 - X - - - - - - - -
     3 - - X - - - - - - -
     4 - - - X - - X - - -
     5 - - - - X - - X - -
     6 - - - - - - - - X -
     7 - - - - - - - - - X
     8 - - - - - - - - - -
     9 - - - - - - - - - - */
    @Test(timeout = 10)
    public void checkDiagonalTest_FiveInLineDiagonalForward_PotentiallyWinningColsSplit_FiveNeededToWin() {
        for (int i = 0; i < 5; i++)
            testedMoves.add(new int[] {i, i + 1});

        for (int i = 6; i < 10; i++)
            testedMoves.add(new int[] {i, i - 2});

        boolean isWinner = testedVictoryChecker.isWinner(testedMoves, 5);
        assertTrue(isWinner);

        List<int[]> winningCells = testedVictoryChecker.getWinningCells();
        LinkedList expectedWinningCells = new LinkedList();
        for (int i = 0; i < 5; i++)
            expectedWinningCells.add(new int[] {i, i + 1});
        assertArrayEquals(winningCells.toArray(), expectedWinningCells.toArray());
    }

    /** 0 1 2 3 4 5 6 7 8 9
     0 - - - - - - - - - -
     1 - - - - - - - - - -
     2 - - - - - - - - - -
     3 - - - - - - X - - -
     4 - - - - - X - - - -
     5 - - - - X - - - - -
     6 - - - X - - - - - -
     7 - - X - - - - - - -
     8 - - - - - - - - - -
     9 - - - - - - - - - - */
    @Test(timeout = 10)
    public void checkDiagonalTest_FiveInLineDiagonalBackward_FiveNeededToWin() {
        for (int i = 7; i > 2; i--)
            testedMoves.add(new int[] {9 - i, i});

        boolean isWinner = testedVictoryChecker.isWinner(testedMoves, 5);
        assertTrue(isWinner);

        List<int[]> winningCells = testedVictoryChecker.getWinningCells();
        assertArrayEquals(winningCells.toArray(), testedMoves.toArray());
    }

    /** 0 1 2 3 4 5 6 7 8 9
     0 - - - - - - - - - -
     1 - - - - - - - - - -
     2 - - - - - - - - - -
     3 - - - - - - X - - -
     4 - - - - - X - - - -
     5 - - - - X - - - - -
     6 - - - X - - - - - -
     7 - - X - - - - - - -
     8 - - - - - - - - - -
     9 - - - - - - - - - - */
    @Test(timeout = 10)
    public void checkDiagonalTest_FiveInLineDiagonalBackward_SixNeededToWin() {
        for (int i = 7; i > 2; i--)
            testedMoves.add(new int[] {9 - i, i});

        boolean isWinner = testedVictoryChecker.isWinner(testedMoves, 6);
        assertFalse(isWinner);

        List<int[]> winningCells = testedVictoryChecker.getWinningCells();
        assertTrue(winningCells.isEmpty());
    }

    /** 0 1 2 3 4 5 6 7 8 9
     0 - - - - - - - - - -
     1 - - - - - - - - - -
     2 - - - - - - - - - -
     3 - - - - - X - - - -
     4 - - - - - X - - - -
     5 - - - - X - - - - -
     6 - - - X - - - - - -
     7 - - X - - - - - - -
     8 - - - - - - - - - -
     9 - - - - - - - - - - */
    @Test(timeout = 10)
    public void checkDiagonalTest_FourInLineDiagonalBackward_FiveNeededToWin() {
        for (int i = 7; i > 3; i--)
            testedMoves.add(new int[] {9 - i, i});

        testedMoves.add(new int[] {5, 3});

        boolean isWinner = testedVictoryChecker.isWinner(testedMoves, 5);
        assertFalse(isWinner);

        List<int[]> winningCells = testedVictoryChecker.getWinningCells();
        assertTrue(winningCells.isEmpty());
    }

    /** 0 1 2 3 4 5 6 7 8 9
     0 - - - - - - - X - -
     1 - - - - - - X - - -
     2 - - - - - X X - - -
     3 - - X - X - - X - -
     4 - - X - X - - - X -
     5 - - X - - X - X - X
     6 - - X - - - X - - -
     7 - - - - - X - X - -
     8 - - - - X - - - - -
     9 - - - X - - - - - - */
    @Test(timeout = 10)
    public void checkDiagonalTest_FiveInLineDiagonalBackward_MoreNoiseMoves_FiveNeededToWin() {
        for (int i = 4; i < 8; i++)
            testedMoves.add(new int[] {i, i});

        for (int i = 2; i < 7; i++)
            testedMoves.add(new int[] {2, i});

        for (int i = 2; i < 6; i++)
            testedMoves.add(new int[] {i + 4, i});

        for (int i = 4; i < 8; i++)
            testedMoves.add(new int[] {i, 7 - i});

        for (int i = 3; i < 8; i++)
            testedMoves.add(new int[] {i, 12 - i});

        boolean isWinner = testedVictoryChecker.isWinner(testedMoves, 5);
        assertTrue(isWinner);

        List<int[]> winningCells = testedVictoryChecker.getWinningCells();
        LinkedList expectedWinningCells = new LinkedList();
        for (int i = 3; i < 8; i++)
            expectedWinningCells.add(new int[] {i, 12 - i});
        assertArrayEquals(winningCells.toArray(), expectedWinningCells.toArray());
    }
}
