package puzzles.fillapix.rules;

import model.gameboard.Board;
import model.rules.CaseRule;

import java.util.ArrayList;

public class BlackOrWhiteCaseRule extends CaseRule
{

    public BlackOrWhiteCaseRule()
    {
        super(null, null,null);
    }

    @Override
    public Board getCaseBoard(Board board)
    {
        return null;
    }

    @Override
    public ArrayList<Board> getCases(Board board, int elementIndex)
    {
        return null;
    }

    @Override
    public String checkRule(Board initialBoard, Board finalBoard)
    {
        return null;
    }

    @Override
    public String checkRuleAt(Board initialBoard, Board finalBoard, int elementIndex)
    {
        return null;
    }

    @Override
    public boolean doDefaultApplication(Board initialBoard, Board finalBoard)
    {
        return false;
    }

    @Override
    public boolean doDefaultApplicationAt(Board initialBoard, Board finalBoard, int elementIndex)
    {
        return false;
    }
}
