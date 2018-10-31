package edu.rpi.legup.puzzle.treetent.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.treetent.TreeTentBoard;
import edu.rpi.legup.puzzle.treetent.TreeTentCell;
import edu.rpi.legup.puzzle.treetent.TreeTentType;

import java.awt.*;

public class TooFewTentsContradictionRule extends ContradictionRule {

    public TooFewTentsContradictionRule() {
        super("Too Few Tents",
                "Rows and columns cannot have fewer tents than their clue.",
                "edu/rpi/legup/images/treetent/too_few_tents.png");
    }

    /**
     * Checks whether the transition has a contradiction at the specific puzzleElement index using this rule
     *
     * @param board         board to check contradiction
     * @param puzzleElement equivalent puzzleElement
     * @return null if the transition contains a contradiction at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        TreeTentBoard treeTentBoard = (TreeTentBoard) board;
        TreeTentCell cell = (TreeTentCell) puzzleElement;

        Point loc = cell.getLocation();
        int rowTents = treeTentBoard.getRowCol(loc.y, TreeTentType.TENT, true).size();
        int colTents = treeTentBoard.getRowCol(loc.x, TreeTentType.TENT, false).size();
        int rowUnknowns = treeTentBoard.getRowCol(loc.y, TreeTentType.UNKNOWN, true).size();
        int colUnknowns = treeTentBoard.getRowCol(loc.x, TreeTentType.UNKNOWN, false).size();

        if (rowTents + rowUnknowns < treeTentBoard.getRowClues().get(loc.y).getData() ||
                colTents + colUnknowns < treeTentBoard.getColClues().get(loc.x).getData()) {
            return null;
        } else {
            return "This cell does not contain a contradiction at this location.";
        }
    }
}
