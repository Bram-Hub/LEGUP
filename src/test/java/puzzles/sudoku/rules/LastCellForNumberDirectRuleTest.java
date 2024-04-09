package puzzles.sudoku.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.sudoku.Sudoku;
import edu.rpi.legup.puzzle.sudoku.SudokuBoard;
import edu.rpi.legup.puzzle.sudoku.SudokuCell;
import edu.rpi.legup.puzzle.sudoku.rules.LastNumberForCellDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class LastCellForNumberDirectRuleTest {
    private static final LastNumberForCellDirectRule RULE = new LastNumberForCellDirectRule();
    private static Sudoku sudoku;
    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        sudoku = new Sudoku();
    }
    @Test
    public void LastNumberForCellDirectRuleTest() throws InvalidFileFormatException {
        //Import board and create transition

        TestUtilities.importTestBoard("puzzles/sudoku/rules/LasCellForNumberDirectRule/TestBoard", sudoku);
        TreeNode rootNode = sudoku.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        for(int i = 1; i < 10; i++){
            //Reset board
            SudokuBoard board = (SudokuBoard) transition.getBoard();
            //Set cell
            SudokuCell cell1 = board.getCell(2, 2);
            cell1.setData(i);
            board.addModifiedData(cell1);

            //Test the case
            if(i == 9) {
                Assert.assertNotNull(RULE.checkRuleAt(transition, cell1));
            } else {
                Assert.assertNull(RULE.checkRuleAt(transition, cell1));
            }
        }

    }
}
