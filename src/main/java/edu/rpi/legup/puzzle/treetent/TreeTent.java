package edu.rpi.legup.puzzle.treetent;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import java.util.List;

public class TreeTent extends Puzzle {

    public TreeTent() {
        super();

        this.name = "TreeTent";

        this.importer = new TreeTentImporter(this);
        this.exporter = new TreeTentExporter(this);

        this.factory = new TreeTentCellFactory();
    }

    /** Initializes the game board. Called by the invoker of the class */
    @Override
    public void initializeView() {
        TreeTentBoard board = (TreeTentBoard) currentBoard;
        boardView = new TreeTentView((TreeTentBoard) currentBoard);
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
     * Determines if the given dimensions are valid for Tree Tent
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
        TreeTentBoard treeTentBoard = (TreeTentBoard) board;

        for (ContradictionRule rule : contradictionRules) {
            if (rule.checkContradiction(treeTentBoard) == null) {
                return false;
            }
        }
        for (PuzzleElement data : treeTentBoard.getPuzzleElements()) {
            TreeTentCell cell = (TreeTentCell) data;
            if (cell.getType() == TreeTentType.UNKNOWN) {
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
     * @return if it is valid TreeTent puzzle must have same number of clues as the dimension size
     */
    @Override
    public boolean checkValidity() {
        TreeTentBoard b = (TreeTentBoard) this.getBoardView().getBoard();
        List<PuzzleElement> elements = b.getPuzzleElements();
        int treeCount = 0;
        for (PuzzleElement element : elements) {
            TreeTentCell c = (TreeTentCell) element;
            if (c.getType() == TreeTentType.TREE) {
                treeCount++;
            }
        }
        return treeCount != 0;
    }
}
