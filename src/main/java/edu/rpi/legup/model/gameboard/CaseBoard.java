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

    /**
     * Constructs a CaseBoard with a base board and a case rule.
     *
     * @param baseBoard the base board to use for this CaseBoard
     * @param caseRule the case rule applied to this CaseBoard
     */
    public CaseBoard(Board baseBoard, CaseRule caseRule) {
        this.baseBoard = baseBoard;
        this.caseRule = caseRule;
        this.pickablePuzzleElements = new HashSet<>();
    }

    /**
     * Adds a puzzle element to the set of pickable elements.
     *
     * @param puzzleElement the puzzle element to add
     */
    public void addPickableElement(PuzzleElement puzzleElement) {
        pickablePuzzleElements.add(puzzleElement);
    }

    /**
     * Removes a puzzle element from the set of pickable elements.
     *
     * @param puzzleElement the puzzle element to remove
     */
    public void removePickableElement(PuzzleElement puzzleElement) {
        pickablePuzzleElements.remove(puzzleElement);
    }

    /**
     * Checks if a puzzle element is pickable based on the mouse event.
     *
     * @param puzzleElement the puzzle element to check
     * @param e the mouse event
     * @return true if the puzzle element is pickable, false otherwise
     */
    public boolean isPickable(PuzzleElement puzzleElement, MouseEvent e) {
        return pickablePuzzleElements.contains(baseBoard.getPuzzleElement(puzzleElement));
    }

    /**
     * Retrieves the base board for this CaseBoard.
     *
     * @return the base board
     */
    public Board getBaseBoard() {
        return baseBoard;
    }

    /**
     * Sets the base board for this CaseBoard.
     *
     * @param baseBoard the new base board
     */
    public void setBaseBoard(Board baseBoard) {
        this.baseBoard = baseBoard;
    }

    /**
     * Retrieves the case rule for this CaseBoard.
     *
     * @return the case rule
     */
    public CaseRule getCaseRule() {
        return caseRule;
    }

    /**
     * Sets the case rule for this CaseBoard.
     *
     * @param caseRule the new case rule
     */
    public void setCaseRule(CaseRule caseRule) {
        this.caseRule = caseRule;
    }

    /**
     * Gets the count of pickable puzzle elements.
     *
     * @return the number of pickable elements
     */
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
