package puzzles.minesweeper;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.minesweeper.Minesweeper;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperBoard;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperCell;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperTileData;
import edu.rpi.legup.puzzle.minesweeper.rules.FinishWithEmptyDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.awt.*;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class FinishWithEmptyDirectRuleTest {

    public static final FinishWithEmptyDirectRule RULE = new FinishWithEmptyDirectRule();
    private static Minesweeper minesweeper;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        minesweeper = new Minesweeper();
    }

    // tests the finish with empty direct rule is many different cases

    @Test
    public void FinishWithEmptyDirectRule_OneUnsetOneEmptyOneNumberTest1()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/rules/3x3test8.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(1, 0);
        cell1.setData(MinesweeperTileData.empty());

        board.addModifiedData(cell1);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell1.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    @Test
    public void FinishWithEmptyDirectRule_FiveUnsetOneEmptyOneNumberTest2()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/rules/3x3test9.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(1, 0);
        cell1.setData(MinesweeperTileData.empty());

        board.addModifiedData(cell1);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell1.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    @Test
    public void FinishWithEmptyDirectRule_NineUnsetOneEmptyZeroNumbersTest3()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/rules/3x3test", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(1, 1);
        cell1.setData(MinesweeperTileData.empty());

        board.addModifiedData(cell1);

        Assert.assertNotNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
            }
        }
    }

    @Test
    public void FinishWithEmptyDirectRule_NineUnsetNineEmptyZeroNumbersTest4()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/rules/3x3test", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(0, 0);
        cell1.setData(MinesweeperTileData.empty());
        MinesweeperCell cell2 = board.getCell(1, 0);
        cell2.setData(MinesweeperTileData.empty());
        MinesweeperCell cell3 = board.getCell(2, 0);
        cell3.setData(MinesweeperTileData.empty());
        MinesweeperCell cell4 = board.getCell(0, 1);
        cell4.setData(MinesweeperTileData.empty());
        MinesweeperCell cell5 = board.getCell(1, 1);
        cell5.setData(MinesweeperTileData.empty());
        MinesweeperCell cell6 = board.getCell(2, 1);
        cell6.setData(MinesweeperTileData.empty());
        MinesweeperCell cell7 = board.getCell(0, 2);
        cell7.setData(MinesweeperTileData.empty());
        MinesweeperCell cell8 = board.getCell(1, 2);
        cell8.setData(MinesweeperTileData.empty());
        MinesweeperCell cell9 = board.getCell(2, 2);
        cell9.setData(MinesweeperTileData.empty());

        board.addModifiedData(cell1);
        board.addModifiedData(cell2);
        board.addModifiedData(cell3);
        board.addModifiedData(cell4);
        board.addModifiedData(cell5);
        board.addModifiedData(cell6);
        board.addModifiedData(cell7);
        board.addModifiedData(cell8);
        board.addModifiedData(cell9);

        Assert.assertNotNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
            }
        }
    }

    @Test
    public void FinishWithEmptyDirectRule_OneUnsetOneEmptyThreeNumbersTest5()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/rules/3x3test10.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(1, 2);
        cell1.setData(MinesweeperTileData.empty());

        board.addModifiedData(cell1);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell1.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    @Test
    public void FinishWithEmptyDirectRule_OneUnsetOneEmptyThreeNumbersTest6()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/rules/3x3test11.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(1, 1);
        cell1.setData(MinesweeperTileData.empty());

        board.addModifiedData(cell1);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell1.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    @Test
    public void FinishWithEmptyDirectRule_FiveUnsetTwoEmptyTwoNumbersTest7()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/rules/3x3test12.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(0, 2);
        cell1.setData(MinesweeperTileData.empty());
        MinesweeperCell cell2 = board.getCell(2, 2);
        cell2.setData(MinesweeperTileData.empty());

        board.addModifiedData(cell1);
        board.addModifiedData(cell2);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell1.getLocation()) || point.equals(cell2.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }
}
