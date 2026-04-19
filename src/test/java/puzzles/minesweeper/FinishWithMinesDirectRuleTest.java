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

    /**
     * Checks that FinishWithMinesDirectRule behaves as expected in simple scenario.
     *<br><br>
     * This is What 3x3test2.txt looks like:
     * <pre>
     *       e  _| e
     *       e  1  e
     *       e  e  e</pre>
     *<br>
     * The center cell in the top row is initially UNSET. This test explicitly changes it to MINE.
     *<br>
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

    /**
    * Check that FinisWithMinesDirectRUle behaves as expected in scenario where it is used one of many
    * viable cells.
    *<br><br>
    * This is what 3x3test3.txt looks like:
    * <pre>
    *        e  _| _|
    *        _| 4  e
    *        _| e  e</pre>
    *<br>
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

    /**
     * Check that FinisWithMinesDirectRUle behaves as expected in scenario where it is used with multiple cells.
     *<br><br>
     * This is what 3x3test3.txt looks like:
     * <pre>
     *        e  _| _|
     *        _| 4  e
     *        _| e  e</pre>
     *<br>
     * This test explicitly changes the center cell in the left most column from UNSET to MINE. As well as the
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

    /**
    * Check that FinishWithMinesDirectRule behaves as expected in 'full bubble' scenario.
    *<br><br>
    * This is what 3x3test4.txt looks like:
    * <pre>
    *    _| _| _|
    *    _| 8  _|
    *    _| _| _|</pre>
    *<br>
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

    /**
    * Check that FinishWithMinesDirectRule behaves as expected in the 'overlapping bubbles' scenario.
    *<br><br>
    * This is what 3x3test5.txt looks like:
    * <pre>
    *    e  1  e
    *    1  _| 1
    *    e  1  e</pre>
    *<br>
    * This test explicitly changes the middle cell from UNSET to mine. Now, this should be a valid use of
    * the rule in relation to each of the '1' cells adjacent to it, and it should be a complete use of the
    * rule for each of those as well. It satisfies the number of mines for all the ones. The important
    * thing is that the rule behaves as it should, allowing itself to work in multiple bubbles at one.
    * */
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

    /**
    * Check that FinishWithMinesDirectRule behaves as expected in the 'overlapping bubbles' scenario.
    *<br><br>
    * This is what 3x3test6.txt looks like:
    * <pre>
    *    e  2  e
    *    _| e  _|
    *    e  2  e</pre>
    *<br>
    * This test explicitly changes the center cells in the furthest left and right columns from UNSET to
    * MINE. The rule should accept both of these as valid uses and no other cells. This continues the bubble
    * overlap and satisfy-mine-count-for-multiple-number-cells logic.
    * */
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

    /**
    * Check that FinishWithMinesDirectRule behaves as expected in diminished bubble overlap scenario.
    *<br><br>
    * This is what 3x3test7.txt looks like:
    * <pre>
    *    3  e  e
    *    e  _| _|
    *    e  _| 3</pre>
    *<br>
    * This test explicitly changes the middle cell from UNSET to MINE. This is an incomplete use of the rule.
    * However, it is accepted in relation to both or either of the number cells, as it is the only cell which
    * they share.
    * */
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

    /**
     * Check that FinishWithMinesDirectRule continues to behave as expected in diminished bubble overlap scenario.
     *<br><br>
     * This is what 3x3test7.txt looks like:
     * <pre>
     *    3  e  e
     *    e  _| _|
     *    e  _| 3</pre>
     *<br>
     * This test explicitly changes each UNSET sell to a MINE. This is a complete use of the rule. It allows for some
     * of the cells to belong to one number's 'domain', and one of them to be shared.
     * */
    @Test
    public void FinishWithMinesDirectRule_ThreeUnsetThreeBombTwoNumbersTest8()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/rules/3x3test7.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(1, 1);
        cell1.setData(MinesweeperTileData.mine());
        board.addModifiedData(cell1);

        MinesweeperCell cell2 = board.getCell(1, 2);
        cell2.setData(MinesweeperTileData.mine());
        board.addModifiedData(cell2);

        MinesweeperCell cell3 = board.getCell(2, 1);
        cell3.setData(MinesweeperTileData.mine());
        board.addModifiedData(cell3);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell1.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else if (point.equals(cell2.getLocation())){
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else if (point.equals(cell3.getLocation())){
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }
}
