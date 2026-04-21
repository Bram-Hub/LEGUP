package puzzles.minesweeper;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.minesweeper.Minesweeper;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperBoard;
import edu.rpi.legup.puzzle.minesweeper.rules.TooManyMinesContradictionRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TooManyMinesContradictionRuleTest {
    private static final TooManyMinesContradictionRule RULE = new TooManyMinesContradictionRule();
    private static Minesweeper minesweeper;

    @BeforeClass
    public static void setUp() {
        minesweeper = new Minesweeper();
    }

    /**
    * Checks that TooManyMinesContradictionRule behaves as expected in basic scenario.
    *<br><br>
    * This is what TooManyMines1.txt looks like:
    * <pre>
    *    e  #  e
    *    #  3  #
    *    e  #  e</pre>
    *<br>
    * This test verifies that the contradiction is recognized for the number cell, but not for the others.
    * */
    @Test
    public void TooManyMinesTest1() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/rules/TooManyMines1.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        // confirm it is impossible to satisfy up the middle cell
        Assert.assertNull(RULE.checkContradictionAt(board, board.getCell(1, 1)));

        // every cell except the middle
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(0, 0)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(1, 0)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(2, 0)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(0, 1)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(2, 1)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(0, 2)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(1, 2)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(2, 2)));
    }

    /**
    * Checks that TooManyMinesContradictionRule behaves as expected in basic scenario.
    *<br><br>
    * This is what TooManyMines2.txt looks like:
    * <pre>
    *    1  #  e
    *    #  #  e
    *    e  e  e</pre>
    *<br>
    * This test verifies that the contradiction is recognized for the number cell, but not for the others
    * */
    @Test
    public void TooManyMinesTest2() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/rules/TooManyMines2.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        // confirm it is impossible to satisfy up the top right corner cell
        Assert.assertNull(RULE.checkContradictionAt(board, board.getCell(0, 0)));

        // every cell except the top right corner
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(1, 0)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(2, 0)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(0, 1)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(1, 1)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(2, 1)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(0, 2)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(1, 2)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(2, 2)));
    }
}
