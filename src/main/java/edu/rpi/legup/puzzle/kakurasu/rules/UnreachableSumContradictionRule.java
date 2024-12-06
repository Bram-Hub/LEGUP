package edu.rpi.legup.puzzle.kakurasu.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.kakurasu.KakurasuBoard;
import edu.rpi.legup.puzzle.kakurasu.KakurasuCell;
import edu.rpi.legup.puzzle.kakurasu.KakurasuType;

import java.awt.*;
import java.util.ArrayList;
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

        // If the value for either the row or col is already exceeded, this is the wrong rule to call.
        if(rowValueRemaining < 0 || colValueRemaining < 0) return super.getNoContradictionMessage();

        // If either value is already 0, then it is already possible to fulfill
        // If it isn't 0, then it's possible for the remaining values to not be able to fulfill it
        boolean rowPossible = (rowValueRemaining==0), colPossible = (colValueRemaining==0);

        int rowTotal = 0, colTotal = 0;
        // No need to sort the values as the KakurasuCells are given in increasing index order
        if(!rowPossible) {
            ArrayList<Integer> rowValues = new ArrayList<>();
            for(KakurasuCell kc : unknownRow) {
                rowValues.add(kc.getLocation().x + 1);
                rowTotal += kc.getLocation().x + 1;
            }
            // If the remaining unknown cells' values is less than the remaining clue value,
            // this requires the usage of a different contradiction rule, not this one.
            if(rowTotal < rowValueRemaining) return super.getNoContradictionMessage();
            rowPossible = isReachable(rowValueRemaining, 0, rowValues);
        }
        if(!colPossible) {
            ArrayList<Integer> colValues = new ArrayList<>();
            for(KakurasuCell kc : unknownCol) {
                colValues.add(kc.getLocation().y + 1);
                colTotal += kc.getLocation().y + 1;
            }
            // If the remaining unknown cells' values is less than the remaining clue value,
            // this requires the usage of a different contradiction rule, not this one.
            if(colTotal < colValueRemaining) return super.getNoContradictionMessage();
            colPossible = isReachable(colValueRemaining, 0, colValues);
        }

        if (!rowPossible || !colPossible) {
            return null;
        } else {
            return super.getNoContradictionMessage();
        }
    }

    /**
     * Helper function that checks if the target clue is reachable given a list of KakurasuCells
     * This function only works if the list of values are given in increasing index order (which it currently is)
     *
     * @param target The integer that we are trying to add up to, given the values
     * @param currentIndex The index of the next value that we are considering
     * @param values Values that we are given to try to sum up to the target
     * @return If it's possible to sum the values in a way to get the target value
     */
    private boolean isReachable(int target, int currentIndex, ArrayList<Integer> values) {
        if(target == 0) return true;
        if(target < 0 || currentIndex >= values.size()) return false;
        return (isReachable(target, currentIndex+1, values) ||
                isReachable(target - values.get(currentIndex), currentIndex+1, values));
    }
}
