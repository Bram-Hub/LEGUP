package goalConditions;

import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import puzzles.testpuzzle.TestPuzzle;

public class ProveValuesArePossible {
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
                        "goalConditions/ProveValuesArePossible/InvalidLine", puzzle));

        Assert.assertFalse(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveValuesArePossible/InvalidContradiction", puzzle));

        Assert.assertFalse(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveValuesArePossible/InvalidCases", puzzle));
    }

    /** Tests puzzle completion with only a root node */
    @Test
    public void TestRootNode() throws InvalidFileFormatException {
        Assert.assertTrue(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveValuesArePossible/RootCompleteNoAssumption", puzzle));

        Assert.assertTrue(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveValuesArePossible/RootCompleteWithAssumption",
                        puzzle));

        Assert.assertFalse(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveValuesArePossible/RootIncompleteNoAssumption",
                        puzzle));

        Assert.assertFalse(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveValuesArePossible/RootIncompleteWithAssumption",
                        puzzle));
    }

    /** Tests puzzle completion with a single path down the tree */
    @Test
    public void TestSinglePath() throws InvalidFileFormatException {
        Assert.assertTrue(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveValuesArePossible/CompleteLineWithAssumption",
                        puzzle));

        Assert.assertTrue(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveValuesArePossible/SolvedLine", puzzle));

        Assert.assertTrue(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveValuesArePossible/IncompleteLineWithMatchAndAssumption",
                        puzzle));

        Assert.assertFalse(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveValuesArePossible/CompleteLineNoAssumption", puzzle));

        Assert.assertFalse(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveValuesArePossible/IncompleteLineWithMatch", puzzle));

        Assert.assertFalse(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveValuesArePossible/ContradictoryLine", puzzle));

        Assert.assertFalse(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveValuesArePossible/IncompleteLineWithoutMatch",
                        puzzle));
    }

    /** Tests puzzle completion with a multiple paths and contradictory paths */
    @Test
    public void TestComplexPaths() throws InvalidFileFormatException {
        Assert.assertTrue(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveValuesArePossible/ClosedAndFinished", puzzle));

        Assert.assertTrue(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveValuesArePossible/MergedSolutions", puzzle));

        Assert.assertTrue(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveValuesArePossible/UnmergedSolutions", puzzle));

        Assert.assertTrue(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveValuesArePossible/MultipleSolutionsOneCorrect",
                        puzzle));

        Assert.assertTrue(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveValuesArePossible/SingleSolutionWithMatchAndUnfinished",
                        puzzle));

        Assert.assertFalse(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveValuesArePossible/MultipleSolutionsOneMatchEach",
                        puzzle));

        Assert.assertFalse(
                TestUtilities.verifyBoard(
                        "goalConditions/ProveValuesArePossible/SingleSolutionAndUnfinishedWithMatch",
                        puzzle));
    }
}
