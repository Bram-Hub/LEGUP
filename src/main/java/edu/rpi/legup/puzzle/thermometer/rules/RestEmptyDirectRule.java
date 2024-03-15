package edu.rpi.legup.puzzle.thermometer.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.thermometer.ThermometerBoard;
import edu.rpi.legup.puzzle.thermometer.ThermometerCell;
import edu.rpi.legup.puzzle.thermometer.ThermometerFill;
import edu.rpi.legup.puzzle.thermometer.elements.Vial;

import java.util.ArrayList;
import java.util.Set;

public class RestEmptyDirectRule extends DirectRule {

    public RestEmptyDirectRule() {
        super(
                "THERM-BASC-0001",
                "Rest is Empty",
                "All tiles following a blocked tile in a vial must be blocked",
                "edu/rpi/legup/images/sudoku/RestEmpty.png");
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
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        ThermometerBoard initialBoard = (ThermometerBoard) transition.getParents().get(0).getBoard();
        ThermometerBoard finalBoard = (ThermometerBoard) transition.getBoard();

        ThermometerCell cell = (ThermometerCell) finalBoard.getPuzzleElement(puzzleElement);
        if (cell.getFill() != ThermometerFill.BLOCKED) {
            return super.getInvalidUseOfRuleMessage() + ": Cell is not blocked at this index";
        }

        ArrayList<Vial> allVials = finalBoard.getVials();
        Vial host = null;
        for(Vial vials: allVials){
            if(vials.containsCell((cell))){
                host = vials;
            }
        }
        if(host == null) return super.getInvalidUseOfRuleMessage() + ": Something went wrong";

        /*if(host.containsCell(finalBoard.getCell())){

        }else if{

        }else if{

        }else{

        }*/

        return super.getInvalidUseOfRuleMessage() + ": Blocked cell is not forced";
    }

    @Override
    public Board getDefaultBoard(TreeNode node) {return null;}
}
