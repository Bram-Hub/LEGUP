package puzzles.sudoku.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.sudoku.Sudoku;
import edu.rpi.legup.puzzle.sudoku.SudokuBoard;
import edu.rpi.legup.puzzle.sudoku.SudokuCell;
import edu.rpi.legup.puzzle.sudoku.rules.RepeatedNumberContradictionRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class RepeatedNumberContradictionRuleTest {
    private static final RepeatedNumberContradictionRule RULE =
            new RepeatedNumberContradictionRule();
    private static Sudoku sudoku;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        sudoku = new Sudoku();
    }

    @Test
    public void RepeatedNumberContradictionRule_GlobalTest() throws InvalidFileFormatException {
        // Import board and create transition
        TestUtilities.importTestBoard(
                "puzzles/sudoku/rules/RepeatedNumberContradictionRule/BlankBoard7", sudoku);
        TreeNode rootNode = sudoku.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        // Loop through every cell
        for (int i = 0; i < 81; i++) {
            // Reset board
            SudokuBoard board = (SudokuBoard) transition.getBoard();
            // Set cell
            int x = i / 9;
            int y = i % 9;
            if (x == 0 && y == 0) {
                continue;
            }
            SudokuCell cell = board.getCell(x, y);
            cell.setData(7);

            // Test the case
            if (x == 0 || y == 0 || (x < 3 && y < 3)) {
                Assert.assertNull(RULE.checkRuleAt(transition, cell));
            } else {
                Assert.assertNotNull(RULE.checkRuleAt(transition, cell));
            }
            cell.setData(0);
        }
        // Import board and create transition
        TestUtilities.importTestBoard(
                "puzzles/sudoku/rules/RepeatedNumberContradictionRule/BlankBoard4", sudoku);
        rootNode = sudoku.getTree().getRootNode();
        transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        // Loop through every cell
        for (int i = 0; i < 81; i++) {
            // Reset board
            SudokuBoard board = (SudokuBoard) transition.getBoard();
            // Set cell
            int x = i / 9;
            int y = i % 9;
            if ((x == 3 && y == 0) || (x == 6 && y == 8)) {
                continue;
            }
            SudokuCell cell = board.getCell(x, y);
            cell.setData(4);

            // Test the case
            if ((x == 3 || y == 0 || x == 6 || y == 8)
                    || (x > 2 && x < 6 && y < 3)
                    || (x > 5 && y > 5)) {
                Assert.assertNull(RULE.checkRuleAt(transition, cell));
            } else {
                Assert.assertNotNull(RULE.checkRuleAt(transition, cell));
            }
            cell.setData(0);
        }
    }
}
