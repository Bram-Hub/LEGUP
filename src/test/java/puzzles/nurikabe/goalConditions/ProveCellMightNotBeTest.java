package puzzles.nurikabe.goalConditions;

import edu.rpi.legup.puzzle.nurikabe.Nurikabe;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProveCellMightNotBeTest {
    private static Nurikabe nurikabe;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        nurikabe = new Nurikabe();
    }

    /** Tests puzzle completion with only a root node*/
    @Test
    public void TestRootNode() throws InvalidFileFormatException {
        Assert.assertTrue(TestUtilities.verifyBoard(
                "puzzles/nurikabe/goalConditions/ProveCellMightNotBe/RootComplete", nurikabe));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "puzzles/nurikabe/goalConditions/ProveCellMightNotBe/RootIncomplete", nurikabe));
    }


    /** Tests puzzle completion with a single path down the tree*/
    @Test
    public void TestSinglePath() throws InvalidFileFormatException {
        Assert.assertTrue(TestUtilities.verifyBoard(
                "puzzles/nurikabe/goalConditions/ProveCellMightNotBe/CompleteLine", nurikabe));

        Assert.assertTrue(TestUtilities.verifyBoard(
                "puzzles/nurikabe/goalConditions/ProveCellMightNotBe/OvercompleteLine", nurikabe));

        Assert.assertTrue(TestUtilities.verifyBoard(
                "puzzles/nurikabe/goalConditions/ProveCellMightNotBe/ContradictoryLine", nurikabe));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "puzzles/nurikabe/goalConditions/ProveCellMightNotBe/IncompleteLine", nurikabe));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "puzzles/nurikabe/goalConditions/ProveCellMightNotBe/SomeUnproven", nurikabe));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "puzzles/nurikabe/goalConditions/ProveCellMightNotBe/InvalidLine", nurikabe));
    }


    /** Tests puzzle completion with a multiple paths and contradictory paths*/
    @Test
    public void TestComplexPaths() throws InvalidFileFormatException {
        Assert.assertTrue(TestUtilities.verifyBoard(
                "puzzles/nurikabe/goalConditions/ProveCellMightNotBe/SimpleContradiction", nurikabe));

        Assert.assertTrue(TestUtilities.verifyBoard(
                "puzzles/nurikabe/goalConditions/ProveCellMightNotBe/MergedSolutions", nurikabe));

        Assert.assertTrue(TestUtilities.verifyBoard(
                "puzzles/nurikabe/goalConditions/ProveCellMightNotBe/UnmergedSolutions", nurikabe));

        Assert.assertTrue(TestUtilities.verifyBoard(
                "puzzles/nurikabe/goalConditions/ProveCellMightNotBe/SingleSolutionAndUnfinished", nurikabe));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "puzzles/nurikabe/goalConditions/ProveCellMightNotBe/PossibleBranch", nurikabe));
    }
}

