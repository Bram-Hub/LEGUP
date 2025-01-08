package puzzles.minesweeper;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.minesweeper.Minesweeper;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperBoard;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperCell;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperTileData;
import edu.rpi.legup.puzzle.minesweeper.rules.SatisfyNumberCaseRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;

public class SatisfyNumberCaseRuleTest {

    private static final SatisfyNumberCaseRule RULE = new SatisfyNumberCaseRule();
    private static Minesweeper minesweeper;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        minesweeper = new Minesweeper();
    }

    /**
     * Tests the Satisfy Number case rule by ensuring that it results in all possibilities
     * for the number. This case tests a number 2 with three unset cells around it, so each
     * case must replace the unset tiles with a different arrangement of two bombs and one empty.
     */
    @Test
    public void SatisfyNumberCaseRuleTest1()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/minesweeper/rules/SatisfyNumber.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();
        MinesweeperCell cell = board.getCell(1, 1);
        ArrayList<Board> cases = RULE.getCases(board, cell);

        Assert.assertEquals(3, cases.size());

        MinesweeperBoard caseBoard = (MinesweeperBoard) cases.get(0);
        MinesweeperBoard caseBoard2 = (MinesweeperBoard) cases.get(1);
        MinesweeperBoard caseBoard3 = (MinesweeperBoard) cases.get(2);

        MinesweeperTileData board1Tile1 = caseBoard.getCell(0, 1).getData();
        MinesweeperTileData board1Tile2 = caseBoard.getCell(2, 1).getData();
        MinesweeperTileData board1Tile3 = caseBoard.getCell(1, 2).getData();
        MinesweeperTileData board2Tile1 = caseBoard2.getCell(0, 1).getData();
        MinesweeperTileData board2Tile2 = caseBoard2.getCell(2, 1).getData();
        MinesweeperTileData board2Tile3 = caseBoard2.getCell(1, 2).getData();
        MinesweeperTileData board3Tile1 = caseBoard3.getCell(0, 1).getData();
        MinesweeperTileData board3Tile2 = caseBoard3.getCell(2, 1).getData();
        MinesweeperTileData board3Tile3 = caseBoard3.getCell(1, 2).getData();

        Assert.assertTrue((board1Tile1.equals(MinesweeperTileData.mine()) && board1Tile2.equals(MinesweeperTileData.mine())
        && board1Tile3.equals(MinesweeperTileData.empty()))
        || (board1Tile1.equals(MinesweeperTileData.mine()) && board1Tile2.equals(MinesweeperTileData.empty()) && board1Tile3.equals(MinesweeperTileData.mine()))
        || ((board1Tile1.equals(MinesweeperTileData.empty()) && board1Tile2.equals(MinesweeperTileData.mine()) && board1Tile3.equals(MinesweeperTileData.mine()))));

        Assert.assertTrue((board2Tile1.equals(MinesweeperTileData.mine()) && board2Tile2.equals(MinesweeperTileData.mine())
        && board2Tile3.equals(MinesweeperTileData.empty()))
        || (board2Tile1.equals(MinesweeperTileData.mine()) && board2Tile2.equals(MinesweeperTileData.empty()) && board2Tile3.equals(MinesweeperTileData.mine()))
        || ((board2Tile1.equals(MinesweeperTileData.empty()) && board2Tile2.equals(MinesweeperTileData.mine()) && board2Tile3.equals(MinesweeperTileData.mine()))));

        Assert.assertTrue((board3Tile1.equals(MinesweeperTileData.mine()) && board3Tile2.equals(MinesweeperTileData.mine())
        && board3Tile3.equals(MinesweeperTileData.empty()))
        || (board3Tile1.equals(MinesweeperTileData.mine()) && board3Tile2.equals(MinesweeperTileData.empty()) && board3Tile3.equals(MinesweeperTileData.mine()))
        || ((board3Tile1.equals(MinesweeperTileData.empty()) && board3Tile2.equals(MinesweeperTileData.mine()) && board3Tile3.equals(MinesweeperTileData.mine()))));

        Assert.assertFalse(board1Tile1.equals(board2Tile1) && board1Tile1.equals(board3Tile1));
        Assert.assertFalse(board1Tile2.equals(board2Tile2) && board1Tile2.equals(board3Tile2));
        Assert.assertFalse(board1Tile3.equals(board2Tile3) && board1Tile3.equals(board3Tile3));

        Assert.assertEquals(caseBoard.getHeight(), caseBoard2.getHeight(), caseBoard3.getHeight());
        Assert.assertEquals(caseBoard.getHeight(), board.getHeight());
        Assert.assertEquals(caseBoard.getWidth(), caseBoard2.getWidth(), caseBoard3.getWidth());
        Assert.assertEquals(caseBoard.getWidth(), board.getWidth());

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
