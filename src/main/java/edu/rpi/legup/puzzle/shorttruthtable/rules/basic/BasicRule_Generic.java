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
        String checkContradiction = correspondingContradictionRule.checkContradictionAt(testBoard, checkElement);
        if (this.eliminationRule)
        {
            System.out.println("Parent check contradiction END");
            System.out.println("Parent contradiction: " + checkContradiction);
        }


        //if there is a contradiction when the modified element is negated, then the basic rule must be true
        if(checkContradiction==null && !eliminationRule)
        {
            return null;
        }
        // if it's an elimination rule, check if the original case was also invalid
        else if (checkContradiction == null && eliminationRule)
        {
            String checkOriginalContradiction = correspondingContradictionRule.checkContradictionAt(originalBoard, checkElement);
            if (checkOriginalContradiction == null)
                return "Invalid use of " + this.ruleName;
            else
                return null;
        }
        return "Negated Contradiction Failed: "+checkContradiction;

//        if (this.eliminationRule)
//        {
//            System.out.println("Elimination rule check entered");
//            // If the rule is an elimination rule, we can check if the statement contains a contradiction. If it does
//            // contain a contradiction, then we know that the rule must be false
//
//            String checkContradiction = correspondingContradictionRule.checkContradictionAt(originalBoard, checkElement);
//            System.out.println("checkContradiction: " + checkContradiction);
//            if (checkContradiction == null) // original board contains a contradiction: this is bad!
//                return "This is not a valid use of " + this.ruleName + "!";
//            return null;
//        }
//        else
//        {
//            // If the rule is not an elimination rule, we can check to see if negating the modified cell will create
//            // a contradiction
//
//            // Use the original board to check what would happen if the cell what the opposite value
//            ShortTruthTableBoard testBoard = originalBoard.copy();
//            ((ShortTruthTableCell) testBoard.getPuzzleElement(element)).setType(cell.getType().getNegation());
//            String checkContradiction = correspondingContradictionRule.checkContradictionAt(testBoard, checkElement);
//            if (checkContradiction == null) // modified board contains a contradiction: this is good!
//                return null;
//            return "Negated Contradiction Failed: "+checkContradiction;
//        }
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
