package edu.rpi.legup.model.elements;

/**
 * Abstract class representing elements that can be placed within the system.
 * Inherits from the {@link Element} class and sets the {@link ElementType} to {@link ElementType#PLACEABLE}.
 */
public abstract class PlaceableElement extends Element {

    /**
     * Constructs a PlaceableElement with the specified details
     *
     * @param elementID      Unique identifier for the element
     * @param elementName    Name of the element
     * @param description    Description of the element
     * @param imageName      Name of the image file representing the element
     */
    public PlaceableElement(
            String elementID, String elementName, String description, String imageName) {
        super(elementID, elementName, description, imageName);
        this.elementType = ElementType.PLACEABLE;
    }
}
