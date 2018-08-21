package edu.rpi.legup.ui.treeview;

import javax.swing.*;

public class TreeToolBarButton extends JButton
{

    private TreeToolBarName name;

    public TreeToolBarButton(ImageIcon imageIcon, TreeToolBarName name)
    {
        super(imageIcon);
        this.name = name;
        this.setSize(60, 60);
    }

    public TreeToolBarName getToolBarName()
    {
        return name;
    }
}
