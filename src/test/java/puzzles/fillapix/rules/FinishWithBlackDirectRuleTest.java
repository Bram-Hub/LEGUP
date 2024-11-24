package puzzles.fillapix.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.fillapix.*;
import edu.rpi.legup.puzzle.fillapix.rules.FinishWithBlackDirectRule;
import edu.rpi.legup.puzzle.fillapix.Fillapix;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.awt.*;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class FinishWithBlackDirectRuleTest{
    private static final FinishWithBlackDirectRule RULE =
            new FinishWithBlackDirectRule();
    private static Fillapix fillapix;

    @BeforeClass
    public static void setUp() {
        fillapix = new Fillapix();
    }

    @Test
    public void FinishSides() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/fillapix/rules/FinishWithBlackDirectRule/FinishSides.txt", fillapix);
        TreeNode rootNode = fillapix.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);
        // get board state
        FillapixBoard board = (FillapixBoard) transition.getBoard();

        /*// change the board's cells considering the TouchingSides rule
        //(3 black cells to the left of 6 and 3 white cells to the right of 3)
        FillapixCell cell1 = board.getCell(0, 0);
        cell1.setData(FillapixCellType.WHITE.value);
        board.addModifiedData(cell1);

        FillapixCell cell2 = board.getCell(0, 1);
        cell2.setData(FillapixCellType.WHITE.value);
        board.addModifiedData(cell2);

        FillapixCell cell3 = board.getCell(0, 2);
        cell3.setData(FillapixCellType.WHITE.value);
        board.addModifiedData(cell3);

        FillapixCell cell4 = board.getCell(1, 0);
        cell4.setData(FillapixCellType.BLACK.value);
        board.addModifiedData(cell4);

        FillapixCell cell5 = board.getCell(1, 2);
        cell5.setData(FillapixCellType.BLACK.value);
        board.addModifiedData(cell5);

        FillapixCell cell6 = board.getCell(2, 0);
        cell6.setData(FillapixCellType.BLACK.value);
        board.addModifiedData(cell6);

        FillapixCell cell7 = board.getCell(2, 1);
        cell7.setData(FillapixCellType.BLACK.value);
        board.addModifiedData(cell7);

        FillapixCell cell8 = board.getCell(2, 2);
        cell8.setData(FillapixCellType.BLACK.value);
        board.addModifiedData(cell8);

        FillapixCell cell9 = board.getCell(1, 1);
        cell9.setData(FillapixCellType.BLACK.value);
        board.addModifiedData(cell4);
    */
        // confirm there is a logical following of the TouchingSides rule
        Assert.assertNull(RULE.checkRule(transition));
    }
}
