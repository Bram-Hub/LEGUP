package model.rules;

import model.gameboard.Board;
import model.gameboard.ElementData;
import model.gameboard.PuzzlePiece;

import java.util.ArrayList;

import static model.rules.RuleType.CASE;

public abstract class CaseRule extends Rule
{
    /**
     * CaseRule Constructor - creates a new case rule
     *
     * @param ruleName name of the rule
     * @param description description of the rule
     * @param imageName file name of the image
     */
    public CaseRule(String ruleName, String description, String imageName)
    {
        super(ruleName, description, imageName);
        this.ruleType = CASE;
    }

    public abstract Board getCaseBoard(Board board);

    //public abstract ArrayList<PuzzlePiece> getPuzzlePieces();

    /**
     * Gets the possible cases at a specific location based on this case rule
     *
     * @param board the current board state
     * @param elementIndex element to determine the possible cases for
     * @return a list of elements the specified could be
     */
    public abstract ArrayList<Board> getCases(Board board, int elementIndex);
}


