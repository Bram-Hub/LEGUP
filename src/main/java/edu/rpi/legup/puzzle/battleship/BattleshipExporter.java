package edu.rpi.legup.puzzle.battleship;

import edu.rpi.legup.model.PuzzleExporter;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import org.w3c.dom.Document;

public class BattleshipExporter extends PuzzleExporter {

    public BattleshipExporter(Battleship battleShip) {
        super(battleShip);
    }

    /**
     * Creates and returns a new board element in the XML document specified
     *
     * @param newDocument the XML document to append to
     * @return the new board element
     */
    @Override
    protected org.w3c.dom.Element createBoardElement(Document newDocument) {
        BattleshipBoard board;
        if (puzzle.getTree() != null) {
            board = (BattleshipBoard) puzzle.getTree().getRootNode().getBoard();
        } else {
            board = (BattleshipBoard) puzzle.getBoardView().getBoard();
        }

        org.w3c.dom.Element boardElement = newDocument.createElement("board");
        boardElement.setAttribute("width", String.valueOf(board.getWidth()));
        boardElement.setAttribute("height", String.valueOf(board.getHeight()));

        org.w3c.dom.Element cellsElement = newDocument.createElement("cells");
        for (PuzzleElement puzzleElement : board.getPuzzleElements()) {
            BattleshipCell cell = (BattleshipCell) puzzleElement;
            if (cell.getData() != BattleshipType.getType(0)) {
                org.w3c.dom.Element cellElement =
                        puzzle.getFactory().exportCell(newDocument, puzzleElement);
                cellsElement.appendChild(cellElement);
            }
        }
        boardElement.appendChild(cellsElement);

        org.w3c.dom.Element axisEast = newDocument.createElement("axis");
        axisEast.setAttribute("side", "east");
        for (BattleshipClue clue : board.getEast()) {
            org.w3c.dom.Element clueElement = newDocument.createElement("clue");
            clueElement.setAttribute("value", String.valueOf(clue.getData()));
            clueElement.setAttribute("index", BattleshipClue.colNumToString(clue.getIndex()));
            axisEast.appendChild(clueElement);
        }
        boardElement.appendChild(axisEast);

        org.w3c.dom.Element axisSouth = newDocument.createElement("axis");
        axisSouth.setAttribute("side", "south");
        for (BattleshipClue clue : board.getEast()) {
            org.w3c.dom.Element clueElement = newDocument.createElement("clue");
            clueElement.setAttribute("value", String.valueOf(clue.getData()));
            clueElement.setAttribute("index", String.valueOf(clue.getIndex()));
            axisSouth.appendChild(clueElement);
        }
        boardElement.appendChild(axisSouth);

        return boardElement;
    }
}
