package edu.rpi.legup.model.gameboard;

public abstract class PuzzleElement<T>
{
    protected int index;
    protected T data;
    protected boolean isModifiable;
    protected boolean isModified;
    protected boolean isGiven;
    protected boolean isValid;

    /**
     * PuzzleElement Constructor - creates a new PuzzleElement
     */
    public PuzzleElement()
    {
        this.index = -1;
        this.data = null;
        this.isModifiable = true;
        this.isModified = false;
        this.isGiven = false;
        this.isValid = true;
    }

    /**
     * PuzzleElement Constructor - creates a new PuzzleElement from the specified data
     *
     * @param data data used to create the puzzle puzzleElement
     */
    public PuzzleElement(T data)
    {
        this();
        this.data = data;
    }

    /**
     * Gets the data that represents this puzzleElement
     *
     * @return data value
     */
    public T getData()
    {
        return data;
    }

    /**
     * Sets the data value that represents this puzzleElement
     *
     * @param data data value that represents this puzzleElement
     */
    public void setData(T data)
    {
        this.data = data;
    }

    /**
     * Gets whether this puzzleElement's puzzleElement is modifiable
     *
     * @return true if this puzzleElement's puzzleElement is modifiable, false otherwise
     */
    public boolean isModifiable()
    {
        return isModifiable;
    }

    /**
     * Sets whether this puzzleElement's puzzleElement is modifiable
     *
     * @param isModifiable true if this puzzleElement's puzzleElement is modifiable, false otherwise
     */
    public void setModifiable(boolean isModifiable)
    {
        this.isModifiable = isModifiable;
    }

    /**
     * Gets whether the puzzleElement of this puzzleElement has been modified by the edu.rpi.legup.user
     *
     * @return true if the puzzleElement has been modified, false otherwise
     */
    public boolean isModified()
    {
        return isModified;
    }

    /**
     * Sets whether the puzzleElement of this puzzleElement has been modified by the edu.rpi.legup.user
     *
     * @param isModified true if the puzzleElement has been modified, false otherwise
     */
    public void setModified(boolean isModified)
    {
        this.isModified = isModified;
    }

    /**
     * Gets the index of this PuzzleElement
     *
     * @return index of this PuzzleElement
     */
    public int getIndex()
    {
        return index;
    }

    /**
     * Sets the index of this PuzzleElement
     *
     * @param index index of this PuzzleElement
     */
    public void setIndex(int index)
    {
        this.index = index;
    }

    /**
     * Gets whether this puzzleElement is given
     *
     * @return true if the puzzleElement is given, false otherwise
     */
    public boolean isGiven()
    {
        return isGiven;
    }

    /**
     * Sets whether this puzzleElement is given
     *
     * @param given true if the puzzleElement is given, false otherwise
     */
    public void setGiven(boolean given)
    {
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
     * Tests whether two PuzzleElement objects have the same puzzleElement
     *
     * @param puzzleElement puzzleElement puzzleElement to check for equality
     * @return true if the puzzleElement are equal, false otherwise
     */
    public boolean equalsData(PuzzleElement<T> puzzleElement)
    {
        return data.equals(puzzleElement.data);
    }

    /**
     * Copies this elements puzzleElement to a new PuzzleElement object
     *
     * @return copied PuzzleElement object
     */
    public abstract PuzzleElement<T> copy();
}
