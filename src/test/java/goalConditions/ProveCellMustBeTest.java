package goalConditions;

import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import puzzles.testpuzzle.TestPuzzle;

public class ProveCellMustBeTest {
    private static TestPuzzle testPuzzle;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        testPuzzle = new TestPuzzle();
    }

    /** Tests puzzle completion with only a root node*/
    @Test
    public void TestRootNode() throws InvalidFileFormatException {
        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/ProveCellMustBe/RootComplete", testPuzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveCellMustBe/RootIncomplete", testPuzzle));
    }


    /** Tests puzzle completion with a single path down the tree*/
    @Test
    public void TestSinglePath() throws InvalidFileFormatException {
        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/ProveCellMustBe/CompleteLine", testPuzzle));

        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/ProveCellMustBe/OvercompleteLine", testPuzzle));

        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/ProveCellMustBe/ContradictoryLine", testPuzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveCellMustBe/IncompleteLine", testPuzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveCellMustBe/SomeUnproven", testPuzzle));
    }


    /** Tests puzzle completion with a multiple paths and contradictory paths */
    @Test
    public void TestComplexPaths() throws InvalidFileFormatException {
        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/ProveCellMustBe/SimpleContradiction", testPuzzle));

        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/ProveCellMustBe/MergedSolutions", testPuzzle));

        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/ProveCellMustBe/UnmergedSolutions", testPuzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveCellMustBe/SingleSolutionAndUnfinished", testPuzzle));
    }
}

