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

public class NEdgeBasicRule extends BasicRule {

    public NEdgeBasicRule() {
        super("SKYS-BASC-0004", "N Edge",
                "If the maximum number appears on an edge, the row or column��s numbers appear in ascending order, starting at that edge.",
                "edu/rpi/legup/images/skyscrapers/NEdge.png");
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
            return super.getInvalidUseOfRuleMessage() + ": Modified cells must be number";
        }

        SkyscrapersBoard emptyCase = initialBoard.copy();
        emptyCase.getPuzzleElement(finalCell).setData(0);
        Point loc = finalCell.getLocation();
        int max = initialBoard.getHeight();

        if (initialBoard.getRow().get(loc.y).getData() == max && finalCell.getData() == loc.x + 1) {
            return null;
        }
        if (initialBoard.getRowClues().get(loc.y).getData() == max && finalCell.getData() == max - loc.x) {
            return null;
        }
        if (initialBoard.getCol().get(loc.x).getData() == max && finalCell.getData() == loc.y + 1) {
            return null;
        }
        if (initialBoard.getColClues().get(loc.x).getData() == max && finalCell.getData() == max - loc.y) {
            return null;
        }

        return super.getInvalidUseOfRuleMessage() + ": This cell is not forced.";

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
        }
        else {
            return lightUpBoard;
        }
    }
}
