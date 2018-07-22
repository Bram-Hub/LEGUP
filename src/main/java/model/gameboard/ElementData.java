package model.gameboard;

public abstract class ElementData
{
    protected int index;
    protected String valueString;
    protected int valueInt;
    protected boolean isModifiable;
    protected boolean isModified;
    protected boolean isGiven;
    protected boolean isCaseApplicable;

    /**
     * ElementData Constructor - creates a new ElementData
     */
    public ElementData()
    {
        this.index = -1;
        this.valueString = null;
        this.valueInt = -1;
        this.isModifiable = true;
        this.isModified = false;
        this.isGiven = false;
        this.isCaseApplicable = false;
    }

    /**
     * ElementData Constructor - creates a new ElementData from the String value
     *
     * @param valueString String value that represents this element
     */
    public ElementData(String valueString)
    {
        this();
        this.valueString = valueString;
    }

    /**
     * ElementData Constructor - creates a new ElementData from the int value
     *
     * @param valueInt int value that represents this element
     */
    public ElementData(int valueInt)
    {
        this();
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

    public boolean isCaseApplicable()
    {
        return isCaseApplicable;
    }

    public void setCaseApplicable(boolean caseApplicable)
    {
        isCaseApplicable = caseApplicable;
    }

    /**
     * Tests whether two ElementData objects have the same element
     *
     * @param data element element to check for equality
     * @return true if the element are equal, false otherwise
     */
    public boolean equals(ElementData data)
    {
        return valueInt == data.valueInt &&
                (valueString == null || data.valueString == null ||
                valueString.equals(data.valueString));
    }

    /**
     * Copies this elements element to a new ElementData object
     *
     * @return copied ElementData object
     */
    public abstract ElementData copy();
}
