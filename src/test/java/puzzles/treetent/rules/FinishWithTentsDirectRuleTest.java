package puzzles.treetent.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.treetent.TreeTent;
import edu.rpi.legup.puzzle.treetent.TreeTentBoard;
import edu.rpi.legup.puzzle.treetent.TreeTentCell;
import edu.rpi.legup.puzzle.treetent.TreeTentType;
import edu.rpi.legup.puzzle.treetent.rules.FinishWithTentsDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.*;
import java.util.*;

public class FinishWithTentsDirectRuleTest {

    private static final FinishWithTentsDirectRule RULE = new FinishWithTentsDirectRule();
    private static TreeTent treetent;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        treetent = new TreeTent();
    }

    @Test
    public void FinishWithHorizontalTentsTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/FinishWithTentsDirectRule/FinishWithHorizontalTents", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        TreeTentCell cell1 = board.getCell(1, 0);
        cell1.setData(TreeTentType.TENT);
        TreeTentCell cell2 = board.getCell(2, 0);
        cell2.setData(TreeTentType.TENT);

        board.addModifiedData(cell1);
        board.addModifiedData(cell2);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                TreeTentCell c = board.getCell(k, i);
                if ((c.getLocation()).equals(cell1.getLocation()) || (c.getLocation()).equals(cell2.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, c));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, c));
                }
            }
        }
    }

    @Test
    public void FinishWithVerticalTentsTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/FinishWithTentsDirectRule/FinishWithVerticalTents", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        TreeTentCell cell1 = board.getCell(0, 1);
        cell1.setData(TreeTentType.TENT);
        TreeTentCell cell2 = board.getCell(0, 2);
        cell2.setData(TreeTentType.TENT);

        board.addModifiedData(cell1);
        board.addModifiedData(cell2);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                TreeTentCell c = board.getCell(k, i);
                if ((c.getLocation()).equals(cell1.getLocation()) || (c.getLocation()).equals(cell2.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, c));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, c));
                }
            }
        }
    }

    @Test
    public void FinishWithTentsTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/FinishWithTentsDirectRule/FinishWithTents", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        TreeTentCell cell1 = board.getCell(1, 0);
        TreeTentCell cell2 = board.getCell(1, 2);
        TreeTentCell cell3 = board.getCell(0, 1);
        TreeTentCell cell4 = board.getCell(2, 1);

        cell1.setData(TreeTentType.TENT);
        cell2.setData(TreeTentType.TENT);
        cell3.setData(TreeTentType.TENT);
        cell4.setData(TreeTentType.TENT);

        board.addModifiedData(cell1);
        board.addModifiedData(cell2);
        board.addModifiedData(cell3);
        board.addModifiedData(cell4);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                TreeTentCell c = board.getCell(k, i);
                if ((c.getLocation()).equals(cell1.getLocation()) ||
                    (c.getLocation()).equals(cell2.getLocation()) ||
                    (c.getLocation()).equals(cell3.getLocation()) ||
                    (c.getLocation()).equals(cell4.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, c));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, c));
                }
            }
        }
    }

    @Test
    public void AdditionalTentsTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/FinishWithTentsDirectRule/AdditionalTents", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        TreeTentCell cell1 = board.getCell(1, 0);
        TreeTentCell cell2 = board.getCell(1, 2);
        TreeTentCell cell3 = board.getCell(0, 1);
        TreeTentCell cell4 = board.getCell(2, 1);

        cell1.setData(TreeTentType.TENT);
        cell2.setData(TreeTentType.TENT);
        cell3.setData(TreeTentType.TENT);
        cell4.setData(TreeTentType.TENT);

        board.addModifiedData(cell1);
        board.addModifiedData(cell2);
        board.addModifiedData(cell3);
        board.addModifiedData(cell4);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                TreeTentCell c = board.getCell(k, i);
                if ((c.getLocation()).equals(cell1.getLocation()) ||
                    (c.getLocation()).equals(cell2.getLocation()) ||
                    (c.getLocation()).equals(cell3.getLocation()) ||
                    (c.getLocation()).equals(cell4.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, c));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, c));
                }
            }
        }
    }

    @Test
    public void FinishWithTentsFailTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/FinishWithTentsDirectRule/FinishWithTentsFail", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        ArrayList<TreeTentCell> cells = new ArrayList<TreeTentCell>();

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                TreeTentCell c = board.getCell(k, i);
                c.setData(TreeTentType.TENT);
                board.addModifiedData(c);
                cells.add(c);
            }
        }

        Assert.assertNotNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
            }
        }
    }

    @Test
    public void TooManyTentsTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/FinishWithTentsDirectRule/TooManyTents", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        ArrayList<TreeTentCell> cells = new ArrayList<TreeTentCell>();

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                if((k == 1)&&(i == 1))
                    continue;
                TreeTentCell c = board.getCell(k, i);
                c.setData(TreeTentType.TENT);
                board.addModifiedData(c);
                cells.add(c);
            }
        }

        Assert.assertNotNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
            }
        }
    }
}



