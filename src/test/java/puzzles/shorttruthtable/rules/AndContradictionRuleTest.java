package puzzles.shorttruthtable.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTable;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import edu.rpi.legup.puzzle.shorttruthtable.rules.contradiction.ContradictionRuleAnd;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class AndContradictionRuleTest {

    private static final ContradictionRuleAnd RULE = new ContradictionRuleAnd();
    private static ShortTruthTable stt;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        stt = new ShortTruthTable();
    }

    /**
     * Given a statement: A ^ B where ^ is true
     *
     * <p>Asserts that this is a valid application of the rule if and only if A or B are set to
     * false.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void trueAndTest() throws InvalidFileFormatException {
        String path = "puzzles/shorttruthtable/rules/AndContradictionRule/";
        String[] letters = {"T", "F", "U"};
        for (String first : letters) {
            for (String second : letters) {
                trueAndTestHelper(path + first + "T" + second);
            }
        }
    }

    private void trueAndTestHelper(String filePath) throws InvalidFileFormatException {
        TestUtilities.importTestBoard(filePath, stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell a = board.getCell(0, 0);
        ShortTruthTableCell b = board.getCell(2, 0);

        if (a.getType() == ShortTruthTableCellType.FALSE
                || b.getType() == ShortTruthTableCellType.FALSE) {
            Assert.assertNull(RULE.checkContradiction(transition.getBoard()));
        } else {
            Assert.assertNotNull(RULE.checkContradiction(transition.getBoard()));
        }
    }

    /**
     * Given a statement: A ^ B where ^ is false
     *
     * <p>Asserts that this is a valid application of the rule if and only if A or B (or both) are
     * set to true.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void falseAndTest() throws InvalidFileFormatException {
        String path = "puzzles/shorttruthtable/rules/AndContradictionRule/";
        String[] letters = {"T", "F", "U"};
        for (String first : letters) {
            for (String second : letters) {
                falseAndTestHelper(path + first + "F" + second);
            }
        }
    }

    private void falseAndTestHelper(String filePath) throws InvalidFileFormatException {
        TestUtilities.importTestBoard(filePath, stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell a = board.getCell(0, 0);
        ShortTruthTableCell b = board.getCell(2, 0);

        if (a.getType() == ShortTruthTableCellType.TRUE
                && b.getType() == ShortTruthTableCellType.TRUE) {
            Assert.assertNull(RULE.checkContradiction(transition.getBoard()));
        } else {
            Assert.assertNotNull(RULE.checkContradiction(transition.getBoard()));
        }
    }

    /**
     * Given a statement: A ^ B where ^ is unknown
     *
     * <p>Asserts that this is not a valid application of this rule.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void unknownAndTest() throws InvalidFileFormatException {
        // Getting the files that have or set to unknown from And Introduction
        String path = "puzzles/shorttruthtable/rules/AndIntroductionDirectRule/";
        String[] letters = {"T", "F", "U"};
        for (String first : letters) {
            for (String second : letters) {
                unknownAndTestHelper(path + first + "U" + second);
            }
        }
    }

    private void unknownAndTestHelper(String filePath) throws InvalidFileFormatException {
        TestUtilities.importTestBoard(filePath, stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        Assert.assertNotNull(RULE.checkContradiction(transition.getBoard()));
    }
}
