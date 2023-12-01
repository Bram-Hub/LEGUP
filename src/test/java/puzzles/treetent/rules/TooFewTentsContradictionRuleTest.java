package puzzles.treetent.rules;

import edu.rpi.legup.puzzle.treetent.TreeTentCell;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.rpi.legup.puzzle.treetent.TreeTent;
import edu.rpi.legup.puzzle.treetent.TreeTentBoard;
import edu.rpi.legup.puzzle.treetent.rules.TooFewTentsContradictionRule;
import edu.rpi.legup.save.InvalidFileFormatException;

import java.awt.*;

public class TooFewTentsContradictionRuleTest {

    private static final TooFewTentsContradictionRule RULE = new TooFewTentsContradictionRule();
    private static TreeTent treetent;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        treetent = new TreeTent();
    }

    /**
     * @throws InvalidFileFormatException
     * Using a 1x1 Puzzle Grid, which is just grass, checks if the fact it expects a tent on the y-axis is caught.
     */
    @Test
    public void TooFewTentsContradictionRule_JustY() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/TooFewTentsContradictionRule/TooFewTentsJustY", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();
        Assert.assertNull(RULE.checkContradiction(board));
        Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(0, 0)));
    }

    /**
     * @throws InvalidFileFormatException
     * Using a 1x1 Puzzle Grid, which is just a tent, checks if the fact it expects 2 tents on the y-axis is caught.
     * (This is an impossible situation given the constraints, but for the purposes of the test it is fine)
     */
    @Test
    public void TooFewTentsContradictionRule_WithTent() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/TooFewTentsContradictionRule/TooFewTentsWithTent", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();
        Assert.assertNull(RULE.checkContradiction(board));
        Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(0, 0)));
    }

    /**
     * @throws InvalidFileFormatException
     * Using a 1x1 Puzzle Grid, which is just grass, checks if the fact it expects a tent on both x and y is caught.
     */
    @Test
    public void TooFewTentsContradictionRule_DoubleBad() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/TooFewTentsContradictionRule/TooFewTentsDoubleBad", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();
        Assert.assertNull(RULE.checkContradiction(board));
        Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(0, 0)));
    }

    /**
     * @throws InvalidFileFormatException
     * Looks at a 2x2 Board in the format:
     *  [] Tr
     *  [] Gr
     *  Column 2 is checked to have 1 Tent (which is not present, thus producing a contradiction)
     */
    @Test
    public void TooFewTentsContradictionRule_2x2ColumnOnly() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/TooFewTentsContradictionRule/TooFewTents2x2Column",treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        Assert.assertNull(RULE.checkContradiction(board));

        TreeTentCell cell1 = board.getCell(1,0);
        TreeTentCell cell2 = board.getCell(1,1);

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

    /**
     * @throws InvalidFileFormatException
     * Looks at a 2x2 Board in the format:
     *  Tr Gr
     *  [] []
     *  Row 1 is checked to have 1 Tent (which is not present, thus producing a contradiction)
     */
    @Test
    public void TooFewTentsContradictionRule_2x2RowOnly() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/TooFewTentsContradictionRule/TooFewTents2x2Row",treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        Assert.assertNull(RULE.checkContradiction(board));

        TreeTentCell cell1 = board.getCell(0,0);
        TreeTentCell cell2 = board.getCell(1,0);

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

    /**
     * @throws InvalidFileFormatException
     * Looks at a 3x3 Board in the format:
     *  [] Tr []
     *  [] Gr []
     *  [] Gr []
     *  Column 2 is checked to have 1 Tent (which is not present, thus producing a contradiction)
     */
    @Test
    public void TooFewTentsContradictionRule_3x3OneColumn() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/TooFewTentsContradictionRule/TooFewTents3x3Column",treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        Assert.assertNull(RULE.checkContradiction(board));

        TreeTentCell cell1 = board.getCell(1,0);
        TreeTentCell cell2 = board.getCell(1,1);
        TreeTentCell cell3 = board.getCell(1,2);

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell1.getLocation()) || point.equals(cell2.getLocation()) || point.equals(cell3.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    /**
     * @throws InvalidFileFormatException
     * Looks at a 3x3 Board in the format:
     *  Gr Tr Gr
     *  Gr [] Gr
     *  Gr Tr Gr
     *  Column 1 and 3 are checked to have 1 Tent (which is not present, thus producing a contradiction)
     */
    @Test
    public void TooFewTentsContradictionRule_3x3TwoColumn() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/TooFewTentsContradictionRule/TooFewTents3x3DoubleColumn",treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        Assert.assertNull(RULE.checkContradiction(board));

        TreeTentCell cell1 = board.getCell(0,0);
        TreeTentCell cell2 = board.getCell(0,1);
        TreeTentCell cell3 = board.getCell(0,2);
        TreeTentCell cell4 = board.getCell(2,0);
        TreeTentCell cell5 = board.getCell(2,1);
        TreeTentCell cell6 = board.getCell(2,2);

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell1.getLocation()) || point.equals(cell2.getLocation()) || point.equals(cell3.getLocation()) ||
                    point.equals(cell4.getLocation()) || point.equals(cell5.getLocation()) || point.equals(cell6.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    /**
     * @throws InvalidFileFormatException
     * Looks at a 2x2 Board in the format:
     *  Tn []
     *  Tr []
     * This should fail the contradiction as it is a legal board.
     */
    @Test
    public void TooFewTentsContradictionRule_NoContradiction() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/TooFewTentsContradictionRule/TooFewTentsNoContradiction",treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        Assert.assertNotNull(RULE.checkContradiction(board));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
            }
        }
    }



}

