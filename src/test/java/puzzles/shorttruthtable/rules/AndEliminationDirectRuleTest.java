package puzzles.shorttruthtable.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTable;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import edu.rpi.legup.puzzle.shorttruthtable.rules.basic.elimination.DirectRuleAndElimination;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class AndEliminationDirectRuleTest {
    private static final DirectRuleAndElimination RULE = new DirectRuleAndElimination();
    private static ShortTruthTable stt;

    @BeforeClass
    public static void setup() {
        MockGameBoardFacade.getInstance();
        stt = new ShortTruthTable();
    }

    /**
     * Given one statement: B^C where ^ is true
     *
     * Checks all possible combinations of true, false, and unknown for B and C
     * except for where both B and C are true and asserts that each one of them
     * is not a valid application of the rule.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void trueAndTest1() throws InvalidFileFormatException {
       TestUtilities.importTestBoard("puzzles/shorttruthtable/rules/AndEliminationDirectRule/TrueAnd", stt);
       TreeNode rootNode = stt.getTree().getRootNode();
       TreeTransition transition = rootNode.getChildren().get(0);
       transition.setRule(RULE);

       ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();

       ShortTruthTableCell bonnie = board.getCell(0, 0);
       bonnie.setData(ShortTruthTableCellType.TRUE);
       board.addModifiedData(bonnie);
       Assert.assertNull(RULE.checkRule(transition));

       ShortTruthTableCell clyde = board.getCell(2, 0);
       clyde.setData(ShortTruthTableCellType.TRUE);
       board.addModifiedData(clyde);
       Assert.assertNull(RULE.checkRule(transition));
    }

    /**
     * Given one statement: B^C where ^ is true
     *
     * This test makes sure that none of the cases tested are valid applications
     * of And Elimination, as all of them have at least one of the variables set
     * to false.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void trueAndTest2() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/shorttruthtable/rules/AndEliminationDirectRule/TrueAnd", stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();

        ShortTruthTableCellType[] cellTypes = {ShortTruthTableCellType.TRUE, ShortTruthTableCellType.FALSE, ShortTruthTableCellType.UNKNOWN};

        ShortTruthTableCell bonnie = board.getCell(0, 0);
        ShortTruthTableCell clyde = board.getCell(2, 0);

        for (ShortTruthTableCellType cellType1 : cellTypes)
            for (ShortTruthTableCellType cellType2 : cellTypes)
            {
                if (cellType1 == cellType2 && cellType1 == ShortTruthTableCellType.TRUE)
                    continue;

                bonnie.setData(cellType1);
                clyde.setData(cellType2);
                board.addModifiedData(bonnie);
                board.addModifiedData(clyde);
                Assert.assertNotNull(RULE.checkRule(transition));
            }
    }

    /**
     * Given one statement: B^C where ^ is false
     *
     * Checks all possible combinations of true, false, and unknown for B and C
     * and asserts that each one of them is not a valid application of the rule.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void falseAndWithUnknownsTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/shorttruthtable/rules/AndEliminationDirectRule/FalseAnd", stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();

        ShortTruthTableCellType[] cellTypes = {ShortTruthTableCellType.TRUE, ShortTruthTableCellType.FALSE, ShortTruthTableCellType.UNKNOWN};

        ShortTruthTableCell bonnie = board.getCell(0, 0);
        ShortTruthTableCell clyde = board.getCell(2, 0);

        for (ShortTruthTableCellType cellType1 : cellTypes)
            for (ShortTruthTableCellType cellType2 : cellTypes)
            {
                bonnie.setData(cellType1);
                clyde.setData(cellType2);
                board.addModifiedData(bonnie);
                board.addModifiedData(clyde);
                Assert.assertNotNull(RULE.checkRule(transition));
            }
    }

    /**
     * Given one statement: B^C where both B and ^ are false
     *
     * Asserts that this is not a valid application of the rule if C is set to
     * either true or false.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void falseAndWithKnownTrueTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/shorttruthtable/rules/AndEliminationDirectRule/FalseAnd", stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();

        ShortTruthTableCell clyde = board.getCell(2, 0);
        clyde.setData(ShortTruthTableCellType.TRUE);
        board.addModifiedData(clyde);
        Assert.assertNotNull(RULE.checkRule(transition));

        clyde.setData(ShortTruthTableCellType.FALSE);
        board.addModifiedData(clyde);
        Assert.assertNotNull(RULE.checkRule(transition));
    }

    /**
     * Given one statement: B^C where both B and ^ are false
     *
     * Asserts that this is not a valid application of the rule if C is set to
     * either true or false.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void falseAndWithKnownTrueTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/shorttruthtable/rules/AndEliminationDirectRule/FalseAnd", stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();

        ShortTruthTableCell clyde = board.getCell(2, 0);
        clyde.setData(ShortTruthTableCellType.TRUE);
        board.addModifiedData(clyde);
        Assert.assertNotNull(RULE.checkRule(transition));

        clyde.setData(ShortTruthTableCellType.FALSE);
        board.addModifiedData(clyde);
        Assert.assertNotNull(RULE.checkRule(transition));
    }
}