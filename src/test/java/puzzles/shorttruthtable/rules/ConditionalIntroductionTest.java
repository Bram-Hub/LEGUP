package puzzles.shorttruthtable.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTable;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import edu.rpi.legup.puzzle.shorttruthtable.rules.basic.introduction.DirectRuleConditionalIntroduction;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConditionalIntroductionTest {
    private static final DirectRuleConditionalIntroduction RULE = new DirectRuleConditionalIntroduction();
    private static ShortTruthTable stt;

    @BeforeClass
    public static void setup() {
        MockGameBoardFacade.getInstance();
        stt = new ShortTruthTable();
    }

    /**
     * Given a statement: A -> B
     *
     * Asserts that if setting -> to false is a valid application of this rule if and
     * only if A is true and B is false.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void FalseConditionalTest() throws InvalidFileFormatException {
        String path = "puzzles/shorttruthtable/rules/ConditionalIntroductionDirectRule/";

        String[] letters = {"T", "F", "U"};
        for (String a : letters) {
            for (String b : letters) {
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
        if (a.getType() == ShortTruthTableCellType.TRUE && b.getType() == ShortTruthTableCellType.FALSE) {
            Assert.assertNull(RULE.checkRule(transition));
        }
        else {
            Assert.assertNotNull(RULE.checkRule(transition));
        }
    }

    /**
     * Given a statement: A -> B
     *
     * Asserts that if setting -> to true is a valid application of this rule if and
     * only if A is false or B is true.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void TrueConditionalTest() throws InvalidFileFormatException {
        String path = "puzzles/shorttruthtable/rules/ConditionalIntroductionDirectRule/";

        String[] letters = {"T", "F", "U"};
        for (String a : letters) {
            for (String b : letters) {
                trueConditionalTestHelper(path + a + "U" + b);
            }
        }
    }

    private void trueConditionalTestHelper(String filePath) throws InvalidFileFormatException {
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
        if (a.getType() == ShortTruthTableCellType.FALSE || b.getType() == ShortTruthTableCellType.TRUE) {
            Assert.assertNull(RULE.checkRule(transition));
        }
        else {
            Assert.assertNotNull(RULE.checkRule(transition));
        }
    }
}