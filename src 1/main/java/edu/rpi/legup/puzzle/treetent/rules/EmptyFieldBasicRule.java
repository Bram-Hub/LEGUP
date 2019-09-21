package edu.rpi.legup.puzzle.treetent.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.BasicRule;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.treetent.TreeTentBoard;
import edu.rpi.legup.puzzle.treetent.TreeTentCell;
import edu.rpi.legup.puzzle.treetent.TreeTentLine;
import edu.rpi.legup.puzzle.treetent.TreeTentType;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EmptyFieldBasicRule extends BasicRule {
    public EmptyFieldBasicRule() {
        super("Empty Field",
                "Blank cells not adjacent to an unlinked tree are grass.",
                "edu/rpi/legup/images/treetent/noTreesAround.png");
    }

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific puzzleElement index using this rule
     *
     * @param transition    transition to check
     * @param puzzleElement equivalent puzzleElement
     * @return null if the child node logically follow from the parent node at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        if(puzzleElement instanceof TreeTentLine) {
            return "Line is not valid for this rule.";
        }
        ContradictionRule contra1 = new NoTreeForTentContradictionRule();
        TreeTentBoard initialBoard = (TreeTentBoard) transition.getParents().get(0).getBoard();
        TreeTentCell initCell = (TreeTentCell) initialBoard.getPuzzleElement(puzzleElement);
        TreeTentBoard finalBoard = (TreeTentBoard)transition.getBoard();
        TreeTentCell finalCell = (TreeTentCell) finalBoard.getPuzzleElement(puzzleElement);
        if(finalCell.getType() != TreeTentType.GRASS){
            return "Only grass cells are allowed for this rule";
        }
        TreeTentBoard modified = initialBoard.copy();
        TreeTentCell modCell = (TreeTentCell) modified.getPuzzleElement(finalCell);
        modCell.setData(TreeTentType.TENT.value);
        if(contra1.checkContradictionAt(modified,modCell) == null){
            return null;
        } else{
            return "Not forced";
        }

//        if(checkValid(mo,finalCell.getLocation())){
//            return null;
//        }
//        else{
//            return "This cell is not forced to be empty.";
//        }
//        if (finalCell.getType() == TreeTentType.GRASS && initCell.getType() == TreeTentType.UNKNOWN) {
//            return null;
//        }
//
//        if(isForced(finalBoard, finalCell)) {
//            return null;
//        } else {
//            return "This cell is not forced to be empty.";
//        }
    }

    private boolean isForced(TreeTentBoard board, TreeTentCell cell) {
        List<TreeTentCell> adjCells = board.getAdjacent(cell, TreeTentType.TREE);
        return adjCells.isEmpty();
    }

    /**
     * Creates a transition {@link Board} that has this rule applied to it using the {@link TreeNode}.
     *
     * @param node tree node used to create default transition board
     * @return default board or null if this rule cannot be applied to this tree node
     */
    @Override
    public Board getDefaultBoard(TreeNode node) {
        TreeTentBoard treeTentBoard = (TreeTentBoard)node.getBoard().copy();
        for(PuzzleElement element : treeTentBoard.getPuzzleElements()) {
            TreeTentCell cell = (TreeTentCell)element;
            if(cell.getType() == TreeTentType.UNKNOWN && isForced(treeTentBoard, cell)) {
                cell.setData(TreeTentType.GRASS.value);
                treeTentBoard.addModifiedData(cell);
            }
        }
        if(treeTentBoard.getModifiedData().isEmpty()) {
            return null;
        } else {
            return treeTentBoard;
        }
    }

    public boolean checkValid(TreeTentBoard board, Point loc){
        ArrayList<TreeTentCell> adjacent = new ArrayList<>();
        adjacent.add(board.getCell(loc.x+1,loc.y));
        adjacent.add(board.getCell(loc.x-1,loc.y));
        adjacent.add(board.getCell(loc.x,loc.y+1));
        adjacent.add(board.getCell(loc.x,loc.y-1));
        for(TreeTentCell cell: adjacent){
            System.out.println(cell.isLinked());
            if (cell != null && (cell.getType() == TreeTentType.TREE && !cell.isLinked())) {
                return false;
            }
        }
        return true;
    }
}
