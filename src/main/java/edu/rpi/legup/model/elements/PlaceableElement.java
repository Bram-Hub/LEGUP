package edu.rpi.legup.model.elements;

public abstract class PlaceableElement extends Element {
    public PlaceableElement(String elementID, String elementName, String description, String imageName) {
        super(elementID, elementName, description, imageName);
        this.elementType = ElementType.PLACEABLE;
    }
}
