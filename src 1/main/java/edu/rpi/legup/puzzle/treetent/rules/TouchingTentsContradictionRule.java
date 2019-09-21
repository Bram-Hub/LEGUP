package edu.rpi.legup.puzzle.treetent.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.treetent.TreeTentBoard;
import edu.rpi.legup.puzzle.treetent.TreeTentCell;
import edu.rpi.legup.puzzle.treetent.TreeTentType;

import java.awt.*;

public class TouchingTentsContradictionRule extends ContradictionRule {

    public TouchingTentsContradictionRule() {
        super("Touching Tents",
                "Tents cannot touch other tents.",
                "edu/rpi/legup/images/treetent/contra_adjacentTents.png");
    }

    /**
     * Checks whether the transition has a contradiction at the specific puzzleElement index using this rule
     *
     * @param board         board to check contradiction
     * @param puzzleElement equivalent puzzleElement
     * @return null if the transition contains a contradiction at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        TreeTentBoard treeTentBoard = (TreeTentBoard) board;
        TreeTentCell cell = (TreeTentCell) puzzleElement;
        return (cell.getType() != TreeTentType.TENT || !checkAdjacentTents(treeTentBoard,cell.getLocation()))?"Does not contain a contradiction":null;
//        TreeTentBoard treeTentBoard = (TreeTentBoard) board;
//        TreeTentCell cell = (TreeTentCell) puzzleElement;
//        System.out.println("Checking");
//        if (cell.getType() != TreeTentType.TENT) {
//            return "This cell does not contain a contradiction at this location.";
//        }
//        int adjTent = treeTentBoard.getAdjacent(cell, TreeTentType.TENT).size();
//        if (adjTent > 0) {
//            System.out.println("Contra");
//            return null;
//        } else {
//            return "This cell does not contain a contradiction at this location.";
//        }
    }
    public boolean checkAdjacentTents(TreeTentBoard board, Point loc){
        for(int i = -1;i <= 1; i++){
            for(int k = -1; k <= 1; k++){
                TreeTentCell cell = board.getCell(loc.x+i,loc.y+k);
                if((i!=0 || k!=0) && cell != null && cell.getType() == TreeTentType.TENT){
                    System.out.println("Contradiction: Touching tents");
                    return true;
                }
            }
        }
        return false;
    }
}
