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

public class PriorFilledDirectRule extends DirectRule {

    public PriorFilledDirectRule() {
        super(
                "THERM-BASC-0002",
                "Prior is Filled",
                "All tiles proceeding a filled tile in a vial must be filled",
                "edu/rpi/legup/images/thermometer/PriorIsFilled.png");
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
        if (cell.getFill() != ThermometerFill.FILLED) {
            return super.getInvalidUseOfRuleMessage() + ": Cell is not filled at this index";
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

        //Identifies next cell from tail location, checks if it is filled
        if(host.getTail() == cell){
            return super.getInvalidUseOfRuleMessage() + ": rule can not apply to tail";
        }else if(host.getTail().getLocation().getX() == x){
            if(host.getTail().getLocation().getY() > y){
                if(initialBoard.getCell(x, y + 1).getFill() == ThermometerFill.FILLED){
                    return null;
                } else{
                    return super.getInvalidUseOfRuleMessage() + "rule does not apply to this cell";
                }
            }else if(host.getTail().getLocation().getY() < y){
                if(initialBoard.getCell(x, y - 1).getFill() == ThermometerFill.FILLED){
                    return null;
                } else{
                    return super.getInvalidUseOfRuleMessage() + "rule does not apply to this cell";
                }
            } else return super.getInvalidUseOfRuleMessage() + ": Something went wrong - 2";
        }else if(host.getTail().getLocation().getY() == y){
            if(host.getTail().getLocation().getX() > x){
                if(initialBoard.getCell(x + 1, y).getFill() == ThermometerFill.FILLED){
                    return null;
                } else{
                    return super.getInvalidUseOfRuleMessage() + "rule does not apply to this cell";
                }
            }else if(host.getTail().getLocation().getX() < x){
                if(initialBoard.getCell(x - 1, y).getFill() == ThermometerFill.FILLED){
                    return null;
                } else{
                    return super.getInvalidUseOfRuleMessage() + "rule does not apply to this cell";
                }
            } else return super.getInvalidUseOfRuleMessage() + ": Something went wrong - 2.1";
        }
        return super.getInvalidUseOfRuleMessage() + "Something went wrong - 3";
    }

    /**
     * Creates a transition {@link Board} that has this rule applied to it using the {@link
     * TreeNode}.
     *
     * @param node tree node used to create default transition board
     * @return default board or null if this rule cannot be applied to this tree node
     */
    @Override
    public Board getDefaultBoard(TreeNode node) {return null;}
}