package puzzles.shorttruthtable.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTable;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import edu.rpi.legup.puzzle.shorttruthtable.rules.caserule.CaseRuleBiconditional;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

public class BiconditionalCaseRuleTest {

    private static final CaseRuleBiconditional RULE = new CaseRuleBiconditional();
    private static ShortTruthTable stt;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        stt = new ShortTruthTable();
    }

    private void trueBiconditionalTest(String fileName,
                                     int biconditionalX, int biconditionalY,
                                     int aX, int aY,
                                     int bX, int bY) throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/shorttruthtable/rules/BiconditionalCaseRule/" + fileName, stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell cell = board.getCell(biconditionalX, biconditionalY);
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

        // Assert that A and B are equal and either true or false in both branches
        Assert.assertEquals(board1A, board1B);
        Assert.assertTrue(
                (board1A.equals(ShortTruthTableCellType.TRUE) && board1B.equals(ShortTruthTableCellType.TRUE)) || (board1A.equals(ShortTruthTableCellType.FALSE) && board1B.equals(ShortTruthTableCellType.FALSE))
        );

        Assert.assertNotEquals(board1B, board2B);
        Assert.assertTrue(
                (board2A.equals(ShortTruthTableCellType.TRUE) && board2B.equals(ShortTruthTableCellType.TRUE)) || (board2A.equals(ShortTruthTableCellType.FALSE) && board2B.equals(ShortTruthTableCellType.FALSE))
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
        trueBiconditionalTest("TrueBiconditional", 1, 0, 0, 0, 2, 0);
    }

    /**
     * Given a statement ~(A|B) -> (C^D) where the -> is true, tests this case rule
     * by ensuring that two branches are created: one where ~ is false and one where
     * ^ is true.
     */
    @Test
    public void ComplexStatement1TrueTest() throws InvalidFileFormatException {
        trueBiconditionalTest("ComplexStatement1_True", 6, 0, 0, 0,
                9, 0);
    }

    private void falseBiconditionalTest(String fileName,
                                      int biconditionalX, int biconditionalY,
                                      int aX, int aY,
                                      int bX, int bY) throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/shorttruthtable/rules/BiconditionalCaseRule/" + fileName, stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell cell = board.getCell(biconditionalX, biconditionalY);
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

        // Assert that A and B are not equal and are both either true or false in both branches
        Assert.assertNotEquals(board1A, board1B);
        Assert.assertTrue(
                (board1A.equals(ShortTruthTableCellType.TRUE) && board1B.equals(ShortTruthTableCellType.FALSE)) || (board1A.equals(ShortTruthTableCellType.FALSE) && board1B.equals(ShortTruthTableCellType.TRUE))
        );

        Assert.assertNotEquals(board2A, board2B);
        Assert.assertTrue(
                (board2A.equals(ShortTruthTableCellType.TRUE) && board2B.equals(ShortTruthTableCellType.FALSE)) || (board2A.equals(ShortTruthTableCellType.FALSE) && board2B.equals(ShortTruthTableCellType.TRUE))
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
     * Given a statement A -> B where -> is false, tests this case rule by ensuring that
     * one branch is created where A is true and B is false.
     */
    @Test
    public void SimpleStatement1FalseTest() throws InvalidFileFormatException {
        falseBiconditionalTest("FalseBiconditional", 1, 0, 0, 0,
                2, 0);
    }

    /**
     * Given a statement ~(A|B) -> (C^D) where -> is true, tests this case rule
     * by ensuring that one branch is created where ~ is true and ^ is false.
     */
    @Test
    public void ComplexStatement1FalseTest() throws InvalidFileFormatException {
        falseBiconditionalTest("ComplexStatement1_False", 6, 0, 0, 0,
                9, 0);
    }
}