package puzzles.fillapix.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.fillapix.Fillapix;
import edu.rpi.legup.puzzle.fillapix.FillapixBoard;
import edu.rpi.legup.puzzle.fillapix.FillapixCell;
import edu.rpi.legup.puzzle.fillapix.FillapixCellType;
import edu.rpi.legup.puzzle.fillapix.FillapixTileData;
import edu.rpi.legup.puzzle.fillapix.rules.BlackOrWhiteCaseRule;

import edu.rpi.legup.save.InvalidFileFormatException;

import java.awt.*;
import java.util.ArrayList;

import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;


public class BlackOrWhiteCaseRuleTest {
    private static final BlackOrWhiteCaseRule RULE = new BlackOrWhiteCaseRule();
    private static Fillapix fillapix;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        fillapix = new Fillapix();
    }

    /**
     * Tests the Mine or Empty case rule by ensuring that it results in two children, that contain a
     * modified cell that is either mine or empty
     */
    @Test
    public void BlackOrWhiteCaseRuleTest1() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/fillapix/rules/BlackOrWhiteCaseRule/BlackOrWhite.txt", fillapix);
        TreeNode rootNode = fillapix.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        FillapixBoard board = (FillapixBoard) transition.getBoard();
        FillapixCell cell = board.getCell(0, 0);
        ArrayList<Board> cases = RULE.getCases(board, cell);

        Assert.assertEquals(2, cases.size());

        FillapixBoard caseBoard = (FillapixBoard) cases.get(0);
        FillapixBoard caseBoard2 = (FillapixBoard) cases.get(1);

        FillapixCellType board1Type = caseBoard.getCell(0, 0).getType();
        FillapixCellType board2Type = caseBoard2.getCell(0, 0).getType();

        Assert.assertTrue(
                ((board1Type==FillapixCellType.BLACK
                        || board1Type==FillapixCellType.WHITE))
                        && (board2Type==FillapixCellType.BLACK
                        || board2Type==FillapixCellType.WHITE));
        Assert.assertFalse(board1Type.equals(board2Type));

        Assert.assertEquals(caseBoard.getHeight(), caseBoard2.getHeight(), board.getHeight());
        Assert.assertEquals(caseBoard.getWidth(), caseBoard2.getWidth(), board.getWidth());

        for (int i = 0; i < caseBoard.getHeight(); i++) {
            for (int k = 0; k < caseBoard.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(caseBoard.getCell(k, i).getLocation())) {
                    continue;
                }
                Assert.assertTrue(caseBoard.getCell(k, i).equals(caseBoard2.getCell(k, i)));
            }
        }
    }
}
