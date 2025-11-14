package puzzles.shorttruthtable.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTable;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import edu.rpi.legup.puzzle.shorttruthtable.rules.basic.DirectRuleAtomic;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class AtomicDirectRuleTest {
    private static final DirectRuleAtomic RULE = new DirectRuleAtomic();
    private static ShortTruthTable stt;

    @BeforeClass
    public static void setup() {
        MockGameBoardFacade.getInstance();
        stt = new ShortTruthTable();
    }

    /**
     * Given two statements: A A where the first A is set to false.
     *
     * <p>This test sets the second A to false and then asserts that this is a valid application of
     * the rule.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void MatchingFalseTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/shorttruthtable/rules/AtomicDirectRule/FalseA", stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();

        ShortTruthTableCell cell = board.getCell(0, 2);
        cell.setData(ShortTruthTableCellType.FALSE);
        board.addModifiedData(cell);

        Assert.assertNull(RULE.checkRule(transition));
    }

    /**
     * Given two statements: A A where the first A is set to false.
     *
     * <p>This test sets the second A to true and then asserts that this is not a valid application
     * of the rule.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void MismatchingFalseTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/shorttruthtable/rules/AtomicDirectRule/FalseA", stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();

        ShortTruthTableCell cell = board.getCell(0, 2);
        cell.setData(ShortTruthTableCellType.TRUE);
        board.addModifiedData(cell);

        Assert.assertNotNull(RULE.checkRule(transition));
    }

    /**
     * Given two statements: B B where the first B is set to true.
     *
     * <p>This test sets the second B to true and then asserts that this is a valid application of
     * the rule.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void MatchingTrueTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/shorttruthtable/rules/AtomicDirectRule/TrueB", stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();

        ShortTruthTableCell cell = board.getCell(0, 2);
        cell.setData(ShortTruthTableCellType.TRUE);
        board.addModifiedData(cell);

        Assert.assertNull(RULE.checkRule(transition));
    }

    /**
     * Given two statements: B B where the first B is set to true.
     *
     * <p>This test sets the second B to false and then asserts that this is not a valid application
     * of the rule.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void MismatchingTrueTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/shorttruthtable/rules/AtomicDirectRule/TrueB", stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();

        ShortTruthTableCell cell = board.getCell(0, 2);
        cell.setData(ShortTruthTableCellType.FALSE);
        board.addModifiedData(cell);

        Assert.assertNotNull(RULE.checkRule(transition));
    }

    /**
     * Given two statements: C C where neither statement is set to anything.
     *
     * <p>This test sets the second C to false and then asserts that this is not a valid application
     * of the rule.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void NothingPreviouslyMarkedTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/shorttruthtable/rules/AtomicDirectRule/Empty", stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();

        ShortTruthTableCell cell = board.getCell(0, 2);
        cell.setData(ShortTruthTableCellType.FALSE);
        board.addModifiedData(cell);

        Assert.assertNotNull(RULE.checkRule(transition));
    }

    /**
     * Given two statements: C C where neither statement is set to anything.
     *
     * <p>This test sets the second C to true and then asserts that this is not a valid application
     * of the rule.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void NothingPreviouslyMarkedTest2() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/shorttruthtable/rules/AtomicDirectRule/Empty", stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();

        ShortTruthTableCell cell = board.getCell(0, 2);
        cell.setData(ShortTruthTableCellType.TRUE);
        board.addModifiedData(cell);

        Assert.assertNotNull(RULE.checkRule(transition));
    }
}
