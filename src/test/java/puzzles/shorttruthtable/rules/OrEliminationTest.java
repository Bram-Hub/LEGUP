package puzzles.shorttruthtable.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTable;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import edu.rpi.legup.puzzle.shorttruthtable.rules.basic.elimination.DirectRuleOrElimination;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class OrEliminationTest {
    private static final DirectRuleOrElimination RULE = new DirectRuleOrElimination();
    private static ShortTruthTable stt;

    @BeforeClass
    public static void setup() {
        MockGameBoardFacade.getInstance();
        stt = new ShortTruthTable();
    }

    /**
     * Given a statement: A V B, where A is false and V is true
     *
     * Asserts that this is a valid application of the rule if and only if B is true.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void FTUTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/shorttruthtable/rules/OrEliminationDirectRule/FTU", stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell cell = board.getCell(2, 0);

        cell.setData(ShortTruthTableCellType.TRUE);
        board.addModifiedData(cell);
        Assert.assertNull(RULE.checkRule(transition));

        cell.setData(ShortTruthTableCellType.FALSE);
        board.addModifiedData(cell);
        Assert.assertNotNull(RULE.checkRule(transition));
    }

    /**
     * Given a statement: A V B, where B is false and V is true
     *
     * Asserts that this is a valid application of the rule if and only if B is true.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void UTFTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/shorttruthtable/rules/OrEliminationDirectRule/UTF", stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell cell = board.getCell(0, 0);

        cell.setData(ShortTruthTableCellType.TRUE);
        board.addModifiedData(cell);
        Assert.assertNull(RULE.checkRule(transition));

        cell.setData(ShortTruthTableCellType.FALSE);
        board.addModifiedData(cell);
        Assert.assertNotNull(RULE.checkRule(transition));
    }

    /**
     * Given a statement: A V B, where V is false
     *
     * Asserts that this is a valid application of the rule if and only if both A
     * and B are false.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void UFUTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/shorttruthtable/rules/OrEliminationDirectRule/UFU", stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableCellType[] cellTypes = {ShortTruthTableCellType.TRUE, ShortTruthTableCellType.FALSE, ShortTruthTableCellType.UNKNOWN};

        for (ShortTruthTableCellType cellType1 : cellTypes) {
            for (ShortTruthTableCellType cellType2 : cellTypes) {
                ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
                ShortTruthTableCell a = board.getCell(0, 0);
                ShortTruthTableCell b = board.getCell(2, 0);

                a.setData(cellType1);
                b.setData(cellType2);

                board.addModifiedData(a);
                board.addModifiedData(b);

                if (cellType1 == ShortTruthTableCellType.FALSE && cellType2 == ShortTruthTableCellType.FALSE) {
                    Assert.assertNull(RULE.checkRule(transition));
                }
                else {
                    Assert.assertNotNull(RULE.checkRule(transition));
                }
            }
        }
    }

    /**
     * Given a statement: A V B, where V is true
     *
     * Asserts that setting both A and B is not a valid application of this rule.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void UTUTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/shorttruthtable/rules/OrEliminationDirectRule/UTU", stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableCellType[] cellTypes = {ShortTruthTableCellType.TRUE, ShortTruthTableCellType.FALSE, ShortTruthTableCellType.UNKNOWN};

        for (ShortTruthTableCellType cellType1 : cellTypes) {
            for (ShortTruthTableCellType cellType2 : cellTypes) {
                ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
                ShortTruthTableCell a = board.getCell(0, 0);
                ShortTruthTableCell b = board.getCell(2, 0);

                a.setData(cellType1);
                b.setData(cellType2);

                board.addModifiedData(a);
                board.addModifiedData(b);

                Assert.assertNotNull(RULE.checkRule(transition));
            }
        }
    }
}