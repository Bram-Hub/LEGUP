package ui.treeview;

import controller.TreeToolBarController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TreeToolbarPanel extends JPanel
{
    private TreePanel treePanel;
    private TreeToolBarButton addChild, delChild, merge, collapse, zoomIn, zoomOut, zoomReset, zoomFit;
    private TreeToolBarController controller;

    /**
     * TreeToolbarPanel Constructor - creates the tree tool mBar panel
     */
    public TreeToolbarPanel(TreePanel treePanel)
    {
        this.treePanel = treePanel;
        this.setLayout(new GridLayout(4, 2));
        this.controller = new TreeToolBarController(treePanel);

        addChild = new TreeToolBarButton(new ImageIcon(ClassLoader.getSystemResource("images/Legup/AddChild.png")), TreeToolBarName.ADD_CHILD);
        delChild = new TreeToolBarButton(new ImageIcon(ClassLoader.getSystemResource("images/Legup/DelChild.png")), TreeToolBarName.DEL_CHILD);
        merge = new TreeToolBarButton(new ImageIcon(ClassLoader.getSystemResource("images/Legup/Merge.png")), TreeToolBarName.MERGE);
        collapse = new TreeToolBarButton(new ImageIcon(ClassLoader.getSystemResource("images/Legup/Collapse.png")), TreeToolBarName.COLLAPSE);

        zoomIn = new TreeToolBarButton(new ImageIcon(ClassLoader.getSystemResource("images/Legup/Zoom In.png")), TreeToolBarName.ZOOM_IN);
        zoomOut = new TreeToolBarButton(new ImageIcon(ClassLoader.getSystemResource("images/Legup/Zoom Out.png")), TreeToolBarName.ZOOM_OUT);
        zoomReset = new TreeToolBarButton(new ImageIcon(ClassLoader.getSystemResource("images/Legup/Normal Zoom.png")), TreeToolBarName.ZOOM_RESET);
        zoomFit = new TreeToolBarButton(new ImageIcon(ClassLoader.getSystemResource("images/Legup/Best Fit.png")), TreeToolBarName.ZOOM_FIT);

        add(addChild);
        addChild.addActionListener(controller);
        addChild.setEnabled(false);
        addChild.setToolTipText("Finalize CaseRule");
        addChild.setBackground(Color.LIGHT_GRAY);

        add(delChild);
        delChild.addActionListener(controller);
        delChild.setToolTipText("Remove currently selected node");
        delChild.setBackground(Color.LIGHT_GRAY);

        add(merge);
        merge.addActionListener(controller);
        merge.setToolTipText("Merge nodes");
        merge.setBackground(Color.LIGHT_GRAY);

        add(collapse);
        collapse.addActionListener(controller);
        collapse.setToolTipText("Collapse nodes");
        collapse.setBackground(Color.LIGHT_GRAY);

        add(zoomIn);
        zoomIn.addActionListener(controller);
        zoomIn.setToolTipText("Zoom In");
        zoomIn.setBackground(Color.LIGHT_GRAY);

        add(zoomOut);
        zoomOut.addActionListener(controller);
        zoomOut.setToolTipText("Zoom Out");
        zoomOut.setBackground(Color.LIGHT_GRAY);

        add(zoomReset);
        zoomReset.addActionListener(controller);
        zoomReset.setToolTipText("Reset Zoom");
        zoomReset.setBackground(Color.LIGHT_GRAY);

        add(zoomFit);
        zoomFit.addActionListener(controller);
        zoomFit.setToolTipText("Best Fit");
        zoomFit.setBackground(Color.LIGHT_GRAY);
    }
}