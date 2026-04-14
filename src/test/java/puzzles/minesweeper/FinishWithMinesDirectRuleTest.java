package puzzles.minesweeper;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.minesweeper.*;
import edu.rpi.legup.puzzle.minesweeper.rules.FinishWithMinesDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.awt.*;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class FinishWithMinesDirectRuleTest {

    public static final FinishWithMinesDirectRule RULE = new FinishWithMinesDirectRule();
    private static Minesweeper minesweeper;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        minesweeper = new Minesweeper();
    }

    // tests the finish with mines direct rule is many different cases

    /*
     * Checks that FinishWithMinesDirectRule behaves as expected in simple scenario.
     *
     * This is What 3x3test2.txt looks like:
     *       e  _| e
     *       e  1  e
     *       e  e  e
     *
     * The center cell in the top row is initially UNSET. This test explicitly changes it to MINE.
     *
     * The point is that the rule works in the simplest possible scenario.
     * */
    @Test
    public void FinishWithMinesDirectRule_OneUnsetOneBombOneNumberTest1()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/rules/3x3test2.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(1, 0);
        cell1.setData(MinesweeperTileData.mine());

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

    /*
    * Check that FinisWithMinesDirectRUle behaves as expected in scenario where it is used one of many
    * viable cells.
    *
    * This is what 3x3test3.txt looks like:
    *        e  _| _|
    *        _| 4  e
    *        _| e  e
    *
    * This test explicitly changes the center cell in the left-hoof column from UNSET to MINE. That cell
    * should be accepted by the rule, although this rule could be used, and validate, with the three other
    * UNSET cells, the rule must only be used on a cell the user has interacted with.
    * */
    @Test
    public void FinishWithMinesDirectRule_FourUnsetOneBombOneNumberTest2()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/rules/3x3test3.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(0, 1);
        cell1.setData(MinesweeperTileData.mine());

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

    /*
     * Check that FinisWithMinesDirectRUle behaves as expected in scenario where it is used with multiple cells.
     *
     * This is what 3x3test3.txt looks like:
     *        e  _| _|
     *        _| 4  e
     *        _| e  e
     *
     * This test explicitly changes the center cell in the left-hoof column from UNSET to MINE. As well as the
     * bottom left one, the top right one, and the center one in the top row. Those changes should represent a
     * complete use of this rule, the '4' cell no has four mines in its bubble. Because of that, all those cells
     * should be accepted by the rule.
     * */
    @Test
    public void FinishWithMinesDirectRule_FourUnsetFourBombsOneNumberTest3()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/rules/3x3test3.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(1, 0);
        cell1.setData(MinesweeperTileData.mine());
        MinesweeperCell cell2 = board.getCell(2, 0);
        cell2.setData(MinesweeperTileData.mine());
        MinesweeperCell cell3 = board.getCell(0, 1);
        cell3.setData(MinesweeperTileData.mine());
        MinesweeperCell cell4 = board.getCell(0, 2);
        cell4.setData(MinesweeperTileData.mine());

        board.addModifiedData(cell1);
        board.addModifiedData(cell2);
        board.addModifiedData(cell3);
        board.addModifiedData(cell4);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell1.getLocation())
                        || point.equals(cell2.getLocation())
                        || point.equals(cell3.getLocation())
                        || point.equals(cell4.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    /*
    * Check that FinishWithMinesDirectRule behaves as expected in 'full bubble' scenario.
    *
    * This is what 3x3test4.txt looks like:
    *    _| _| _|
    *    _| 8  _|
    *    _| _| _|
    *
    * This test explicitly changes all the UNSET cells (the entire bubble of the '8' cell) to MINEs.
    * That is a complete use of this rule. The '8' cell is satisfied by that number of mines, and all
    * those changed cells should be valid uses of this rule.
    * */
    @Test
    public void FinishWithMinesDirectRule_EightUnsetEightBombsOneNumberTest4()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/rules/3x3test4.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(0, 0);
        cell1.setData(MinesweeperTileData.mine());
        MinesweeperCell cell2 = board.getCell(1, 0);
        cell2.setData(MinesweeperTileData.mine());
        MinesweeperCell cell3 = board.getCell(2, 0);
        cell3.setData(MinesweeperTileData.mine());
        MinesweeperCell cell4 = board.getCell(0, 1);
        cell4.setData(MinesweeperTileData.mine());
        MinesweeperCell cell5 = board.getCell(2, 1);
        cell5.setData(MinesweeperTileData.mine());
        MinesweeperCell cell6 = board.getCell(0, 2);
        cell6.setData(MinesweeperTileData.mine());
        MinesweeperCell cell7 = board.getCell(1, 2);
        cell7.setData(MinesweeperTileData.mine());
        MinesweeperCell cell8 = board.getCell(2, 2);
        cell8.setData(MinesweeperTileData.mine());

        board.addModifiedData(cell1);
        board.addModifiedData(cell2);
        board.addModifiedData(cell3);
        board.addModifiedData(cell4);
        board.addModifiedData(cell5);
        board.addModifiedData(cell6);
        board.addModifiedData(cell7);
        board.addModifiedData(cell8);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell1.getLocation())
                        || point.equals(cell2.getLocation())
                        || point.equals(cell3.getLocation())
                        || point.equals(cell4.getLocation())
                        || point.equals(cell5.getLocation())
                        || point.equals(cell6.getLocation())
                        || point.equals(cell7.getLocation())
                        || point.equals(cell8.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    @Test
    public void FinishWithMinesDirectRule_OneUnsetOneBombFourNumbersTest5()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/rules/3x3test5.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(1, 1);
        cell1.setData(MinesweeperTileData.mine());

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
    public void FinishWithMinesDirectRule_TwoUnsetTwoBombsTwoNumbersTest6()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/rules/3x3test6.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(0, 1);
        cell1.setData(MinesweeperTileData.mine());
        MinesweeperCell cell2 = board.getCell(2, 1);
        cell2.setData(MinesweeperTileData.mine());

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

    @Test
    public void FinishWithMinesDirectRule_ThreeUnsetOneBombTwoNumbersTest7()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/rules/3x3test7.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(1, 1);
        cell1.setData(MinesweeperTileData.mine());

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
}
