package ui.treeview;

import model.tree.TreeElement;
import model.tree.TreeElementType;
;

import java.awt.*;

public abstract class TreeElementView implements Shape
{
    protected TreeElement treeElement;
    protected boolean isSelected;
    protected boolean isHover;
    protected TreeElementType type;
    protected boolean isVisible;

    protected TreeElementView(TreeElementType type, TreeElement treeElement)
    {
        this.type = type;
        this.treeElement = treeElement;
        this.isSelected = false;
        this.isHover = false;
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

    public boolean isHover()
    {
        return isHover;
    }

    public void setHover(boolean isHovered)
    {
        this.isHover = isHovered;
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
