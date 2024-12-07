package puzzles.minesweeper;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.minesweeper.Minesweeper;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperBoard;
import edu.rpi.legup.puzzle.minesweeper.rules.IsolateMineContradictionRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class IsolateMineContradictionRuleTest {
    private static final IsolateMineContradictionRule RULE = new IsolateMineContradictionRule();
    private static Minesweeper minesweeper;

    @BeforeClass
    public static void setUp() {
        minesweeper = new Minesweeper();
    }

    @Test
    // tests a 3x3 board with a mine in the center surrounded by empty cells
    public void IsolateMineTest1() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/minesweeper/rules/IsolateMine1.txt", minesweeper);
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
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(2, 2)));
    }

    @Test
    // tests a 3x3 board with a mine in the center surrounded by unset cells
    public void IsolateMineTest2() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/minesweeper/rules/IsolateMine2.txt", minesweeper);
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
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(2, 2)));
    }

    @Test
    // tests a 3x3 board full of mines only
    public void IsolateMineTest3() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/minesweeper/rules/IsolateMine3.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        // confirm it is impossible to satisfy any of the squares
        Assert.assertNull(RULE.checkContradictionAt(board, board.getCell(1, 1)));
        Assert.assertNull(RULE.checkContradictionAt(board, board.getCell(0, 0)));
        Assert.assertNull(RULE.checkContradictionAt(board, board.getCell(1, 0)));
        Assert.assertNull(RULE.checkContradictionAt(board, board.getCell(2, 0)));
        Assert.assertNull(RULE.checkContradictionAt(board, board.getCell(0, 1)));
        Assert.assertNull(RULE.checkContradictionAt(board, board.getCell(2, 1)));
        Assert.assertNull(RULE.checkContradictionAt(board, board.getCell(0, 2)));
        Assert.assertNull(RULE.checkContradictionAt(board, board.getCell(1, 2)));
        Assert.assertNull(RULE.checkContradictionAt(board, board.getCell(2, 2)));
    }
}
