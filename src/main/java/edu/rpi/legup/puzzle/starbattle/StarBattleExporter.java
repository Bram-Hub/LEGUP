package edu.rpi.legup.puzzle.starbattle;

import edu.rpi.legup.model.PuzzleExporter;
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
        for (StarBattleRegion sb_region : board.regions) {
            org.w3c.dom.Element regionsElement = newDocument.createElement("region");
            org.w3c.dom.Element cellsElement = newDocument.createElement("cells");
            for (StarBattleCell cell : sb_region.getCells()) {
                if (cell.getData() == 0) {
                    org.w3c.dom.Element cellElement =
                            puzzle.getFactory().exportCell(newDocument, cell);
                    cellsElement.appendChild(cellElement);
                }
                regionsElement.appendChild(cellsElement);
            }
            boardElement.appendChild(regionsElement);
        }

        return boardElement;
    }
}
