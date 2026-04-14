package goalConditions;

import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import puzzles.testpuzzle.TestPuzzle;

public class ProveNoSolutionTest {
    private static TestPuzzle puzzle;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        puzzle = new TestPuzzle();
    }

    /** Tests invalid rules */
    @Test
    public void TestInvalidRules() throws InvalidFileFormatException {
        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveNoSolution/InvalidLine", puzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveNoSolution/InvalidContradiction", puzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveNoSolution/InvalidCases", puzzle));
    }

    
    /** Tests puzzle completion with only a root node*/
    @Test
    public void TestRootNode() throws InvalidFileFormatException {
        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveNoSolution/RootIncomplete", puzzle));
    }


    /** Tests puzzle completion with a single path down the tree*/
    @Test
    public void TestSinglePath() throws InvalidFileFormatException {
        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/ProveNoSolution/ContradictoryLine", puzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveNoSolution/CompleteLine", puzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveNoSolution/IncompleteLine", puzzle));
    }


    /** Tests puzzle completion with a multiple paths and contradictory paths */
    @Test
    public void TestComplexPaths() throws InvalidFileFormatException {
        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/ProveNoSolution/ForkedContradiction", puzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveNoSolution/ClosedAndFinished", puzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveNoSolution/SingleSolutionAndUnfinished", puzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveNoSolution/SimpleContradiction", puzzle));
    }
}

