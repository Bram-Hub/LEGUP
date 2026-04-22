package edu.rpi.legup.puzzle.starbattle;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;

public class StarBattle extends Puzzle {

    /**
     * Constructs a StarBattle puzzle and initializes its components such as importer,
     * exporter, and cell factory.
     */
    public StarBattle() {
        super();
        this.name = "StarBattle";

        this.importer = new StarBattleImporter(this);
        this.exporter = new StarBattleExporter(this);

        this.factory = new StarBattleCellFactory();
    }

    /**
     * Initializes the view for the current board and registers it as a listener.
     */
    @Override
    public void initializeView() {
        boardView = new StarBattleView((StarBattleBoard) currentBoard);
        addBoardListener(boardView);
    }

    /**
     * Generates a StarBattle puzzle based on the specified difficulty.
     *
     * @param difficulty the difficulty level of the puzzle
     * @return a generated Board, or null if not implemented
     */
    @Override
    public Board generatePuzzle(int difficulty) {
        return null;
    }

    /**
     * Determines whether the given board is complete.
     *
     * @param board the board to check
     * @return true if the board is complete, false otherwise
     */
    @Override
    public boolean isBoardComplete(Board board) {
        return true;
    }

    /**
     * Handles logic that should occur when the board changes.
     *
     * @param board the updated board
     */
    @Override
    public void onBoardChange(Board board) {}
}
