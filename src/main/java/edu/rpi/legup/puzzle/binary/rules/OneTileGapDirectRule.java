package edu.rpi.legup.puzzle.binary.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;
import edu.rpi.legup.puzzle.binary.BinaryType;

public class OneTileGapDirectRule extends DirectRule {
    private final String INVALID_USE_MESSAGE = "Number at cell is incorrect";
    public OneTileGapDirectRule() {
        super("BINA-BASC-0002",
                "One Tile Gap",
                "If an empty tile is in between the same value, fill the gap with the other value.",
                "edu/rpi/legup/images/binary/rules/OneTileGapDirectRule.png");
    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        BinaryBoard board = (BinaryBoard) transition.getBoard();
        BinaryBoard origBoard = (BinaryBoard) transition.getParents().get(0).getBoard();
        ContradictionRule contraRule = new ThreeAdjacentContradictionRule();


        BinaryCell cell = (BinaryCell) board.getPuzzleElement(puzzleElement);   
        if (cell.getType() == BinaryType.UNKNOWN) {
            return "Only ONE or ZERO cells are allowed for this rule!";
        }

        if(cell.getType() != BinaryType.UNKNOWN){
            if (contraRule.checkContradictionAt(origBoard, puzzleElement) == null) {
                return "Grouping of Three Ones or Zeros found";
            }
                
        }
        return null;

    @Override
    public Board getDefaultBoard(TreeNode node) {
        return null;
    }
}