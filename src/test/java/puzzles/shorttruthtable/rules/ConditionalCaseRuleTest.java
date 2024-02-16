package puzzles.shorttruthtable.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTable;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import edu.rpi.legup.puzzle.shorttruthtable.rules.caserule.CaseRuleConditional;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

public class ConditionalCaseRuleTest {

    private static final CaseRuleConditional RULE = new CaseRuleConditional();
    private static ShortTruthTable stt;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        stt = new ShortTruthTable();
    }

    private void trueConditionalTest(String fileName,
                                     int conditionalX, int conditionalY,
                                     int aX, int aY,
                                     int bX, int bY) throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/shorttruthtable/rules/ConditionalCaseRule/" + fileName, stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell cell = board.getCell(conditionalX, conditionalY);
        ArrayList<Board> cases = RULE.getCases(board, cell);

        // Make sure that the rule checks out
        Assert.assertNull(RULE.checkRule(transition));

        // Make sure there are two branches
        Assert.assertEquals(2, cases.size());

        ShortTruthTableBoard caseBoard1 = (ShortTruthTableBoard) cases.get(0);
        ShortTruthTableCellType board1A = caseBoard1.getCell(aX, aY).getType();
        ShortTruthTableCellType board1B = caseBoard1.getCell(bX, bY).getType();

        ShortTruthTableBoard caseBoard2 = (ShortTruthTableBoard) cases.get(1);
        ShortTruthTableCellType board2A = caseBoard2.getCell(aX, aY).getType();
        ShortTruthTableCellType board2B = caseBoard2.getCell(bX, bY).getType();

        // Assert that the corresponding cells for the different case rules do not
        // match with each other
        Assert.assertNotEquals(board1A, board2A);
        Assert.assertNotEquals(board1B, board2B);

        // Assert that A is unknown in one board and false in the other
        Assert.assertNotEquals(board1A, board2A);
        Assert.assertTrue(
                (board1A.equals(ShortTruthTableCellType.UNKNOWN) && board2A.equals(ShortTruthTableCellType.FALSE))
                || (board1A.equals(ShortTruthTableCellType.FALSE) && board2A.equals(ShortTruthTableCellType.UNKNOWN))
        );

        // Assert that B is unknown in one board and true in the other
        Assert.assertNotEquals(board1B, board2B);
        Assert.assertTrue(
                (board1B.equals(ShortTruthTableCellType.UNKNOWN) && board2B.equals(ShortTruthTableCellType.TRUE))
                || (board1B.equals(ShortTruthTableCellType.TRUE) && board2B.equals(ShortTruthTableCellType.UNKNOWN))
        );

        // Verify the board dimensions are unchanged
        Assert.assertEquals(caseBoard1.getHeight(), caseBoard2.getHeight(), board.getHeight());
        Assert.assertEquals(caseBoard1.getWidth(), caseBoard2.getWidth(), board.getWidth());

        // Verify that everywhere else on the board is unchanged
        for (int i = 0; i< caseBoard1.getWidth(); i++) {
            for (int j = 0; j < caseBoard1.getHeight(); j++) {
                // Make sure not to check the two cells that should be different
                if (!((i == aX && j == aY) || (i == bX && j == bY))) {
                    Assert.assertEquals(caseBoard1.getCell(i, j).getType(), caseBoard2.getCell(i, j).getType());
                }
            }
        }
    }

    /**
     * Given a statement A -> B where ^ is true, tests this case rule by ensuring that
     * two branches are created: one where A is false and one where B is true.
     */
    @Test
    public void SimpleStatement1TrueTest() throws InvalidFileFormatException {
        trueConditionalTest("TrueConditional", 1, 0, 0, 0,
                2, 0);
    }

    /**
     * Given a statement ~(A|B) -> (C^D) where the -> is true, tests this case rule
     * by ensuring that two branches are created: one where ~ is false and one where
     * ^ is true.
     */
    @Test
    public void ComplexStatement1TrueTest() throws InvalidFileFormatException {
        trueConditionalTest("ComplexStatement1_True", 6, 0, 0, 0,
                9, 0);
    }

    private void falseConditionalTest(String fileName,
                                      int conditionalX, int conditionalY,
                                      int aX, int aY,
                                      int bX, int bY) throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/shorttruthtable/rules/ConditionalCaseRule/" + fileName, stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell cell = board.getCell(conditionalX, conditionalY);
        ArrayList<Board> cases = RULE.getCases(board, cell);

        // Make sure that the rule checks out
        Assert.assertNull(RULE.checkRule(transition));

        // There should only be 1 branch
        Assert.assertEquals(1, cases.size());

        ShortTruthTableBoard caseBoard = (ShortTruthTableBoard) cases.get(0);
        ShortTruthTableCellType caseBoardAType = caseBoard.getCell(aX, aY).getType();
        ShortTruthTableCellType caseBoardBType = caseBoard.getCell(bX, bY).getType();

        // A should be true and B should be false
        Assert.assertEquals(caseBoardAType, ShortTruthTableCellType.TRUE);
        Assert.assertEquals(caseBoardBType, ShortTruthTableCellType.FALSE);

        // Verify the board dimensions are unchanged
        Assert.assertEquals(caseBoard.getHeight(), caseBoard.getHeight(), board.getHeight());
    }

    /**
     * Given a statement A -> B where -> is false, tests this case rule by ensuring that
     * one branch is created where A is true and B is false.
     */
    @Test
    public void SimpleStatement1FalseTest() throws InvalidFileFormatException {
        falseConditionalTest("FalseConditional", 1, 0, 0, 0,
                2, 0);
    }

    /**
     * Given a statement ~(A|B) -> (C^D) where -> is true, tests this case rule
     * by ensuring that one branch is created where ~ is true and ^ is false.
     */
    @Test
    public void ComplexStatement1FalseTest() throws InvalidFileFormatException {
        falseConditionalTest("ComplexStatement1_False", 6, 0, 0, 0,
                9, 0);
    }
}