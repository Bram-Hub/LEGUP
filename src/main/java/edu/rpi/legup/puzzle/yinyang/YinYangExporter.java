package edu.rpi.legup.puzzle.yinyang;

import edu.rpi.legup.model.PuzzleExporter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class YinYangExporter extends PuzzleExporter {
    public YinYangExporter(YinYang puzzle) {
        super(puzzle);
    }

    @Override
    public Element exportBoard(Document document) {
        YinYangBoard board = (YinYangBoard) puzzle.getBoardView().getBoard();
        Element boardElement = document.createElement("board");
        boardElement.setAttribute("width", String.valueOf(board.getWidth()));
        boardElement.setAttribute("height", String.valueOf(board.getHeight()));

        for (PuzzleElement element : board.getPuzzleElements()) {
            YinYangCell cell = (YinYangCell) element;
            Element cellElement = document.createElement("cell");
            cellElement.setAttribute("x", String.valueOf(cell.getX()));
            cellElement.setAttribute("y", String.valueOf(cell.getY()));
            cellElement.setAttribute("type", cell.getType().toString());
            boardElement.appendChild(cellElement);
        }

        return boardElement;
    }
}