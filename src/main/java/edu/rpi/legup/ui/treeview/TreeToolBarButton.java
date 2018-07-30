package edu.rpi.legup.ui.treeview;

import javax.swing.*;

public class TreeToolBarButton extends JButton
{

    private TreeToolBarName name;

    public TreeToolBarButton(ImageIcon imageIcon, TreeToolBarName name)
    {
        super(imageIcon);
        this.name = name;
    }

    public TreeToolBarName getToolBarName()
    {
        return name;
    }
}
