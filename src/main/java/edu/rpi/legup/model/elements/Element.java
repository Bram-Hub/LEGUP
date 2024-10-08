package edu.rpi.legup.model.elements;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

/**
 * The Element class serves as an abstract base class for various elements used in the system. It
 * handles basic properties such as ID, name, description, and image associated with the element.
 */
@RegisterElement
public abstract class Element {
    protected String elementID;
    protected String elementName;
    protected String description;

    protected String imageName;
    protected ImageIcon image;

    protected ElementType elementType;

    private final String INVALID_USE_MESSAGE;

    public Element(String elementID, String elementName, String description, String imageName) {
        this.elementID = elementID;
        this.elementName = elementName;
        this.description = description;
        this.imageName = imageName;
        this.INVALID_USE_MESSAGE = "Invalid use of the rule " + this.elementName;
        loadImage();
    }

    /**
     * Loads the image for the element and resizes it to a width of 100 pixels while maintaining
     * aspect ratio
     */
    private void loadImage() {
        if (imageName != null) {
            this.image = new ImageIcon(ClassLoader.getSystemClassLoader().getResource(imageName));
            // Resize images to be 100px wide
            Image image = this.image.getImage();
            if (this.image.getIconWidth() < 120) return;
            int height =
                    (int) (100 * ((double) this.image.getIconHeight() / this.image.getIconWidth()));
            if (height == 0) {
                System.out.println("height is 0 error");
                System.out.println("height: " + this.image.getIconHeight());
                System.out.println("width:  " + this.image.getIconWidth());
                return;
            }
            BufferedImage bimage = new BufferedImage(100, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bimage.createGraphics();
            g.drawImage(image, 0, 0, 100, height, null);
            this.image = new ImageIcon(bimage);
        }
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getElementID() {
        return elementID;
    }

    public String getDescription() {
        return description;
    }

    public ImageIcon getImageIcon() {
        return image;
    }

    public ElementType getElementType() {
        return elementType;
    }

    public String getInvalidUseOfRuleMessage() {
        return this.INVALID_USE_MESSAGE;
    }
}
