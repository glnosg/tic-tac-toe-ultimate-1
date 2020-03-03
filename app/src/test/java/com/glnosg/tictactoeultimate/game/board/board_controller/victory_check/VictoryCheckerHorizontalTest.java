package com.glnosg.tictactoeultimate.game.board.board_controller.victory_check;

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;

public class VictoryCheckerHorizontalTest {

    private VictoryChecker testedVictoryChecker;
    private LinkedList<int[]> testedMoves;

    @Before
    public void init() {
        testedVictoryChecker = new VictoryCheckerHorizontal();
        testedMoves = new LinkedList<>();
    }


    /** 0 1 2 3 4 5 6 7 8 9
     0 - - - - - - - - - -
     1 - - - - - - - - - -
     2 - - - - - - - - - -
     3 - - - - - - - - - -
     4 - - - - - - - - - -
     5 - - - X X X X X X -
     6 - - - - - - - - - -
     7 - - - - - - - - - -
     8 - - - - - - - - - -
     9 - - - - - - - - - - */
    @Test(timeout = 10)
    public void checkHorizontalTest_FiveInLineHorizontally_FiveNeededToWin() {
        for (int i = 3; i < 8; i++)
            testedMoves.add(new int[] {i, 5});

        boolean isWinner = testedVictoryChecker.isWinner(testedMoves, 5);
        assertTrue(isWinner);

        List<int[]> winningCells = testedVictoryChecker.getWinningCells();
        assertArrayEquals(winningCells.toArray(), testedMoves.toArray());
    }

    /** 0 1 2 3 4 5 6 7 8 9
     0 - - - - - - - - - -
     1 - - - - - - - - - -
     2 - - - - - - - - - -
     3 - - - - - - - - - -
     4 - - - - - - - - - -
     5 - - - X X X X X X -
     6 - - - - - - - - - -
     7 - - - - - - - - - -
     8 - - - - - - - - - -
     9 - - - - - - - - - - */
    @Test(timeout = 10)
    public void checkHorizontalTest_FiveInLineHorizontally_SixNeededToWin() {
        for (int i = 3; i < 8; i++)
            testedMoves.add(new int[] {i, 5});

        boolean isWinner = testedVictoryChecker.isWinner(testedMoves, 6);
        assertFalse(isWinner);

        List<int[]> winningCells = testedVictoryChecker.getWinningCells();
        assertTrue(winningCells.isEmpty());
    }

    /** 0 1 2 3 4 5 6 7 8 9
      0 - - - - - - - - - -
      1 - - - - - - - - - -
      2 - - - - - - - - - -
      3 - - - - - - - - - -
      4 - - - - - - - - - -
      5 - X X - - X X X - -
      6 - - - - - - - - - -
      7 - - - - - - - - - -
      8 - - - - - - - - - -
      9 - - - - - - - - - - */
    @Test(timeout = 10)
    public void checkHorizontalTest_FiveInLineHorizontallyWithAGap_FourNeededToWin() {
        for (int i = 1; i < 3; i++)
            testedMoves.add(new int[] {i, 5});

        for (int i = 5; i < 8; i++)
            testedMoves.add(new int[] {i, 5});

        boolean isWinner = testedVictoryChecker.isWinner(testedMoves, 4);
        assertFalse(isWinner);

        List<int[]> winningCells = testedVictoryChecker.getWinningCells();
        assertTrue(winningCells.isEmpty());
    }

    /** 0 1 2 3 4 5 6 7 8 9
     0 - - - - - - - - - -
     1 - - - - - - - - - -
     2 - - - - - - - - - -
     3 - - - - - - - - - -
     4 - - - - - - - - - -
     5 X - X - X - X - X -
     6 - - - - - - - - - -
     7 - - - - - - - - - -
     8 - - - - - - - - - -
     9 - - - - - - - - - - */
    @Test(timeout = 10)
    public void checkHorizontalTest_FiveInLineHorizontallyNotConsecutiveColumns_FiveNeededToWin() {
        for (int i = 0; i < 5; i++)
            testedMoves.add(new int[] {i * 2, 5});

        boolean isWinner = testedVictoryChecker.isWinner(testedMoves, 5);
        assertFalse(isWinner);

        List<int[]> winningCells = testedVictoryChecker.getWinningCells();
        assertTrue(winningCells.isEmpty());
    }

    /** 0 1 2 3 4 5 6 7 8 9
     0 - - - - - - - - - -
     1 - - - - - - - - - -
     2 - - - - - - - - - -
     3 - - - - - - - - - -
     4 - - - - - - - X - -
     5 - - - X X X X - - -
     6 - - - - - - - - - -
     7 - - - - - - - - - -
     8 - - - - - - - - - -
     9 - - - - - - - - - - */
    @Test(timeout = 10)
    public void checkHorizontalTest_FourInLineHorizontallyConsecutiveColumns_FiveNeededToWin() {
        for (int i = 3; i < 7; i++)
            testedMoves.add(new int[] {i, 5});

        testedMoves.add(new int[] {7, 4});

        boolean isWinner = testedVictoryChecker.isWinner(testedMoves, 5);
        assertFalse(isWinner);

        List<int[]> winningCells = testedVictoryChecker.getWinningCells();
        assertTrue(winningCells.isEmpty());
    }


    /** 0 1 2 3 4 5 6 7 8 9
     0 - - - - - - - - - -
     1 - - - - - - - - - -
     2 - X X X X - - - - -
     3 - - - - - - - - - -
     4 - X - X X X X X - -
     5 - - - - - - - - - -
     6 - X - X X X X - - -
     7 - - - - - - - - - -
     8 - - - - - - - - - -
     9 - - - - - - - - - - */
    @Test(timeout = 10)
    public void checkHorizontalTest_FiveInLineHorizontally_MoreNoiseMoves_FiveNeededToWin() {
        for (int i = 1; i < 5; i++)
            testedMoves.add(new int[] {i, 2});

        for (int i = 3; i < 8; i++)
            testedMoves.add(new int[] {i, 4});

        for (int i = 3; i < 7; i++)
            testedMoves.add(new int[] {i, 6});

        testedMoves.add(new int[] {1, 4});
        testedMoves.add(new int[] {1, 6});

        boolean isWinner = testedVictoryChecker.isWinner(testedMoves, 5);
        assertTrue(isWinner);

        List<int[]> winningCells = testedVictoryChecker.getWinningCells();
        LinkedList<int[]> expectedWinningCells = new LinkedList<>();
        for (int i = 3; i < 8; i++)
            expectedWinningCells.add(new int[] {i, 4});
        assertArrayEquals(expectedWinningCells.toArray(), winningCells.toArray());
    }
}
