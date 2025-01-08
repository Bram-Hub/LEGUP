package puzzles.minesweeper;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.minesweeper.Minesweeper;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperBoard;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperCell;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperTileData;
import edu.rpi.legup.puzzle.minesweeper.rules.MineOrEmptyCaseRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;

public class MineOrEmptyCaseRuleTest {

    private static final MineOrEmptyCaseRule RULE = new MineOrEmptyCaseRule();
    private static Minesweeper minesweeper;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        minesweeper = new Minesweeper();
    }

    /**
     * Tests the Mine or Empty case rule by ensuring that it results in two children, that contain
     * a modified cell that is either mine or empty
     */
    @Test
    public void MineOrEmptyCaseRuleTest1()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/minesweeper/rules/MineOrEmpty.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();
        MinesweeperCell cell = board.getCell(0, 0);
        ArrayList<Board> cases = RULE.getCases(board, cell);

        Assert.assertEquals(2, cases.size());

        MinesweeperBoard caseBoard = (MinesweeperBoard) cases.get(0);
        MinesweeperBoard caseBoard2 = (MinesweeperBoard) cases.get(1);

        MinesweeperTileData board1Type = caseBoard.getCell(0, 0).getData();
        MinesweeperTileData board2Type = caseBoard2.getCell(0, 0).getData();

        Assert.assertTrue(
                ((board1Type.equals(MinesweeperTileData.mine()) || board1Type.equals(MinesweeperTileData.empty())))
                        && (board2Type.equals(MinesweeperTileData.mine()) || board2Type.equals(MinesweeperTileData.empty())));
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
