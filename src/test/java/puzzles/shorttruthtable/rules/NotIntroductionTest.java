package puzzles.shorttruthtable.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTable;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import edu.rpi.legup.puzzle.shorttruthtable.rules.basic.introduction.DirectRuleNotIntroduction;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class NotIntroductionTest {
    private static final DirectRuleNotIntroduction RULE = new DirectRuleNotIntroduction();
    private static ShortTruthTable stt;

    @BeforeClass
    public static void setup() {
        MockGameBoardFacade.getInstance();
        stt = new ShortTruthTable();
    }

    /**
     * Given one statement: ¬A where A is false
     *
     * Asserts that this is a valid application of this rule if and only if ¬ is true
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void FalseNot() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/shorttruthtable/rules/NotIntroductionDirectRule/FalseA", stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableCellType[] cellTypes = {ShortTruthTableCellType.TRUE, ShortTruthTableCellType.FALSE, ShortTruthTableCellType.UNKNOWN};

        for (ShortTruthTableCellType cellType : cellTypes) {
            ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
            ShortTruthTableCell not = board.getCell(0, 0);
            not.setData(cellType);
            board.addModifiedData(not);

            if (cellType == ShortTruthTableCellType.TRUE) {
                Assert.assertNull(RULE.checkRule(transition));
            }
            else {
                Assert.assertNotNull(RULE.checkRule(transition));
            }
        }
    }

    /**
     * Given one statement: ¬A where A is true
     *
     * Asserts that this is a valid application of this rule if and only if ¬ is false
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void TrueNot() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/shorttruthtable/rules/NotIntroductionDirectRule/TrueA", stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableCellType[] cellTypes = {ShortTruthTableCellType.TRUE, ShortTruthTableCellType.FALSE, ShortTruthTableCellType.UNKNOWN};

        for (ShortTruthTableCellType cellType : cellTypes) {
            ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
            ShortTruthTableCell not = board.getCell(0, 0);
            not.setData(cellType);
            board.addModifiedData(not);

            if (cellType == ShortTruthTableCellType.FALSE) {
                Assert.assertNull(RULE.checkRule(transition));
            }
            else {
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
        TestUtilities.importTestBoard("puzzles/shorttruthtable/rules/NotIntroductionDirectRule/BlankA", stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableCellType[] cellTypes = {ShortTruthTableCellType.TRUE, ShortTruthTableCellType.FALSE, ShortTruthTableCellType.UNKNOWN};

        for (ShortTruthTableCellType cellType1 : cellTypes) {
            for (ShortTruthTableCellType cellType2 : cellTypes) {
                ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
                ShortTruthTableCell not = board.getCell(0, 0);
                ShortTruthTableCell a = board.getCell(1, 0);

                not.setData(cellType1);
                a.setData(cellType2);

                board.addModifiedData(not);
                board.addModifiedData(a);

                Assert.assertNotNull(RULE.checkRule(transition));
            }
        }
    }
}