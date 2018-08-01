package edu.rpi.legup.puzzle.nurikabe.rules;

import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.nurikabe.NurikabeBoard;
import edu.rpi.legup.puzzle.nurikabe.NurikabeCell;
import edu.rpi.legup.puzzle.nurikabe.NurikabeType;

public class BlackSquareContradictionRule extends ContradictionRule
{

    public BlackSquareContradictionRule()
    {
        super("Black Square", "There cannot be a 2x2 square of black.", "images/nurikabe/contradictions/BlackSquare.png");
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
        NurikabeBoard board = (NurikabeBoard) transition.getBoard();
        int height = board.getHeight();
        int width = board.getWidth();

        NurikabeCell cell = (NurikabeCell)board.getPuzzleElement(puzzleElement);
        if(cell.getType() != NurikabeType.BLACK)
        {
            return "Does not contain a contradiction at this index";
        }

        for(int x = cell.getLocation().x - 1; x >= 0 && x < cell.getLocation().x + 1 && x < width - 1 ; x++)
        {
            for(int y = cell.getLocation().y - 1; y >= 0 && y < cell.getLocation().y + 1&& y < height - 1; y++)
            {
                if(board.getCell(x, y).getType() == NurikabeType.BLACK &&
                        board.getCell(x + 1,y).getType() == NurikabeType.BLACK &&
                        board.getCell(x,y + 1).getType() == NurikabeType.BLACK &&
                        board.getCell(x + 1,y + 1).getType() == NurikabeType.BLACK)
                {
                    return null;
                }
            }
        }

        return "No 2x2 square of black exists.";
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
     * @param puzzleElement equivalent puzzleElement
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
