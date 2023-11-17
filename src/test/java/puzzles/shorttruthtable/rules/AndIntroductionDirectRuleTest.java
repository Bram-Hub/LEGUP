package puzzles.shorttruthtable.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTable;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import edu.rpi.legup.puzzle.shorttruthtable.rules.basic.introduction.DirectRuleAndIntroduction;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class AndIntroductionDirectRuleTest {
    private static final DirectRuleAndIntroduction RULE = new DirectRuleAndIntroduction();
    private static ShortTruthTable stt;

    @BeforeClass
    public static void setup() {
        MockGameBoardFacade.getInstance();
        stt = new ShortTruthTable();
    }

    /**
     * Given a statement: A ^ B
     *
     * Asserts that if at least 1 of A or B is false, then this is a valid application
     * of the rule if and only if ^ is false.
     *
     * @param filePath The file path for test board setup.
     * @throws InvalidFileFormatException
     */
    @Test
    public void FalseAndTest() throws InvalidFileFormatException {
        String path = "puzzles/shorttruthtable/rules/AndIntroductionDirectRule/";
        falseAndTestHelper(path + "FUF");
        falseAndTestHelper(path + "FUU");
        falseAndTestHelper(path + "UUF");
        falseAndTestHelper(path + "FUT");
        falseAndTestHelper(path + "TUF");
    }

    private void falseAndTestHelper(String filePath) throws InvalidFileFormatException {
        TestUtilities.importTestBoard(filePath, stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell and = board.getCell(1, 0);

        and.setData(ShortTruthTableCellType.TRUE);
        board.addModifiedData(and);
        Assert.assertNotNull(RULE.checkRule(transition));

        and.setData(ShortTruthTableCellType.FALSE);
        board.addModifiedData(and);
        Assert.assertNull(RULE.checkRule(transition));
    }

    /**
     * Given a statement: A ^ B
     *
     * Asserts that setting ^ to true is a valid application of the rule if
     * and only if both A and B are true.
     *
     * @param filePath The file path for test board setup.
     * @throws InvalidFileFormatException
     */
    @Test
    public void FalseOrTest() throws InvalidFileFormatException {
        String path = "puzzles/shorttruthtable/rules/AndIntroductionDirectRule/";
        String[] letters = {"T", "F", "U"};
        for (String first : letters) {
            for (String second : letters) {
                trueAndTestHelper(path + first + "U" + second);
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
        ShortTruthTableCell and = board.getCell(1, 0);

        and.setData(ShortTruthTableCellType.TRUE);
        board.addModifiedData(and);

        if (a.getType() == ShortTruthTableCellType.TRUE && b.getType() == ShortTruthTableCellType.TRUE) {
            Assert.assertNull(RULE.checkRule(transition));
        }
        else {
            Assert.assertNotNull(RULE.checkRule(transition));
        }
    }
}