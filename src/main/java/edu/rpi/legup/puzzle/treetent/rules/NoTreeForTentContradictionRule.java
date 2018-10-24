package edu.rpi.legup.puzzle.treetent.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.treetent.TreeTentBoard;
import edu.rpi.legup.puzzle.treetent.TreeTentCell;
import edu.rpi.legup.puzzle.treetent.TreeTentType;
import java.awt.*;
import java.util.ArrayList;

public class NoTreeForTentContradictionRule extends ContradictionRule {

    public NoTreeForTentContradictionRule() {
        super("No Tree For Tent",
                "Each tent must link to a tree.",
                "edu/rpi/legup/images/treetent/contra_NoTreeForTent.png");
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
        return(cell.getType() != TreeTentType.TENT || cell.isLinked() || checkTreeforTent(treeTentBoard,cell.getLocation()))?"This cell does not contain a contradiction at this location." : null;
//        TreeTentBoard treeTentBoard = (TreeTentBoard) board;
//        TreeTentCell cell = (TreeTentCell) puzzleElement;
//        if (cell.getType() != TreeTentType.TENT) {
//            return "This cell does not contain a contradiction at this location.";
//        }
//        int adjTree = treeTentBoard.getAdjacent(cell, TreeTentType.TREE).size();
//        int adjUnknown = treeTentBoard.getAdjacent(cell, TreeTentType.UNKNOWN).size();
//        if (adjTree == 0 && adjUnknown == 0) {
//            return null;
//        } else {
//            return "This cell does not contain a contradiction at this location.";
//        }
    }

    public boolean checkTreeforTent(TreeTentBoard board, Point loc){
        ArrayList<TreeTentCell> adjacent = new ArrayList<>();
        adjacent.add(board.getCell(loc.x+1,loc.y));
        adjacent.add(board.getCell(loc.x-1,loc.y));
        adjacent.add(board.getCell(loc.x,loc.y+1));
        adjacent.add(board.getCell(loc.x,loc.y-1));
        for(TreeTentCell cell: adjacent){
            if (cell != null && (cell.getType() == TreeTentType.TREE && !cell.isLinked())) {
                return true;
            }
        }
        System.out.println("Contradiction: No tree for tent");
        return false;
    }
}
