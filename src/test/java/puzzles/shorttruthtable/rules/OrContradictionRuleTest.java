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
     * Given a statement: A V B where V is true
     *
     * Asserts that this is a valid application of the rule if
     * and only if both A and B are set to false.
     *
     * @param filePath The file path for test board setup.
     * @throws InvalidFileFormatException
     */
    @Test
    public void trueOrTest() throws InvalidFileFormatException {
        String path = "puzzles/shorttruthtable/rules/OrContradictionRule/";
        String[] letters = {"T", "F", "U"};
        for (String first : letters) {
            for (String second : letters) {
                trueOrTestHelper(path + first + "T" + second);
            }
        }
    }

    private void trueOrTestHelper(String filePath) throws InvalidFileFormatException {
        TestUtilities.importTestBoard(filePath, stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell a = board.getCell(0, 0);
        ShortTruthTableCell b = board.getCell(2, 0);

        if (a.getType() == ShortTruthTableCellType.FALSE && b.getType() == ShortTruthTableCellType.FALSE) {
            Assert.assertNull(RULE.checkContradiction(transition.getBoard()));
        }
        else {
            Assert.assertNotNull(RULE.checkContradiction(transition.getBoard()));
        }
    }

    /**
     * Given a statement: A V B where V is false
     *
     * Asserts that this is a valid application of the rule if
     * and only if A or B is set to true or both A and B are set
     * to true.
     *
     * @param filePath The file path for test board setup.
     * @throws InvalidFileFormatException
     */
    @Test
    public void falseOrTest() throws InvalidFileFormatException {
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

        if (a.getType() == ShortTruthTableCellType.TRUE || b.getType() == ShortTruthTableCellType.TRUE) {
            Assert.assertNull(RULE.checkContradiction(transition.getBoard()));
        }
        else {
            Assert.assertNotNull(RULE.checkContradiction(transition.getBoard()));
        }
    }

    /**
     * Given a statement: A V B where V is unknown
     *
     * Asserts that this is not a valid application of this rule.
     *
     * @param filePath The file path for test board setup.
     * @throws InvalidFileFormatException
     */
    @Test
    public void unknownOrTest() throws InvalidFileFormatException {
        // Getting the files that have or set to unknown from Or Introduction
        String path = "puzzles/shorttruthtable/rules/OrIntroductionDirectRule/";
        String[] letters = {"T", "F", "U"};
        for (String first : letters) {
            for (String second : letters) {
                unknownOrTestHelper(path + first + "U" + second);
            }
        }
    }

    private void unknownOrTestHelper(String filePath) throws InvalidFileFormatException {
        TestUtilities.importTestBoard(filePath, stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        Assert.assertNotNull(RULE.checkContradiction(transition.getBoard()));
    }
}
