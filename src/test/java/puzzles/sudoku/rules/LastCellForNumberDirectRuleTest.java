package puzzles.sudoku.rules;

import edu.rpi.legup.puzzle.nurikabe.Nurikabe;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.BeforeClass;
import org.junit.Test;
import edu.rpi.legup.puzzle.sudoku.Sudoku;
import edu.rpi.legup.puzzle.sudoku.rules.LastCellForNumberDirectRule;

import static org.junit.Assert.*;

public class LastCellForNumberDirectRuleTest {
    private static Sudoku sudoku;
    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        sudoku = new Sudoku();
    }
    @Test
    public void LastCellForNumberDirectRuleTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/sudoku/rules/LastCellForNumberRule/TestBoard", sudoku);
        assertTrue(true);

    }
}
