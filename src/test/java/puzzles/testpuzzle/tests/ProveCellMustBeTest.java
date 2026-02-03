package puzzles.testpuzzle.tests;

import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import puzzles.testpuzzle.TestPuzzle;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProveCellMustBeTest {
    private static TestPuzzle puzzle;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        puzzle = new TestPuzzle();
    }

    /** Tests puzzle completion with only a root node*/
    @Test
    public void TestRootNode() throws InvalidFileFormatException {
        Assert.assertTrue(TestUtilities.verifyBoard(
                "goalConditions/ProveCellMustBe/TestPuzzleCheck", puzzle));
    }

}

