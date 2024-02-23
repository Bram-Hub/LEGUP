package puzzles.skyscrapers.rules;


import edu.rpi.legup.puzzle.skyscrapers.Skyscrapers;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersBoard;
import edu.rpi.legup.puzzle.skyscrapers.rules.DuplicateNumberContradictionRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DuplicateNumberContradictionTest {

    private static final DuplicateNumberContradictionRule RULE = new DuplicateNumberContradictionRule();
    private static Skyscrapers skyscrapers;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        skyscrapers = new Skyscrapers();
    }

    //empty
    @Test
    public void DuplicateNumberContradictionRule_EmptyBoardTest() throws InvalidFileFormatException {
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

    //correct board, no cont
    @Test
    public void DuplicateNumberContradictionRule_SolvedBoardTest() throws InvalidFileFormatException {
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

    //invalid board, no cont
    @Test
    public void DuplicateNumberContradictionRule_OtherContradictionTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/skyscrapers/rules/VisibilityContradictionRules/FullRowContradiction", skyscrapers);

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

    //on row
    @Test
    public void DuplicateNumberContradictionRule_RowContradictionTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/skyscrapers/rules/DuplicateNumberContradictionRule/RowContradiction", skyscrapers);

        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        Assert.assertNull(RULE.checkContradiction((SkyscrapersBoard) transition.getBoard()));

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                if((k==0 || k==1) && i==0){
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
                else{
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    //on col
    @Test
    public void DuplicateNumberContradictionRule_ColContradictionTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/skyscrapers/rules/DuplicateNumberContradictionRule/ColContradiction", skyscrapers);

        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        Assert.assertNull(RULE.checkContradiction((SkyscrapersBoard) transition.getBoard()));

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                if(k==0 && (i==0 || i==1)){
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
                else{
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    //multitudes
    @Test
    public void DuplicateNumberContradictionRule_AllContradictionTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/skyscrapers/rules/DuplicateNumberContradictionRule/AllContradiction", skyscrapers);

        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        Assert.assertNull(RULE.checkContradiction((SkyscrapersBoard) transition.getBoard()));

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
            }
        }
    }
}