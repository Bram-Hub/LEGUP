package puzzles.minesweeper;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.minesweeper.Minesweeper;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperBoard;
import edu.rpi.legup.puzzle.minesweeper.rules.TooFewMinesContradictionRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TooFewMinesContradictionRuleTest {
    private static final TooFewMinesContradictionRule RULE = new TooFewMinesContradictionRule();
    private static Minesweeper minesweeper;

    @BeforeClass
    public static void setUp() {
        minesweeper = new Minesweeper();
    }

    /**
     * Checks that TooFewMinesContradictionRule behaves as expected in basic scenario. <br>
     * <br>
     * This is what TooFewMines1.txt looks like:
     *
     * <pre>
     *    e  e  e
     *    #  3  #
     *    e  e  e</pre>
     *
     * <br>
     * This test verifies that the contradiction is recognized for that number cell, but not for the
     * others.
     */
    @Test
    public void TooFewMinesTest1() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/rules/TooFewMines1.txt", minesweeper);
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
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(2, 1)));
    }

    /**
     * Checks that TooFewMinesContradictionRule behaves as expected in basic zero-mine scenario.
     * <br>
     * <br>
     * This is what TooFewMines2.txt looks like:
     *
     * <pre>
     *    e  _| e
     *    e  3  e
     *    e  _| e</pre>
     *
     * <br>
     * This test verifies that the contradiction is recognized for that number cell, but not for the
     * others. It is supposed to function even though there may or may not be mines under those two
     * UNSET cells. Because there's two of them and if they were both mines it would not validate
     * for the FinishWithMinesDirectRule in relation to the '3' cell.
     */
    @Test
    public void TooFewMinesTest2() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/rules/TooFewMines2.txt", minesweeper);
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
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(2, 1)));
    }
}
