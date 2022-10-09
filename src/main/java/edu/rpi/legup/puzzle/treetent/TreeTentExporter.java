package edu.rpi.legup.puzzle.treetent;

import edu.rpi.legup.model.PuzzleExporter;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import org.w3c.dom.Document;

public class TreeTentExporter extends PuzzleExporter {

    public TreeTentExporter(TreeTent treeTent) {
        super(treeTent);
    }

    @Override
    protected org.w3c.dom.Element createBoardElement(Document newDocument) {
        TreeTentBoard board = (TreeTentBoard) puzzle.getTree().getRootNode().getBoard();

        org.w3c.dom.Element boardElement = newDocument.createElement("board");
        boardElement.setAttribute("width", String.valueOf(board.getWidth()));
        boardElement.setAttribute("height", String.valueOf(board.getHeight()));

        org.w3c.dom.Element cellsElement = newDocument.createElement("cells");
        for (PuzzleElement puzzleElement : board.getPuzzleElements()) {
            TreeTentCell cell = (TreeTentCell) puzzleElement;
            if (cell.getData() != 0) {
                org.w3c.dom.Element cellElement = puzzle.getFactory().exportCell(newDocument, puzzleElement);
                cellsElement.appendChild(cellElement);
            }
        }
        boardElement.appendChild(cellsElement);

        org.w3c.dom.Element axisEast = newDocument.createElement("axis");
        axisEast.setAttribute("side", "east");
        for (TreeTentClue clue : board.getRowClues()) {
            org.w3c.dom.Element clueElement = newDocument.createElement("clue");
            clueElement.setAttribute("value", String.valueOf(clue.getData()));
            clueElement.setAttribute("index", TreeTentClue.colNumToString(clue.getIndex()));
            axisEast.appendChild(clueElement);
        }
        boardElement.appendChild(axisEast);

        org.w3c.dom.Element axisSouth = newDocument.createElement("axis");
        axisSouth.setAttribute("side", "south");
        for (TreeTentClue clue : board.getRowClues()) {
            org.w3c.dom.Element clueElement = newDocument.createElement("clue");
            clueElement.setAttribute("value", String.valueOf(clue.getData()));
            clueElement.setAttribute("index", String.valueOf(clue.getIndex()));
            axisSouth.appendChild(clueElement);
        }
        boardElement.appendChild(axisSouth);

        if (!board.getLines().isEmpty()) {
            org.w3c.dom.Element linesElement = newDocument.createElement("lines");
            for (PuzzleElement data : board.getLines()) {
                org.w3c.dom.Element lineElement = puzzle.getFactory().exportCell(newDocument, data);
                linesElement.appendChild(lineElement);
            }
            boardElement.appendChild(linesElement);
        }
        return boardElement;
    }
}
