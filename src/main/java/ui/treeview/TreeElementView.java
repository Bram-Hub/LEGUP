package ui.treeview;

import model.tree.TreeElement;
import model.tree.TreeElementType;

import java.awt.*;

public abstract class TreeElementView implements Shape
{
    protected TreeElement treeElement;
    protected int span;
    protected boolean isSelected;
    protected boolean isHover;
    protected TreeElementType type;
    protected boolean isVisible;

    /**
     * TreeElementView Constructor - creates a tree element view
     *
     * @param type tree element type
     * @param treeElement tree element data associated with this view
     */
    protected TreeElementView(TreeElementType type, TreeElement treeElement)
    {
        this.type = type;
        this.treeElement = treeElement;
        this.isSelected = false;
        this.isHover = false;
        this.isVisible = true;
    }

    /**
     * Draws the tree element view
     *
     * @param graphics2D graphics2D object used to draw the tree element view
     */
    public abstract void draw(Graphics2D graphics2D);

    /**
     * Gets the span for the sub tree rooted at this view
     *
     * @return span
     */
    public int getSpan()
    {
        return span;
    }

    /**
     * Sets the span for the sub tree rooted at this view.
     *
     * @param span
     */
    public void setSpan(int span)
    {
        this.span = span;
    }

    /**
     * Gets the tree element type for this view
     *
     * @return tree element type
     */
    public TreeElementType getType()
    {
        return type;
    }

    /**
     * Gets the tree element associated with this view
     *
     * @return tree element associated with this view
     */
    public TreeElement getTreeElement()
    {
        return treeElement;
    }

    /**
     * Sets the tree element associated with this view
     *
     * @param treeElement tree element associated with this view
     */
    public void setTreeElement(TreeElement treeElement)
    {
        this.treeElement = treeElement;
    }

    /**
     * Gets the mouse selection
     *
     * @return mouse selection
     */
    public boolean isSelected()
    {
        return isSelected;
    }

    /**
     * Sets the mouse selection
     *
     * @param isSelected mouse selection
     */
    public void setSelected(boolean isSelected)
    {
        this.isSelected = isSelected;
    }

    /**
     * Gets the mouse hover
     *
     * @return mouse hover
     */
    public boolean isHover()
    {
        return isHover;
    }

    /**
     * Sets the mouse hover
     *
     * @param isHovered mouse hover
     */
    public void setHover(boolean isHovered)
    {
        this.isHover = isHovered;
    }

    /**
     * Gets the visibility of the tree element.
     * Tells the TreeView whether or not to draw the tree element
     *
     * @return visibility of the tree element
     */
    public boolean isVisible()
    {
        return isVisible;
    }

    /**
     * Sets the visibility of the tree element
     *
     * @param isVisible visibility of the tree element
     */
    public void setVisible(boolean isVisible)
    {
        this.isVisible = isVisible;
    }
}
