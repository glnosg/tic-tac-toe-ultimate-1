package com.glnosg.tictactoeultimate.game.board.board_controller.victory_check;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        VictoryCheckerDiagonalTest.class,
        VictoryCheckerVerticalTest.class,
        VictoryCheckerHorizontalTest.class
})
public class VictoryCheckerSuiteTest {
}
