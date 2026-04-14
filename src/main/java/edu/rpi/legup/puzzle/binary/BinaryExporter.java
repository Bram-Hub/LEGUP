package edu.rpi.legup.puzzle.binary;

import edu.rpi.legup.model.PuzzleExporter;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import org.w3c.dom.Document;

public class BinaryExporter extends PuzzleExporter {

    public BinaryExporter(Binary binary) {
        super(binary);
    }

    /**
     * Generates an XML element for the binary puzzle board, including its dimensions and the state
     * of each cell. Binary cells that are not in the `UNKNOWN` state are included in the XML.
     *
     * @param newDocument The XML document to which the board element belongs.
     * @return The XML element representing the board.
     */
    @Override
    protected org.w3c.dom.Element createBoardElement(Document newDocument) {
        BinaryBoard board;
        if (puzzle.getTree() != null) {
            board = (BinaryBoard) puzzle.getTree().getRootNode().getBoard();
        } else {
            board = (BinaryBoard) puzzle.getBoardView().getBoard();
        }

        org.w3c.dom.Element boardElement = newDocument.createElement("board");
        boardElement.setAttribute("width", String.valueOf(board.getWidth()));
        boardElement.setAttribute("height", String.valueOf(board.getHeight()));

        org.w3c.dom.Element cellsElement = newDocument.createElement("cells");
        for (PuzzleElement puzzleElement : board.getPuzzleElements()) {
            BinaryCell cell = (BinaryCell) puzzleElement;
            if (cell.getData() != BinaryType.UNKNOWN.toValue()) {
                org.w3c.dom.Element cellElement =
                        puzzle.getFactory().exportCell(newDocument, puzzleElement);
                cellsElement.appendChild(cellElement);
            }
        }

        boardElement.appendChild(cellsElement);
        return boardElement;
    }
}
