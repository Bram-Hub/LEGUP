package puzzles.treetent.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.treetent.TreeTent;
import edu.rpi.legup.puzzle.treetent.TreeTentBoard;
import edu.rpi.legup.puzzle.treetent.TreeTentCell;
import edu.rpi.legup.puzzle.treetent.TreeTentType;
import edu.rpi.legup.puzzle.treetent.rules.EmptyFieldDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.*;

public class EmptyFieldDirectRuleTest {

    private static final EmptyFieldDirectRule RULE = new EmptyFieldDirectRule();
    private static TreeTent treetent;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        treetent = new TreeTent();
    }

    // creates a 3x3 puzzle with no trees
    // make the (1,1) tile GRASS
    // checks if tiles logically follow the EmptyFieldDirectRule
    @Test
    public void EmptyFieldTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/EmptyFieldDirectRule/EmptyField", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        // get board state
        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        // change the board's cells considering the EmptyField rule
        TreeTentCell cell1 = board.getCell(1, 1);
        cell1.setData(TreeTentType.GRASS);
        board.addModifiedData(cell1);

        // confirm there is a logical following of the EmptyField rule
        Assert.assertNull(RULE.checkRule(transition));

        // only the cell above should change following the rule
        TreeTentCell c;
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                c = board.getCell(k, i);
                if (c.getLocation().equals(cell1.getLocation())) {
                    // logically follows
                    Assert.assertNull(RULE.checkRuleAt(transition, c));
                }
                else {
                    // does not use the rule to logically follow
                    Assert.assertNotNull(RULE.checkRuleAt(transition, c));
                }
            }
        }
    }

    // creates a 3x3 puzzle with 4 trees
    // trees are at (0,0), (2,0), (0,2), and (2,2)
    // make the (1,1) tile GRASS.
    // checks if tiles logically follow the EmptyFieldDirectRule
    @Test
    public void DiagonalTreeTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/EmptyFieldDirectRule/DiagonalTree", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        // get board state
        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        //change the board's cells considering the EmptyField rule
        TreeTentCell cell1 = board.getCell(1, 1);
        cell1.setData(TreeTentType.GRASS);
        board.addModifiedData(cell1);

        // confirm there is a logical following of the EmptyField rule
        Assert.assertNull(RULE.checkRule(transition));

        // only the cell above should change following the rule
        TreeTentCell c;
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                c = board.getCell(k, i);
                if (c.getLocation().equals(cell1.getLocation())) {
                    // logically follows
                    Assert.assertNull(RULE.checkRuleAt(transition, c));
                }
                else {
                    // does not use the rule to logically follow
                    Assert.assertNotNull(RULE.checkRuleAt(transition, c));
                }
            }
        }
    }

    // creates a 3x3 puzzle with 4 trees
    // trees are at (0,1), (1,0), (1,2), and (2,1)
    // make the (1,1) tile GRASS.
    // checks if tiles don't logically follow the EmptyFieldDirectRule
    @Test
    public void EmptyFieldTestFail() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/EmptyFieldDirectRule/EmptyFieldFail", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        // get board state
        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        //change the board's cells considering breaking the EmptyField rule
        TreeTentCell cell1 = board.getCell(1, 1);
        cell1.setData(TreeTentType.GRASS);
        board.addModifiedData(cell1);

        // confirm there is not a logical following of the EmptyField rule
        Assert.assertNotNull(RULE.checkRule(transition));

        // the cells should not follow the rule
        TreeTentCell c;
        for (int i = 0; i < board.getWidth(); i++) {
            for (int j = 0; j < board.getHeight(); j++) {
                c = board.getCell(j, i);
                // does not use the rule to logically follow
                Assert.assertNotNull(RULE.checkRuleAt(transition, c));
            }
        }
    }
}

