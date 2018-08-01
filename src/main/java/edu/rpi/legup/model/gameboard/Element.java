package edu.rpi.legup.model.gameboard;

public abstract class Element<T>
{
    protected int index;
    protected T data;
    protected boolean isModifiable;
    protected boolean isModified;
    protected boolean isGiven;

    /**
     * Element Constructor - creates a new Element
     */
    public Element()
    {
        this.index = -1;
        this.data = null;
        this.isModifiable = true;
        this.isModified = false;
        this.isGiven = false;
    }

    /**
     * Element Constructor - creates a new Element from the specified data
     *
     * @param data data used to create the puzzle element
     */
    public Element(T data)
    {
        this();
        this.data = data;
    }

    /**
     * Gets the data that represents this element
     *
     * @return data value
     */
    public T getData()
    {
        return data;
    }

    /**
     * Sets the data value that represents this element
     *
     * @param data data value that represents this element
     */
    public void setData(T data)
    {
        this.data = data;
    }

    /**
     * Gets whether this element's element is modifiable
     *
     * @return true if this element's element is modifiable, false otherwise
     */
    public boolean isModifiable()
    {
        return isModifiable;
    }

    /**
     * Sets whether this element's element is modifiable
     *
     * @param isModifiable true if this element's element is modifiable, false otherwise
     */
    public void setModifiable(boolean isModifiable)
    {
        this.isModifiable = isModifiable;
    }

    /**
     * Gets whether the element of this element has been modified by the edu.rpi.legup.user
     *
     * @return true if the element has been modified, false otherwise
     */
    public boolean isModified()
    {
        return isModified;
    }

    /**
     * Sets whether the element of this element has been modified by the edu.rpi.legup.user
     *
     * @param isModified true if the element has been modified, false otherwise
     */
    public void setModified(boolean isModified)
    {
        this.isModified = isModified;
    }

    /**
     * Gets the index of this Element
     *
     * @return index of this Element
     */
    public int getIndex()
    {
        return index;
    }

    /**
     * Sets the index of this Element
     *
     * @param index index of this Element
     */
    public void setIndex(int index)
    {
        this.index = index;
    }

    /**
     * Gets whether this element is given
     *
     * @return true if the element is given, false otherwise
     */
    public boolean isGiven()
    {
        return isGiven;
    }

    /**
     * Sets whether this element is given
     *
     * @param given true if the element is given, false otherwise
     */
    public void setGiven(boolean given)
    {
        isGiven = given;
    }

    /**
     * Tests whether two Element objects have the same element
     *
     * @param element element element to check for equality
     * @return true if the element are equal, false otherwise
     */
    public boolean equalsData(Element<T> element)
    {
        return data.equals(element.data);
    }

    /**
     * Copies this elements element to a new Element object
     *
     * @return copied Element object
     */
    public abstract Element<T> copy();
}
