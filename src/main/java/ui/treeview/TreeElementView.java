package ui.treeview;

import model.rules.TreeElement;
import model.rules.TreeElementType;
;

import java.awt.*;

public abstract class TreeElementView implements Shape
{
    protected TreeElement treeElement;
    protected boolean isSelected;
    private TreeElementType type;
    private boolean isVisible;

    protected TreeElementView(TreeElementType type, TreeElement treeElement)
    {
        this.type = type;
        this.isSelected = false;
        this.treeElement = treeElement;
        this.isVisible = true;
    }

    public abstract void draw(Graphics2D graphics2D);

    public TreeElementType getType()
    {
        return type;
    }

    public TreeElement getTreeElement()
    {
        return treeElement;
    }

    public void setTreeElement(TreeElement treeElement)
    {
        this.treeElement = treeElement;
    }

    public boolean isSelected()
    {
        return isSelected;
    }

    public void setSelected(boolean isSelected)
    {
        this.isSelected = isSelected;
    }

    public boolean isVisible()
    {
        return isVisible;
    }

    public void setVisible(boolean isVisible)
    {
        this.isVisible = isVisible;
    }
}
