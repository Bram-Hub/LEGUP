package puzzles.testpuzzle;

import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.controller.ElementController;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.GridBoard;
import edu.rpi.legup.model.gameboard.GridCell;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.ui.boardview.GridBoardView;

public class TestPuzzle extends Puzzle {
    public TestPuzzle() {
        super();

        this.name = "TestPuzzle";

        this.importer = new TestPuzzleImporter(this);
        this.exporter = new TestPuzzleExporter(this);

        this.factory = new TestPuzzleCellFactory();
    }

    @Override
    public void initializeView() {
        boardView = new GridBoardView(new BoardController(), new ElementController(), ((GridBoard) currentBoard).getDimension());
        boardView.setBoard(currentBoard);
        addBoardListener(boardView);
    }

    @Override
    public Board generatePuzzle(int difficulty) {
        return null;
    }

    @Override
    public boolean isBoardComplete(Board board) {
        GridBoard gridBoard = (GridBoard) board;
        for (PuzzleElement data : gridBoard.getPuzzleElements()) {
            GridCell cell = (GridCell) data;
            if (!cell.isKnown()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onBoardChange(Board board) {

    }
}