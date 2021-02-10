package edu.rpi.legup.puzzle.skyscrapers.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.BasicRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersBoard;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersCell;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersType;

import java.awt.Point;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class LastCellBasicRule extends BasicRule {

    public LastCellBasicRule() {
        super("Last Cell",
                "A certain number must go in a certain cell, because that cell is the last place that number can appear in that row/column.",
                "edu/rpi/legup/images/skyscrapers/LastCell.png");
    }

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific puzzleElement index using this rule
     *
     * @param transition    transition to check
     * @param puzzleElement index of the puzzleElement
     * @return null if the child node logically follow from the parent node at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
    	SkyscrapersBoard initialBoard = (SkyscrapersBoard) transition.getParents().get(0).getBoard();
    	SkyscrapersCell initCell = (SkyscrapersCell) initialBoard.getPuzzleElement(puzzleElement);
    	SkyscrapersBoard finalBoard = (SkyscrapersBoard) transition.getBoard();
        SkyscrapersCell finalCell = (SkyscrapersCell) finalBoard.getPuzzleElement(puzzleElement);
        if (!(initCell.getType() == SkyscrapersType.UNKNOWN && finalCell.getType() == SkyscrapersType.Number)) {
            return "Modified cells must be number";
        }

        SkyscrapersBoard emptyCase = initialBoard.copy();
        emptyCase.getPuzzleElement(finalCell).setData(0);
        Point loc = finalCell.getLocation();
        
        Set<Integer> candidates = new HashSet<Integer>();
        for (int i = 1; i <= initialBoard.getWidth(); i++) {
        	candidates.add(i);
        }
        
        //check row
        for (int i = 0; i < initialBoard.getWidth(); i++) {
        	SkyscrapersCell c = initialBoard.getCell(i, loc.y);
            if (i != loc.x && c.getType() == SkyscrapersType.Number) {
            	candidates.remove(c.getData());
            	//System.out.print(c.getData());
            	//System.out.println(finalCell.getData());
            }
        }
        if (candidates.size() == 1) {
        	Iterator<Integer> it = candidates.iterator();
        	if (it.next() == finalCell.getData()) {
        		return null;
        	}
        	return "Wrong number in the cell.";
        }
        
        candidates.clear();
        for (int i = 1; i <= initialBoard.getWidth(); i++) {
        	candidates.add(i);
        }
        
        // check column
        for (int i = 0; i < initialBoard.getHeight(); i++) {
        	SkyscrapersCell c = initialBoard.getCell(loc.x, i);
        	if (i != loc.y && c.getType() == SkyscrapersType.Number) {
        		candidates.remove(c.getData());
        		//System.out.print(c.getData());
            	//System.out.println(finalCell.getData());
            }
        }
        if (candidates.size() == 1) {
        	Iterator<Integer> it = candidates.iterator();
        	if (it.next() == finalCell.getData()) {
        		return null;
        	}
        	return "Wrong number in the cell.";
        }
        
        return "This cell is not forced.";
    }

    private boolean isForced(SkyscrapersBoard board, SkyscrapersCell cell) {
        SkyscrapersBoard emptyCase = board.copy();
        emptyCase.getPuzzleElement(cell).setData(0);
        DuplicateNumberContradictionRule duplicate = new DuplicateNumberContradictionRule();
        if (duplicate.checkContradictionAt(emptyCase, cell) == null) {
        	System.out.println("no contradiction ln");
            return true;
        }
        return false;
    }

    /**
     * Creates a transition {@link Board} that has this rule applied to it using the {@link TreeNode}.
     *
     * @param node tree node used to create default transition board
     * @return default board or null if this rule cannot be applied to this tree node
     */
    @Override
    public Board getDefaultBoard(TreeNode node) {
    	SkyscrapersBoard initialBoard = (SkyscrapersBoard) node.getBoard();
    	SkyscrapersBoard lightUpBoard = (SkyscrapersBoard) node.getBoard().copy();
    	System.out.println(lightUpBoard.getPuzzleElements().size());
        for (PuzzleElement element : lightUpBoard.getPuzzleElements()) {
        	System.out.println("123");
        	SkyscrapersCell cell = (SkyscrapersCell) element;
            if (cell.getType() == SkyscrapersType.UNKNOWN && isForced(initialBoard, cell)) {
                //cell.setData(SkyscrapersType.BULB.value);
                lightUpBoard.addModifiedData(cell);
            }
        }
        if (lightUpBoard.getModifiedData().isEmpty()) {
            return null;
        } else {
            return lightUpBoard;
        }
    }
}
