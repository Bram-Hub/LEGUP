package puzzles.fillapix.rules;

import model.gameboard.Board;
import model.rules.ContradictionRule;

public class TooFewBlackCellsContradictionRule extends ContradictionRule
{

    public TooFewBlackCellsContradictionRule()
    {
        super(null, null, null);
    }

    @Override
    public String checkContradiction(Board board)
    {
        return null;
    }

    @Override
    public String checkContradictionAt(Board board, int elementIndex)
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
