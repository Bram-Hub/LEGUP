package puzzles.sudoku.rules;

import edu.rpi.legup.puzzle.nurikabe.Nurikabe;
import legup.MockGameBoardFacade;
import org.junit.Test;
import edu.rpi.legup.puzzle.sudoku.Sudoku;

import static org.junit.Assert.*;

public class LastCellForNumberDirectRuleTest {
    private Sudoku sudoku;
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        sudoku = new Sudoku();
    }
    @Test
    public void LastCellForNumberDirectRuleTest() {
        assertTrue(true);
    }
}
