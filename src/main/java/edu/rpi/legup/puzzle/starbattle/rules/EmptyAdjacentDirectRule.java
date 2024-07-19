package edu.rpi.legup.puzzle.starbattle.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.starbattle.StarBattleBoard;
import edu.rpi.legup.puzzle.starbattle.StarBattleCell;
import edu.rpi.legup.puzzle.starbattle.StarBattleCellType;

public class EmptyAdjacentDirectRule extends DirectRule {

    public EmptyAdjacentDirectRule() {
        super(
                "STBL-BASC-0010",
                "Empty Adjacent",
                "Tiles next to other tiles that need to contain a star to reach the puzzle number for their region/row/column need to be blacked out.",
                "edu/rpi/legup/images/starbattle/rules/EmptyAdjacent.png");
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
            return "Only black cells are allowed for this rule!";
        }

        StarBattleBoard modified = (StarBattleBoard) origBoard.copy();
        int X = cell.getLocation().x;
        int Y = cell.getLocation().y;
        for(int i = X-1; i <= X+1; i++){
            for(int j = Y-1; j <= Y+1; j++){
                if(i < 0 || i >= modified.getPuzzleNumber() || j < 0 || j >= modified.getPuzzleNumber()){
                    continue;
                } //else
                if(modified.getCell(i,j).getType() == StarBattleCellType.UNKNOWN){
                    modified.getCell(i,j).setData(StarBattleCellType.BLACK.value);
                }//only modify empty cells, star cells remain as is and black cells ignored in case they're unmodifiable
            }
        }

        if (contraRule.checkContradictionAt(modified, puzzleElement) != null) {
            return "Black cells must be placed adjacent to a tile(s) where a star is needed!";
        }
        return null;
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
