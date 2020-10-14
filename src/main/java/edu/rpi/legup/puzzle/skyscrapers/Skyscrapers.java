package edu.rpi.legup.puzzle.skyscrapers;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;

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

    /**
     * Determines if the current board is a valid state
     *
     * @param board board to check for validity
     * @return true if board is valid, false otherwise
     */
    @Override
    public boolean isBoardComplete(Board board) {
        return false;
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
