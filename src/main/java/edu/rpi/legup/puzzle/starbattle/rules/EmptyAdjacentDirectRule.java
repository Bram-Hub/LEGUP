package edu.rpi.legup.puzzle.starbattle.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.nurikabe.NurikabeCell;
import edu.rpi.legup.puzzle.nurikabe.NurikabeType;
import edu.rpi.legup.puzzle.starbattle.StarBattleBoard;
import edu.rpi.legup.puzzle.starbattle.StarBattleCell;
import edu.rpi.legup.puzzle.starbattle.StarBattleCellType;

public class EmptyAdjacentDirectRule extends DirectRule {

    public EmptyAdjacentDirectRule() {
        super(
                "STBL-BASC-0010",
                "Empty Adjacent",
                "Tiles next to other tiles that need to contain a star to reach the puzzle number for their region/row/column need to be blacked out.",
                "edu/rpi/legup/images/starbattle/rules/EmptyAdjacentDirectRule.png");
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
        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
        StarBattleBoard origBoard = (StarBattleBoard) transition.getParents().get(0).getBoard();
        ContradictionRule contraRule = new TooFewStarsContradictionRule();

        StarBattleCell cell = (StarBattleCell) board.getPuzzleElement(puzzleElement);

        if (cell.getType() != StarBattleCellType.BLACK) {
            return super.getInvalidUseOfRuleMessage()
                    + ": Only black cells are allowed for this rule!";
        }

        int x = cell.getLocation().x;
        int y = cell.getLocation().y;

        StarBattleCell northWest = board.getCell(x - 1, y - 1);
        StarBattleCell north = board.getCell(x, y - 1);
        StarBattleCell northEast = board.getCell(x + 1, y - 1);
        StarBattleCell west = board.getCell(x - 1, y);
        StarBattleCell east = board.getCell(x + 1, y);
        StarBattleCell southWest = board.getCell(x - 1, y + 1);
        StarBattleCell south = board.getCell(x, y + 1);
        StarBattleCell southEast = board.getCell(x + 1, y + 1);

        StarBattleCell[] adjacent = {northWest, north, northEast, west, east, southWest, south, southEast};

        StarBattleBoard modified = (StarBattleBoard) origBoard.copy();
        modified.getPuzzleElement(puzzleElement).setData(StarBattleCellType.STAR.value);
        for(int i = 0; i < 8; i++){                 //sets each spot to a black square if not filled
            StarBattleCell temp = adjacent[i];

            if (temp != null && temp.getType() == StarBattleCellType.UNKNOWN) {
                temp.setData(StarBattleCellType.BLACK.value);
                int X = temp.getLocation().x;
                int Y = temp.getLocation().y;
                modified.getCell(X,Y).setData(StarBattleCellType.BLACK.value);
                System.out.println("covering square " + X + " " + Y + " type " + modified.getCell(X,Y).getType() + " i = " + i + "\n");
                if(contraRule.checkContradictionAt(modified, temp) == null){
                    System.out.println("Good job!");
                    return null;        //used correctly if even one space causes a toofewstars issue
                }
            }
        }
        System.out.println("Wait why did this exit?\n");

        return "Black cells must be placed adjacent to a tile(s) where a star is needed!";
    }

    /**
     * Creates a transition {@link Board} that has this rule applied to it using the {@link
     * TreeNode}.
     *
     * @param node tree node used to create default transition board
     * @return default board or null if this rule cannot be applied to this tree node
     */
    @Override
    public Board getDefaultBoard(TreeNode node) {

        return null;
    }
}
