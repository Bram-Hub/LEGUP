package edu.rpi.legup.puzzle.starbattle.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.starbattle.StarBattleBoard;
import edu.rpi.legup.puzzle.starbattle.StarBattleCell;
import edu.rpi.legup.puzzle.starbattle.StarBattleCellType;
import edu.rpi.legup.puzzle.starbattle.StarBattleRegion;
import java.awt.*;
import java.util.List;

public class TooManyStarsContradictionRule extends ContradictionRule {
    private final String INVALID_USE_MESSAGE =
            "Contradiction must be applied to a cell containing a star.";

    public TooManyStarsContradictionRule() {
        super(
                "STBL-CONT-0001",
                "Too Many Stars",
                "There are too many stars in this region/row/column.",
                "edu/rpi/legup/images/starbattle/empty/TooManyStarsContradictionRule.png");
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
        StarBattleBoard starbattleBoard = (StarBattleBoard) board;
        StarBattleCell cell = (StarBattleCell) starbattleBoard.getPuzzleElement(puzzleElement);
        Point location = cell.getLocation();
        int starCount = 0;
        int puzzleNum = starbattleBoard.getPuzzleNumber();
        boolean valid = true;

        if (cell.getType() != StarBattleCellType.STAR) {
            return this.INVALID_USE_MESSAGE;
        }

        // check row
        for (int i = location.x - 1; i >= 0; i--) {
            StarBattleCell check = starbattleBoard.getCell(i, location.y);
            if (check.getType() == StarBattleCellType.STAR) {
                starCount++;
                if (starCount >= puzzleNum) {
                    valid = false;
                    break;
                }
            }
        }

        for (int i = location.x + 1; i < starbattleBoard.getWidth(); i++) {
            StarBattleCell check = starbattleBoard.getCell(i, location.y);
            if (check.getType() == StarBattleCellType.STAR) {
                starCount++;
                if (starCount >= puzzleNum) {
                    valid = false;
                    break;
                }
            }
        }

        // check column
        starCount = 0;
        for (int j = location.y - 1; j >= 0; j--) {
            StarBattleCell check = starbattleBoard.getCell(location.x, j);
            if (check.getType() == StarBattleCellType.STAR) {
                starCount++;
                if (starCount >= puzzleNum) {
                    valid = false;
                    break;
                }
            }
        }

        for (int j = location.y + 1; j < starbattleBoard.getWidth(); j++) {
            StarBattleCell check = starbattleBoard.getCell(location.x, j);
            if (check.getType() == StarBattleCellType.STAR) {
                starCount++;
                if (starCount >= puzzleNum) {
                    valid = false;
                    break;
                }
            }
        }

        // check region
        starCount = 0;
        StarBattleRegion reg = starbattleBoard.getRegion(cell);
        List<StarBattleCell> cellList = reg.getCells(); // list of cells
        for (int k = 0; k < cellList.size(); k++) {
            if (cellList.get(k).getType() == StarBattleCellType.STAR) {
                starCount++;
                if (starCount > puzzleNum) {
                    valid = false;
                    break;
                }
            }
        }

        if (valid) {
            return super.getNoContradictionMessage();
        }
        return null;
    }
}
