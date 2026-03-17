package edu.rpi.legup.puzzle.shorttruthtable.rules.caserule;

import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeTransition;

public abstract class CaseRule_Generic extends CaseRule {

    public CaseRule_Generic(String ruleID, String ruleName, String title, String description) {
        super(
                ruleID,
                title,
                description,
                "edu/rpi/legup/images/shorttruthtable/ruleimages/case/" + ruleName + ".png");
    }

    /**
     * Checks whether the child node logically follows from the parent node at the specific
     * puzzleElement index using this rule
     *
     * @param transition transition to check
     * @param puzzleElement equivalent puzzleElement
     * @return null if the child node logically follow from the parent node at the specified
     *     puzzleElement, otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return checkRuleRaw(transition);
    }
}
