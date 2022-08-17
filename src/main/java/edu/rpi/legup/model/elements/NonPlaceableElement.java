package edu.rpi.legup.model.elements;

public abstract class NonPlaceableElement extends Element {
    public NonPlaceableElement(String elementID, String elementName, String description, String imageName) {
        super(elementID, elementName, description, imageName);
        this.elementType = ElementType.NONPLACEABLE;
    }
}
