package puzzles.shorttruthtable.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTable;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import edu.rpi.legup.puzzle.shorttruthtable.rules.contradiction.ContradictionRuleNot;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class NotContradictionRuleTest {

    private static final ContradictionRuleNot RULE = new ContradictionRuleNot();
    private static ShortTruthTable stt;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        stt = new ShortTruthTable();
    }

    /**
     * Given a statement: ¬A
     *
     * Asserts that this is a valid application of the rule if
     * and only if A and B are both true or are both false.
     *
     * @param filePath The file path for test board setup.
     * @throws InvalidFileFormatException
     */
    @Test
    public void notContradictionTest() throws InvalidFileFormatException {
        String path = "puzzles/shorttruthtable/rules/NotContradictionRule/";
        String[] letters = {"T", "F", "U"};
        for (String first : letters) {
            for (String second : letters) {
                notContradictionTestHelper(path + first + second);
            }
        }
    }

    private void notContradictionTestHelper(String filePath) throws InvalidFileFormatException {
        TestUtilities.importTestBoard(filePath, stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell not = board.getCell(0, 0);
        ShortTruthTableCell a = board.getCell(1, 0);

        System.out.println(not + " " + a);
        if ((not.getType() == ShortTruthTableCellType.TRUE && a.getType() == ShortTruthTableCellType.TRUE) || (not.getType() == ShortTruthTableCellType.FALSE && a.getType() == ShortTruthTableCellType.FALSE)) {
            Assert.assertNull(RULE.checkContradiction(transition.getBoard()));
        }
        else {
            Assert.assertNotNull(RULE.checkContradiction(transition.getBoard()));
        }
    }
}
