package goalConditions;

import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import puzzles.testpuzzle.TestPuzzle;

public class ProveCellMustBeTest {
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
                TestUtilities.verifyBoard("goalConditions/ProveCellMustBe/InvalidLine", puzzle));

        Assert.assertFalse(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveCellMustBe/InvalidContradiction", puzzle));

        Assert.assertFalse(
                TestUtilities.verifyBoard("goalConditions/ProveCellMustBe/InvalidCases", puzzle));
    }

    /** Tests puzzle completion with only a root node */
    @Test
    public void TestRootNode() throws InvalidFileFormatException {
        Assert.assertTrue(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveCellMustBe/RootCompleteWithAssumption", puzzle));

        Assert.assertTrue(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveCellMustBe/RootCompleteNoAssumption", puzzle));

        Assert.assertFalse(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveCellMustBe/RootIncompleteWithAssumption", puzzle));

        Assert.assertFalse(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveCellMustBe/RootIncompleteNoAssumption", puzzle));
    }

    /** Tests puzzle completion with a single path down the tree */
    @Test
    public void TestSinglePath() throws InvalidFileFormatException {
        Assert.assertTrue(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveCellMustBe/CompleteLineWithAssumption", puzzle));

        Assert.assertTrue(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveCellMustBe/OvercompleteLine", puzzle));

        Assert.assertTrue(
                TestUtilities.verifyBoard("goalConditions/ProveCellMustBe/SolvedLine", puzzle));

        Assert.assertFalse(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveCellMustBe/ContradictoryLine", puzzle));

        Assert.assertFalse(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveCellMustBe/CompleteLineNoAssumption", puzzle));

        Assert.assertFalse(
                TestUtilities.verifyBoard("goalConditions/ProveCellMustBe/IncompleteLine", puzzle));
    }

    /** Tests puzzle completion with a multiple paths and contradictory paths */
    @Test
    public void TestComplexPaths() throws InvalidFileFormatException {
        Assert.assertTrue(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveCellMustBe/SimpleContradiction", puzzle));

        Assert.assertTrue(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveCellMustBe/MergedSolutions", puzzle));

        Assert.assertTrue(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveCellMustBe/UnmergedSolutions", puzzle));

        Assert.assertFalse(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveCellMustBe/SingleSolutionAndUnfinished", puzzle));
    }
}
