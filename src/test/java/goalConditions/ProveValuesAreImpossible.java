package goalConditions;

import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import puzzles.testpuzzle.TestPuzzle;

public class ProveValuesAreImpossible {
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
                "goalConditions/ProveValuesAreImpossible/InvalidLine", puzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveValuesAreImpossible/InvalidContradiction", puzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveValuesAreImpossible/InvalidCases", puzzle));

    }

    
    /** Tests puzzle completion with only a root node*/
    @Test
    public void TestRootNode() throws InvalidFileFormatException {
        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/ProveValuesAreImpossible/RootComplete", puzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveValuesAreImpossible/RootIncomplete", puzzle));
    }


    /** Tests puzzle completion with a single path down the tree*/
    @Test
    public void TestSinglePath() throws InvalidFileFormatException {
        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/ProveValuesAreImpossible/CompleteLine", puzzle));

        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/ProveValuesAreImpossible/IncompleteLineWithoutMatch", puzzle));

        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/ProveValuesAreImpossible/ContradictoryLine", puzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveValuesAreImpossible/IncompleteLineWithMatch", puzzle));
    }


    /** Tests puzzle completion with a multiple paths and contradictory paths */
    @Test
    public void TestComplexPaths() throws InvalidFileFormatException {
        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/ProveValuesAreImpossible/ClosedAndFinished", puzzle));

        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/ProveValuesAreImpossible/MergedSolutions", puzzle));

        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/ProveValuesAreImpossible/UnmergedSolutions", puzzle));

        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/ProveValuesAreImpossible/MultipleSolutionsOneMatchEach", puzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveValuesAreImpossible/SingleSolutionWithMatchAndUnfinished", puzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveValuesAreImpossible/MultipleSolutionsOneCorrect", puzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveValuesAreImpossible/SingleSolutionAndUnfinishedWithMatch", puzzle));

    }
}

