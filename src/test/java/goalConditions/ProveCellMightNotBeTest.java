package goalConditions;

import puzzles.testpuzzle.TestPuzzle;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProveCellMightNotBeTest {
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
                "goalConditions/ProveCellMightNotBe/InvalidLine", puzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveCellMightNotBe/InvalidContradiction", puzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveCellMightNotBe/InvalidCases", puzzle));
    }


    /** Tests puzzle completion with only a root node*/
    @Test
    public void TestRootNode() throws InvalidFileFormatException {
        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/ProveCellMightNotBe/RootComplete", puzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveCellMightNotBe/RootIncomplete", puzzle));
    }


    /** Tests puzzle completion with a single path down the tree*/
    @Test
    public void TestSinglePath() throws InvalidFileFormatException {
        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/ProveCellMightNotBe/CompleteLine", puzzle));

        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/ProveCellMightNotBe/OvercompleteLine", puzzle));

        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/ProveCellMightNotBe/ContradictoryLine", puzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveCellMightNotBe/IncompleteLine", puzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveCellMightNotBe/SomeUnproven", puzzle));
    }


    /** Tests puzzle completion with a multiple paths and contradictory paths*/
    @Test
    public void TestComplexPaths() throws InvalidFileFormatException {
        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/ProveCellMightNotBe/SimpleContradiction", puzzle));

        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/ProveCellMightNotBe/MergedSolutions", puzzle));

        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/ProveCellMightNotBe/UnmergedSolutions", puzzle));

        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/ProveCellMightNotBe/SingleSolutionAndUnfinished", puzzle));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "goalConditions/ProveCellMightNotBe/PossibleBranch", puzzle));
    }
}

