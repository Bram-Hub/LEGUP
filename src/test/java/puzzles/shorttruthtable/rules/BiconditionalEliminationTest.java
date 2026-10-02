package puzzles.shorttruthtable.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTable;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import edu.rpi.legup.puzzle.shorttruthtable.rules.basic.elimination.DirectRuleBiconditionalElimination;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class BiconditionalEliminationTest {
    private static final DirectRuleBiconditionalElimination RULE =
            new DirectRuleBiconditionalElimination();
    private static ShortTruthTable stt;

    @BeforeClass
    public static void setup() {
        MockGameBoardFacade.getInstance();
        stt = new ShortTruthTable();
    }

    /**
     * Given one statement: A biconditional B where both A and the biconditional are true
     *
     * <p>Asserts that this is a valid application of the rule if and only if B is true.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void TrueBiconditionalWithTrueATest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/shorttruthtable/rules/BiconditionalEliminationDirectRule/TrueBiconditionalWithTrueA",
                stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell morty = board.getCell(2, 0);

        // Asserts that this is not a valid application of the rule when B is unknown
        morty.setData(ShortTruthTableCellType.UNKNOWN);
        board.addModifiedData(morty);
        Assert.assertNotNull(RULE.checkRule(transition));

        // Asserts that this is a valid application of the rule when B is true
        morty.setData(ShortTruthTableCellType.TRUE);
        board.addModifiedData(morty);
        Assert.assertNull(RULE.checkRule(transition));

        // Asserts that this is not a valid application of the rule when B is false
        morty.setData(ShortTruthTableCellType.FALSE);
        board.addModifiedData(morty);
        Assert.assertNotNull(RULE.checkRule(transition));
    }

    /**
     * Given one statement: A biconditional B where both B and the biconditional are true
     *
     * <p>Asserts that this is a valid application of the rule if and only if A is true.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void TrueBiconditionalWithTrueBTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/shorttruthtable/rules/BiconditionalEliminationDirectRule/TrueBiconditionalWithTrueB",
                stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell rick = board.getCell(0, 0);

        // Asserts that this is a valid application of the rule when A is true
        rick.setData(ShortTruthTableCellType.TRUE);
        board.addModifiedData(rick);
        Assert.assertNull(RULE.checkRule(transition));

        // Asserts that this is not a valid application of the rule when A is unknown
        rick.setData(ShortTruthTableCellType.UNKNOWN);
        board.addModifiedData(rick);
        Assert.assertNotNull(RULE.checkRule(transition));

        // Asserts that this is not a valid application of the rule when A is false
        rick.setData(ShortTruthTableCellType.FALSE);
        board.addModifiedData(rick);
        Assert.assertNotNull(RULE.checkRule(transition));
    }

    /**
     * Given one statement: A biconditional B where A is false and the biconditional is true
     *
     * <p>Asserts that this is a valid application of the rule if and only if B is false.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void TrueBiconditionalWithFalseATest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/shorttruthtable/rules/BiconditionalEliminationDirectRule/TrueBiconditionalWithFalseA",
                stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell morty = board.getCell(2, 0);

        // Asserts that this is not a valid application of the rule when B is unknown
        morty.setData(ShortTruthTableCellType.UNKNOWN);
        board.addModifiedData(morty);
        Assert.assertNotNull(RULE.checkRule(transition));

        // Asserts that this is not a valid application of the rule when B is true
        morty.setData(ShortTruthTableCellType.TRUE);
        board.addModifiedData(morty);
        Assert.assertNotNull(RULE.checkRule(transition));

        // Asserts that this is a valid application of the rule when B is false
        morty.setData(ShortTruthTableCellType.FALSE);
        board.addModifiedData(morty);
        Assert.assertNull(RULE.checkRule(transition));
    }

    /**
     * Given one statement: A biconditional B where B is false and the biconditional is true
     *
     * <p>Asserts that this is a valid application of the rule if and only if A is false.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void TrueBiconditionalWithFalseBTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/shorttruthtable/rules/BiconditionalEliminationDirectRule/TrueBiconditionalWithFalseB",
                stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell rick = board.getCell(0, 0);

        // Asserts that this is not a valid application of the rule when A is unknown
        rick.setData(ShortTruthTableCellType.UNKNOWN);
        board.addModifiedData(rick);
        Assert.assertNotNull(RULE.checkRule(transition));

        // Asserts that this is not a valid application of the rule when A is true
        rick.setData(ShortTruthTableCellType.TRUE);
        board.addModifiedData(rick);
        Assert.assertNotNull(RULE.checkRule(transition));

        // Asserts that this is a valid application of the rule when A is false
        rick.setData(ShortTruthTableCellType.FALSE);
        board.addModifiedData(rick);
        Assert.assertNull(RULE.checkRule(transition));
    }

    /**
     * Given one statement: A biconditional B where A is true and the biconditional is false
     *
     * <p>Asserts that this is a valid application of the rule if and only if B is false.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void FalseBiconditionalWithTrueATest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/shorttruthtable/rules/BiconditionalEliminationDirectRule/FalseBiconditionalWithTrueA",
                stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell morty = board.getCell(2, 0);

        // Asserts that this is not a valid application of the rule when B is unknown
        morty.setData(ShortTruthTableCellType.UNKNOWN);
        board.addModifiedData(morty);
        Assert.assertNotNull(RULE.checkRule(transition));

        // Asserts that this is not a valid application of the rule when B is true
        morty.setData(ShortTruthTableCellType.TRUE);
        board.addModifiedData(morty);
        Assert.assertNotNull(RULE.checkRule(transition));

        // Asserts that this is a valid application of the rule when B is false
        morty.setData(ShortTruthTableCellType.FALSE);
        board.addModifiedData(morty);
        Assert.assertNull(RULE.checkRule(transition));
    }

    /**
     * Given one statement: A biconditional B where B is true and the biconditional is false
     *
     * <p>Asserts that this is a valid application of the rule if and only if A is false.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void FalseBiconditionalWithTrueBTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/shorttruthtable/rules/BiconditionalEliminationDirectRule/FalseBiconditionalWithTrueB",
                stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell rick = board.getCell(0, 0);

        // Asserts that this is not a valid application of the rule when A is unknown
        rick.setData(ShortTruthTableCellType.UNKNOWN);
        board.addModifiedData(rick);
        Assert.assertNotNull(RULE.checkRule(transition));

        // Asserts that this is not a valid application of the rule when A is true
        rick.setData(ShortTruthTableCellType.TRUE);
        board.addModifiedData(rick);
        Assert.assertNotNull(RULE.checkRule(transition));

        // Asserts that this is a valid application of the rule when A is false
        rick.setData(ShortTruthTableCellType.FALSE);
        board.addModifiedData(rick);
        Assert.assertNull(RULE.checkRule(transition));
    }

    /**
     * Given one statement: A biconditional B where A and the biconditional are false
     *
     * <p>Asserts that this is a valid application of the rule if and only if B is true.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void FalseBiconditionalWithFalseATest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/shorttruthtable/rules/BiconditionalEliminationDirectRule/FalseBiconditionalWithFalseA",
                stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell morty = board.getCell(2, 0);

        // Asserts that this is not a valid application of the rule when B is unknown
        morty.setData(ShortTruthTableCellType.UNKNOWN);
        board.addModifiedData(morty);
        Assert.assertNotNull(RULE.checkRule(transition));

        // Asserts that this is a valid application of the rule when B is true
        morty.setData(ShortTruthTableCellType.TRUE);
        board.addModifiedData(morty);
        Assert.assertNull(RULE.checkRule(transition));

        // Asserts that this is not a valid application of the rule when B is false
        morty.setData(ShortTruthTableCellType.FALSE);
        board.addModifiedData(morty);
        Assert.assertNotNull(RULE.checkRule(transition));
    }

    /**
     * Given one statement: A biconditional B where B and the biconditional are false
     *
     * <p>Asserts that this is a valid application of the rule if and only if A is true.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void FalseBiconditionalWithFalseBTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/shorttruthtable/rules/BiconditionalEliminationDirectRule/FalseBiconditionalWithFalseB",
                stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell rick = board.getCell(0, 0);

        // Asserts that this is not a valid application of the rule when A is unknown
        rick.setData(ShortTruthTableCellType.UNKNOWN);
        board.addModifiedData(rick);
        Assert.assertNotNull(RULE.checkRule(transition));

        // Asserts that this is not a valid application of the rule when A is false
        rick.setData(ShortTruthTableCellType.FALSE);
        board.addModifiedData(rick);
        Assert.assertNotNull(RULE.checkRule(transition));

        // Asserts that this is a valid application of the rule when A is true
        rick.setData(ShortTruthTableCellType.TRUE);
        board.addModifiedData(rick);
        Assert.assertNull(RULE.checkRule(transition));
    }

    /**
     * Given one statement: A biconditional B where the biconditional is true
     *
     * <p>Asserts that setting any combination of A and B at the same time is not a valid
     * application of this rule
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void TrueBiconditionalSetBothAtOnceTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/shorttruthtable/rules/BiconditionalEliminationDirectRule/TrueBiconditional",
                stt);
        TreeNode rootNode = stt.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        ShortTruthTableCellType[] cellTypes = {
            ShortTruthTableCellType.TRUE,
            ShortTruthTableCellType.FALSE,
            ShortTruthTableCellType.UNKNOWN
        };

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

    /**
     * Asserts that setting any combination of A and B at the same time is not a valid application
     * of this rule. This is tested on multiple files.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void CannotSetBothAandBAtOnceTest() throws InvalidFileFormatException {
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

        ShortTruthTableCellType[] cellTypes = {
            ShortTruthTableCellType.TRUE,
            ShortTruthTableCellType.FALSE,
            ShortTruthTableCellType.UNKNOWN
        };

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
