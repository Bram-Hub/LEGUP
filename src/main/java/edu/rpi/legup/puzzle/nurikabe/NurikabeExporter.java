package edu.rpi.legup.puzzle.nurikabe;

import edu.rpi.legup.model.PuzzleExporter;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import org.w3c.dom.Document;

public class NurikabeExporter extends PuzzleExporter {

    public NurikabeExporter(Nurikabe nurikabe) {
        super(nurikabe);
    }

    /**
     * Generates an XML element for the nurikabe puzzle board, including its dimensions and the
     * state of each cell. Nurikabe cells that are not empty are included in the XML.
     *
     * @param newDocument The XML document to which the board element belongs.
     * @return The XML element representing the board.
     */
    @Override
    protected org.w3c.dom.Element createBoardElement(Document newDocument) {
        NurikabeBoard board;
        if (puzzle.getTree() != null) {
            board = (NurikabeBoard) puzzle.getTree().getRootNode().getBoard();
        } else {
            board = (NurikabeBoard) puzzle.getBoardView().getBoard();
        }

        org.w3c.dom.Element boardElement = newDocument.createElement("board");
        boardElement.setAttribute("width", String.valueOf(board.getWidth()));
        boardElement.setAttribute("height", String.valueOf(board.getHeight()));

        org.w3c.dom.Element cellsElement = newDocument.createElement("cells");
        for (PuzzleElement puzzleElement : board.getPuzzleElements()) {
            NurikabeCell cell = (NurikabeCell) puzzleElement;
            if (cell.getData() != -2) {
                org.w3c.dom.Element cellElement =
                        puzzle.getFactory().exportCell(newDocument, puzzleElement);
                cellsElement.appendChild(cellElement);
            }
        }

        boardElement.appendChild(cellsElement);
        return boardElement;
    }
}
