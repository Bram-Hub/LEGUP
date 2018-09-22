package edu.rpi.legup.model.gameboard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Board {

    protected List<PuzzleElement> puzzleElements;
    protected Set<PuzzleElement> modifiedData;
    protected boolean isModifiable;

    /**
     * Board Constructor creates an empty board.
     */
    public Board() {
        this.puzzleElements = new ArrayList<>();
        this.modifiedData = new HashSet<>();
        this.isModifiable = true;
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
     * Gets a specific {@link PuzzleElement} on this board.
     *
     * @param puzzleElement equivalent puzzleElement
     * @return equivalent puzzleElement on this board
     */
    public PuzzleElement getPuzzleElement(PuzzleElement puzzleElement) {
        int index = puzzleElement.getIndex();
        return index < puzzleElements.size() ? puzzleElements.get(index) : null;
    }

    /**
     * Sets a specific {@link PuzzleElement} on the board.
     *
     * @param index         index of the puzzleElement
     * @param puzzleElement new puzzleElement at the index
     */
    public void setPuzzleElement(int index, PuzzleElement puzzleElement) {
        if (index < puzzleElements.size()) {
            puzzleElements.set(index, puzzleElement);
        }
    }

    /**
     * Gets the number of elements on the board.
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
     * Called when a {@link PuzzleElement} data on this has changed and passes in the equivalent puzzle element with
     * the new data.
     *
     * @param puzzleElement equivalent puzzle element with the new data.
     */
    @SuppressWarnings("unchecked")
    public void notifyChange(PuzzleElement puzzleElement) {
        puzzleElements.get(puzzleElement.getIndex()).setData(puzzleElement.getData());
    }

    /**
     * Called when a {@link PuzzleElement} has been added and passes in the equivalent puzzle element with the data.
     *
     * @param puzzleElement equivalent puzzle element with the data.
     */
    public void notifyAddition(PuzzleElement puzzleElement) {

    }

    /**
     * Called when a {@link PuzzleElement} has been deleted and passes in the equivalent puzzle element with the data.
     *
     * @param puzzleElement equivalent puzzle element with the data.
     */
    public void notifyDeletion(PuzzleElement puzzleElement) {

    }

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
     *
     * @return a new copy of the board that is independent of this one
     */
    public abstract Board copy();
}
