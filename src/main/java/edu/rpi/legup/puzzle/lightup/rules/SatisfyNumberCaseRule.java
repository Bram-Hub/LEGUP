package edu.rpi.legup.puzzle.lightup.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.rules.RegisterRule;
import edu.rpi.legup.model.rules.RuleType;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.lightup.LightUp;
import edu.rpi.legup.puzzle.lightup.LightUpBoard;
import edu.rpi.legup.puzzle.lightup.LightUpCell;
import edu.rpi.legup.puzzle.lightup.LightUpCellType;
import edu.rpi.legup.puzzle.sudoku.SudokuCell;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SatisfyNumberCaseRule extends CaseRule
{

    public SatisfyNumberCaseRule()
    {
        super("Satisfy Number",
                "The different ways a blocks number can be satisfied.",
                "edu/rpi/legup/images/lightup/cases/SatisfyNumber.png");
    }

    @Override
    public CaseBoard getCaseBoard(Board board)
    {
        LightUpBoard lightUpBoard = (LightUpBoard) board.copy();
        CaseBoard caseBoard = new CaseBoard(lightUpBoard, this);
        lightUpBoard.setModifiable(false);
        for(PuzzleElement data: lightUpBoard.getPuzzleElements())
        {
            if(((LightUpCell)data).getType() == LightUpCellType.NUMBER)
            {
                caseBoard.addPickableElement(data);
            }
        }
        return caseBoard;
    }

    /**
     * Gets the possible cases at a specific location based on this case rule
     *
     * @param board        the current board state
     * @param puzzleElement puzzleElement to determine the possible cases for
     *
     * @return a list of elements the specified could be
     */
    @Override
    public ArrayList<Board> getCases(Board board, PuzzleElement puzzleElement)
    {
        LightUpBoard lightUpBoard = (LightUpBoard)board;
        LightUpCell cell = (LightUpCell) puzzleElement;
        Point loc = cell.getLocation();

        List<LightUpCell> openSpots = new ArrayList<>();

        int numNeeded = cell.getData();

        LightUpCell checkCell = lightUpBoard.getCell(loc.x + 1, loc.y);
        if(checkCell != null)
        {
            if(checkCell.getType() == LightUpCellType.UNKNOWN && !cell.isLite())
            {
                openSpots.add(checkCell);
            }
            else if(checkCell.getType() == LightUpCellType.BULB)
            {
                numNeeded--;
            }
        }
        checkCell = lightUpBoard.getCell(loc.x, loc.y + 1);
        if(checkCell != null)
        {
            if(checkCell.getType() == LightUpCellType.UNKNOWN && !cell.isLite())
            {
                openSpots.add(checkCell);
            }
            else if(checkCell.getType() == LightUpCellType.BULB)
            {
                numNeeded--;
            }
        }
        checkCell = lightUpBoard.getCell(loc.x - 1, loc.y);
        if(checkCell != null)
        {
            if(checkCell.getType() == LightUpCellType.UNKNOWN && !cell.isLite())
            {
                openSpots.add(checkCell);
            }
            else if(checkCell.getType() == LightUpCellType.BULB)
            {
                numNeeded--;
            }
        }
        checkCell = lightUpBoard.getCell(loc.x, loc.y - 1);
        if(checkCell != null)
        {
            if(checkCell.getType() == LightUpCellType.UNKNOWN && !cell.isLite())
            {
                openSpots.add(checkCell);
            }
            else if(checkCell.getType() == LightUpCellType.BULB)
            {
                numNeeded--;
            }
        }

        ArrayList<Board> cases = new ArrayList<>();
        if(numNeeded == 0)
        {
            return cases;
        }

        generateCases(lightUpBoard, cell.getData(), openSpots, cases);

        return cases;
    }

    private void generateCases(final LightUpBoard board, final int num, List<LightUpCell> openSpots, List<Board> cases)
    {
        if(num > openSpots.size())
        {
            return;
        }

        for(int i = 0; i < openSpots.size(); i++)
        {
            LightUpCell c = openSpots.get(i);
            LightUpBoard newCase = board.copy();
            LightUpCell newCell = c.copy();
            Point loc = c.getLocation();

            newCell.setData(-4);
            newCase.setCell(loc.x, loc.y, newCell);
            newCase.addModifiedData(newCell);

            generateCases(board, num, openSpots, cases, newCase, i);
        }
    }

    private void generateCases(final LightUpBoard board, final int num, List<LightUpCell> openSpots, List<Board> cases, LightUpBoard curBoard, int index)
    {
        if(num <= curBoard.getModifiedData().size())
        {
            cases.add(curBoard);
            return;
        }

        for(int i = index + 1; i < openSpots.size(); i++)
        {
            LightUpCell c = openSpots.get(i);
            Point loc = c.getLocation();
            LightUpCell cc = curBoard.getCell(loc.x, loc.y);
            if (!curBoard.getModifiedData().contains(cc))
            {
                LightUpBoard newCase = board.copy();
                LightUpCell newCell = c.copy();

                for(PuzzleElement mod : curBoard.getModifiedData())
                {
                    LightUpCell modCell = (LightUpCell)mod.copy();
                    Point modLoc = modCell.getLocation();

                    modCell.setData(-4);

                    newCase.setCell(modLoc.x, modLoc.y, modCell);
                    newCase.addModifiedData(modCell);
                }

                newCell.setData(-4);

                newCase.setCell(loc.x, loc.y, newCell);
                newCase.addModifiedData(newCell);

                generateCases(board, num, openSpots, cases, newCase, i);
            }
        }
    }

    /**
     * Checks whether the transition logically follows from the parent node using this rule
     *
     * @param transition transition to check
     *
     * @return null if the child node logically follow from the parent node, otherwise error message
     */
    @Override
    public String checkRuleRaw(TreeTransition transition)
    {
        List<TreeTransition> childTransitions = transition.getParents().get(0).getChildren();
        if(childTransitions.size() != 2) {
            return "This case rule must have 2 children.";
        }

        TreeTransition case1 = childTransitions.get(0);
        TreeTransition case2 = childTransitions.get(1);
        if(case1.getBoard().getModifiedData().size() != 1 ||
                case2.getBoard().getModifiedData().size() != 1) {
            return "This case rule must have 1 modified cell for each case.";
        }

        LightUpCell mod1 = (LightUpCell) case1.getBoard().getModifiedData().iterator().next();
        LightUpCell mod2 = (LightUpCell) case2.getBoard().getModifiedData().iterator().next();
        if(!mod1.getLocation().equals(mod2.getLocation())) {
            return "This case rule must modify the same cell for each case.";
        }

        if(!((mod1.getType() == LightUpCellType.EMPTY && mod2.getType() == LightUpCellType.BULB) ||
                (mod2.getType() == LightUpCellType.EMPTY && mod1.getType() == LightUpCellType.BULB))) {
            return "This case rule must an empty cell and a lite cell.";
        }

        return null;
    }

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific puzzleElement index using this rule
     *
     * @param transition   transition to check
     * @param puzzleElement index of the puzzleElement
     *
     * @return null if the child node logically follow from the parent node at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement)
    {
        return null;
    }

    private List<LightUpCell> getPossibleSpots(TreeTransition transition) {
        List<LightUpCell> spots = new ArrayList<>();
        LightUpBoard board = (LightUpBoard) transition.getBoard();
        Set<PuzzleElement> modCells = transition.getBoard().getModifiedData();
        switch(modCells.size()) {
            case 0:
                break;
            case 1:
                LightUpCell c = (LightUpCell)modCells.iterator().next();
                spots.addAll(getAdjacentCells(board, c));
                break;
            case 2:
                for(PuzzleElement element : modCells) {

                }
                break;
            case 3:

                break;
            case 4:

                break;
            default:
                break;
        }
        return spots;
    }

    private List<LightUpCell> getAdjacentCells(LightUpBoard board, LightUpCell cell) {
        List<LightUpCell> cells = new ArrayList<>();
        Point point = cell.getLocation();
        LightUpCell right = board.getCell(point.x + 1, point.y);
        if(right != null) {
            cells.add(right);
        }
        LightUpCell down = board.getCell(point.x, point.y + 1);
        if(down != null) {
            cells.add(down);
        }
        LightUpCell left = board.getCell(point.x - 1, point.y);
        if(left != null) {
            cells.add(left);
        }
        LightUpCell up = board.getCell(point.x, point.y - 1);
        if(up != null) {
            cells.add(up);
        }
        return cells;
    }

    /**
     * Checks whether the child node logically follows from the parent node using this rule
     * and if so will perform the default application of the rule
     *
     * @param transition transition to apply default application
     *
     * @return true if the child node logically follow from the parent node and accepts the changes
     * to the board, otherwise false
     */
    @Override
    public boolean doDefaultApplication(TreeTransition transition)
    {
        return false;
    }

    /**
     * Checks whether the child node logically follows from the parent node at the
     * specific puzzleElement index using this rule and if so will perform the default application of the rule
     *
     * @param transition   transition to apply default application
     * @param puzzleElement
     *
     * @return true if the child node logically follow from the parent node and accepts the changes
     * to the board, otherwise false
     */
    @Override
    public boolean doDefaultApplicationAt(TreeTransition transition, PuzzleElement puzzleElement)
    {
        return false;
    }
}
