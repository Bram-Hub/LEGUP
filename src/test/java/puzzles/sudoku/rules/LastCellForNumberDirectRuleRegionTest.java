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
    @Test
    public void basicSpotTest() throws InvalidFileFormatException{
        TestUtilities.importTestBoard(
                "puzzles/sudoku/rules/LastCellForNumberDirectRule/OnePossible", sudoku
                );
        TreeNode rootNode = sudoku.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        for(int i = 1; i <= 9; ++i) {
            SudokuBoard board = (SudokuBoard) transition.getBoard();
            SudokuCell cell = board.getCell(1,1);

            cell.setData(i);
            board.addModifiedData(cell);
            if(i == 5) {
                Assert.assertNull(RULE.checkRuleAt(transition, cell));
            }
            else {
                Assert.assertNotNull(RULE.checkRuleAt(transition, cell));
            }

        }

    }

}