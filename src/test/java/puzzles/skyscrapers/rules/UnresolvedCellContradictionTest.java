package puzzles.skyscrapers.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.skyscrapers.Skyscrapers;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersBoard;
import edu.rpi.legup.puzzle.skyscrapers.rules.UnresolvedCellContradictionRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class UnresolvedCellContradictionTest {

    private static final UnresolvedCellContradictionRule RULE =
            new UnresolvedCellContradictionRule();
    private static Skyscrapers skyscrapers;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        skyscrapers = new Skyscrapers();
    }

    // empty
    @Test
    public void UnresolvedCellContradictionRule_EmptyBoardTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/skyscrapers/rules/common/empty", skyscrapers);

        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        Assert.assertNotNull(RULE.checkContradiction((SkyscrapersBoard) transition.getBoard()));

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
            }
        }
    }

    // correct board, no cont
    @Test
    public void UnresolvedCellContradictionRule_SolvedBoardTest()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/skyscrapers/rules/common/Solved", skyscrapers);

        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        Assert.assertNotNull(RULE.checkContradiction((SkyscrapersBoard) transition.getBoard()));

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
            }
        }
    }

    // invalid board, no cont
    @Test
    public void UnresolvedCellContradictionRule_OtherContradictionTest()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/skyscrapers/rules/VisibilityContradictionRules/ImpliedAllContradiction",
                skyscrapers);

        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        Assert.assertNotNull(RULE.checkContradiction((SkyscrapersBoard) transition.getBoard()));

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
            }
        }
    }

    // 3 in a row, 1 in col creates contradiction
    @Test
    public void UnresolvedCellContradictionRule_RowContradictionTest()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/skyscrapers/rules/UnresolvedContradictionRules/3-1RowContradiction",
                skyscrapers);

        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        Assert.assertNull(RULE.checkContradiction((SkyscrapersBoard) transition.getBoard()));

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                if (k == 2 && i == 3) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    // 3 in a col, 1 in row creates contradiction
    @Test
    public void UnresolvedCellContradictionRule_ColContradictionTest()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/skyscrapers/rules/UnresolvedContradictionRules/3-1ColContradiction",
                skyscrapers);

        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        Assert.assertNull(RULE.checkContradiction((SkyscrapersBoard) transition.getBoard()));

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                if (k == 1 && i == 0) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    // 2 in a col, 2 in row creates cell contradiction
    @Test
    public void UnresolvedCellContradictionRule_MixedContradictionTest()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/skyscrapers/rules/UnresolvedContradictionRules/2-2CellContradiction",
                skyscrapers);

        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        Assert.assertNull(RULE.checkContradiction((SkyscrapersBoard) transition.getBoard()));

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                if (k == 2 && i == 3) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }
}
