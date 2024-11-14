package puzzles.minesweeper;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.minesweeper.*;
import edu.rpi.legup.puzzle.minesweeper.rules.MustContainBombDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.*;

public class MustContainBombDirectRuleTest {

    public static final MustContainBombDirectRule RULE = new MustContainBombDirectRule();
    private static Minesweeper minesweeper;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        minesweeper = new Minesweeper();
    }

    @Test
    public void MustContainBombDirectRule_OneUnsetTileOneBombTest1()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/utilities/3x3test2.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(1, 0);
        cell1.setData(MinesweeperTileData.bomb());

        board.addModifiedData(cell1);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell1.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    @Test
    public void MustContainBombDirectRule_FourUnsetTilesOneBombTest2()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/utilities/3x3test3.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(0, 1);
        cell1.setData(MinesweeperTileData.bomb());

        board.addModifiedData(cell1);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell1.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }


    @Test
    public void MustContainBombDirectRule_FourUnsetTilesFourBombsTest3()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/utilities/3x3test3.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(1, 0);
        cell1.setData(MinesweeperTileData.bomb());
        MinesweeperCell cell2 = board.getCell(2, 0);
        cell2.setData(MinesweeperTileData.bomb());
        MinesweeperCell cell3 = board.getCell(0, 1);
        cell3.setData(MinesweeperTileData.bomb());
        MinesweeperCell cell4 = board.getCell(0, 2);
        cell4.setData(MinesweeperTileData.bomb());

        board.addModifiedData(cell1);
        board.addModifiedData(cell2);
        board.addModifiedData(cell3);
        board.addModifiedData(cell4);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell1.getLocation()) || point.equals(cell2.getLocation())
                || point.equals(cell3.getLocation()) || point.equals(cell4.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    @Test
    public void MustContainBombDirectRule_EightUnsetTilesEightBombsTest4()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/utilities/3x3test4.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(0, 0);
        cell1.setData(MinesweeperTileData.bomb());
        MinesweeperCell cell2 = board.getCell(1, 0);
        cell2.setData(MinesweeperTileData.bomb());
        MinesweeperCell cell3 = board.getCell(2, 0);
        cell3.setData(MinesweeperTileData.bomb());
        MinesweeperCell cell4 = board.getCell(0, 1);
        cell4.setData(MinesweeperTileData.bomb());
        MinesweeperCell cell5 = board.getCell(2, 1);
        cell5.setData(MinesweeperTileData.bomb());
        MinesweeperCell cell6 = board.getCell(0, 2);
        cell6.setData(MinesweeperTileData.bomb());
        MinesweeperCell cell7 = board.getCell(1, 2);
        cell7.setData(MinesweeperTileData.bomb());
        MinesweeperCell cell8 = board.getCell(2, 2);
        cell8.setData(MinesweeperTileData.bomb());

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
                if (point.equals(cell1.getLocation()) || point.equals(cell2.getLocation())
                        || point.equals(cell3.getLocation()) || point.equals(cell4.getLocation())
                        || point.equals(cell5.getLocation()) || point.equals(cell6.getLocation())
                        || point.equals(cell7.getLocation()) || point.equals(cell8.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    @Test
    public void MustContainBombDirectRule_OneUnsetTileOneBombFourFlagsTest5()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/utilities/3x3test5.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(1, 1);
        cell1.setData(MinesweeperTileData.bomb());

        board.addModifiedData(cell1);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell1.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    @Test
    public void MustContainBombDirectRule_TwoUnsetTilesTwoBombsTwoFlagsTest6()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/utilities/3x3test6.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(0, 1);
        cell1.setData(MinesweeperTileData.bomb());
        MinesweeperCell cell2 = board.getCell(2, 1);
        cell2.setData(MinesweeperTileData.bomb());

        board.addModifiedData(cell1);
        board.addModifiedData(cell2);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell1.getLocation()) || point.equals(cell2.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    @Test
    public void MustContainBombDirectRule_ThreeUnsetTilesOneBombTwoFlagsTest7()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/utilities/3x3test7.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(1, 1);
        cell1.setData(MinesweeperTileData.bomb());

        board.addModifiedData(cell1);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell1.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }
}
