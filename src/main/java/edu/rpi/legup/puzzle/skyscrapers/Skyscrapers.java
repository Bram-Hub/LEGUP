package edu.rpi.legup.puzzle.skyscrapers;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.lightup.LightUpBoard;
import edu.rpi.legup.puzzle.lightup.LightUpCell;
import edu.rpi.legup.puzzle.lightup.LightUpCellType;

public class Skyscrapers extends Puzzle {

    public Skyscrapers() {
        super();

        this.name = "Skyscrapers";

        this.importer = new SkyscrapersImporter(this);
        this.exporter = new SkyscrapersExporter(this);

        this.factory = new SkyscrapersCellFactory();
    }

    /**
     * Initializes the game board. Called by the invoker of the class
     */
    @Override
    public void initializeView() {
        SkyscrapersBoard board = (SkyscrapersBoard) currentBoard;
        boardView = new SkyscrapersView((SkyscrapersBoard) currentBoard);
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
     * Determines if the given dimensions are valid for Skyscrapers
     *
     * @param rows      the number of rows
     * @param columns   the number of columns
     * @return true if the given dimensions are valid for Skyscrapers, false otherwise
     */
    public boolean isValidDimensions(int rows, int columns) {
        // This is a placeholder, this method needs to be implemented
        throw new UnsupportedOperationException();
    }

    /**
     * Determines if the current board is a valid state
     *
     * @param board board to check for validity
     * @return true if board is valid, false otherwise
     */
    @Override
    public boolean isBoardComplete(Board board) {
        SkyscrapersBoard SkyscraperBoard = (SkyscrapersBoard) board;

        for (ContradictionRule rule : contradictionRules) {
            if (rule.checkContradiction(SkyscraperBoard) == null) {
                System.out.println(rule.getRuleName());
                return false;
            }
        }
        for (PuzzleElement data : SkyscraperBoard.getPuzzleElements()) {
            SkyscrapersCell cell = (SkyscrapersCell) data;
            if (cell.getType() == SkyscrapersType.UNKNOWN) {
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
    public void onBoardChange(Board board) {

    }
}
