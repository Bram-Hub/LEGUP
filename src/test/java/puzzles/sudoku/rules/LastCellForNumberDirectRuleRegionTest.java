package puzzles.sudoku.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.sudoku.Sudoku;
import edu.rpi.legup.puzzle.sudoku.SudokuBoard;
import edu.rpi.legup.puzzle.sudoku.SudokuCell;
import edu.rpi.legup.puzzle.sudoku.rules.LastCellForNumberDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class LastCellForNumberDirectRuleRegionTest {
    private static final LastCellForNumberDirectRule RULE = new LastCellForNumberDirectRule();
    private static Sudoku sudoku;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        sudoku = new Sudoku();
    }
    /*test ideas:
    -basic test (based off icon)
    -"staircase" test
    -false
    */

}