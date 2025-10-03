package edu.rpi.legup.puzzle.minesweeper;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Minesweeper extends Puzzle {

    public static final String NAME = "Minesweeper";

    public Minesweeper() {
        this.name = NAME;
        this.importer = new MinesweeperImporter(this);
        this.exporter = new MinesweeperExporter(this);
        this.factory = MinesweeperCellFactory.INSTANCE;
    }

    @Override
    @Contract(pure = false)
    public void initializeView() {
        this.boardView = new MinesweeperView((MinesweeperBoard) this.currentBoard);
        this.boardView.setBoard(this.currentBoard);
        addBoardListener(boardView);
    }

    @Override
    @Contract("_ -> null")
    public @Nullable Board generatePuzzle(int difficulty) {
        return null;
    }

    @Override
    @Contract(pure = true)
    public boolean isBoardComplete(@NotNull Board board) {
        MinesweeperBoard minesweeperBoard = (MinesweeperBoard) board;

        for (ContradictionRule rule : contradictionRules) {
            if (rule.checkContradiction(minesweeperBoard) == null) {
                return false;
            }
        }
        for (PuzzleElement<?> data : minesweeperBoard.getPuzzleElements()) {
            final MinesweeperCell cell = (MinesweeperCell) data;
            if (cell.getData().equals(MinesweeperTileData.unset())) {
                return false;
            }
        }
        return true;
    }

    @Override
    @Contract(pure = true)
    public void onBoardChange(@NotNull Board board) {}

    @Override
    @Contract(pure = true)
    public boolean isValidDimensions(int rows, int columns) {
        return rows >= 1 && columns >= 1;
    }
}
