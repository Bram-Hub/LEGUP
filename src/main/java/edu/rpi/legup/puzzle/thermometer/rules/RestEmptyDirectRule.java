package edu.rpi.legup.puzzle.thermometer.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.thermometer.ThermometerBoard;
import edu.rpi.legup.puzzle.thermometer.ThermometerCell;
import edu.rpi.legup.puzzle.thermometer.ThermometerFill;
import edu.rpi.legup.puzzle.thermometer.ThermometerVial;

import java.util.ArrayList;
import java.util.Set;

public class RestEmptyDirectRule extends DirectRule {

    public RestEmptyDirectRule() {
        super(
                "THERM-BASC-0001",
                "Rest is Empty",
                "All tiles following a blocked tile in a vial must be blocked",
                "edu/rpi/legup/images/Thermometer/RestEmpty.png");
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

        ArrayList<ThermometerVial> allVials = finalBoard.getVials();
        ThermometerVial host = null;
        for(ThermometerVial vials: allVials){
            if(vials.containsCell((cell))){
                host = vials;
            }
        }
        if(host == null) return super.getInvalidUseOfRuleMessage() + ": Something went wrong - 1";
        int x = (int)cell.getLocation().getX();
        int y = (int)cell.getLocation().getX();

        //Identifies previous cell from head location, checks if it is blocked
        if(host.getHead() == cell){
            return super.getInvalidUseOfRuleMessage() + ": rule can not apply to head";
        }else if(host.getHead().getLocation().getX() == x){
            if(host.getHead().getLocation().getY() > y){
                if(initialBoard.getCell(x, y + 1).getFill() == ThermometerFill.BLOCKED){
                    return null;
                } else{
                    return super.getInvalidUseOfRuleMessage() + "rule does not apply to this cell";
                }
            }else if(host.getHead().getLocation().getY() < y){
                if(initialBoard.getCell(x, y - 1).getFill() == ThermometerFill.BLOCKED){
                    return null;
                } else{
                    return super.getInvalidUseOfRuleMessage() + "rule does not apply to this cell";
                }
            } else return super.getInvalidUseOfRuleMessage() + ": Something went wrong - 2";
        }else if(host.getHead().getLocation().getY() == y){
            if(host.getHead().getLocation().getX() > x){
                if(initialBoard.getCell(x + 1, y).getFill() == ThermometerFill.BLOCKED){
                    return null;
                } else{
                    return super.getInvalidUseOfRuleMessage() + "rule does not apply to this cell";
                }
            }else if(host.getHead().getLocation().getX() < x){
                if(initialBoard.getCell(x - 1, y).getFill() == ThermometerFill.BLOCKED){
                    return null;
                } else{
                    return super.getInvalidUseOfRuleMessage() + "rule does not apply to this cell";
                }
            } else return super.getInvalidUseOfRuleMessage() + ": Something went wrong - 2.1";
        }
        return super.getInvalidUseOfRuleMessage() + "Something went wrong - 3";
    }

    @Override
    public Board getDefaultBoard(TreeNode node) {return null;}
}
