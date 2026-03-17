package goalConditions;

import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import puzzles.testpuzzle.TestPuzzle;

public class FindAllSolutionsTest {
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
                "goalConditions/FindAllSolutions/InvalidLine", puzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/FindAllSolutions/InvalidContradiction", puzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/FindAllSolutions/InvalidCases", puzzle));
    }

    
    /** Tests puzzle completion with only a root node*/
    @Test
    public void TestRootNode() throws InvalidFileFormatException {
        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/FindAllSolutions/RootComplete", puzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/FindAllSolutions/RootIncomplete", puzzle));
    }


    /** Tests puzzle completion with a single path down the tree*/
    @Test
    public void TestSinglePath() throws InvalidFileFormatException {
        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/FindAllSolutions/CompleteLine", puzzle));

        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/FindAllSolutions/ContradictoryLine", puzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/FindAllSolutions/IncompleteLine", puzzle));
    }


    /** Tests puzzle completion with a multiple paths and contradictory paths */
    @Test
    public void TestComplexPaths() throws InvalidFileFormatException {
        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/FindAllSolutions/ClosedAndFinished", puzzle));

        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/FindAllSolutions/MergedSolutions", puzzle));

        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/FindAllSolutions/UnmergedSolutions", puzzle));

        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/FindAllSolutions/MultipleSolutions", puzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/FindAllSolutions/SingleSolutionAndUnfinished", puzzle));
    }
}

