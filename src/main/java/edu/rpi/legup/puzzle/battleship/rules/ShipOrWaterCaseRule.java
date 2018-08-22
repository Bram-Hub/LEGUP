package edu.rpi.legup.puzzle.battleship.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeTransition;

import java.util.List;

public class ShipOrWaterCaseRule extends CaseRule {

    public ShipOrWaterCaseRule() {
        super("Ship or Water",
                "",
                "edu/rpi/legup/images/battleship/cases/ShipOrWater.png");
    }

    /**
     * Gets the case board that indicates where this case rule can be applied on the given {@link Board}.
     *
     * @param board board to find locations where this case rule can be applied
     * @return a case board
     */
    @Override
    public CaseBoard getCaseBoard(Board board) {
        return null;
    }

    /**
     * Gets the possible cases for this {@link Board} at a specific {@link PuzzleElement} based on this case rule.
     *
     * @param board         the current board state
     * @param puzzleElement equivalent puzzleElement
     * @return a list of elements the specified could be
     */
    @Override
    public List<Board> getCases(Board board, PuzzleElement puzzleElement) {
        return null;
    }

    /**
     * Checks whether the child node logically follows from the parent node using this rule
     * and if so will perform the default application of the rule
     *
     * @param transition transition to apply default application
     * @return true if the child node logically follow from the parent node and accepts the changes
     * to the board, otherwise false
     */
    @Override
    public boolean doDefaultApplication(TreeTransition transition) {
        return false;
    }

    /**
     * Checks whether the child node logically follows from the parent node at the
     * specific puzzleElement index using this rule and if so will perform the default application of the rule
     *
     * @param transition    transition to apply default application
     * @param puzzleElement equivalent puzzleElement
     * @return true if the child node logically follow from the parent node and accepts the changes
     * to the board, otherwise false
     */
    @Override
    public boolean doDefaultApplicationAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return false;
    }
}
