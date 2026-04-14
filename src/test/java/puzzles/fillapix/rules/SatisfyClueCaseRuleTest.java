package puzzles.fillapix.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.fillapix.Fillapix;
import edu.rpi.legup.puzzle.fillapix.FillapixBoard;
import edu.rpi.legup.puzzle.fillapix.FillapixCell;
import edu.rpi.legup.puzzle.fillapix.FillapixTileData;
import edu.rpi.legup.puzzle.fillapix.FillapixCellType;
import edu.rpi.legup.puzzle.fillapix.rules.SatisfyClueCaseRule;
import edu.rpi.legup.save.InvalidFileFormatException;

import java.awt.*;
import java.util.ArrayList;

import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class SatisfyClueCaseRuleTest {

    private static final SatisfyClueCaseRule RULE = new SatisfyClueCaseRule();
    private static Fillapix fillapix;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        fillapix = new Fillapix();
    }

    /**
     * Tests Satisfy Clue Case Rule on a clue 2 with 3 unknown neighbors.
     */
    @Test
    public void SatisfyClueCaseRuleTest1() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/fillapix/rules/SatisfyClueCaseRule/SatisfyClue.txt", fillapix);
        TreeNode rootNode = fillapix.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        FillapixBoard board = (FillapixBoard) transition.getBoard();
        FillapixCell cell = board.getCell(1, 1);
        ArrayList<Board> cases = RULE.getCases(board, cell);

        Assert.assertEquals(3, cases.size());

        FillapixBoard caseBoard1 = (FillapixBoard) cases.get(0);
        FillapixBoard caseBoard2 = (FillapixBoard) cases.get(1);
        FillapixBoard caseBoard3 = (FillapixBoard) cases.get(2);


        FillapixCellType a1 = caseBoard1.getCell(0, 1).getType();
        FillapixCellType b1 = caseBoard1.getCell(2, 1).getType();
        FillapixCellType c1 = caseBoard1.getCell(1, 2).getType();

        FillapixCellType a2 = caseBoard2.getCell(0, 1).getType();
        FillapixCellType b2 = caseBoard2.getCell(2, 1).getType();
        FillapixCellType c2 = caseBoard2.getCell(1, 2).getType();

        FillapixCellType a3 = caseBoard3.getCell(0, 1).getType();
        FillapixCellType b3 = caseBoard3.getCell(2, 1).getType();
        FillapixCellType c3 = caseBoard3.getCell(1, 2).getType();


        Assert.assertTrue(
                (a1==FillapixCellType.BLACK && b1==FillapixCellType.BLACK && c1==FillapixCellType.WHITE)
                        || (a1==FillapixCellType.BLACK && b1==FillapixCellType.WHITE && c1==FillapixCellType.BLACK)
                        || (a1==FillapixCellType.WHITE && b1==FillapixCellType.BLACK && c1==FillapixCellType.BLACK));

        Assert.assertTrue(
                (a2==FillapixCellType.BLACK && b2==FillapixCellType.BLACK && c2==FillapixCellType.WHITE)
                        || (a2==FillapixCellType.BLACK && b2==FillapixCellType.WHITE && c2==FillapixCellType.BLACK)
                        || (a2==FillapixCellType.WHITE && b2==FillapixCellType.BLACK && c2==FillapixCellType.BLACK));

        Assert.assertTrue(
                (a3==FillapixCellType.BLACK && b3==FillapixCellType.BLACK && c3==FillapixCellType.WHITE)
                        || (a3==FillapixCellType.BLACK && b3==FillapixCellType.WHITE && c3==FillapixCellType.BLACK)
                        || (a3==FillapixCellType.WHITE && b3==FillapixCellType.BLACK && c3==FillapixCellType.BLACK));


        Assert.assertFalse(a1.equals(a2) && a1.equals(a3));
        Assert.assertFalse(b1.equals(b2) && b1.equals(b3));
        Assert.assertFalse(c1.equals(c2) && c1.equals(c3));


        Assert.assertEquals(board.getHeight(), caseBoard1.getHeight());
        Assert.assertEquals(board.getWidth(), caseBoard1.getWidth());
        Assert.assertEquals(board.getHeight(), caseBoard2.getHeight());
        Assert.assertEquals(board.getWidth(), caseBoard2.getWidth());
        Assert.assertEquals(board.getHeight(), caseBoard3.getHeight());
        Assert.assertEquals(board.getWidth(), caseBoard3.getWidth());


        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                Point p = new Point(col, row);
                if (p.equals(new Point(0,1)) || p.equals(new Point(2,1)) || p.equals(new Point(1,2))) {
                    continue;
                }
                Assert.assertTrue(caseBoard1.getCell(col,row).equals(caseBoard2.getCell(col,row)));
            }
        }
    }
}
