package edu.rpi.legup.model.gameboard;

import edu.rpi.legup.model.rules.CaseRule;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a game board with specific rules for selecting puzzle elements. Extends the abstract
 * `Board` class and adds functionality for handling pickable elements and case rules.
 */
public class CaseBoard extends Board {
    protected Board baseBoard;
    protected CaseRule caseRule;
    protected Set<PuzzleElement> pickablePuzzleElements;

    public CaseBoard(Board baseBoard, CaseRule caseRule) {
        this.baseBoard = baseBoard;
        this.caseRule = caseRule;
        this.pickablePuzzleElements = new HashSet<>();
    }

    public void addPickableElement(PuzzleElement puzzleElement) {
        pickablePuzzleElements.add(puzzleElement);
    }

    public void removePickableElement(PuzzleElement puzzleElement) {
        pickablePuzzleElements.remove(puzzleElement);
    }

    public boolean isPickable(PuzzleElement puzzleElement, MouseEvent e) {
        return pickablePuzzleElements.contains(baseBoard.getPuzzleElement(puzzleElement));
    }

    public Board getBaseBoard() {
        return baseBoard;
    }

    public void setBaseBoard(Board baseBoard) {
        this.baseBoard = baseBoard;
    }

    public CaseRule getCaseRule() {
        return caseRule;
    }

    public void setCaseRule(CaseRule caseRule) {
        this.caseRule = caseRule;
    }

    public int getCount() {
        return pickablePuzzleElements.size();
    }

    /**
     * Performs a deep copy of this CaseBoard. CURRENTLY NOT IMPLEMENTED AND RETURNS NULL
     *
     * @return a new copy of the CaseBoard
     */
    public CaseBoard copy() {
        return null;
    }
}
