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

    /**
     * Given a statement A ^ B where ^ is false, tests this case rule by ensuring that
     * two branches are created: one where A is false and one where B is false.
     */
    @Test
    public void SimpleStatement1FalseTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/shorttruthtable/rules/AndCaseRule/SimpleStatement1_False", stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell cell = board.getCell(1,0);
        ArrayList<Board> cases = RULE.getCases(board, cell);

        Assert.assertEquals(2, cases.size());

        ShortTruthTableBoard caseBoard1 = (ShortTruthTableBoard) cases.get(0);
        ShortTruthTableCellType board1A = caseBoard1.getCell(0, 0).getType();
        ShortTruthTableCellType board1B = caseBoard1.getCell(2, 0).getType();

        ShortTruthTableBoard caseBoard2 = (ShortTruthTableBoard) cases.get(1);
        ShortTruthTableCellType board2A = caseBoard2.getCell(0, 0).getType();
        ShortTruthTableCellType board2B = caseBoard2.getCell(2, 0).getType();

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

        // Verify that everywhere else on the board is unchanged, which, in this case,
        // is just the and cell
        ShortTruthTableCellType board1And = caseBoard1.getCell(1, 0).getType();
        ShortTruthTableCellType board2And = caseBoard1.getCell(1, 0).getType();
        Assert.assertEquals(board1And, board2And);
    }
}