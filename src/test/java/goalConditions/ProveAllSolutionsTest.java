package goalConditions;

import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import puzzles.testpuzzle.TestPuzzle;

public class ProveAllSolutionsTest {
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
                "goalConditions/ProveAllSolutions/InvalidLine", puzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveAllSolutions/InvalidContradiction", puzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveAllSolutions/InvalidCases", puzzle));
    }

    
    /** Tests puzzle completion with only a root node*/
    @Test
    public void TestRootNode() throws InvalidFileFormatException {
        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/ProveAllSolutions/RootComplete", puzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveAllSolutions/RootIncomplete", puzzle));
    }


    /** Tests puzzle completion with a single path down the tree*/
    @Test
    public void TestSinglePath() throws InvalidFileFormatException {
        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/ProveAllSolutions/CompleteLine", puzzle));

        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/ProveAllSolutions/ContradictoryLine", puzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveAllSolutions/IncompleteLine", puzzle));
    }


    /** Tests puzzle completion with a multiple paths and contradictory paths */
    @Test
    public void TestComplexPaths() throws InvalidFileFormatException {
        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/ProveAllSolutions/ClosedAndFinished", puzzle));

        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/ProveAllSolutions/MergedSolutions", puzzle));

        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/ProveAllSolutions/UnmergedSolutions", puzzle));

        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/ProveAllSolutions/MultipleSolutions", puzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveAllSolutions/SingleSolutionAndUnfinished", puzzle));
    }
}

