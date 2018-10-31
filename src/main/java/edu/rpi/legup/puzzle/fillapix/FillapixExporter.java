package edu.rpi.legup.puzzle.fillapix;

import edu.rpi.legup.model.PuzzleExporter;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import org.w3c.dom.Document;

public class FillapixExporter extends PuzzleExporter {

    public FillapixExporter(Fillapix fillapix) {
        super(fillapix);
    }

    @Override
    protected org.w3c.dom.Element createBoardElement(Document newDocument) {
        FillapixBoard board = (FillapixBoard) puzzle.getTree().getRootNode().getBoard();

        org.w3c.dom.Element boardElement = newDocument.createElement("board");
        boardElement.setAttribute("width", String.valueOf(board.getWidth()));
        boardElement.setAttribute("height", String.valueOf(board.getHeight()));

        org.w3c.dom.Element cellsElement = newDocument.createElement("cells");
        for (PuzzleElement puzzleElement : board.getPuzzleElements()) {
            FillapixCell cell = (FillapixCell) puzzleElement;
            if (cell.getNumber() != -1 || cell.getType() != FillapixCellType.UNKNOWN) {
                org.w3c.dom.Element cellElement = puzzle.getFactory().exportCell(newDocument, puzzleElement);
                cellsElement.appendChild(cellElement);
            }
        }

        boardElement.appendChild(cellsElement);
        return boardElement;
    }
}
