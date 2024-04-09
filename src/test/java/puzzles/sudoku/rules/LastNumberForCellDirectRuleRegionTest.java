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

public class LastNumberForCellDirectRuleRegionTest {
    private static final LastNumberForCellDirectRule RULE = new LastNumberForCellDirectRule();
    private static Sudoku sudoku;
    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        sudoku = new Sudoku();
    }
    @Test
    public void LastNumberForCellDirectRule_FullRegionTest() throws InvalidFileFormatException {
        //Import board and create transition
        TestUtilities.importTestBoard("puzzles/sudoku/rules/LastNumberForCellDirectRule/FullRegion", sudoku);
        TreeNode rootNode = sudoku.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        //Loop all numbers at point
        for(int i = 1; i < 10; i++){
            //Reset board
            SudokuBoard board = (SudokuBoard) transition.getBoard();
            //Set cell
            SudokuCell cell1 = board.getCell(2, 5);
            cell1.setData(i);
            board.addModifiedData(cell1);

            //Test the case
            if(i == 9) {
                Assert.assertNull(RULE.checkRuleAt(transition, cell1));
            } else {
                Assert.assertNotNull(RULE.checkRuleAt(transition, cell1));
            }
        }

        //Import Board and create transition
        TestUtilities.importTestBoard("puzzles/sudoku/rules/LastNumberForCellDirectRule/FullRow", sudoku);
        rootNode = sudoku.getTree().getRootNode();
        transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        //Loop all numbers at point
        for(int i = 1; i < 10; i++){
            //Reset board
            SudokuBoard board = (SudokuBoard) transition.getBoard();
            //Set cell
            SudokuCell cell = board.getCell(4, 4);
            cell.setData(i);
            board.addModifiedData(cell);

            //Test the case
            if(i == 5) {
                Assert.assertNull(RULE.checkRuleAt(transition, cell));
            } else {
                Assert.assertNotNull(RULE.checkRuleAt(transition, cell));
            }
        }

        //Import Board and create transition
        TestUtilities.importTestBoard("puzzles/sudoku/rules/LastNumberForCellDirectRule/FullMixed", sudoku);
        rootNode = sudoku.getTree().getRootNode();
        transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        //Loop all numbers at point
        for(int i = 1; i < 10; i++){
            //Reset board
            SudokuBoard board = (SudokuBoard) transition.getBoard();
            //Set cell
            SudokuCell cell = board.getCell(5, 3);
            cell.setData(i);
            board.addModifiedData(cell);

            //Test the case
            if(i == 2) {
                Assert.assertNull(RULE.checkRuleAt(transition, cell));
            } else {
                Assert.assertNotNull(RULE.checkRuleAt(transition, cell));
            }
        }
    }
}
