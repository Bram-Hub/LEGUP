package puzzles.shorttruthtable.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTable;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import edu.rpi.legup.puzzle.shorttruthtable.rules.caserule.CaseRuleAtomic;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

public class TrueOrFalseCaseRuleTest {

    private static final CaseRuleAtomic RULE = new CaseRuleAtomic();
    private static ShortTruthTable stt;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        stt = new ShortTruthTable();
    }

    /**
     * Tests the True or False case rule by ensuring that it results in two children,
     * one that contains Statement as true and one that contains Statement as false.
     */
    @Test
    public void TwoBranchesTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/shorttruthtable/rules/TrueOrFalseCaseRule/Statement", stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell cell = board.getCell(0,0);
        ArrayList<Board> cases = RULE.getCases(board, cell);

        // Make sure that the rule checks out
        Assert.assertNotNull(RULE.checkRule(transition));

        // Make sure there are two branches
        Assert.assertEquals(2, cases.size());

        ShortTruthTableBoard caseBoard1 = (ShortTruthTableBoard) cases.get(0);
        ShortTruthTableBoard caseBoard2 = (ShortTruthTableBoard) cases.get(1);

        ShortTruthTableCellType cellType1 = caseBoard1.getCell(0,0).getType();
        ShortTruthTableCellType cellType2 = caseBoard2.getCell(0,0).getType();

        // First assert the two cells are not equal, then verify that they are true
        // or false.
        Assert.assertNotEquals(cellType1, cellType2);
        Assert.assertTrue(cellType1.equals(ShortTruthTableCellType.TRUE) || cellType1.equals(ShortTruthTableCellType.FALSE));
        Assert.assertTrue(cellType2.equals(ShortTruthTableCellType.TRUE) || cellType2.equals(ShortTruthTableCellType.FALSE));

        // Verify the board dimensions are unchanged
        Assert.assertEquals(caseBoard1.getHeight(), caseBoard2.getHeight(), board.getHeight());
        Assert.assertEquals(caseBoard1.getWidth(), caseBoard2.getWidth(), board.getWidth());

        // Verify that everywhere else on the board is unchanged
        for (int i = 0; i< caseBoard1.getWidth(); i++) {
            for (int j = 0; j < caseBoard1.getHeight(); j++) {
                // Make sure not to check the one cell that should be different
                if (i != 0 && j != 0) {
                    Assert.assertEquals(caseBoard1.getCell(i, j).getType(), caseBoard2.getCell(i, j).getType());
                }
            }
        }
    }
}