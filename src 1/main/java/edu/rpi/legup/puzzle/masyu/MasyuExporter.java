package edu.rpi.legup.puzzle.masyu;

import edu.rpi.legup.model.PuzzleExporter;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import org.w3c.dom.Document;

public class MasyuExporter extends PuzzleExporter {

    public MasyuExporter(Masyu masyu) {
        super(masyu);
    }

    @Override
    protected org.w3c.dom.Element createBoardElement(Document newDocument) {
        MasyuBoard board = (MasyuBoard) puzzle.getTree().getRootNode().getBoard();

        org.w3c.dom.Element boardElement = newDocument.createElement("board");
        boardElement.setAttribute("width", String.valueOf(board.getWidth()));
        boardElement.setAttribute("height", String.valueOf(board.getHeight()));

        org.w3c.dom.Element cellsElement = newDocument.createElement("cells");
        for (PuzzleElement puzzleElement : board.getPuzzleElements()) {
            MasyuCell cell = (MasyuCell) puzzleElement;
            if (cell.getData() != -2) {
                org.w3c.dom.Element cellElement = puzzle.getFactory().exportCell(newDocument, puzzleElement);
                cellsElement.appendChild(cellElement);
            }
        }

        boardElement.appendChild(cellsElement);
        return boardElement;
    }
}
