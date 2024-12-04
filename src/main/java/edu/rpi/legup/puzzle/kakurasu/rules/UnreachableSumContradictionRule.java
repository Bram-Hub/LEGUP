package edu.rpi.legup.puzzle.kakurasu.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.kakurasu.KakurasuBoard;
import edu.rpi.legup.puzzle.kakurasu.KakurasuCell;
import edu.rpi.legup.puzzle.kakurasu.KakurasuType;

import java.awt.*;
import java.util.List;

public class UnreachableSumContradictionRule extends ContradictionRule {
    public UnreachableSumContradictionRule() {
        super(
                "KAKU-CONT-0003",
                "Unreachable Sum",
                "The combination of available values cannot exactly land on the clue's value.",
                "edu/rpi/legup/images/kakurasu/temp.png");
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
        KakurasuBoard kakurasuBoard = (KakurasuBoard) board;
        KakurasuCell cell = (KakurasuCell) puzzleElement;

        // TODO: Finish this rule

        Point loc = cell.getLocation();
        List<KakurasuCell> filledRow = kakurasuBoard.getRowCol(loc.y, KakurasuType.FILLED, true);
        List<KakurasuCell> unknownRow = kakurasuBoard.getRowCol(loc.y, KakurasuType.UNKNOWN, true);
        List<KakurasuCell> filledCol = kakurasuBoard.getRowCol(loc.x, KakurasuType.FILLED, false);
        List<KakurasuCell> unknownCol = kakurasuBoard.getRowCol(loc.x, KakurasuType.UNKNOWN, false);

        int rowValueRemaining = kakurasuBoard.getClue(kakurasuBoard.getWidth(), loc.y).getData();
        for(KakurasuCell kc : filledRow) {
            rowValueRemaining -= kc.getLocation().x + 1;
        }
        int colValueRemaining = kakurasuBoard.getClue(loc.x, kakurasuBoard.getHeight()).getData();
        for(KakurasuCell kc : filledCol) {
            colValueRemaining -= kc.getLocation().y + 1;
        }

        if (true) {
            return null;
        } else {
            return super.getNoContradictionMessage();
        }
    }
}
