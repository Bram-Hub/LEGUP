package edu.rpi.legup.puzzle.battleship;

import edu.rpi.legup.model.PuzzleExporter;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import org.w3c.dom.Document;

public class BattleShipExporter extends PuzzleExporter {

    public BattleShipExporter(BattleShip battleShip) {
        super(battleShip);
    }

    @Override
    protected org.w3c.dom.Element createBoardElement(Document newDocument) {
        BattleShipBoard board = (BattleShipBoard) puzzle.getTree().getRootNode().getBoard();

        org.w3c.dom.Element boardElement = newDocument.createElement("board");
        boardElement.setAttribute("width", String.valueOf(board.getWidth()));
        boardElement.setAttribute("height", String.valueOf(board.getHeight()));

        org.w3c.dom.Element cellsElement = newDocument.createElement("cells");
        for (PuzzleElement puzzleElement : board.getPuzzleElements()) {
            BattleShipCell cell = (BattleShipCell) puzzleElement;
            if (cell.getData() != 0) {
                org.w3c.dom.Element cellElement = puzzle.getFactory().exportCell(newDocument, puzzleElement);
                cellsElement.appendChild(cellElement);
            }
        }
        boardElement.appendChild(cellsElement);

        org.w3c.dom.Element axisEast = newDocument.createElement("axis");
        axisEast.setAttribute("side", "east");
        for (BattleShipClue clue : board.getEast()) {
            org.w3c.dom.Element clueElement = newDocument.createElement("clue");
            clueElement.setAttribute("value", String.valueOf(clue.getData()));
            clueElement.setAttribute("index", BattleShipClue.colNumToString(clue.getIndex()));
            axisEast.appendChild(clueElement);
        }
        boardElement.appendChild(axisEast);

        org.w3c.dom.Element axisSouth = newDocument.createElement("axis");
        axisSouth.setAttribute("side", "south");
        for (BattleShipClue clue : board.getEast()) {
            org.w3c.dom.Element clueElement = newDocument.createElement("clue");
            clueElement.setAttribute("value", String.valueOf(clue.getData()));
            clueElement.setAttribute("index", String.valueOf(clue.getIndex()));
            axisSouth.appendChild(clueElement);
        }
        boardElement.appendChild(axisSouth);

        return boardElement;
    }
}
