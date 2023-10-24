package puzzles.shorttruthtable.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTable;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import edu.rpi.legup.puzzle.shorttruthtable.rules.basic.elimination.DirectRuleBiconditionalElimination;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class BiconditionalEliminationTest {
    private static final DirectRuleBiconditionalElimination RULE = new DirectRuleBiconditionalElimination();
    private static ShortTruthTable stt;

    @BeforeClass
    public static void setup() {
        MockGameBoardFacade.getInstance();
        stt = new ShortTruthTable();
    }

    /**
     *
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void TrueBiconditionalTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/shorttruthtable/rules/BiconditionalEliminationDirectRule/TrueBiconditionalWithTrueA", stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell morty = board.getCell(2, 0);

        // Asserts that this is a valid application of the rule when B is true
        morty.setData(ShortTruthTableCellType.TRUE);
        board.addModifiedData(morty);
        Assert.assertNull(RULE.checkRule(transition));

        // Asserts that this is not a valid application of the rule when B is false.
        morty.setData(ShortTruthTableCellType.FALSE);
        board.addModifiedData(morty);
        Assert.assertNotNull(RULE.checkRule(transition));
    }
}