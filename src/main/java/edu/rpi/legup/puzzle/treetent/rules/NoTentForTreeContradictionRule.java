package edu.rpi.legup.puzzle.treetent.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.treetent.TreeTentBoard;
import edu.rpi.legup.puzzle.treetent.TreeTentCell;
import edu.rpi.legup.puzzle.treetent.TreeTentType;
import edu.rpi.legup.puzzle.treetent.TreeTentLine;

import java.util.List;
import java.util.Iterator;

public class NoTentForTreeContradictionRule extends ContradictionRule {

    public NoTentForTreeContradictionRule() {
        super("TREE-CONT-0001", "No Tent For Tree",
                "Each tree must link to a tent.",
                "edu/rpi/legup/images/treetent/contra_NoTentForTree.png");
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
        if (cell.getType() != TreeTentType.TREE) {
            return super.getNoContradictionMessage();
        }
        int adjTent = treeTentBoard.getAdjacent(cell, TreeTentType.TENT).size();
        int adjUnknown = treeTentBoard.getAdjacent(cell, TreeTentType.UNKNOWN).size();
        if (adjTent == 0 && adjUnknown == 0) {
            return null;
        }
        else {
            if (adjTent != 0) {
                List<TreeTentLine> lines = treeTentBoard.getLines();
                List<TreeTentCell> adjTents = treeTentBoard.getAdjacent(cell, TreeTentType.TENT);
                for (TreeTentLine l : lines) {
                    Iterator<TreeTentCell> i = adjTents.iterator();
                    while (i.hasNext()) {
                        TreeTentCell t = i.next();
                        if (t.getLocation().equals(l.getC1().getLocation()) && !(cell.getLocation().equals(l.getC2().getLocation()))) {
                            i.remove();
                        }
                        if (t.getLocation().equals(l.getC2().getLocation()) && !(cell.getLocation().equals(l.getC2().getLocation()))) {
                            i.remove();
                        }
                    }
                }
                if (adjTents.size() == 0 && adjUnknown == 0) {
                    return null;
                }
            }
            return super.getNoContradictionMessage();
        }
    }
}
