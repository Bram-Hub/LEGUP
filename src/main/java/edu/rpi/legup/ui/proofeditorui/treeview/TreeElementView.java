package edu.rpi.legup.ui.proofeditorui.treeview;

import edu.rpi.legup.model.tree.TreeElement;
import edu.rpi.legup.model.tree.TreeElementType;
import java.awt.*;

public abstract class TreeElementView implements Shape {
    protected TreeElement treeElement;
    protected double span;
    protected int depth;
    protected boolean isSelected;
    protected boolean isHover;
    protected TreeElementType type;
    protected boolean isVisible;
    protected boolean isCollapsed;

    /**
     * TreeElementView Constructor creates a tree puzzleElement view
     *
     * @param type tree puzzleElement type
     * @param treeElement tree puzzleElement puzzleElement associated with this view
     */
    protected TreeElementView(TreeElementType type, TreeElement treeElement) {
        this.type = type;
        this.treeElement = treeElement;
        this.isSelected = false;
        this.isHover = false;
        this.isVisible = true;
    }

    /**
     * Draws the tree puzzleElement view
     *
     * @param graphics2D graphics2D object used to draw the tree puzzleElement view
     */
    public abstract void draw(Graphics2D graphics2D);

    /**
     * Gets the span for the sub tree rooted at this view
     *
     * @return span bounded y span
     */
    public double getSpan() {
        return span;
    }

    /**
     * Sets the span for the sub tree rooted at this view.
     *
     * @param span bounded y span
     */
    public void setSpan(double span) {
        this.span = span;
    }

    /**
     * Gets the depth of this tree puzzleElement in the tree
     *
     * @return depth of this tree puzzleElement
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Sets the depth of this tree puzzleElement in the tree
     *
     * @param depth depth of this tree puzzleElement
     */
    public void setDepth(int depth) {
        this.depth = depth;
    }

    /**
     * Gets the tree puzzleElement type for this view
     *
     * @return tree puzzleElement type
     */
    public TreeElementType getType() {
        return type;
    }

    /**
     * Gets the tree puzzleElement associated with this view
     *
     * @return tree puzzleElement associated with this view
     */
    public TreeElement getTreeElement() {
        return treeElement;
    }

    /**
     * Sets the tree puzzleElement associated with this view
     *
     * @param treeElement tree puzzleElement associated with this view
     */
    public void setTreeElement(TreeElement treeElement) {
        this.treeElement = treeElement;
    }

    /**
     * Gets the mouse selection
     *
     * @return mouse selection
     */
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * Sets the mouse selection
     *
     * @param isSelected mouse selection
     */
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    /**
     * Gets the mouse hover
     *
     * @return mouse hover
     */
    public boolean isHover() {
        return isHover;
    }

    /**
     * Sets the mouse hover
     *
     * @param isHovered mouse hover
     */
    public void setHover(boolean isHovered) {
        this.isHover = isHovered;
    }

    /**
     * Gets the visibility of the tree puzzleElement. Tells the TreeView whether or not to draw the
     * tree puzzleElement
     *
     * @return visibility of the tree puzzleElement
     */
    public boolean isVisible() {
        return isVisible;
    }

    /**
     * Sets the visibility of the tree puzzleElement
     *
     * @param isVisible visibility of the tree puzzleElement
     */
    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    /**
     * Is this tree node view collapsed in the view
     *
     * @return true if the node is collapsed, false otherwise
     */
    public boolean isCollapsed() {
        return isCollapsed;
    }

    /**
     * Sets the tree node view collapsed field
     *
     * @param isCollapsed true if the node is collapsed, false otherwise
     */
    public void setCollapsed(boolean isCollapsed) {
        this.isCollapsed = isCollapsed;
    }
}
