package puzzles.shorttruthtable.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTable;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import edu.rpi.legup.puzzle.shorttruthtable.rules.contradiction.ContradictionRuleAtomic;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class AtomicContradictionRuleTest {

    private static final ContradictionRuleAtomic RULE = new ContradictionRuleAtomic();
    private static ShortTruthTable stt;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        stt = new ShortTruthTable();
    }

    /**
     * Given two statements: A, A
     *
     * Asserts that this is a valid application of the rule if
     * and only if both A and B are not unknown and different.
     *
     * @param filePath The file path for test board setup.
     * @throws InvalidFileFormatException
     */
    @Test
    public void mismatchTest() throws InvalidFileFormatException {
        String path = "puzzles/shorttruthtable/rules/AtomicContradictionRule/";
        String[] letters = {"T", "F", "U"};
        for (String first : letters) {
            for (String second : letters) {
                mismatchHelper(path + first + second);
            }
        }
    }

    private void mismatchHelper(String filePath) throws InvalidFileFormatException {
        TestUtilities.importTestBoard(filePath, stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell a = board.getCell(0, 0);
        ShortTruthTableCell b = board.getCell(0, 2);

        if (a.getType() != ShortTruthTableCellType.UNKNOWN && b.getType() != ShortTruthTableCellType.UNKNOWN && a.getType() != b.getType()) {
            Assert.assertNull(RULE.checkContradiction(transition.getBoard()));
        }
        else {
            Assert.assertNotNull(RULE.checkContradiction(transition.getBoard()));
        }
    }
}
