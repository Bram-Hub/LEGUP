package edu.rpi.legup.puzzle.treetent.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.treetent.TreeTentBoard;
import edu.rpi.legup.puzzle.treetent.TreeTentCell;
import edu.rpi.legup.puzzle.treetent.TreeTentType;

public class TouchingTentsContradictionRule extends ContradictionRule {

    public TouchingTentsContradictionRule() {
        super("TREE-CONT-0005", "Touching Tents",
                "Tents cannot touch other tents.",
                "edu/rpi/legup/images/treetent/contra_adjacentTents.png");
    }

    /**
     * Checks whether the transition has a contradiction at the specific
     * {@link PuzzleElement} index using this rule
     *
     * @param board         board to check contradiction
     * @param puzzleElement equivalent {@link PuzzleElement}
     * @return              <code>null</code> if the transition contains a
     *                      contradiction at the specified puzzleElement,
     *                      otherwise error message.
     */
    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        TreeTentBoard treeTentBoard = (TreeTentBoard) board;
        TreeTentCell cell = (TreeTentCell) puzzleElement;
        if (cell.getType() != TreeTentType.TREE) {
            return super.getNoContradictionMessage();
        }
        int adjTree = treeTentBoard.getAdjacent(cell, TreeTentType.TREE).size();
        if (adjTree > 0) {
            return null;
        } else {
            return super.getNoContradictionMessage();
        }
    }
}
