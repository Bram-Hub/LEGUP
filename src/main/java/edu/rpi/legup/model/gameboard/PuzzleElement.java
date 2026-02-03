package edu.rpi.legup.model.gameboard;

import edu.rpi.legup.model.elements.Element;
import java.awt.event.MouseEvent;

/**
 * PuzzleElement represents a single element in a puzzle grid. It holds data and provides various
 * methods to manage and retrieve its properties, including modifiability, modification status, and
 * validity.
 *
 * @param <T> the type of data held by the PuzzleElement
 */
public abstract class PuzzleElement<T> {
    protected int index;
    protected T data;
    protected boolean isModifiable;
    protected boolean isModified;
    protected boolean isModifiableCaseRule;
    protected boolean isGiven;
    protected boolean isGoal;
    protected boolean isValid;
    protected int casesDepended;

    /** PuzzleElement Constructor creates a new puzzle element. */
    public PuzzleElement() {
        this.index = -1;
        this.data = null;
        this.isModifiable = true;
        this.isModifiableCaseRule = true;
        this.isModified = false;
        this.isGiven = false;
        this.isGoal = false;
        this.isValid = true;
        this.casesDepended = 0;
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
     * Gets whether this puzzle element is modifiable as a result of a case rule.
     *
     * @return true if this puzzle element is modifiable, false otherwise
     */
    public boolean isModifiableCaseRule() {
        return isModifiableCaseRule;
    }

    /**
     * Sets whether this puzzle element is modifiable as a result of a case rule.
     *
     * @param isModifiableCaseRule true if this puzzle element is modifiable, false otherwise
     */
    public void setModifiableCaseRule(boolean isModifiableCaseRule) {
        this.isModifiableCaseRule = isModifiableCaseRule;
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
     * Gets whether this puzzle element is a goal condition
     *
     * @return true if the puzzle element is a goal condition, false otherwise
     */
    public boolean isGoal() {
        return isGoal;
    }

    /**
     * Sets whether this puzzle element is a goal condition
     *
     * @param goal true if the puzzle element is a goal condition, false otherwise
     */
    public void setGoal(boolean goal) {
        isGoal = goal;
    }

    /**
     * Get whether this puzzle element data is a valid change according to the rule applied to the
     * transition that this puzzle element is contained in.
     *
     * @return true if the puzzle element logically follows from the rule, otherwise false.
     */
    public boolean isValid() {
        return this.isValid;
    }

    /**
     * Sets whether this puzzle element data is a valid change according to the rule applied to the
     * transition that this puzzle element is contained in.
     *
     * @param isValid true if the puzzle element logically follows from the rule, otherwise false.
     */
    public void setValid(boolean isValid) {
        this.isValid = isValid;
    }

    /**
     * Get the number of case rules that depend upon the state of this element
     *
     * @return number of cases
     */
    public int getCasesDepended() {
        return this.casesDepended;
    }

    /**
     * Sets the number of case rules that depend upon the state of this element
     *
     * @param cases number of cases
     */
    public void setCasesDepended(int cases) {
        this.casesDepended = cases;
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
