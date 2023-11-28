package puzzles.shorttruthtable.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTable;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import edu.rpi.legup.puzzle.shorttruthtable.rules.basic.introduction.DirectRuleOrIntroduction;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class OrIntroductionTest {
    private static final DirectRuleOrIntroduction RULE = new DirectRuleOrIntroduction();
    private static ShortTruthTable stt;

    @BeforeClass
    public static void setup() {
        MockGameBoardFacade.getInstance();
        stt = new ShortTruthTable();
    }

    /**
     * Given a statement: A V B
     *
     * Asserts that if at least 1 of A or B is true, then this is a valid application
     * of the rule if and only if V is true.
     *
     * @param filePath The file path for test board setup.
     * @throws InvalidFileFormatException
     */
    @Test
    public void TrueOrTest() throws InvalidFileFormatException {
        String path = "puzzles/shorttruthtable/rules/OrIntroductionDirectRule/";
        trueOrTestHelper(path + "TUT");
        trueOrTestHelper(path + "TUU");
        trueOrTestHelper(path + "UUT");
        trueOrTestHelper(path + "TUF");
        trueOrTestHelper(path + "FUT");
    }

    private void trueOrTestHelper(String filePath) throws InvalidFileFormatException {
        TestUtilities.importTestBoard(filePath, stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell or = board.getCell(1, 0);

        or.setData(ShortTruthTableCellType.TRUE);
        board.addModifiedData(or);
        Assert.assertNull(RULE.checkRule(transition));

        or.setData(ShortTruthTableCellType.FALSE);
        board.addModifiedData(or);
        Assert.assertNotNull(RULE.checkRule(transition));
    }

    /**
     * Given a statement: A V B
     *
     * Asserts that setting V to false is a valid application of the rule if
     * and only if both A and B are false.
     *
     * @param filePath The file path for test board setup.
     * @throws InvalidFileFormatException
     */
    @Test
    public void FalseOrTest() throws InvalidFileFormatException {
        String path = "puzzles/shorttruthtable/rules/OrIntroductionDirectRule/";
        String[] letters = {"T", "F", "U"};
        for (String first : letters) {
            for (String second : letters) {
                falseOrTestHelper(path + first + "U" + second);
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

        if (a.getType() == ShortTruthTableCellType.FALSE && b.getType() == ShortTruthTableCellType.FALSE) {
            Assert.assertNull(RULE.checkRule(transition));
        }
        else {
            Assert.assertNotNull(RULE.checkRule(transition));
        }
    }
}