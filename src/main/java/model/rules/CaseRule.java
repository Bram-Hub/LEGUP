package model.rules;

import model.gameboard.Board;
import model.gameboard.ElementData;

import java.util.ArrayList;

import static model.rules.RuleType.CASE;

public abstract class CaseRule extends Rule
{
    /**
     * CaseRule Constructor - creates a new case rule
     *
     * @param ruleName name of the rule
     * @param imageName file name of the image
     * @param description description of the rule
     */
    public CaseRule(String ruleName, String imageName, String description)
    {
        super(ruleName, imageName, description);
        ruleType = CASE;
    }

    /**
     * Gets the possible cases at a specific location based on this case rule
     *
     * @param board the current board state
     * @param elementIndex element to determine the possible cases for
     * @return a list of elements the specified could be
     */
    public abstract ArrayList<ElementData> getCases(Board board, int elementIndex);
}


