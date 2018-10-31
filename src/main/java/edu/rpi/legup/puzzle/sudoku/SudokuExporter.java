package edu.rpi.legup.puzzle.sudoku;

import edu.rpi.legup.model.PuzzleExporter;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import org.w3c.dom.Document;

public class SudokuExporter extends PuzzleExporter {

    public SudokuExporter(Sudoku sudoku) {
        super(sudoku);
    }

    @Override
    protected org.w3c.dom.Element createBoardElement(Document newDocument) {
        SudokuBoard board = (SudokuBoard) puzzle.getTree().getRootNode().getBoard();

        org.w3c.dom.Element boardElement = newDocument.createElement("board");
        boardElement.setAttribute("size", String.valueOf(board.getSize()));

        org.w3c.dom.Element cellsElement = newDocument.createElement("cells");
        for (PuzzleElement puzzleElement : board.getPuzzleElements()) {
            SudokuCell cell = (SudokuCell) puzzleElement;
            if (cell.getData() != 0) {
                org.w3c.dom.Element cellElement = puzzle.getFactory().exportCell(newDocument, puzzleElement);
                cellsElement.appendChild(cellElement);
            }
        }

        boardElement.appendChild(cellsElement);
        return boardElement;
    }
}
