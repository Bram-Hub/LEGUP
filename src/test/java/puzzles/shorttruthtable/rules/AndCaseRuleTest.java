package puzzles.shorttruthtable.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTable;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import edu.rpi.legup.puzzle.shorttruthtable.rules.caserule.CaseRuleAnd;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

public class AndCaseRuleTest {

    private static final CaseRuleAnd RULE = new CaseRuleAnd();
    private static ShortTruthTable stt;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        stt = new ShortTruthTable();
    }

    private void falseAndTest(String fileName,
                              int andX, int andY,
                              int aX, int aY,
                              int bX, int bY) throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/shorttruthtable/rules/AndCaseRule/" + fileName, stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell cell = board.getCell(andX,andY);
        ArrayList<Board> cases = RULE.getCases(board, cell);

        // Make sure that the rule checks out
        Assert.assertNotNull(RULE.checkRule(transition));

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

        // First assert the two cells are not equal, then verify that they are either
        // unknown or false.
        Assert.assertNotEquals(board1A, board1B);
        Assert.assertTrue(board1A.equals(ShortTruthTableCellType.UNKNOWN) || board1A.equals(ShortTruthTableCellType.FALSE));
        Assert.assertTrue(board1B.equals(ShortTruthTableCellType.UNKNOWN) || board1B.equals(ShortTruthTableCellType.FALSE));

        Assert.assertNotEquals(board2A, board2B);
        Assert.assertTrue(board2A.equals(ShortTruthTableCellType.UNKNOWN) || board1A.equals(ShortTruthTableCellType.FALSE));
        Assert.assertTrue(board2B.equals(ShortTruthTableCellType.UNKNOWN) || board2B.equals(ShortTruthTableCellType.FALSE));

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
     * Given a statement A ^ B where ^ is false, tests this case rule by ensuring that
     * two branches are created: one where A is false and one where B is false.
     */
    @Test
    public void SimpleStatement1FalseTest() throws InvalidFileFormatException {
        falseAndTest("SimpleStatement1_False", 1, 0, 0, 0,
                2, 0);
    }

    /**
     * Given a statement ~(A|B)^(C^D) where the first ^ is false, tests this case rule
     * by ensuring that two branches are created: one where ~ is false and one where
     * the second ^ is false.
     */
    @Test
    public void ComplexStatement1FalseTest() throws InvalidFileFormatException {
        falseAndTest("ComplexStatement1_False", 6, 0, 0, 0,
                9, 0);
    }

    private void trueAndTest(String fileName,
                             int andX, int andY,
                             int aX, int aY,
                             int bX, int bY) throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/shorttruthtable/rules/AndCaseRule/" + fileName, stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell cell = board.getCell(andX,andY);
        ArrayList<Board> cases = RULE.getCases(board, cell);

        // Make sure that the rule checks out
        Assert.assertNotNull(RULE.checkRule(transition));

        // There should only be 1 branch
        Assert.assertEquals(1, cases.size());

        ShortTruthTableBoard caseBoard = (ShortTruthTableBoard) cases.get(0);
        ShortTruthTableCellType caseBoardAType = caseBoard.getCell(aX, aY).getType();
        ShortTruthTableCellType caseBoardBType = caseBoard.getCell(bX, bY).getType();

        // Both cells should be true
        Assert.assertEquals(caseBoardAType, ShortTruthTableCellType.TRUE);
        Assert.assertEquals(caseBoardBType, ShortTruthTableCellType.TRUE);
        Assert.assertEquals(caseBoardAType, caseBoardBType);

        // Verify the board dimensions are unchanged
        Assert.assertEquals(caseBoard.getHeight(), caseBoard.getHeight(), board.getHeight());
    }

    /**
     * Given a statement A ^ B where ^ is false, tests this case rule by ensuring that
     * one branch is created where A and B are both true.
     */
    @Test
    public void SimpleStatement1AndTest() throws InvalidFileFormatException {
        trueAndTest("SimpleStatement1_True", 1, 0, 0, 0,
                2, 0);
    }

    /**
     * Given a statement ~(A|B)^(C^D) where the first ^ is true, tests this case rule
     * by ensuring that one branch is created where both ~ and the second ^ are true.
     */
    @Test
    public void ComplexStatement1TrueTest() throws InvalidFileFormatException {
        trueAndTest("ComplexStatement1_True", 6, 0, 0, 0,
                9, 0);
    }
}