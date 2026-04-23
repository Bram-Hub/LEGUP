package goalConditions;

import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import puzzles.testpuzzle.TestPuzzle;

public class ProveAnySolutionTest {
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
                TestUtilities.verifyBoard("goalConditions/ProveAnySolution/InvalidLine", puzzle));

        Assert.assertFalse(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveAnySolution/InvalidContradiction", puzzle));

        Assert.assertFalse(
                TestUtilities.verifyBoard("goalConditions/ProveAnySolution/InvalidCases", puzzle));
    }

    /** Tests puzzle completion with only a root node */
    @Test
    public void TestRootNode() throws InvalidFileFormatException {
        Assert.assertTrue(
                TestUtilities.verifyBoard("goalConditions/ProveAnySolution/RootComplete", puzzle));

        Assert.assertFalse(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveAnySolution/RootIncomplete", puzzle));
    }

    /** Tests puzzle completion with a single path down the tree */
    @Test
    public void TestSinglePath() throws InvalidFileFormatException {
        Assert.assertTrue(
                TestUtilities.verifyBoard("goalConditions/ProveAnySolution/CompleteLine", puzzle));

        Assert.assertFalse(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveAnySolution/ContradictoryLine", puzzle));

        Assert.assertFalse(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveAnySolution/IncompleteLine", puzzle));
    }

    /** Tests puzzle completion with a multiple paths and contradictory paths */
    @Test
    public void TestComplexPaths() throws InvalidFileFormatException {
        Assert.assertTrue(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveAnySolution/ClosedAndFinished", puzzle));

        Assert.assertTrue(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveAnySolution/MergedSolutions", puzzle));

        Assert.assertTrue(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveAnySolution/UnmergedSolutions", puzzle));

        Assert.assertTrue(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveAnySolution/MultipleSolutions", puzzle));

        Assert.assertTrue(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveAnySolution/SingleSolutionAndUnfinished", puzzle));

        Assert.assertFalse(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveAnySolution/SimpleContradiction", puzzle));
    }
}
