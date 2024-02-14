package puzzles.treetent.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.treetent.TreeTent;
import edu.rpi.legup.puzzle.treetent.TreeTentBoard;
import edu.rpi.legup.puzzle.treetent.TreeTentCell;
import edu.rpi.legup.puzzle.treetent.TreeTentType;
import edu.rpi.legup.puzzle.treetent.rules.LastCampingSpotDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.awt.*;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class LastCampingSpotDirectRuleTest {

    private static final LastCampingSpotDirectRule RULE = new LastCampingSpotDirectRule();
    private static TreeTent treetent;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        treetent = new TreeTent();
    }

    /**
     * @throws InvalidFileFormatException
     *     <p>Checks if a test works for an empty square above a tree which is surrounded on all
     *     other sides.
     */
    @Test
    public void EmptyFieldTest_Up() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/treetent/rules/LastCampingSpotDirectRule/LastCampingSpotUp", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        TreeTentCell cell1 = board.getCell(1, 0);
        cell1.setData(TreeTentType.TENT);

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
     * @throws InvalidFileFormatException
     *     <p>Checks if a test works for an empty square below a tree which is surrounded on all
     *     other sides.
     */
    @Test
    public void EmptyFieldTest_Down() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/treetent/rules/LastCampingSpotDirectRule/LastCampingSpotDown", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        TreeTentCell cell1 = board.getCell(1, 2);
        cell1.setData(TreeTentType.TENT);

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
     * @throws InvalidFileFormatException
     *     <p>Checks if a test works for an empty square to the left of a tree which is surrounded
     *     on all other sides.
     */
    @Test
    public void EmptyFieldTest_Left() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/treetent/rules/LastCampingSpotDirectRule/LastCampingSpotLeft", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        TreeTentCell cell1 = board.getCell(0, 1);
        cell1.setData(TreeTentType.TENT);

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
     * @throws InvalidFileFormatException
     *     <p>Checks if a test works for an empty square to the right of a tree which is surrounded
     *     on all other sides.
     */
    @Test
    public void EmptyFieldTest_Right() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/treetent/rules/LastCampingSpotDirectRule/LastCampingSpotRight", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        TreeTentCell cell1 = board.getCell(2, 1);
        cell1.setData(TreeTentType.TENT);

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
