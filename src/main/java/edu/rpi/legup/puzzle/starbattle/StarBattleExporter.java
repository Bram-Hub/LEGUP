package edu.rpi.legup.puzzle.starbattle;

import edu.rpi.legup.model.PuzzleExporter;
import org.w3c.dom.Document;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class StarBattleExporter extends PuzzleExporter {
    public StarBattleExporter(@NotNull StarBattle starbattle) {
        super(starbattle);
    }

    @Override
    @Contract(pure = true)
    protected @NotNull org.w3c.dom.Element createBoardElement(@NotNull Document newDocument) {
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
