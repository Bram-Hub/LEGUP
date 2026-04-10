package goalConditions;

import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import puzzles.testpuzzle.TestPuzzle;

public class ProveMultipleCellValueTest {
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
                "goalConditions/ProveMultipleCellValue/InvalidLine", puzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveMultipleCellValue/InvalidContradiction", puzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveMultipleCellValue/InvalidCases", puzzle));
    }

    
    /** Tests puzzle completion with only a root node*/
    @Test
    public void TestRootNode() throws InvalidFileFormatException {
        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveMultipleCellValue/RootIncomplete", puzzle));
    }


    /** Tests puzzle completion with a single path down the tree*/
    @Test
    public void TestSinglePath() throws InvalidFileFormatException {
        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveMultipleCellValue/ContradictoryLine", puzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveMultipleCellValue/IncompleteLine", puzzle));

    }


    /** Tests puzzle completion with a multiple paths and contradictory paths */
    @Test
    public void TestComplexPaths() throws InvalidFileFormatException {
        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/ProveMultipleCellValue/CompleteDiffering", puzzle));

        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/ProveMultipleCellValue/ThreeLines", puzzle));

        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/ProveMultipleCellValue/ThreeLinesTwoMatch", puzzle));

        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/ProveMultipleCellValue/OneUnmatchedCellOfMany", puzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveMultipleCellValue/CompleteUndiffering", puzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveMultipleCellValue/IncompleteDiffering", puzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveMultipleCellValue/IncompleteUndiffering", puzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveMultipleCellValue/SingleSolutionAndUnfinished", puzzle));
    }
}

