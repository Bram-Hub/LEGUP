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
     * Given a statement: A V B
     *
     * Asserts that if either A or B are false,
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void OneFalseCannotDetermineFalseTest() throws InvalidFileFormatException {
        String directory = "puzzles/shorttruthtable/rules/BiconditionalEliminationDirectRule/";
        setAandBBothAtOnceTest(directory + "FalseBiconditional");
        setAandBBothAtOnceTest(directory + "TrueBiconditional");
        setAandBBothAtOnceTest(directory + "FalseBiconditionalWithFalseA");
        setAandBBothAtOnceTest(directory + "TrueBiconditionalWithFalseA");
        setAandBBothAtOnceTest(directory + "FalseBiconditionalWithTrueA");
        setAandBBothAtOnceTest(directory + "TrueBiconditionalWithTrueA");
    }

    /**
     * Helper function to test biconditional elimination rule with given file path.
     *
     * @param filePath The file path for test board setup.
     * @throws InvalidFileFormatException
     */
    private void setAandBBothAtOnceTest(String filePath) throws InvalidFileFormatException {
        TestUtilities.importTestBoard(filePath, stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableCellType[] cellTypes = {ShortTruthTableCellType.TRUE, ShortTruthTableCellType.FALSE, ShortTruthTableCellType.UNKNOWN};

        for (ShortTruthTableCellType cellType1 : cellTypes) {
            for (ShortTruthTableCellType cellType2 : cellTypes) {
                ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
                ShortTruthTableCell rick = board.getCell(0, 0);
                ShortTruthTableCell morty = board.getCell(2, 0);

                rick.setData(cellType1);
                morty.setData(cellType2);

                board.addModifiedData(rick);
                board.addModifiedData(morty);

                Assert.assertNotNull(RULE.checkRule(transition));
            }
        }
    }
}