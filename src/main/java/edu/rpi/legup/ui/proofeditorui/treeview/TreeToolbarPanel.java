package edu.rpi.legup.ui.proofeditorui.treeview;

import java.awt.*;
import javax.swing.*;

public class TreeToolbarPanel extends JPanel {
  private TreePanel treePanel;
  private TreeToolBarButton addChild, delChild, merge, collapse;

  /**
   * TreeToolbarPanel Constructor - creates the tree tool mBar panel
   *
   * @param treePanel treePanel input
   */
  public TreeToolbarPanel(TreePanel treePanel) {
    this.treePanel = treePanel;
    this.setLayout(new GridLayout(4, 1, 0, 2));

    addChild =
        new TreeToolBarButton(
            new ImageIcon(ClassLoader.getSystemResource("edu/rpi/legup/images/Legup/AddChild.png")),
            TreeToolBarName.ADD_CHILD);
    delChild =
        new TreeToolBarButton(
            new ImageIcon(ClassLoader.getSystemResource("edu/rpi/legup/images/Legup/DelChild.png")),
            TreeToolBarName.DEL_CHILD);
    merge =
        new TreeToolBarButton(
            new ImageIcon(ClassLoader.getSystemResource("edu/rpi/legup/images/Legup/Merge.png")),
            TreeToolBarName.MERGE);
    collapse =
        new TreeToolBarButton(
            new ImageIcon(ClassLoader.getSystemResource("edu/rpi/legup/images/Legup/Collapse.png")),
            TreeToolBarName.COLLAPSE);

    add(addChild);
    addChild.addActionListener(a -> treePanel.add());
    addChild.setToolTipText("Add tree element");

    add(delChild);
    delChild.addActionListener(a -> treePanel.delete());
    delChild.setToolTipText("Remove selected tree elements");

    add(merge);
    merge.addActionListener(a -> treePanel.merge());
    merge.setToolTipText("Merge selected tree nodes");

    add(collapse);
    collapse.addActionListener(a -> treePanel.collapse());
    collapse.setToolTipText("Collapse nodes");
    collapse.setEnabled(false);
  }
}
