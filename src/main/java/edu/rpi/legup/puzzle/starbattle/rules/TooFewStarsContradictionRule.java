package edu.rpi.legup.puzzle.starbattle.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.rules.Rule;
import edu.rpi.legup.puzzle.starbattle.StarBattleBoard;
import edu.rpi.legup.puzzle.starbattle.StarBattleCell;
import edu.rpi.legup.puzzle.starbattle.StarBattleCellType;
import edu.rpi.legup.puzzle.starbattle.StarBattleRegion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;

public class TooFewStarsContradictionRule extends ContradictionRule {
    private static final Logger LOGGER = LogManager.getLogger(TooFewStarsContradictionRule.class.getName());

    public TooFewStarsContradictionRule() {
        super(
                "STBL-CONT-0002",
                "Too Few Stars",
                "There are too few stars in this region/row/column and there are not enough places to put the remaining stars.",
                "edu/rpi/legup/images/starbattle/contradictions/TooFewStarsContradictionRule.png");
    }

    /**
     * Checks whether the transition has a contradiction at the specific puzzleElement index using
     * this rule
     *
     * @param board board to check contradiction
     * @param puzzleElement equivalent puzzleElement
     * @return null if the transition contains a contradiction at the specified puzzleElement,
     *     otherwise error message
     */
    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        StarBattleBoard sbBoard = (StarBattleBoard) board;
        StarBattleCell cell = (StarBattleCell) puzzleElement;
        Point location = cell.getLocation();
        int column = location.x;
        int row = location.y;
        int rowCount = 0;
        int columnCount = 0;
        for (int i = 0; i < sbBoard.getSize(); ++i) {
            if (sbBoard.getCell(i, row).getType() != StarBattleCellType.BLACK) {
                ++rowCount;
            }
            if (sbBoard.getCell(column, i).getType() != StarBattleCellType.BLACK) {
                ++columnCount;
            }
        }
        LOGGER.trace("rowCount = {} columnCount = {} at {},{}\n", rowCount, columnCount, column, row);
        if (rowCount < sbBoard.getPuzzleNumber() || columnCount < sbBoard.getPuzzleNumber()) {
            LOGGER.trace("Returning Null\n");
            return null;
        }
        StarBattleRegion region = sbBoard.getRegion(cell);
        int regionCount = 0;
        for (StarBattleCell c : region.getCells()) {
            if (c.getType() != StarBattleCellType.BLACK) {
                ++regionCount;
            }
        }
        if (regionCount < sbBoard.getPuzzleNumber()) {
            return null;
        }
        return super.getNoContradictionMessage();
    }
}
