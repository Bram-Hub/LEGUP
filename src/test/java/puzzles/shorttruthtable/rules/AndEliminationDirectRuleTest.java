package puzzles.shorttruthtable.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTable;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import edu.rpi.legup.puzzle.shorttruthtable.rules.basic.DirectRuleAtomic;
import edu.rpi.legup.puzzle.shorttruthtable.rules.basic.elimination.DirectRuleAndElimination;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class AndEliminationDirectRuleTest {
    private static final DirectRuleAndElimination RULE = new DirectRuleAndElimination();
    private static ShortTruthTable stt;

    @BeforeClass
    public static void setup() {
        MockGameBoardFacade.getInstance();
        stt = new ShortTruthTable();
    }

    /**
     * Given one statement: B^C
     *
     * This test first sets B to true, then asserts that this is a valid application
     * of the rule. Then, the test sets C to true, then asserts that this is a valid
     * application of the rule.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void trueAndTest1() throws InvalidFileFormatException {
       TestUtilities.importTestBoard("puzzles/shorttruthtable/rules/AndEliminationDirectRule/TrueAnd", stt);
       TreeNode rootNode = stt.getTree().getRootNode();
       TreeTransition transition = rootNode.getChildren().get(0);
       transition.setRule(RULE);

       ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();

       ShortTruthTableCell bonnie = board.getCell(0, 0);
       bonnie.setData(ShortTruthTableCellType.TRUE);
       board.addModifiedData(bonnie);
       Assert.assertNull(RULE.checkRule(transition));

       ShortTruthTableCell clyde = board.getCell(2, 0);
       clyde.setData(ShortTruthTableCellType.TRUE);
       board.addModifiedData(clyde);
       Assert.assertNull(RULE.checkRule(transition));
    }
}