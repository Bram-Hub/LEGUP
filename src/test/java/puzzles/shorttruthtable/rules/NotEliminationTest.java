package puzzles.shorttruthtable.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTable;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import edu.rpi.legup.puzzle.shorttruthtable.rules.basic.elimination.DirectRuleNotElimination;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class NotEliminationTest {
    private static final DirectRuleNotElimination RULE = new DirectRuleNotElimination();
    private static ShortTruthTable stt;

    @BeforeClass
    public static void setup() {
        MockGameBoardFacade.getInstance();
        stt = new ShortTruthTable();
    }

    /**
     * Given one statement: ¬A where ¬ is false
     *
     * <p>Asserts that this is a valid application of this rule if and only if A is true
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void FalseNot() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/shorttruthtable/rules/NotEliminationDirectRule/FalseNot", stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableCellType[] cellTypes = {
            ShortTruthTableCellType.TRUE,
            ShortTruthTableCellType.FALSE,
            ShortTruthTableCellType.UNKNOWN
        };

        for (ShortTruthTableCellType cellType : cellTypes) {
            ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
            ShortTruthTableCell a = board.getCell(1, 0);
            a.setData(cellType);
            board.addModifiedData(a);

            if (cellType == ShortTruthTableCellType.TRUE) {
                Assert.assertNull(RULE.checkRule(transition));
            } else {
                Assert.assertNotNull(RULE.checkRule(transition));
            }
        }
    }

    /**
     * Given one statement: ¬A where ¬ is true
     *
     * <p>Asserts that this is a valid application of this rule if and only if A is false
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void TrueNot() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/shorttruthtable/rules/NotEliminationDirectRule/TrueNot", stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableCellType[] cellTypes = {
            ShortTruthTableCellType.TRUE,
            ShortTruthTableCellType.FALSE,
            ShortTruthTableCellType.UNKNOWN
        };

        for (ShortTruthTableCellType cellType : cellTypes) {
            ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
            ShortTruthTableCell a = board.getCell(1, 0);
            a.setData(cellType);
            board.addModifiedData(a);

            if (cellType == ShortTruthTableCellType.FALSE) {
                Assert.assertNull(RULE.checkRule(transition));
            } else {
                Assert.assertNotNull(RULE.checkRule(transition));
            }
        }
    }

    /**
     * Given one statement: ¬A
     *
     * Asserts that setting both ¬ and A to any values would not be a valid
     * application of this rule
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void CannotSetBothAtOnceTest() throws InvalidFileFormatException {

    TestUtilities.importTestBoard("puzzles/shorttruthtable/rules/NotEliminationDirectRule/BlankNot", stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableCellType[] cellTypes = {ShortTruthTableCellType.TRUE,
    ShortTruthTableCellType.FALSE, ShortTruthTableCellType.UNKNOWN};

        for (ShortTruthTableCellType cellType1 : cellTypes) {
            for (ShortTruthTableCellType cellType2 : cellTypes) {
                ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
                ShortTruthTableCell not = board.getCell(0, 0);
                ShortTruthTableCell a = board.getCell(1, 0);

                not.setData(cellType1);
                a.setData(cellType2);

                board.addModifiedData(not);
                board.addModifiedData(a);

                System.out.println("TYPE1:" + cellType1);
                System.out.println("TYPE2:" + cellType2);
                Assert.assertNotNull(RULE.checkRule(transition));
            }
        }
    }
}
