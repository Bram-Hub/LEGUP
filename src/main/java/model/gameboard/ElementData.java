package model.gameboard;

public abstract class ElementData
{
    protected int index;
    protected String valueString;
    protected int valueInt;
    protected boolean isModifiable;

    /**
     * ElementData Constructor - creates a new ElementData
     */
    public ElementData()
    {
        this.valueString = null;
        this.valueInt = -1;
        isModifiable = true;
    }

    /**
     * ElementData Constructor - creates a new ElementData from the String value
     *
     * @param valueString String value that represents this element
     */
    public ElementData(String valueString)
    {
        super();
        this.valueString = valueString;
    }

    /**
     * ElementData Constructor - creates a new ElementData from the int value
     *
     * @param valueInt
     */
    public ElementData(int valueInt)
    {
        super();
        this.valueInt = valueInt;
    }

    /**
     * Gets the string value that represents this element
     *
     * @return string value
     */
    public String getValueString()
    {
        return valueString;
    }

    /**
     * Sets the String value that represents this element
     *
     * @param valueString String value that represents this element
     */
    public void setValueString(String valueString)
    {
        this.valueString = valueString;
    }

    /**
     * Gets the int value that represents this element
     *
     * @return int value
     */
    public int getValueInt()
    {
        return valueInt;
    }

    /**
     * Sets the int value that represents this element
     *
     * @param valueInt int value that represents this element
     */
    public void setValueInt(int valueInt)
    {
        this.valueInt = valueInt;
    }

    /**
     * Gets whether this element's data is modifiable
     *
     * @return true if this element's data is modifiable, false otherwise
     */
    public boolean isModifiable()
    {
        return isModifiable;
    }

    /**
     * Sets whether this element's data is modifiable
     *
     * @param isModifiable true if this element's data is modifiable, false otherwise
     */
    public void setModifiable(boolean isModifiable)
    {
        this.isModifiable = isModifiable;
    }

    /**
     * Gets the index of this ElementData
     *
     * @return index of this ElementData
     */
    public int getIndex()
    {
        return index;
    }

    /**
     * Sets the index of this ElementData
     *
     * @param index index of this ElementData
     */
    public void setIndex(int index)
    {
        this.index = index;
    }

    /**
     * Tests whether two ElementData objects have the same data
     *
     * @param data element data to check for equality
     * @return true if the data are equal, false otherwise
     */
    public boolean equals(ElementData data)
    {
        return valueInt == data.valueInt && valueString.equals(data.valueString);
    }

    public abstract ElementData copy();
}
