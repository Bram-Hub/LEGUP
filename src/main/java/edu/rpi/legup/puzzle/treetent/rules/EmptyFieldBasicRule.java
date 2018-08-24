package edu.rpi.legup.puzzle.treetent.rules;

import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.BasicRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.treetent.TreeTentBoard;
import edu.rpi.legup.puzzle.treetent.TreeTentCell;
import edu.rpi.legup.puzzle.treetent.TreeTentType;

public class EmptyFieldBasicRule extends BasicRule {
    public EmptyFieldBasicRule() {
        super("Empty Field",
                "blank cells not adjacent to an unlinked tree are grass.",
                "edu/rpi/legup/images/treetent/noTreesAround.png");
    }

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific puzzleElement index using this rule
     *
     * @param transition    transition to check
     * @param puzzleElement equivalent puzzleElement
     * @return null if the child node logically follow from the parent node at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        TreeTentBoard initialBoard = (TreeTentBoard) transition.getParents().get(0).getBoard();
        TreeTentCell initCell = (TreeTentCell) initialBoard.getPuzzleElement(puzzleElement);
        TreeTentCell finalCell = (TreeTentCell) transition.getBoard().getPuzzleElement(puzzleElement);
        if (finalCell.getType() == TreeTentType.GRASS && initCell.getType() == TreeTentType.UNKNOWN) {
            return null;
        }
        return "Cell is not forced to be empty";
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
