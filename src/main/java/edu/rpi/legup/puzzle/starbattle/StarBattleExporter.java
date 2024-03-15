package edu.rpi.legup.puzzle.starbattle;

import edu.rpi.legup.model.PuzzleExporter;
import edu.rpi.legup.model.gameboard.PuzzleElement;

import org.w3c.dom.Document;

public class StarBattleExporter extends PuzzleExporter {
    public StarBattleExporter(StarBattle starbattle) {
        super(starbattle);
    }

    @Override
    protected org.w3c.dom.Element createBoardElement(Document newDocument) {
        StarBattleBoard board = (StarBattleBoard) puzzle.getTree().getRootNode().getBoard();
        org.w3c.dom.Element boardElement = newDocument.createElement("board");
        boardElement.setAttribute("size", String.valueOf(board.getSize()));
        boardElement.setAttribute("puzzle_num", String.valueOf(board.getPuzzleNumber()));
        for (PuzzleElement puzzleElement : board.getPuzzleElements()) {
            org.w3c.dom.Element regionsElement = newDocument.createElement("region");
            StarBattleRegion region = (StarBattleRegion) puzzleElement;
            org.w3c.dom.Element cellsElement = newDocument.createElement("cells");
            for (PuzzleElement puzzleElement : region.getPuzzleElements()) {
                StarBattleCell cell = (StarBattleCell) puzzleElement;
                if (cell.getData() != -2) {
                    org.w3c.dom.Element cellElement = puzzle.getFactory().exportCell(newDocument, puzzleElement);
                    cellsElement.appendChild(cellElement);
                }
                regionsElement.appendChild(cellsElement);
            }
            boardElement.appendChild(regionsElement);
        }
        
        return boardElement;
    }
}
