package edu.rpi.legup.puzzle.yinyang;

import edu.rpi.legup.model.PuzzleExporter;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class YinYangExporter extends PuzzleExporter {

    public YinYangExporter(YinYang puzzle) {
        super(puzzle);
    }

    @Override
    protected Element createBoardElement(Document document) {
        YinYangBoard board;

        // Check whether the tree or the board view should be used
        if (puzzle.getTree() != null) {
            board = (YinYangBoard) puzzle.getTree().getRootNode().getBoard();
        } else {
            board = (YinYangBoard) puzzle.getBoardView().getBoard();
        }

        Element boardElement = document.createElement("board");
        boardElement.setAttribute("width", String.valueOf(board.getWidth()));
        boardElement.setAttribute("height", String.valueOf(board.getHeight()));

        // Create the cells element
        Element cellsElement = document.createElement("cells");
        for (PuzzleElement element : board.getPuzzleElements()) {
            YinYangCell cell = (YinYangCell) element;

            // Only export cells that are valid and initialized
            if (cell.getType() != YinYangType.UNKNOWN) {
                Element cellElement = puzzle.getFactory().exportCell(document, cell);
                cellsElement.appendChild(cellElement);
            }
        }

        boardElement.appendChild(cellsElement);
        return boardElement;
    }
}