package edu.rpi.legup.puzzle.skyscrapers;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.RegisterPuzzle;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;

@RegisterPuzzle
public class Skyscrapers extends Puzzle {

    public Skyscrapers() {
        super();
        this.name = "Skyscrapers";

        /* Needs implementing
        this.importer = new SkyscrapersImporter(this);
        this.exporter = new SkyscrapersExporter(this);

        this.factory = new SkyscrapersFactory();
        */
    }
    /**
     *Initializes the game board. Called by the invoker of the class
     */
    @Override
    public void initializeView() {
        /* SkyscrapersView and SkyscrapersBoard need implementing
        boardView = new SkyscrapersView((SkyscrapersBoard) currentBoard);
        */
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
        SkyscrapersBoard skyscrapersBoard = (SkyscrapersBoard) board;
        //do something with the board
        
        for (ContradictionRule rule : contradictionRules) {
            if (rule.checkContradiction(skyscrapersBoard) == null){
                return false;
            }
        }
        for (PuzzleElement data : skyscrapersBoard.getPuzzleElements()) {
            SkyscrapersCell cell = (SkyscrapersCell) data;
            //check elements
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
