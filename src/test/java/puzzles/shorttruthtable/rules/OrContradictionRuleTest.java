package puzzles.shorttruthtable.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTable;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import edu.rpi.legup.puzzle.shorttruthtable.rules.contradiction.ContradictionRuleOr;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class OrContradictionRuleTest {

    private static final ContradictionRuleOr RULE = new ContradictionRuleOr();
    private static ShortTruthTable stt;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        stt = new ShortTruthTable();
    }

    /**
     * Given a statement: A V B where V is false
     *
     * Asserts that setting  is a valid application of the rule if
     * A or B is set to true or both A and B are set to true.
     *
     * @param filePath The file path for test board setup.
     * @throws InvalidFileFormatException
     */
    @Test
    public void FalseOrTest() throws InvalidFileFormatException {
        String path = "puzzles/shorttruthtable/rules/OrContradictionRule/";
        String[] letters = {"T", "F", "U"};
        for (String first : letters) {
            for (String second : letters) {
                falseOrTestHelper(path + first + "F" + second);
            }
        }
    }

    private void falseOrTestHelper(String filePath) throws InvalidFileFormatException {
        TestUtilities.importTestBoard(filePath, stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell a = board.getCell(0, 0);
        ShortTruthTableCell b = board.getCell(2, 0);
        ShortTruthTableCell or = board.getCell(1, 0);

        or.setData(ShortTruthTableCellType.FALSE);
        board.addModifiedData(or);
        
        if (a.getType() == ShortTruthTableCellType.TRUE || b.getType() == ShortTruthTableCellType.TRUE) {
            Assert.assertNull(RULE.checkContradiction(transition.getBoard()));
        }
        else {
            Assert.assertNotNull(RULE.checkContradiction(transition.getBoard()));
        }
    }
}
