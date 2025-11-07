package puzzles.nurikabe.goalConditions;

import edu.rpi.legup.puzzle.nurikabe.Nurikabe;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProveCellMustBeTest {
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
                "puzzles/nurikabe/goalConditions/ProveCellMustBe/RootComplete", nurikabe));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "puzzles/nurikabe/goalConditions/ProveCellMustBe/RootIncomplete", nurikabe));
    }


    /** Tests puzzle completion with a single path down the tree*/
    @Test
    public void TestSinglePath() throws InvalidFileFormatException {
        Assert.assertTrue(TestUtilities.verifyBoard(
                "puzzles/nurikabe/goalConditions/ProveCellMustBe/CompleteLine", nurikabe));

        Assert.assertTrue(TestUtilities.verifyBoard(
                "puzzles/nurikabe/goalConditions/ProveCellMustBe/OvercompleteLine", nurikabe));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "puzzles/nurikabe/goalConditions/ProveCellMustBe/IncompleteLine", nurikabe));

        Assert.assertFalse(TestUtilities.verifyBoard(
                "puzzles/nurikabe/goalConditions/ProveCellMustBe/IncorrectLine", nurikabe));
    }
    
}

