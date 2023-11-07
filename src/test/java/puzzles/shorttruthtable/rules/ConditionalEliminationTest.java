package puzzles.shorttruthtable.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTable;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import edu.rpi.legup.puzzle.shorttruthtable.rules.basic.elimination.DirectRuleConditionalElimination;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConditionalEliminationTest {
    private static final DirectRuleConditionalElimination RULE = new DirectRuleConditionalElimination();
    private static ShortTruthTable stt;

    @BeforeClass
    public static void setup() {
        MockGameBoardFacade.getInstance();
        stt = new ShortTruthTable();
    }

    /**
     * Given one statement: A -> B where -> is false
     *
     * Asserts that the only valid combination of A and B that is a valid application
     * of this rule is when A is true and B is false
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void FalseConditionalTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/shorttruthtable/rules/ConditionalEliminationDirectRule/FalseConditional", stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableCellType[] cellTypes = {ShortTruthTableCellType.TRUE, ShortTruthTableCellType.FALSE, ShortTruthTableCellType.UNKNOWN};

        for (ShortTruthTableCellType cellType1 : cellTypes) {
            for (ShortTruthTableCellType cellType2 : cellTypes) {
                ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
                ShortTruthTableCell aubergine = board.getCell(0, 0);
                ShortTruthTableCell boniato = board.getCell(2, 0);

                aubergine.setData(cellType1);
                boniato.setData(cellType2);

                board.addModifiedData(aubergine);
                board.addModifiedData(boniato);

                if (cellType1 == ShortTruthTableCellType.TRUE && cellType2 == ShortTruthTableCellType.FALSE) {
                    Assert.assertNull(RULE.checkRule(transition));
                }
                else {
                    Assert.assertNotNull(RULE.checkRule(transition));
                }
            }
        }
    }

    /**
     * Given one statement: A -> B where -> is false
     *
     * Asserts that this is a valid application of the rule if and only if A
     * is set to true.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void FalseConditionalTrueATest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/shorttruthtable/rules/ConditionalEliminationDirectRule/FalseConditional", stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell aubergine = board.getCell(0, 0);

        aubergine.setData(ShortTruthTableCellType.TRUE);
        board.addModifiedData(aubergine);
        Assert.assertNull(RULE.checkRule(transition));

        aubergine.setData(ShortTruthTableCellType.FALSE);
        board.addModifiedData(aubergine);
        Assert.assertNotNull(RULE.checkRule(transition));
    }

    /**
     * Given one statement: A -> B where -> is false
     *
     * Asserts that this is a valid application of the rule if and only if B is
     * set to false.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void FalseConditionalFalseBTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/shorttruthtable/rules/ConditionalEliminationDirectRule/FalseConditional", stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell boniato = board.getCell(2, 0);

        boniato.setData(ShortTruthTableCellType.FALSE);
        board.addModifiedData(boniato);
        Assert.assertNull(RULE.checkRule(transition));

        boniato.setData(ShortTruthTableCellType.TRUE);
        board.addModifiedData(boniato);
        Assert.assertNotNull(RULE.checkRule(transition));
    }

    /**
     * Given one statement: A -> B where -> is true
     *
     * Asserts that you cannot set any combination of both A and B at the same time.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void CannotSetBothAandBTrueConditionalTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/shorttruthtable/rules/ConditionalEliminationDirectRule/TrueConditional", stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableCellType[] cellTypes = {ShortTruthTableCellType.TRUE, ShortTruthTableCellType.FALSE, ShortTruthTableCellType.UNKNOWN};

        for (ShortTruthTableCellType cellType1 : cellTypes) {
            for (ShortTruthTableCellType cellType2 : cellTypes) {
                ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
                ShortTruthTableCell aubergine = board.getCell(0, 0);
                ShortTruthTableCell boniato = board.getCell(2, 0);

                aubergine.setData(cellType1);
                boniato.setData(cellType2);

                board.addModifiedData(aubergine);
                board.addModifiedData(boniato);

                Assert.assertNotNull(RULE.checkRule(transition));
            }
        }
    }

    /**
     * Given one statement: A -> B where A and -> are true
     *
     * Asserts that this is a valid application of this rule if and only if B
     * is set to true.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void TrueAMeansTrueBTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/shorttruthtable/rules/ConditionalEliminationDirectRule/TrueConditionalWithTrueA", stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell boniato = board.getCell(2, 0);

        boniato.setData(ShortTruthTableCellType.TRUE);
        board.addModifiedData(boniato);
        Assert.assertNull(RULE.checkRule(transition));

        boniato.setData(ShortTruthTableCellType.FALSE);
        board.addModifiedData(boniato);
        Assert.assertNotNull(RULE.checkRule(transition));
    }

    /**
     * Given one statement: A -> B where B is false and -> is true
     *
     * Asserts that this is a valid application of this rule if and only if A
     * is set to false.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void FalseBMeansFalseATest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/shorttruthtable/rules/ConditionalEliminationDirectRule/TrueConditionalWithFalseB", stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell aubergine = board.getCell(0, 0);

        aubergine.setData(ShortTruthTableCellType.FALSE);
        board.addModifiedData(aubergine);
        Assert.assertNull(RULE.checkRule(transition));

        aubergine.setData(ShortTruthTableCellType.TRUE);
        board.addModifiedData(aubergine);
        Assert.assertNotNull(RULE.checkRule(transition));
    }

    /**
     * Given one statement: A -> B where B and -> are true
     *
     * Asserts that this is not a valid application of this rule no matter what
     * A is set to.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void TrueBCannotDetermineA() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/shorttruthtable/rules/ConditionalEliminationDirectRule/TrueConditionalWithTrueB", stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell boniato = board.getCell(2, 0);

        boniato.setData(ShortTruthTableCellType.TRUE);
        board.addModifiedData(boniato);
        Assert.assertNotNull(RULE.checkRule(transition));

        boniato.setData(ShortTruthTableCellType.FALSE);
        board.addModifiedData(boniato);
        Assert.assertNotNull(RULE.checkRule(transition));
    }
}