package puzzles.minesweeper;

import edu.rpi.legup.puzzle.minesweeper.Minesweeper;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperBoard;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperCell;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperUtilities;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.util.stream.Stream;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class MinesweeperUtilitiesTest {

    private static Minesweeper minesweeper;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        minesweeper = new Minesweeper();
    }

    @Test
    public void getSurroundingCellsSizeThreeByThreeAtOneXOneTest()
            throws InvalidFileFormatException {

        TestUtilities.importTestBoard("puzzles/minesweeper/utilities/3x3test", minesweeper);

        final MinesweeperBoard board = (MinesweeperBoard) minesweeper.getCurrentBoard();
        MinesweeperCell cell = board.getCell(1, 1);

        final Stream<MinesweeperCell> cells = MinesweeperUtilities.getSurroundingCells(board, cell);

        final long count = cells.count();
        Assert.assertEquals(count, 8);
    }

    @Test
    public void getSurroundingCellsSizeThreeByThreeAtZeroXZeroTest()
            throws InvalidFileFormatException {

        TestUtilities.importTestBoard("puzzles/minesweeper/utilities/3x3test", minesweeper);

        final MinesweeperBoard board = (MinesweeperBoard) minesweeper.getCurrentBoard();
        MinesweeperCell cell = board.getCell(0, 0);

        final Stream<MinesweeperCell> cells = MinesweeperUtilities.getSurroundingCells(board, cell);

        final long count = cells.count();
        Assert.assertEquals(count, 3);
    }

    @Test
    public void getSurroundingCellsSizeThreeByThreeAtZeroXOneTest()
            throws InvalidFileFormatException {

        TestUtilities.importTestBoard("puzzles/minesweeper/utilities/3x3test", minesweeper);

        final MinesweeperBoard board = (MinesweeperBoard) minesweeper.getCurrentBoard();
        MinesweeperCell cell = board.getCell(0, 1);

        final Stream<MinesweeperCell> cells = MinesweeperUtilities.getSurroundingCells(board, cell);

        final long count = cells.count();
        Assert.assertEquals(count, 5);
    }
}
