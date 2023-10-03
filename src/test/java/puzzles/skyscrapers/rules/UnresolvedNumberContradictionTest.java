package puzzles.skyscrapers.rules;


import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.skyscrapers.Skyscrapers;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersBoard;
import edu.rpi.legup.puzzle.skyscrapers.rules.UnresolvedNumberContradictionRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class UnresolvedNumberContradictionTest {

    private static final UnresolvedNumberContradictionRule RULE = new UnresolvedNumberContradictionRule();
    private static Skyscrapers skyscrapers;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        skyscrapers = new Skyscrapers();
    }

    //empty
    @Test
    public void UnresolvedNumberContradictionRule_EmptyBoardTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/skyscrapers/rules/common/empty", skyscrapers);

        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        Assert.assertNotNull(RULE.checkContradiction((SkyscrapersBoard) transition.getBoard()));

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();
        for (int i = 0; i < board.getHeight(); i++) {
            Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(i, i)));
        }
    }

    //correct board, no cont
    @Test
    public void UnresolvedNumberContradictionRule_SolvedBoardTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/skyscrapers/rules/common/Solved", skyscrapers);

        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        Assert.assertNotNull(RULE.checkContradiction((SkyscrapersBoard) transition.getBoard()));

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();
        for (int i = 0; i < board.getHeight(); i++) {
            Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(i, i)));
        }
    }

    //invalid board, no cont
    @Test
    public void UnresolvedNumberContradictionRule_OtherContradictionTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/skyscrapers/rules/VisibilityContradictionRules/ImpliedAllContradiction", skyscrapers);

        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        Assert.assertNotNull(RULE.checkContradiction((SkyscrapersBoard) transition.getBoard()));

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();
        for (int i = 0; i < board.getHeight(); i++) {
            Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(i, i)));
        }
    }

    //3 in a row, 1 in col creates contradiction
    @Test
    public void UnresolvedNumberContradictionRule_RowContradictionTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/skyscrapers/rules/UnresolvedContradictionRules/3-1RowContradiction", skyscrapers);

        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        Assert.assertNull(RULE.checkContradiction((SkyscrapersBoard) transition.getBoard()));

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();
        for (int i = 0; i < board.getHeight(); i++) {
            if (i == 3) {
                Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(i, i)));
            }
            else {
                Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(i, i)));
            }
        }
    }

    //3 in a col, 1 in row creates contradiction
    @Test
    public void UnresolvedNumberContradictionRule_ColContradictionTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/skyscrapers/rules/UnresolvedContradictionRules/3-1ColContradiction", skyscrapers);

        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        Assert.assertNull(RULE.checkContradiction((SkyscrapersBoard) transition.getBoard()));

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();
        for (int i = 0; i < board.getHeight(); i++) {
            if (i == 1) {
                Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(i, i)));
            }
            else {
                Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(i, i)));
            }
        }
    }

    //2 in a row/col, 2 in other row/cols creates number contradiction
    @Test
    public void UnresolvedNumberContradictionRule_TwoContradictionTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/skyscrapers/rules/UnresolvedContradictionRules/2-2NumberContradiction", skyscrapers);

        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        Assert.assertNull(RULE.checkContradiction((SkyscrapersBoard) transition.getBoard()));

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();
        for (int i = 0; i < board.getHeight(); i++) {
            if (i == 1 || i == 3) {
                Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(i, i)));
            }
            else {
                Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(i, i)));
            }
        }
    }

    //1 in a row/col, 3 in other row/cols creates number contradiction
    @Test
    public void UnresolvedNumberContradictionRule_ThreeContradictionTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/skyscrapers/rules/UnresolvedContradictionRules/1-3NumberContradiction", skyscrapers);

        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        Assert.assertNull(RULE.checkContradiction((SkyscrapersBoard) transition.getBoard()));

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();
        for (int i = 0; i < board.getHeight(); i++) {
            if (i == 1 || i == 2) {
                Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(i, i)));
            }
            else {
                Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(i, i)));
            }
        }
    }
}