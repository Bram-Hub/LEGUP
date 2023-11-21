package puzzles.shorttruthtable.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTable;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import edu.rpi.legup.puzzle.shorttruthtable.rules.basic.introduction.DirectRuleBiconditionalIntroduction;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class BiconditionalIntroductionTest {
    private static final DirectRuleBiconditionalIntroduction RULE = new DirectRuleBiconditionalIntroduction();
    private static ShortTruthTable stt;

    @BeforeClass
    public static void setup() {
        MockGameBoardFacade.getInstance();
        stt = new ShortTruthTable();
    }

    /**
     * Given a statement: A <-> B
     *
     * Asserts that if setting <-> to false is a valid application of this rule if and
     * only if A and B do not match.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void FalseConditionalTest() throws InvalidFileFormatException {
        String path = "puzzles/shorttruthtable/rules/BiconditionalIntroductionDirectRule/";

        String[] letters = {"T", "F", "U"};
        for (String a : letters) {
            for (String b : letters) {
                System.out.println(a + b);
                falseConditionalHelper(path + a + "U" + b);
            }
        }
    }

    private void falseConditionalHelper(String filePath) throws InvalidFileFormatException {
        TestUtilities.importTestBoard(filePath, stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell conditional = board.getCell(1, 0);

        conditional.setData(ShortTruthTableCellType.FALSE);
        board.addModifiedData(conditional);

        ShortTruthTableCell a = board.getCell(0, 0);
        ShortTruthTableCell b = board.getCell(2, 0);
        if (a.getType() != b.getType()) {
            // Not valid if they don't match but at least one of the values of A or B is unknown
            if (a.getType() == ShortTruthTableCellType.UNKNOWN || b.getType() == ShortTruthTableCellType.UNKNOWN) {
                Assert.assertNotNull(RULE.checkRule(transition));
            }
            else {
                Assert.assertNull(RULE.checkRule(transition));
            }
        }
        else {
            Assert.assertNotNull(RULE.checkRule(transition));
        }
    }

    /**
     * Given a statement: A <-> B
     *
     * Asserts that if setting <-> to true is a valid application of this rule if and
     * only if A and B match.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void TrueConditionalTest() throws InvalidFileFormatException {
        String path = "puzzles/shorttruthtable/rules/BiconditionalIntroductionDirectRule/";

        String[] letters = {"T", "F", "U"};
        for (String a : letters) {
            for (String b : letters) {
                System.out.println(a + b);
                trueConditionalHelper(path + a + "U" + b);
            }
        }
    }

    private void trueConditionalHelper(String filePath) throws InvalidFileFormatException {
        TestUtilities.importTestBoard(filePath, stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell conditional = board.getCell(1, 0);

        conditional.setData(ShortTruthTableCellType.TRUE);
        board.addModifiedData(conditional);

        ShortTruthTableCell a = board.getCell(0, 0);
        ShortTruthTableCell b = board.getCell(2, 0);
        if (a.getType() == b.getType() && a.getType() != ShortTruthTableCellType.UNKNOWN) {
            Assert.assertNull(RULE.checkRule(transition));
        }
        else {
            Assert.assertNotNull(RULE.checkRule(transition));
        }
    }
}