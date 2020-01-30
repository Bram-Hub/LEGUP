package edu.rpi.legup.puzzle.shorttruthtable.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.BasicRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import edu.rpi.legup.model.rules.ContradictionRule;

import java.awt.*;
import java.util.List;

public class BasicRuleAtomic extends BasicRule {

    public BasicRuleAtomic() {
        super("Fill in all atoms",
                "If one atomic value is known, all can be filled in with that value.",
                "edu/rpi/legup/images/shorttruthtable/ruleimages/contradiction/And.png");
    }

    public String checkRuleRawAt(TreeTransition transition, PuzzleElement element){

        //Check that the puzzle element is no unknown
        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell cell = (ShortTruthTableCell) board.getPuzzleElement(element);
        System.out.print("Testing cell "+cell+"     ");
        if(!cell.isAssigned()){
            System.out.println("not assigned");
            return "Only assigned cells are allowed for this rule";
        }

        //check that it is assigned to the right value
        ShortTruthTableBoard originalBoard = (ShortTruthTableBoard) transition.getParents().get(0).getBoard();

        //Use this board to check what would happen if the cell what the oppisite value
        ShortTruthTableBoard testBoard = originalBoard.copy();
        ((ShortTruthTableCell) testBoard.getPuzzleElement(element)).setType(cell.getType().getNegation());

        //see if there is a contradiction
        ContradictionRule contradictionRule = new ContradictionRuleAtomic();
        if(contradictionRule.checkContradictionAt(testBoard, element) != null){
            System.out.println("there is a contradiction");
            return "The variable must be assigned to the same value as existing variables";
        }

        System.out.println("Valid");

        return null;

    }


    private boolean isForced(ShortTruthTableBoard board, ShortTruthTableCell cell){
        return false;
    }

    /**
     * Creates a transition {@link Board} that has this rule applied to it using the {@link TreeNode}.
     *
     * @param node short truth table board used to create default transition board
     * @return default board or null if this rule cannot be applied to this tree node
     */
    @Override
    public Board getDefaultBoard(TreeNode node) {
        ShortTruthTableBoard sttBoard = (ShortTruthTableBoard) node.getBoard().copy();
        System.out.println("Basic Atomic Rule:\n"+sttBoard);
        /*for (PuzzleElement element : sttBoard.getPuzzleElements()) {
            ShortTruthTableCell cell = (ShortTruthTableCell) element;
            if (cell.getType() == ShortTruthTableCellType.UNKNOWN && isForced(sttBoard, cell)) {
                cell.setData(TreeTentType.GRASS.value);
                treeTentBoard.addModifiedData(cell);
            }
        }
        if (treeTentBoard.getModifiedData().isEmpty()) {
            return null;
        } else {
            return treeTentBoard;
        }*/
        return null;
    }
}
