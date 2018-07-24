package model.gameboard;

public abstract class Element
{
    protected int index;
    protected Object data;
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
     * Element Constructor - creates a new Element from the String value
     *
     * @param data String value that represents this element
     */
    public Element(String data)
    {
        this();
        this.data = data;
    }

    /**
     * Element Constructor - creates a new Element from the int value
     *
     * @param data int value that represents this element
     */
    public Element(int data)
    {
        this();
        this.data = data;
    }

    /**
     * Gets the int value that represents this element
     *
     * @return int value
     */
    public Object getData()
    {
        return data;
    }

    /**
     * Sets the int value that represents this element
     *
     * @param data int value that represents this element
     */
    public void setData(Object data)
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
     * Gets whether the element of this element has been modified by the user
     *
     * @return true if the element has been modified, false otherwise
     */
    public boolean isModified()
    {
        return isModified;
    }

    /**
     * Sets whether the element of this element has been modified by the user
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
    public boolean equalsData(Element element)
    {
        return data.equals(element.data);
    }

    /**
     * Copies this elements element to a new Element object
     *
     * @return copied Element object
     */
    public abstract Element copy();
}
