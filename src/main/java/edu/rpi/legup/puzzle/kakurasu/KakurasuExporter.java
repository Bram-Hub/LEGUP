package edu.rpi.legup.puzzle.kakurasu;

import edu.rpi.legup.model.PuzzleExporter;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import org.w3c.dom.Document;

public class KakurasuExporter extends PuzzleExporter {

    public KakurasuExporter(Kakurasu kakurasu) {
        super(kakurasu);
    }

    /**
     * Creates and returns a new board element in the XML document specified
     *
     * @param newDocument the XML document to append to
     * @return the new board element
     */
    @Override
    protected org.w3c.dom.Element createBoardElement(Document newDocument) {
        KakurasuBoard board;
        if (puzzle.getTree() != null) {
            board = (KakurasuBoard) puzzle.getTree().getRootNode().getBoard();
        } else {
            board = (KakurasuBoard) puzzle.getBoardView().getBoard();
        }

        org.w3c.dom.Element boardElement = newDocument.createElement("board");
        boardElement.setAttribute("width", String.valueOf(board.getWidth()));
        boardElement.setAttribute("height", String.valueOf(board.getHeight()));

        org.w3c.dom.Element cellsElement = newDocument.createElement("cells");
        for (PuzzleElement puzzleElement : board.getPuzzleElements()) {
            KakurasuCell cell = (KakurasuCell) puzzleElement;
            if (cell.getData() != KakurasuType.UNKNOWN) {
                org.w3c.dom.Element cellElement =
                        puzzle.getFactory().exportCell(newDocument, puzzleElement);
                cellsElement.appendChild(cellElement);
            }
        }
        boardElement.appendChild(cellsElement);

        org.w3c.dom.Element axisEast = newDocument.createElement("axis");
        axisEast.setAttribute("side", "east");
        for (KakurasuClue clue : board.getRowClues()) {
            org.w3c.dom.Element clueElement = newDocument.createElement("clue");
            clueElement.setAttribute("value", String.valueOf(clue.getData()));
            clueElement.setAttribute("index", String.valueOf(clue.getClueIndex()));
            axisEast.appendChild(clueElement);
        }
        boardElement.appendChild(axisEast);

        org.w3c.dom.Element axisSouth = newDocument.createElement("axis");
        axisSouth.setAttribute("side", "south");
        for (KakurasuClue clue : board.getColClues()) {
            org.w3c.dom.Element clueElement = newDocument.createElement("clue");
            clueElement.setAttribute("value", String.valueOf(clue.getData()));
            clueElement.setAttribute("index", String.valueOf(clue.getClueIndex()));
            axisSouth.appendChild(clueElement);
        }
        boardElement.appendChild(axisSouth);

        return boardElement;
    }
}
