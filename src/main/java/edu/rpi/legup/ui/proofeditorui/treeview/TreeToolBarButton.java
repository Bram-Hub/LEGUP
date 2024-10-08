package edu.rpi.legup.ui.proofeditorui.treeview;

import java.awt.Dimension;
import javax.swing.*;

/** {@code TreeToolBarButton} is a JButton that represents a button in the tree toolbar. */
public class TreeToolBarButton extends JButton {

    private TreeToolBarName name;
    private final Dimension MINIMUM_DIMENSION = new Dimension(60, 60);

    public TreeToolBarButton(ImageIcon imageIcon, TreeToolBarName name) {
        super(imageIcon);
        this.name = name;
        this.setSize(MINIMUM_DIMENSION.width, MINIMUM_DIMENSION.height);
        this.setMinimumSize(this.MINIMUM_DIMENSION);
        this.setFocusPainted(false);
    }

    public TreeToolBarName getToolBarName() {
        return name;
    }
}
