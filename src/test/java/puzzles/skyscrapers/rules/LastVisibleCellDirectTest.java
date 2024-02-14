package puzzles.skyscrapers.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.skyscrapers.Skyscrapers;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersBoard;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersCell;
import edu.rpi.legup.puzzle.skyscrapers.rules.LastVisibleCellDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.awt.*;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class LastVisibleCellDirectTest {

    private static final LastVisibleCellDirectRule RULE = new LastVisibleCellDirectRule();
    private static Skyscrapers skyscrapers;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        skyscrapers = new Skyscrapers();
    }

    // full row
    @Test
    public void LastVisibleCellDirectRule_FullRowTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/skyscrapers/rules/common/3-0RowOpening", skyscrapers);
        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();
        SkyscrapersCell cell = board.getCell(2, 3);
        cell.setData(1);

        board.addModifiedData(cell);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    // full col
    @Test
    public void LastVisibleCellDirectRule_FullColTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/skyscrapers/rules/common/3-0ColOpening", skyscrapers);
        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();
        SkyscrapersCell cell = board.getCell(3, 1);
        cell.setData(1);

        board.addModifiedData(cell);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    // empty row
    @Test
    public void LastVisibleCellDirectRule_EmptyRowTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/skyscrapers/rules/common/empty", skyscrapers);
        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();
        SkyscrapersCell cell = board.getCell(0, 2);
        cell.setData(5);

        board.addModifiedData(cell);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    // empty col
    @Test
    public void LastVisibleCellDirectRule_EmptyColTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/skyscrapers/rules/common/empty", skyscrapers);
        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();
        SkyscrapersCell cell = board.getCell(3, 4);
        cell.setData(5);

        board.addModifiedData(cell);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    // 1-2 row
    public void LastVisibleCellDirectRule_PartialRowTest1() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/skyscrapers/rules/LastVisibleDirectRules/1-2RowOpening", skyscrapers);
        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();
        SkyscrapersCell cell = board.getCell(1, 2);
        cell.setData(3);

        board.addModifiedData(cell);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    // 1-2 col
    public void LastVisibleCellDirectRule_PartialColTest1() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/skyscrapers/rules/LastVisibleDirectRules/1-2ColOpening", skyscrapers);
        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();
        SkyscrapersCell cell = board.getCell(2, 2);
        cell.setData(2);

        board.addModifiedData(cell);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    // 2-1 row
    public void LastVisibleCellDirectRule_PartialRowTest2() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/skyscrapers/rules/LastVisibleDirectRules/2-1RowOpening", skyscrapers);
        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();
        SkyscrapersCell cell = board.getCell(1, 1);
        cell.setData(2);

        board.addModifiedData(cell);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    // 2-1 col
    public void LastVisibleCellDirectRule_PartialColTest2() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/skyscrapers/rules/LastVisibleDirectRules/2-1ColOpening", skyscrapers);
        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();
        SkyscrapersCell cell = board.getCell(0, 2);
        cell.setData(1);

        board.addModifiedData(cell);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }
}
