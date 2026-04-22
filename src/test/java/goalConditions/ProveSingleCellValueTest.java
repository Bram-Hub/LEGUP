package goalConditions;

import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import puzzles.testpuzzle.TestPuzzle;

public class ProveSingleCellValueTest {
    private static TestPuzzle puzzle;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        puzzle = new TestPuzzle();
    }

    /** Tests invalid rules */
    @Test
    public void TestInvalidRules() throws InvalidFileFormatException {
        Assert.assertFalse(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveSingleCellValue/InvalidLine", puzzle));

        Assert.assertFalse(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveSingleCellValue/InvalidContradiction", puzzle));

        Assert.assertFalse(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveSingleCellValue/InvalidCases", puzzle));
    }

    /** Tests puzzle completion with only a root node */
    @Test
    public void TestRootNode() throws InvalidFileFormatException {
        Assert.assertTrue(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveSingleCellValue/RootCompleteWithAssumption", puzzle));

        Assert.assertTrue(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveSingleCellValue/RootCompleteNoAssumption", puzzle));

        Assert.assertFalse(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveSingleCellValue/RootIncompleteWithAssumption",
                        puzzle));

        Assert.assertFalse(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveSingleCellValue/RootIncompleteNoAssumption", puzzle));
    }

    /** Tests puzzle completion with a single path down the tree */
    @Test
    public void TestSinglePath() throws InvalidFileFormatException {
        Assert.assertTrue(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveSingleCellValue/CompleteLineWithAssumption", puzzle));

        Assert.assertTrue(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveSingleCellValue/SolvedLine", puzzle));

        Assert.assertTrue(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveSingleCellValue/OvercompleteLine", puzzle));

        Assert.assertFalse(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveSingleCellValue/ContradictoryLine", puzzle));

        Assert.assertFalse(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveSingleCellValue/IncompleteLine", puzzle));

        Assert.assertFalse(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveSingleCellValue/CompleteLineNoAssumption", puzzle));
    }

    /** Tests puzzle completion with a multiple paths and contradictory paths */
    @Test
    public void TestComplexPaths() throws InvalidFileFormatException {
        Assert.assertTrue(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveSingleCellValue/SimpleContradiction", puzzle));

        Assert.assertTrue(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveSingleCellValue/MergedSolutions", puzzle));

        Assert.assertTrue(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveSingleCellValue/UnmergedSolutions", puzzle));

        Assert.assertFalse(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveSingleCellValue/MultipleValues", puzzle));

        Assert.assertFalse(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveSingleCellValue/SingleSolutionAndUnfinished", puzzle));
    }
}
