package edu.rpi.legup.puzzle.treetent.rules;

import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.rules.RegisterRule;
import edu.rpi.legup.model.tree.TreeTransition;

public class TooManyTentsContradictionRule extends ContradictionRule
{

    public TooManyTentsContradictionRule()
    {
        super("Too Many Tents",
                "Rows and columns cannot have more tents than their clue.",
                "edu/rpi/legup/images/treetent/too_many_tents.png");
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
        return null;
    }
}
