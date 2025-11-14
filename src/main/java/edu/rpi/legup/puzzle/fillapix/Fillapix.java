package edu.rpi.legup.puzzle.fillapix;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;

public class Fillapix extends Puzzle {
    /** Fillapix Constructor */
    public Fillapix() {
        super();

        this.name = "Fillapix";

        this.importer = new FillapixImporter(this);
        this.exporter = new FillapixExporter(this);

        this.factory = new FillapixCellFactory();
    }

    /** Initializes the game board */
    @Override
    public void initializeView() {
        boardView = new FillapixView((FillapixBoard) currentBoard);
        boardView.setBoard(currentBoard);
        addBoardListener(boardView);
    }

    @Override
    public Board generatePuzzle(int difficulty) {
        return null;
    }

    @Override
    /**
     * Determines if the given dimensions are valid for Fillapix
     *
     * @param rows the number of rows
     * @param columns the number of columns
     * @return true if the given dimensions are valid for Fillapix, false otherwise
     */
    public boolean isValidDimensions(int rows, int columns) {
        return super.isValidDimensions(rows, columns);
    }

    @Override
    public boolean isBoardComplete(Board board) {
        FillapixBoard fillapixBoard = (FillapixBoard) board;
        for (ContradictionRule rule : contradictionRules) {
            if (rule.checkContradiction(fillapixBoard) == null) {
                return false;
            }
        }
        for (PuzzleElement element : fillapixBoard.getPuzzleElements()) {
            FillapixCell cell = (FillapixCell) element;
            if (cell.getType() == FillapixCellType.UNKNOWN) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onBoardChange(Board board) {}
}
