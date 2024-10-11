package edu.rpi.legup.ui.proofeditorui.treeview;

import java.awt.Dimension;
import javax.swing.*;

/** {@code TreeToolBarButton} is a JButton that represents a button in the tree toolbar. */
public class TreeToolBarButton extends JButton {

    private TreeToolBarName name;
    private final Dimension MINIMUM_DIMENSION = new Dimension(60, 60);

    /**
     * Constructs a {@code TreeToolBarButton} with the specified icon and name.
     *
     * @param imageIcon the {@link ImageIcon} to be displayed on the button
     * @param name the {@link TreeToolBarName} associated with this button
     */
    public TreeToolBarButton(ImageIcon imageIcon, TreeToolBarName name) {
        super(imageIcon);
        this.name = name;
        this.setSize(MINIMUM_DIMENSION.width, MINIMUM_DIMENSION.height);
        this.setMinimumSize(this.MINIMUM_DIMENSION);
        this.setFocusPainted(false);
    }

    /**
     * Gets the {@link TreeToolBarName} associated with this button
     *
     * @return the {@link TreeToolBarName} associated with this button
     */
    public TreeToolBarName getToolBarName() {
        return name;
    }
}
