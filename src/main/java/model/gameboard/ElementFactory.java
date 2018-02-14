package model.gameboard;

public abstract class ElementFactory
{
    /**
     * Creates a element based on the String value
     *
     * @param value value that represents the element
     * @return ElementData that represents the value
     */
    public abstract ElementData createCell(String value);

    /**
     * Creates a element based on the int value
     *
     * @param value value the represents the element
     * @return ElementData that represents the value
     */
    public abstract ElementData createCell(int value);
}