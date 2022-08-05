package edu.rpi.legup.puzzle.shorttruthtable.rules.basic;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.BasicRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.model.rules.ContradictionRule;

public abstract class BasicRule_Generic extends BasicRule {

    final ContradictionRule CORRESPONDING_CONTRADICTION_RULE;
    final boolean ELIMINATION_RULE;

    public BasicRule_Generic(String ruleID, String ruleName, String description, String imageName, ContradictionRule contraRule, boolean eliminationRule) {
        super(ruleID, ruleName, description, "edu/rpi/legup/images/shorttruthtable/ruleimages/basic/" + imageName + ".png");
        this.CORRESPONDING_CONTRADICTION_RULE = contraRule;
        this.ELIMINATION_RULE = eliminationRule;
    }

    public String checkRuleRawAt(TreeTransition transition, PuzzleElement element) {

        // Check that the puzzle element is not unknown
        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell cell = (ShortTruthTableCell) board.getPuzzleElement(element);

        if (!cell.isAssigned()) {
            return super.getInvalidUseOfRuleMessage() + ": Only assigned cells are allowed for basic rules";
        }

        if (this.ELIMINATION_RULE) {
            // Strategy: If this is an elimination rule, simply check if there is a contradiction at the specified statement

            // This gets the operator of the parent statement, which is what we need for checking the contradiction
            PuzzleElement checkElement = cell.getStatementReference().getParentStatement().getCell();

            String contradictionMessage = CORRESPONDING_CONTRADICTION_RULE.checkContradictionAt(board, checkElement);
            if (contradictionMessage != null) {
                if (contradictionMessage.startsWith(CORRESPONDING_CONTRADICTION_RULE.getNoContradictionMessage())) {
                    return null;
                }
                else {
                    return super.getInvalidUseOfRuleMessage() + ": " + contradictionMessage;
                }
            }
            else {
                return super.getInvalidUseOfRuleMessage();
            }
        }
        else {
            // Strategy: Negate the modified cell and check if there is a contradiction. If there is one, then the
            // original statement must be true. If there isn't one, then the original statement must be false.

            ShortTruthTableBoard modifiedBoard = board.copy();
            ((ShortTruthTableCell) modifiedBoard.getPuzzleElement(element)).setType(cell.getType().getNegation());

            String contradictionMessage = CORRESPONDING_CONTRADICTION_RULE.checkContradictionAt(modifiedBoard, element);
            if (contradictionMessage == null) { // A contradiction exists in the modified statement; this is good!
                return null;
            }
            return super.getInvalidUseOfRuleMessage() + ": " + contradictionMessage;
        }
    }

    /**
     * Creates a transition {@link Board} that has this rule applied to it using the {@link TreeNode}.
     *
     * @param node short truth table board used to create default transition board
     * @return default board or null if this rule cannot be applied to this tree node
     */
    @Override
    public Board getDefaultBoard(TreeNode node) {
        return null;
    }
}
