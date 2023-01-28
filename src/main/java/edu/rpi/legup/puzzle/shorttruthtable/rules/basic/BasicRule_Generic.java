package edu.rpi.legup.puzzle.shorttruthtable.rules.basic;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.BasicRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;

import edu.rpi.legup.puzzle.lightup.LightUpBoard;
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
        ShortTruthTableBoard parentBoard = (ShortTruthTableBoard) transition.getParents().get(0).getBoard();
        ShortTruthTableBoard finalBoard = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell parentCell = (ShortTruthTableCell) parentBoard.getPuzzleElement(element);
        ShortTruthTableCell finalCell = (ShortTruthTableCell) finalBoard.getPuzzleElement(element);

        if (!finalCell.isAssigned()) {
            return super.getInvalidUseOfRuleMessage() + ": Only assigned cells are allowed for basic rules";
        }

        // Strategy: Negate the modified cell and check if there is a contradiction. If there is one, then the
        // original statement must be true. If there isn't one, then the original statement must be false.

        ShortTruthTableBoard modifiedBoard = parentBoard.copy();

        PuzzleElement checkElement =
                this.ELIMINATION_RULE
                ? parentCell.getStatementReference().getParentStatement().getCell()
                : element;

        ShortTruthTableCell checkCell =
                this.ELIMINATION_RULE
                ? (ShortTruthTableCell) modifiedBoard.getCell(parentCell.getX(), parentCell.getY())
                : (ShortTruthTableCell) modifiedBoard.getPuzzleElement(element);

        checkCell.setType(finalCell.getType().getNegation());

        String contradictionMessage = CORRESPONDING_CONTRADICTION_RULE.checkContradictionAt(modifiedBoard, checkElement);
        if (contradictionMessage == null) { // A contradiction exists in the modified statement; this is good!
            return null;
        }

        return super.getInvalidUseOfRuleMessage();
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
