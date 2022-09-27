package edu.rpi.legup.model.gameboard;

import edu.rpi.legup.model.elements.Element;

import java.awt.event.MouseEvent;

public abstract class PuzzleElement<T> {
    protected int index;
    protected T data;
    protected boolean isModifiable;
    protected boolean isModified;
    protected boolean isGiven;
    protected boolean isValid;

    /**
     * PuzzleElement Constructor creates a new puzzle element.
     */
    public PuzzleElement() {
        this.index = -1;
        this.data = null;
        this.isModifiable = true;
        this.isModified = false;
        this.isGiven = false;
        this.isValid = true;
    }

    /**
     * PuzzleElement Constructor creates a new puzzle element from the specified data.
     *
     * @param data data used to create the puzzle element
     */
    public PuzzleElement(T data) {
        this();
        this.data = data;
    }

    /**
     * Gets the data that represents this puzzle element.
     *
     * @return data value
     */
    public T getData() {
        return data;
    }

    /**
     * Sets the data value that represents this puzzle element.
     *
     * @param data data value that represents this puzzle element
     */
    public void setData(T data) {
        this.data = data;
    }

    public void setType(Element e, MouseEvent m) {
        return;
    }

    /**
     * Gets whether this puzzle element is modifiable.
     *
     * @return true if this puzzle element is modifiable, false otherwise
     */
    public boolean isModifiable() {
        return isModifiable;
    }

    /**
     * Sets whether this puzzle element is modifiable.
     *
     * @param isModifiable true if this puzzle element is modifiable, false otherwise
     */
    public void setModifiable(boolean isModifiable) {
        this.isModifiable = isModifiable;
    }

    /**
     * Gets whether the puzzle element has been modified.
     *
     * @return true if the puzzle element has been modified, false otherwise
     */
    public boolean isModified() {
        return isModified;
    }

    /**
     * Sets whether the puzzle element has been modified.
     *
     * @param isModified true if the puzzle element has been modified, false otherwise
     */
    public void setModified(boolean isModified) {
        this.isModified = isModified;
    }

    /**
     * Gets the index of this puzzle element
     *
     * @return index of this puzzle element
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets the index of this puzzle element
     *
     * @param index index of this puzzle element
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Gets whether this puzzle element is given
     *
     * @return true if the puzzle element is given, false otherwise
     */
    public boolean isGiven() {
        return isGiven;
    }

    /**
     * Sets whether this puzzle element is given
     *
     * @param given true if the puzzle element is given, false otherwise
     */
    public void setGiven(boolean given) {
        isGiven = given;
    }

    /**
     * Get whether this puzzle element data is a valid change according to the rule applied to the transition that
     * this puzzle element is contained in.
     *
     * @return true if the puzzle element logically follows from the rule, otherwise false.
     */
    public boolean isValid() {
        return this.isValid;
    }

    /**
     * Sets whether this puzzle element data is a valid change according to the rule applied to the transition that
     * this puzzle element is contained in.
     *
     * @param isValid true if the puzzle element logically follows from the rule, otherwise false.
     */
    public void setValid(boolean isValid) {
        this.isValid = isValid;
    }

    /**
     * Tests whether two puzzle elements objects have the same puzzle element
     *
     * @param puzzleElement puzzle element to check for equality
     * @return true if the puzzle element are equal, false otherwise
     */
    public boolean equalsData(PuzzleElement<T> puzzleElement) {
        return data.equals(puzzleElement.data);
    }

    /**
     * Copies this puzzle element to a new puzzle element object
     *
     * @return copied puzzle element object
     */
    public abstract PuzzleElement<T> copy();
}
