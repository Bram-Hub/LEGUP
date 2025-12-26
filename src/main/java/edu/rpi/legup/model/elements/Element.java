package edu.rpi.legup.model.elements;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Element class serves as an abstract base class for various elements used in the system. It
 * handles basic properties such as ID, name, description, and image associated with the element.
 */
@RegisterElement
public abstract class Element {
    private static final Logger LOGGER = LogManager.getLogger(Element.class.getName());

    protected String elementID;
    protected String elementName;
    protected String description;

    protected String imageName;
    protected ImageIcon image;

    protected ElementType elementType;

    private final String INVALID_USE_MESSAGE;

    /**
     * Constructs an Element with the specified ID, name, description, and image name
     *
     * @param elementID Unique identifier for the element
     * @param elementName Name of the element
     * @param description Description of the element
     * @param imageName File name of the image associated with the element
     */
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
                LOGGER.error("height is 0 error");
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("height: {}", this.image.getIconHeight());
                    LOGGER.debug("width:  {}", this.image.getIconWidth());
                }
                return;
            }
            BufferedImage bimage = new BufferedImage(100, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bimage.createGraphics();
            g.drawImage(image, 0, 0, 100, height, null);
            this.image = new ImageIcon(bimage);
        }
    }

    /**
     * Gets the name of the element
     *
     * @return The name of the element
     */
    public String getElementName() {
        return elementName;
    }

    /**
     * Sets the name of the element
     *
     * @param elementName The new name for the element
     */
    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    /**
     * Gets the unique identifier of the element
     *
     * @return The ID of the element
     */
    public String getElementID() {
        return elementID;
    }

    /**
     * Gets the description of the element
     *
     * @return The description of the element
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the image icon associated with the element
     *
     * @return The ImageIcon for the element
     */
    public ImageIcon getImageIcon() {
        return image;
    }

    /**
     * Gets the type of the element
     *
     * @return The ElementType of the element
     */
    public ElementType getElementType() {
        return elementType;
    }

    /**
     * Gets the message for invalid use of the rule
     *
     * @return The invalid use message
     */
    public String getInvalidUseOfRuleMessage() {
        return this.INVALID_USE_MESSAGE;
    }
}
