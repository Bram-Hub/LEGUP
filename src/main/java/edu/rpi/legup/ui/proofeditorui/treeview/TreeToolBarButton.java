package edu.rpi.legup.ui.proofeditorui.treeview;

import javax.swing.*;
import java.awt.Dimension;

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
