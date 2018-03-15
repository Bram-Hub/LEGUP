package puzzles.fillapix.rules;

import model.gameboard.Board;
import model.rules.BasicRule;

public class FinishWithBlackBasicRule extends BasicRule
{

    public FinishWithBlackBasicRule()
    {
        super(null, null, null);
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
