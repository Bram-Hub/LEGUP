package edu.rpi.legup.puzzle.treetent.rules;

import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.treetent.TreeTentBoard;
import edu.rpi.legup.puzzle.treetent.TreeTentCell;
import edu.rpi.legup.puzzle.treetent.TreeTentType;

public class NoTentForTreeContradictionRule extends ContradictionRule
{

    public NoTentForTreeContradictionRule()
    {
        super("No Tent For Tree",
                "Each tree must link to a tent.",
                "edu/rpi/legup/images/treetent/contra_NoTentForTree.png");
    }

    /**
     * Checks whether the transition has a contradiction at the specific puzzleElement index using this rule
     *
     * @param transition   transition to check contradiction
     * @param puzzleElement equivalent puzzleElement
     *
     * @return null if the transition contains a contradiction at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkContradictionAt(TreeTransition transition, PuzzleElement puzzleElement)
    {
        TreeTentBoard board = (TreeTentBoard) transition.getBoard();
        TreeTentCell cell = (TreeTentCell)puzzleElement;
        if(cell.getType() != TreeTentType.TREE) {
            return "This cell does not contain a contradiction at this location.";
        }
        int adjTent = board.getAdjacent(cell, TreeTentType.TENT).size();
        int adjUnknown = board.getAdjacent(cell, TreeTentType.UNKNOWN).size();
        if(adjTent == 0 && adjUnknown == 0) {
            return null;
        } else {
            return "This cell does not contain a contradiction at this location.";
        }
    }
}
