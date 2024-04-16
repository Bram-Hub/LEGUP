package edu.rpi.legup.puzzle.skyscrapers;

import edu.rpi.legup.model.PuzzleExporter;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import org.w3c.dom.Document;

public class SkyscrapersExporter extends PuzzleExporter {

    public SkyscrapersExporter(Skyscrapers skyscrapers) {
        super(skyscrapers);
    }

    @Override
    protected org.w3c.dom.Element createBoardElement(Document newDocument) {
        SkyscrapersBoard board;
        if (puzzle.getTree() != null) {
            board = (SkyscrapersBoard) puzzle.getTree().getRootNode().getBoard();
        } else {
            board = (SkyscrapersBoard) puzzle.getBoardView().getBoard();
        }

        org.w3c.dom.Element boardElement = newDocument.createElement("board");
        boardElement.setAttribute("size", String.valueOf(board.getWidth()));

        org.w3c.dom.Element cellsElement = newDocument.createElement("cells");
        for (PuzzleElement puzzleElement : board.getPuzzleElements()) {
            SkyscrapersCell cell = (SkyscrapersCell) puzzleElement;
            if (cell.getData() != 0) {
                org.w3c.dom.Element cellElement =
                        puzzle.getFactory().exportCell(newDocument, puzzleElement);
                cellsElement.appendChild(cellElement);
            }
        }
        boardElement.appendChild(cellsElement);

        org.w3c.dom.Element axisEast = newDocument.createElement("axis");
        axisEast.setAttribute("side", "east");
        for (int i = 0; i < board.getWidth(); i++) {
            org.w3c.dom.Element clueElement = newDocument.createElement("clue");
            clueElement.setAttribute(
                    "value", String.valueOf(board.getEastClues().get(i).getData()));
            clueElement.setAttribute(
                    "index", String.valueOf(board.getWestClues().get(i).getData()));
            axisEast.appendChild(clueElement);
        }
        boardElement.appendChild(axisEast);

        org.w3c.dom.Element axisSouth = newDocument.createElement("axis");
        axisSouth.setAttribute("side", "south");
        for (int i = 0; i < board.getWidth(); i++) {
            org.w3c.dom.Element clueElement = newDocument.createElement("clue");
            clueElement.setAttribute(
                    "value", String.valueOf(board.getSouthClues().get(i).getData()));
            clueElement.setAttribute(
                    "index", String.valueOf(board.getNorthClues().get(i).getData()));
            axisSouth.appendChild(clueElement);
        }
        boardElement.appendChild(axisSouth);

        org.w3c.dom.Element flagsElement = newDocument.createElement("flags");
        flagsElement.setAttribute("dupe", String.valueOf(board.getDupeFlag()));
        flagsElement.setAttribute("view", String.valueOf(board.getViewFlag()));
        boardElement.appendChild(flagsElement);

        return boardElement;
    }
}
