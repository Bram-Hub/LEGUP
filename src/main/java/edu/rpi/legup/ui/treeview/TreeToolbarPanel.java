package edu.rpi.legup.ui.treeview;

import edu.rpi.legup.controller.TreeToolBarController;

import javax.swing.*;
import java.awt.*;

public class TreeToolbarPanel extends JPanel {
    private TreePanel treePanel;
    private TreeToolBarButton addChild, delChild, merge, collapse;
    private TreeToolBarController controller;

    /**
     * TreeToolbarPanel Constructor - creates the tree tool mBar panel
     */
    public TreeToolbarPanel(TreePanel treePanel) {
        this.treePanel = treePanel;
        this.setLayout(new GridLayout(4, 1));
        this.controller = new TreeToolBarController(treePanel);

        addChild = new TreeToolBarButton(new ImageIcon(ClassLoader.getSystemResource("edu/rpi/legup/images/Legup/AddChild.png")), TreeToolBarName.ADD_CHILD);
        delChild = new TreeToolBarButton(new ImageIcon(ClassLoader.getSystemResource("edu/rpi/legup/images/Legup/DelChild.png")), TreeToolBarName.DEL_CHILD);
        merge = new TreeToolBarButton(new ImageIcon(ClassLoader.getSystemResource("edu/rpi/legup/images/Legup/Merge.png")), TreeToolBarName.MERGE);
        collapse = new TreeToolBarButton(new ImageIcon(ClassLoader.getSystemResource("edu/rpi/legup/images/Legup/Collapse.png")), TreeToolBarName.COLLAPSE);

        add(addChild);
        addChild.addActionListener(controller);
        addChild.setToolTipText("Add tree element");

        add(delChild);
        delChild.addActionListener(controller);
        delChild.setToolTipText("Remove selected tree elements");

        add(merge);
        merge.addActionListener(controller);
        merge.setToolTipText("Merge selected tree nodes");

        add(collapse);
        collapse.addActionListener(controller);
        collapse.setToolTipText("Collapse nodes");
        collapse.setEnabled(false);
    }
}