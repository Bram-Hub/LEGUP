package edu.rpi.legup.model.gameboard;

import edu.rpi.legup.model.Goal;
import edu.rpi.legup.model.rules.CaseRule;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Abstract class representing a game board. This class provides functionality for managing puzzle
 * elements, tracking modifications, and determining if the board is modifiable.
 */
public abstract class Board {

    protected List<PuzzleElement> puzzleElements;
    protected Set<PuzzleElement> modifiedData;
    protected Goal goal;
    protected boolean isModifiable;

    protected CaseRule caseRule;
    protected Set<PuzzleElement> casePickableElements;

    /** Board Constructor creates an empty board. */
    public Board() {
        this.puzzleElements = new ArrayList<>();
        this.modifiedData = new HashSet<>();
        this.isModifiable = true;
        this.goal = null;

        this.caseRule = null;
        this.casePickableElements = new HashSet<>();
    }

    /**
     * Board Constructor creates a board with null elements.
     *
     * @param size number of elements for the board
     */
    public Board(int size) {
        this();
        for (int i = 0; i < size; i++) {
            puzzleElements.add(null);
        }
    }

    /**
     * Gets a specific {@link PuzzleElement} from the board.
     *
     * @param puzzleElement the puzzle element to retrieve
     * @return the puzzle element at the corresponding index, or null if not found
     */
    public PuzzleElement getPuzzleElement(PuzzleElement puzzleElement) {
        if (puzzleElement == null) {
            return null;
        }
        int index = puzzleElement.getIndex();
        return index < puzzleElements.size() ? puzzleElements.get(index) : null;
    }

    /**
     * Sets a specific {@link PuzzleElement} on the board
     *
     * @param index index of the puzzleElement
     * @param puzzleElement the puzzleElement to set at the index
     */
    public void setPuzzleElement(int index, PuzzleElement puzzleElement) {
        if (index < puzzleElements.size()) {
            puzzleElements.set(index, puzzleElement);
        }
    }

    /**
     * Gets the number of elements on the board
     *
     * @return number of elements on the board
     */
    public int getElementCount() {
        return puzzleElements.size();
    }

    /**
     * Gets the {@link PuzzleElement} on the board.
     *
     * @return puzzle elements on the board
     */
    public List<PuzzleElement> getPuzzleElements() {
        return puzzleElements;
    }

    /**
     * Sets the {@link PuzzleElement} on the board.
     *
     * @param puzzleElements elements on the board
     */
    public void setPuzzleElements(List<PuzzleElement> puzzleElements) {
        this.puzzleElements = puzzleElements;
    }

    /**
     * Retrieves the case rule for this board.
     *
     * @return the case rule
     */
    public CaseRule getCaseRule() {
        return caseRule;
    }

    /**
     * Sets the case rule for this board.
     *
     * @param caseRule the new case rule
     */
    public void setCaseRule(CaseRule caseRule) {
        this.caseRule = caseRule;
    }

    /**
     * Retrieves the pickable puzzle elements for this board
     *
     * @return the pickable puzzle elements
     */
    public Set<PuzzleElement> getCasePickableElements() { return casePickableElements; }

    /**
     * Sets the pickable puzzle elements for this board.
     *
     * @param casePickableElements the new pickable puzzle elements
     */
    public void setCasePickableElements(Set<PuzzleElement> casePickableElements) { this.casePickableElements = casePickableElements; }

    /**
     * Adds a puzzle element to the set of pickable elements.
     *
     * @param puzzleElement the puzzle element to add
     */
    public void addCasePickableElement(PuzzleElement puzzleElement) {
        casePickableElements.add(puzzleElement);
    }

    /**
     * Removes a puzzle element from the set of pickable elements.
     *
     * @param puzzleElement the puzzle element to remove
     */
    public void removeCasePickableElement(PuzzleElement puzzleElement) {
        casePickableElements.remove(puzzleElement);
    }

    /**
     * Gets the count of pickable puzzle elements.
     *
     * @return the number of pickable elements
     */
    public int getCasePickableCount() {
        return casePickableElements.size();
    }

    /**
     * Checks if a puzzle element is pickable based on the mouse event.
     *
     * @param puzzleElement the puzzle element to check
     * @param e the mouse event
     * @return true if the puzzle element is pickable, false otherwise
     */
    public boolean isPickable(PuzzleElement puzzleElement, MouseEvent e) {
        return casePickableElements.contains(getPuzzleElement(puzzleElement));
    }

    /**
     * Gets the modifiable attribute for the board.
     *
     * @return true if the board is modifiable, false otherwise
     */
    public boolean isModifiable() {
        return isModifiable;
    }

    /**
     * Sets the modifiable attribute for the board.
     *
     * @param isModifiable true if the board is modifiable, false otherwise
     */
    public void setModifiable(boolean isModifiable) {
        this.isModifiable = isModifiable;
    }

    /**
     * Gets whether any of {@link PuzzleElement} of this board has been modified by the user.
     *
     * @return true if the board has been modified, false otherwise
     */
    public boolean isModified() {
        return !modifiedData.isEmpty();
    }

    /**
     * Gets the set of modified {@link PuzzleElement} of the board.
     *
     * @return set of modified puzzle element of the board
     */
    public Set<PuzzleElement> getModifiedData() {
        return modifiedData;
    }

    /**
     * Adds a {@link PuzzleElement} that has been modified to the list.
     *
     * @param puzzleElement puzzleElement that has been modified
     */
    public void addModifiedData(PuzzleElement puzzleElement) {
        modifiedData.add(puzzleElement);
        puzzleElement.setModified(true);
    }

    /**
     * Removes a {@link PuzzleElement} that is no longer modified.
     *
     * @param data puzzleElement that is no longer modified
     */
    public void removeModifiedData(PuzzleElement data) {
        modifiedData.remove(data);
        data.setModified(false);
    }

    /**
     * Called when a {@link PuzzleElement} data on this has changed and passes in the equivalent
     * puzzle element with the new data.
     *
     * @param puzzleElement equivalent puzzle element with the new data.
     */
    @SuppressWarnings("unchecked")
    public void notifyChange(PuzzleElement puzzleElement) {
        puzzleElements.set(puzzleElement.getIndex(), puzzleElement);
    }

    /**
     * Called when a {@link PuzzleElement} has been added and passes in the equivalent puzzle
     * element with the data.
     *
     * @param puzzleElement equivalent puzzle element with the data.
     */
    public void notifyAddition(PuzzleElement puzzleElement) {}

    /**
     * Called when a {@link PuzzleElement} has been deleted and passes in the equivalent puzzle
     * element with the data.
     *
     * @param puzzleElement equivalent puzzle element with the data.
     */
    public void notifyDeletion(PuzzleElement puzzleElement) {}

    @SuppressWarnings("unchecked")
    public Board mergedBoard(Board lca, List<Board> boards) {
        if (lca == null || boards.isEmpty()) {
            return null;
        }

        Board mergedBoard = lca.copy();

        Board firstBoard = boards.get(0);
        for (PuzzleElement lcaData : lca.getPuzzleElements()) {
            PuzzleElement mData = firstBoard.getPuzzleElement(lcaData);

            boolean isSame = true;
            for (Board board : boards) {
                isSame &= mData.equalsData(board.getPuzzleElement(lcaData));
            }

            if (isSame && !lcaData.equalsData(mData)) {
                PuzzleElement mergedData = mergedBoard.getPuzzleElement(lcaData);
                mergedData.setData(mData.getData());
                mergedBoard.addModifiedData(mergedData);
            }
        }

        return mergedBoard;
    }

    /**
     * Determines if this board contains the equivalent puzzle elements as the one specified
     *
     * @param board board to check equivalence
     * @return true if the boards are equivalent, false otherwise
     */
    @SuppressWarnings("unchecked")
    public boolean equalsBoard(Board board) {
        for (PuzzleElement element : puzzleElements) {
            if (!element.equalsData(board.getPuzzleElement(element))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Performs a deep copy of this board.
     * Note: This method does not copy caseRule and pickablePuzzleElements.
     *
     * @return a new copy of the board that is independent of this one
     */
    public abstract Board copy();
}
