package edu.rpi.legup.puzzle.sudoku;

import edu.rpi.legup.model.PuzzleExporter;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import org.w3c.dom.Document;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class SudokuExporter extends PuzzleExporter {

    public SudokuExporter(@NotNull Sudoku sudoku) {
        super(sudoku);
    }

    @Override
    @Contract (pure = true)
    protected @NotNull org.w3c.dom.Element createBoardElement(@NotNull Document newDocument) {
        SudokuBoard board;
        if (puzzle.getTree() != null) {
            board = (SudokuBoard) puzzle.getTree().getRootNode().getBoard();
        } else {
            board = (SudokuBoard) puzzle.getBoardView().getBoard();
        }

        org.w3c.dom.Element boardElement = newDocument.createElement("board");
        boardElement.setAttribute("size", String.valueOf(board.getSize()));

        org.w3c.dom.Element cellsElement = newDocument.createElement("cells");
        for (PuzzleElement puzzleElement : board.getPuzzleElements()) {
            SudokuCell cell = (SudokuCell) puzzleElement;
            if (cell.getData() != 0) {
                org.w3c.dom.Element cellElement =
                        puzzle.getFactory().exportCell(newDocument, puzzleElement);
                cellsElement.appendChild(cellElement);
            }
        }

        boardElement.appendChild(cellsElement);
        return boardElement;
    }
}
