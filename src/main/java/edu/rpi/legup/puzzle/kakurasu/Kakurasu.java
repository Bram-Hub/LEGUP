package edu.rpi.legup.puzzle.kakurasu;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;

import java.util.List;

public class Kakurasu extends Puzzle {

    public Kakurasu() {
        super();

        this.name = "Kakurasu";

        this.importer = new KakurasuImporter(this);
        this.exporter = new KakurasuExporter(this);

        this.factory = new KakurasuCellFactory();
    }

    /** Initializes the game board. Called by the invoker of the class */
    @Override
    public void initializeView() {
        KakurasuBoard board = (KakurasuBoard) currentBoard;
        boardView = new KakurasuView((KakurasuBoard) currentBoard);
        boardView.setBoard(board);
    }

    /**
     * Generates a random edu.rpi.legup.puzzle based on the difficulty
     *
     * @param difficulty level of difficulty (1-10)
     * @return board of the random edu.rpi.legup.puzzle
     */
    @Override
    public Board generatePuzzle(int difficulty) {
        return null;
    }

    @Override
    /**
     * Determines if the given dimensions are valid for Kakurasu
     *
     * @param rows the number of rows
     * @param columns the number of columns
     * @return true if the given dimensions are valid for Tree Tent, false otherwise
     */
    public boolean isValidDimensions(int rows, int columns) {
        // This is a placeholder, this method needs to be implemented
        return rows > 0 && columns > 0;
    }

    /**
     * Determines if the current board is a valid state
     *
     * @param board board to check for validity
     * @return true if board is valid, false otherwise
     */
    @Override
    public boolean isBoardComplete(Board board) {
        KakurasuBoard kakurasuBoard = (KakurasuBoard) board;

        for (ContradictionRule rule : contradictionRules) {
            if (rule.checkContradiction(kakurasuBoard) == null) {
                return false;
            }
        }
        for (PuzzleElement data : kakurasuBoard.getPuzzleElements()) {
            KakurasuCell cell = (KakurasuCell) data;
            if (cell.getType() == KakurasuType.UNKNOWN) {
                return false;
            }
        }
        return true;
    }

    /**
     * Callback for when the board puzzleElement changes
     *
     * @param board the board that has changed
     */
    @Override
    public void onBoardChange(Board board) {}

    /**
     * @return if it is valid Kakurasu puzzle must have same number of clues as the dimension size
     */
    @Override
    public boolean checkValidity() {
        // TODO: Fix this function
        KakurasuBoard b = (KakurasuBoard) this.getBoardView().getBoard();
        List<PuzzleElement> elements = b.getPuzzleElements();
        int treeCount = 0;
        for (PuzzleElement element : elements) {
            KakurasuCell c = (KakurasuCell) element;
            if (c.getType() == KakurasuType.TREE) {
                treeCount++;
            }
        }
        return treeCount != 0;
    }
}
