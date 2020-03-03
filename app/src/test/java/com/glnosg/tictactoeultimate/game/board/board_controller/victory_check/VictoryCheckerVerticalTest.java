package com.glnosg.tictactoeultimate.game.board.board_controller.victory_check;

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;

public class VictoryCheckerVerticalTest {

    private VictoryChecker testedVictoryChecker;
    private LinkedList<int[]> testedMoves;

    @Before
    public void init() {
        testedVictoryChecker = new VictoryCheckerVertical();
        testedMoves = new LinkedList<>();
    }


    /** 0 1 2 3 4 5 6 7 8 9
     0 - - - - - - - - - -
     1 - - - - - - - - - -
     2 - - - - - - - - - -
     3 - - - - - X - - - -
     4 - - - - - X - - - -
     5 - - - - - X - - - -
     6 - - - - - X - - - -
     7 - - - - - X - - - -
     8 - - - - - - - - - -
     9 - - - - - - - - - - */
    @Test(timeout = 10)
    public void checkVerticalTest_FiveInLineVertically_FiveNeededToWin() {
        for (int i = 3; i < 8; i++)
            testedMoves.add(new int[] {5, i});

        boolean isWinner = testedVictoryChecker.isWinner(testedMoves, 5);
        assertTrue(isWinner);

        List<int[]> winningCells = testedVictoryChecker.getWinningCells();
        assertArrayEquals(winningCells.toArray(), testedMoves.toArray());
    }

    /** 0 1 2 3 4 5 6 7 8 9
     0 - - - - - - - - - -
     1 - - - - - - - - - -
     2 - - - - - - - - - -
     3 - - - - - X - - - -
     4 - - - - - X - - - -
     5 - - - - - X - - - -
     6 - - - - - X - - - -
     7 - - - - - X - - - -
     8 - - - - - - - - - -
     9 - - - - - - - - - - */
    @Test(timeout = 10)
    public void checkVerticalTest_FiveInLineVertically_SixNeededToWin() {
        for (int i = 3; i < 8; i++)
            testedMoves.add(new int[] {5, i});

        boolean isWinner = testedVictoryChecker.isWinner(testedMoves, 6);
        assertFalse(isWinner);

        List<int[]> winningCells = testedVictoryChecker.getWinningCells();
        assertTrue(winningCells.isEmpty());
    }

    /** 0 1 2 3 4 5 6 7 8 9
      0 - - - - - - - - - -
      1 - - - - - X - - - -
      2 - - - - - X - - - -
      3 - - - - - - - - - -
      4 - - - - - - - - - -
      5 - - - - - X - - - -
      6 - - - - - X - - - -
      7 - - - - - X - - - -
      8 - - - - - - - - - -
      9 - - - - - - - - - - */
    @Test(timeout = 10)
    public void checkVerticalTest_FiveInLineVerticallyWithAGap_FourNeededToWin() {
        for (int i = 1; i < 3; i++)
            testedMoves.add(new int[] {5, i});

        for (int i = 5; i < 8; i++)
            testedMoves.add(new int[] {5, i});

        boolean isWinner = testedVictoryChecker.isWinner(testedMoves, 4);
        assertFalse(isWinner);

        List<int[]> winningCells = testedVictoryChecker.getWinningCells();
        assertTrue(winningCells.isEmpty());
    }

    /** 0 1 2 3 4 5 6 7 8 9
     0 - - - - - X - - - -
     1 - - - - - - - - - -
     2 - - - - - X - - - -
     3 - - - - - - - - - -
     4 - - - - - X - - - -
     5 - - - - - - - - - -
     6 - - - - - X - - - -
     7 - - - - - - - - - -
     8 - - - - - X - - - -
     9 - - - - - - - - - - */
    @Test(timeout = 10)
    public void checkVerticalTest_FiveInLineVerticallyNotConsecutiveRows_FiveNeededToWin() {
        for (int i = 0; i < 5; i++)
            testedMoves.add(new int[] {5, i * 2});

        boolean isWinner = testedVictoryChecker.isWinner(testedMoves, 5);
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
     5 - - - - - X - - - -
     6 - - - - - X - - - -
     7 - - - - X - - - - -
     8 - - - - - - - - - -
     9 - - - - - - - - - - */
    @Test(timeout = 10)
    public void checkVerticalTest_FourInLineVerticallyConsecutiveRows_FiveNeededToWin() {
        for (int i = 3; i < 7; i++)
            testedMoves.add(new int[] {5, i});

        testedMoves.add(new int[] {4, 7});

        boolean isWinner = testedVictoryChecker.isWinner(testedMoves, 5);
        assertFalse(isWinner);

        List<int[]> winningCells = testedVictoryChecker.getWinningCells();
        assertTrue(winningCells.isEmpty());
    }

    /** 0 1 2 3 4 5 6 7 8 9
     0 - - - - - - - - - -
     1 - X - - - - - - - -
     2 - X - - - - - - - -
     3 - X - - X - - - X -
     4 - X X - X - - - X -
     5 - - X - X - - - X -
     6 - - X - X - - - X -
     7 - - X - - X X - X -
     8 - - - - - X X - - -
     9 - - X - - X X - - - */
    @Test(timeout = 10)
    public void checkVerticalTest_FiveInLineVertically_MoreNoiseMoves_FiveNeededToWin() {
        for (int i = 1; i < 5; i++)
            testedMoves.add(new int[] {1, i});

        for (int i = 4; i < 8; i++)
            testedMoves.add(new int[] {2, i});

        for (int i = 7; i < 10; i++) {
            testedMoves.add(new int[] {5, i});
            testedMoves.add(new int[] {6, i});
        }

        for (int i = 3; i < 8; i++)
            testedMoves.add(new int[] {8, i});

        testedMoves.add(new int[] {1, 9});

        boolean isWinner = testedVictoryChecker.isWinner(testedMoves, 5);
        assertTrue(isWinner);

        List<int[]> winningCells = testedVictoryChecker.getWinningCells();
        LinkedList<int[]> expectedWinningCells = new LinkedList<>();
        for (int i = 3; i < 8; i++)
            expectedWinningCells.add(new int[] {8, i});
        assertArrayEquals(winningCells.toArray(), expectedWinningCells.toArray());
    }
}
