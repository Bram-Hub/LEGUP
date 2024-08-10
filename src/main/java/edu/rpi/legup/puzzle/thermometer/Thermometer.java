package edu.rpi.legup.puzzle.thermometer;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

// basically just copy-pasted from dev guide on wiki
public class Thermometer extends Puzzle {
    public Thermometer() {
        super();

        this.name = "Thermometer";

        this.importer = new ThermometerImporter(this);
        this.exporter = new ThermometerExporter(this);
        // we do not have a thermometerCellFactory class as
        // thermometerVial has its own thermometerCell factory method
    }

    /** Initializes the game board. Called by the invoker of the class */
    @Override
    @Contract(pure = false)
    public void initializeView() {
        boardView = new ThermometerView((ThermometerBoard) currentBoard);
        boardView.setBoard(currentBoard);
        addBoardListener(boardView);
    }

    /**
     * Generates a random edu.rpi.legup.puzzle based on the difficulty
     *
     * @param difficulty level of difficulty (1-10)
     * @return board of the random edu.rpi.legup.puzzle
     */
    @Override
    @Contract("_ -> null")
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
    @Contract(pure = true)
    public boolean isBoardComplete(@NotNull Board board) {
        return true;
    }

    /**
     * Callback for when the board puzzleElement changes
     *
     * @param board the board that has changed
     */
    @Override
    @Contract(pure = true)
    public void onBoardChange(@NotNull Board board) {}
}
