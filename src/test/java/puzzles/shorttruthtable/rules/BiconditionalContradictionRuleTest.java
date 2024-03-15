package puzzles.shorttruthtable.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTable;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import edu.rpi.legup.puzzle.shorttruthtable.rules.contradiction.ContradictionRuleBiconditional;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class BiconditionalContradictionRuleTest {

    private static final ContradictionRuleBiconditional RULE = new ContradictionRuleBiconditional();
    private static ShortTruthTable stt;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        stt = new ShortTruthTable();
    }

    /**
     * Given a statement: A <-> B where <-> is true
     *
     * Asserts that this is a valid application of the rule if
     * and only if A and B are not unknown and A does not equal B
     *
     * @param filePath The file path for test board setup.
     * @throws InvalidFileFormatException
     */
    @Test
    public void trueBiconditionalTest() throws InvalidFileFormatException {
        String path = "puzzles/shorttruthtable/rules/BiconditionalContradictionRule/";
        String[] letters = {"T", "F", "U"};
        for (String first : letters) {
            for (String second : letters) {
                trueBiconditionalTestHelper(path + first + "T" + second);
            }
        }
    }

    private void trueBiconditionalTestHelper(String filePath) throws InvalidFileFormatException {
        TestUtilities.importTestBoard(filePath, stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell a = board.getCell(0, 0);
        ShortTruthTableCell b = board.getCell(2, 0);

        if (a.getType() != ShortTruthTableCellType.UNKNOWN && b.getType() != ShortTruthTableCellType.UNKNOWN && a.getType() != b.getType()) {
            Assert.assertNull(RULE.checkContradiction(transition.getBoard()));
        }
        else {
            Assert.assertNotNull(RULE.checkContradiction(transition.getBoard()));
        }
    }

    /**
     * Given a statement: A <-> B where <-> is false
     *
     * Asserts that this is a valid application of the rule if
     * and only if A and B are not unknown and A equals B
     *
     * @param filePath The file path for test board setup.
     * @throws InvalidFileFormatException
     */
    @Test
    public void falseConditionalTest() throws InvalidFileFormatException {
        String path = "puzzles/shorttruthtable/rules/BiconditionalContradictionRule/";
        String[] letters = {"T", "F", "U"};
        for (String first : letters) {
            for (String second : letters) {
                falseBiconditionalTestHelper(path + first + "F" + second);
            }
        }
    }

    private void falseBiconditionalTestHelper(String filePath) throws InvalidFileFormatException {
        TestUtilities.importTestBoard(filePath, stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell a = board.getCell(0, 0);
        ShortTruthTableCell b = board.getCell(2, 0);

        if (a.getType() != ShortTruthTableCellType.UNKNOWN && b.getType() != ShortTruthTableCellType.UNKNOWN && a.getType() == b.getType()) {
            Assert.assertNull(RULE.checkContradiction(transition.getBoard()));
        }
        else {
            Assert.assertNotNull(RULE.checkContradiction(transition.getBoard()));
        }
    }

    /**
     * Given a statement: A <-> B where <-> is unknown
     *
     * Asserts that this is not a valid application of this rule.
     *
     * @param filePath The file path for test board setup.
     * @throws InvalidFileFormatException
     */
    @Test
    public void unknownBiconditionalTest() throws InvalidFileFormatException {
        // Getting the files that have or set to unknown from Biconditional Introduction
        String path = "puzzles/shorttruthtable/rules/BiconditionalIntroductionDirectRule/";
        String[] letters = {"T", "F", "U"};
        for (String first : letters) {
            for (String second : letters) {
                unknownBiconditionalTestHelper(path + first + "U" + second);
            }
        }
    }

    private void unknownBiconditionalTestHelper(String filePath) throws InvalidFileFormatException {
        TestUtilities.importTestBoard(filePath, stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        Assert.assertNotNull(RULE.checkContradiction(transition.getBoard()));
    }
}
