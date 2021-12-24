package edu.rpi.legup.puzzle.shorttruthtable.rules.basic;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.BasicRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.model.rules.ContradictionRule;

public abstract class BasicRule_Generic extends BasicRule {

    final ContradictionRule correspondingContradictionRule;
    final boolean eliminationRule;

    public BasicRule_Generic(String ruleName, String description, String imageName, ContradictionRule contraRule, boolean eliminationRule){
        super(ruleName, description, "edu/rpi/legup/images/shorttruthtable/ruleimages/basic/"+imageName+".png");
        this.correspondingContradictionRule = contraRule;
        this.eliminationRule = eliminationRule;
    }

    public String checkRuleRawAt(TreeTransition transition, PuzzleElement element){

        //Check that the puzzle element is not unknown
        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell cell = (ShortTruthTableCell) board.getPuzzleElement(element);

        System.out.println("Check Basic Rule Generic: "+cell);

        if(!cell.isAssigned()){
            System.out.println("  Only assigned cells");
            return "Only assigned cells are allowed for basic rules";
        }

        //check that it is assigned to the right value
        ShortTruthTableBoard originalBoard = (ShortTruthTableBoard) transition.getParents().get(0).getBoard();

        //Use this board to check what would happen if the cell what the opposite value
        ShortTruthTableBoard testBoard = originalBoard.copy();
        ((ShortTruthTableCell) testBoard.getPuzzleElement(element)).setType(cell.getType().getNegation());

        //if this is elimination/introduction check the cell or its parent
        PuzzleElement checkElement = element;

        //if elimination, check the parent
        if(this.eliminationRule){
            System.out.println("Is an elimination rule");
            checkElement = cell.getStatementReference().getParentStatement().getCell();
        }

        //see if there is a contradiction
        if (this.eliminationRule)
            System.out.println("Parent check contradiction START");
        System.out.println("checkElement: " + checkElement);
        String checkContradiction = correspondingContradictionRule.checkContradictionAt(testBoard, checkElement);
        System.out.println("Contradiction message: " + checkContradiction);
        if (this.eliminationRule)
        {
            System.out.println("Parent check contradiction END");
            System.out.println("Parent contradiction: " + checkContradiction);
        }

        // MISTAKE IS HERE:
        //if there is a contradiction when the modified element is negated, then the basic rule must be true
        // ^ that is false. Given F^T, where T was the modified element, if T is negated, it becomes F^F. F^F is F, but
        // that does not mean that F^T is true.
        if(checkContradiction==null)
            return null;

        return "Negated Contradiction Failed: "+checkContradiction;

    }

    /**
     * Creates a transition {@link Board} that has this rule applied to it using the {@link TreeNode}.
     *
     * @param node short truth table board used to create default transition board
     * @return default board or null if this rule cannot be applied to this tree node
     */
    @Override
    public Board getDefaultBoard(TreeNode node) {
        return null;
    }
}
