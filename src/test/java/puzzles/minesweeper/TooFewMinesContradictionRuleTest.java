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

    @Test
    // tests a 3x3 board with a 3 in the center surrounded by 2 mines and 6 empty
    public void TooManyMinesTest1() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/minesweeper/rules/TooFewMines1.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        // confirm it is impossible to satisfy up the center square
        Assert.assertNull(RULE.checkContradictionAt(board, board.getCell(1, 1)));

        // every square except the center
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(0, 0)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(1, 0)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(2, 0)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(0, 1)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(2, 1)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(0, 2)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(1, 2)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(2, 1)));
    }

    @Test
    // tests a 3x3 board with a 3 in the center surrounded by 2 unset and 6 empty
    public void TooManyMinesTest2() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/minesweeper/rules/TooFewMines2.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        // confirm it is impossible to satisfy up the center square
        Assert.assertNull(RULE.checkContradictionAt(board, board.getCell(1, 1)));

        // every square except the center
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
