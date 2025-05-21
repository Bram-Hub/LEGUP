package puzzles.starbattle.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.starbattle.StarBattle;
import edu.rpi.legup.puzzle.starbattle.StarBattleBoard;
import edu.rpi.legup.puzzle.starbattle.StarBattleCell;
import edu.rpi.legup.puzzle.starbattle.StarBattleCellType;
import edu.rpi.legup.puzzle.starbattle.rules.ColumnsWithinRegionsDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.awt.*;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ColumnsWithinRegionsDirectRuleTest {

    private static final ColumnsWithinRegionsDirectRule RULE = new ColumnsWithinRegionsDirectRule();
    private static StarBattle starbattle;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        starbattle = new StarBattle();
    }

    @Test
    public void ColumnsWithinRegionsDirectRule_OneColumnOneCell()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/starbattle/rules/ColumnsWithinRegionsDirectRule/OneColumnOneCell",
                starbattle);
        TreeNode rootNode = starbattle.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
        StarBattleCell cell = board.getCell(1, 0);
        cell.setData(StarBattleCellType.BLACK.value);
        board.addModifiedData(cell);

        Assert.assertNull(RULE.checkRule(transition));

        Point location = new Point(1, 0);
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(location)) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    @Test
    public void ColumnsWithinRegionsDirectRule_PartialColumnOneCell()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/starbattle/rules/ColumnsWithinRegionsDirectRule/PartialColumnOneCell",
                starbattle);
        TreeNode rootNode = starbattle.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
        StarBattleCell cell = board.getCell(1, 2);
        cell.setData(StarBattleCellType.BLACK.value);
        board.addModifiedData(cell);

        Assert.assertNull(RULE.checkRule(transition));

        Point location = new Point(1, 2);
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(location)) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    @Test
    public void ColumnsWithinRegionsDirectRule_PartialColumnTwoCells()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/starbattle/rules/ColumnsWithinRegionsDirectRule/PartialColumnTwoCells",
                starbattle);
        TreeNode rootNode = starbattle.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
        StarBattleCell cell1 = board.getCell(0, 1);
        cell1.setData(StarBattleCellType.BLACK.value);
        board.addModifiedData(cell1);
        StarBattleCell cell2 = board.getCell(2, 1);
        cell2.setData(StarBattleCellType.BLACK.value);
        board.addModifiedData(cell2);

        Assert.assertNull(RULE.checkRule(transition));

        Point location1 = new Point(0, 1);
        Point location2 = new Point(2, 1);
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(location1) || point.equals(location2)) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    @Test
    public void ColumnsWithinRegionsDirectRule_TwoColumns() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/starbattle/rules/ColumnsWithinRegionsDirectRule/TwoColumns", starbattle);
        TreeNode rootNode = starbattle.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
        StarBattleCell cell1 = board.getCell(2, 1);
        cell1.setData(StarBattleCellType.BLACK.value);
        board.addModifiedData(cell1);
        StarBattleCell cell2 = board.getCell(2, 2);
        cell2.setData(StarBattleCellType.BLACK.value);
        board.addModifiedData(cell2);

        Assert.assertNull(RULE.checkRule(transition));

        Point location1 = new Point(2, 1);
        Point location2 = new Point(2, 2);
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(location1) || point.equals(location2)) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    /* Wrote this to figure out the specifics of how the rule is functioning - might change
     * what the expected result is. */
    @Test
    public void ColumnsWithinRegionsDirectRule_TwoColumnsWaitAMinute()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/starbattle/rules/ColumnsWithinRegionsDirectRule/TwoColumns", starbattle);
        TreeNode rootNode = starbattle.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
        StarBattleCell cell1 = board.getCell(2, 1);
        cell1.setData(StarBattleCellType.BLACK.value);
        board.addModifiedData(cell1);

        Assert.assertNull(RULE.checkRule(transition));

        Point location1 = new Point(2, 1);
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(location1)) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    @Test
    public void ColumnsWithinRegionsDirectRule_TwoColumnsStarOverlap()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/starbattle/rules/ColumnsWithinRegionsDirectRule/TwoColumnsStarOverlap",
                starbattle);
        TreeNode rootNode = starbattle.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
        StarBattleCell cell1 = board.getCell(2, 3);
        cell1.setData(StarBattleCellType.BLACK.value);
        board.addModifiedData(cell1);

        Assert.assertNull(RULE.checkRule(transition));

        Point location1 = new Point(2, 3);
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(location1)) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    @Test
    public void ColumnsWithinRegionsDirectRule_FalseColumnsWithinRegions1()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/starbattle/rules/ColumnsWithinRegionsDirectRule/OneColumnOneCell",
                starbattle);
        TreeNode rootNode = starbattle.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
        StarBattleCell cell1 = board.getCell(1, 0);
        cell1.setData(StarBattleCellType.BLACK.value);
        board.addModifiedData(cell1);
        StarBattleCell cell2 = board.getCell(0, 0);
        cell2.setData(StarBattleCellType.BLACK.value);
        board.addModifiedData(cell2);

        Assert.assertNotNull(RULE.checkRule(transition));

        Point location = new Point(1, 0);
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(location)) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    @Test
    public void ColumnsWithinRegionsDirectRule_FalseColumnsWithinRegions2()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/starbattle/rules/ColumnsWithinRegionsDirectRule/OneColumnOneCell",
                starbattle);
        TreeNode rootNode = starbattle.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
        StarBattleCell cell1 = board.getCell(1, 0);
        cell1.setData(StarBattleCellType.BLACK.value);
        board.addModifiedData(cell1);
        StarBattleCell cell2 = board.getCell(1, 1);
        cell2.setData(StarBattleCellType.BLACK.value);
        board.addModifiedData(cell2);

        Assert.assertNotNull(RULE.checkRule(transition));

        Point location = new Point(1, 0);
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(location)) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    @Test
    public void ColumnsWithinRegionsDirectRule_PartialRemoval() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/starbattle/rules/ColumnsWithinRegionsDirectRule/PartialColumnTwoCells",
                starbattle);
        TreeNode rootNode = starbattle.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
        StarBattleCell cell1 = board.getCell(0, 1);
        cell1.setData(StarBattleCellType.BLACK.value);
        board.addModifiedData(cell1);

        Assert.assertNull(RULE.checkRule(transition));

        Point location1 = new Point(0, 1);
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(location1)) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    @Test
    public void ColumnsWithinRegionsDirectRule_FalseColumnsWithinRegions4()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/starbattle/rules/ColumnsWithinRegionsDirectRule/FalseStarOverlap",
                starbattle);
        TreeNode rootNode = starbattle.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
        StarBattleCell cell1 = board.getCell(2, 2);
        cell1.setData(StarBattleCellType.BLACK.value);
        board.addModifiedData(cell1);
        StarBattleCell cell2 = board.getCell(2, 3);
        cell2.setData(StarBattleCellType.BLACK.value);
        board.addModifiedData(cell2);

        Assert.assertNotNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
            }
        }
    }
}
