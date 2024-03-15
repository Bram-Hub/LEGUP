package edu.rpi.legup.puzzle.rippleeffect;

import edu.rpi.legup.model.PuzzleExporter;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import org.w3c.dom.Document;

public class RippleEffectExporter extends PuzzleExporter {

    public RippleEffectExporter(RippleEffect rippleEffect) {
        super(rippleEffect);
    }

    @Override
    protected org.w3c.dom.Element createBoardElement(Document newDocument) {
        RippleEffectBoard board;
        if (puzzle.getTree() != null) {
            board = (RippleEffectBoard) puzzle.getTree().getRootNode().getBoard();
        }
        else {
            board = (RippleEffectBoard) puzzle.getBoardView().getBoard();
        }

        org.w3c.dom.Element boardElement = newDocument.createElement("board");
        boardElement.setAttribute("width", String.valueOf(board.getWidth()));
        boardElement.setAttribute("height", String.valueOf(board.getHeight()));

        org.w3c.dom.Element cellsElement = newDocument.createElement("cells");
        for (PuzzleElement puzzleElement : board.getPuzzleElements()) {
            RippleEffectCell cell = (RippleEffectCell) puzzleElement;
            // if (cell.getData() != -2) {
            //     org.w3c.dom.Element cellElement = puzzle.getFactory().exportCell(newDocument, puzzleElement);
            //     cellsElement.appendChild(cellElement);
            // }
        }

        boardElement.appendChild(cellsElement);
        return boardElement;
    }
}