package edu.rpi.legup.puzzle.minesweeper;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.PuzzleExporter;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MinesweeperExporter extends PuzzleExporter {

    public MinesweeperExporter(@NotNull Puzzle puzzle) {
        super(puzzle);
    }

    @Override
    @Contract(pure = true)
    protected @NotNull Element createBoardElement(@NotNull Document newDocument) {
        MinesweeperBoard board;
        if (puzzle.getTree() != null) {
            board = (MinesweeperBoard) puzzle.getTree().getRootNode().getBoard();
        } else {
            board = (MinesweeperBoard) puzzle.getBoardView().getBoard();
        }

        final org.w3c.dom.Element boardElement = newDocument.createElement("board");
        boardElement.setAttribute("width", String.valueOf(board.getWidth()));
        boardElement.setAttribute("height", String.valueOf(board.getHeight()));

        final org.w3c.dom.Element cellsElement = newDocument.createElement("cells");
        for (PuzzleElement puzzleElement : board.getPuzzleElements()) {
            MinesweeperCell cell = (MinesweeperCell) puzzleElement;
            if (!MinesweeperTileData.unset().equals(cell.getData())) {
                final org.w3c.dom.Element cellElement =
                        puzzle.getFactory().exportCell(newDocument, puzzleElement);
                cellsElement.appendChild(cellElement);
            }
        }

        boardElement.appendChild(cellsElement);
        return boardElement;
    }
}
