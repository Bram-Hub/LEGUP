package edu.rpi.legup.ui.proofeditorui.treeview;

import edu.rpi.legup.model.tree.TreeElement;
import edu.rpi.legup.model.tree.TreeElementType;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Abstract base class for views of tree elements in the tree structure. This class implements the
 * Shape interface to support custom drawing and interaction with tree elements. It holds properties
 * for rendering, interaction, and layout of the tree elements.
 */
public abstract class TreeElementView implements Shape {
    protected TreeElement treeElement;
    protected int span;
    protected int depth;
    protected boolean isSelected;
    protected boolean isHover;
    protected TreeElementType type;
    protected boolean isVisible;
    protected boolean isCollapsed;

    /**
     * {@code TreeElementView} constructor creates a tree element view.
     *
     * @param type tree element type
     * @param treeElement tree element associated with this view
     */
    protected TreeElementView(@NotNull TreeElementType type, @NotNull TreeElement treeElement) {
        this.type = type;
        this.treeElement = treeElement;
        this.isSelected = false;
        this.isHover = false;
        this.isVisible = true;
    }

    /**
     * Draws the tree element view.
     *
     * @param graphics2D {@code Graphics2D} object used to draw the tree element view
     */
    public abstract void draw(@NotNull Graphics2D graphics2D);

    /**
     * Gets the span for the subtree rooted at this view.
     *
     * @return minor axis span of this element view
     */
    public int getSpan() { return span; }

    /**
     * Sets the span for the subtree rooted at this view.
     *
     * @param span minor axis span of this element view
     */
    public void setSpan(int span) { this.span = span; }

    /**
     * Gets the depth of this tree element in the tree.
     *
     * @return depth of this tree element
     */
    public int getDepth() { return depth; }

    /**
     * Sets the depth of this tree element in the tree.
     *
     * @param depth depth of this tree element
     */
    public void setDepth(int depth) { this.depth = depth; }

    /**
     * Gets the tree element type for this view.
     *
     * @return tree element type
     */
    public TreeElementType getType() { return type; }

    /**
     * Gets the tree element associated with this view.
     *
     * @return tree element associated with this view
     */
    public TreeElement getTreeElement() { return treeElement; }

    /**
     * Sets the tree element associated with this view.
     *
     * @param treeElement tree element associated with this view
     */
    public void setTreeElement(@NotNull TreeElement treeElement) { this.treeElement = treeElement; }

    /**
     * Gets the mouse selection status.
     *
     * @return selection status
     */
    public boolean isSelected() { return isSelected; }

    /**
     * Sets the mouse selection status.
     *
     * @param isSelected selection status
     */
    public void setSelected(boolean isSelected) { this.isSelected = isSelected; }

    /**
     * Gets the mouse hover status.
     *
     * @return hover status
     */
    public boolean isHover() { return isHover; }

    /**
     * Sets the mouse hover status.
     *
     * @param isHovered hover status
     */
    public void setHover(boolean isHovered) { this.isHover = isHovered; }

    /**
     * Gets the visibility of the tree element. Tells the TreeView whether to draw the tree element.
     *
     * @return visibility of the tree element
     */
    public boolean isVisible() { return isVisible; }

    /**
     * Sets the visibility of the tree element.
     *
     * @param isVisible visibility of the tree element
     */
    public void setVisible(boolean isVisible) { this.isVisible = isVisible; }

    /**
     * Is this tree node view collapsed in the view.
     *
     * @return {@code true} if the node is collapsed; {@code false} otherwise
     */
    public boolean isCollapsed() { return isCollapsed; }

    /**
     * Sets the tree node view collapsed field.
     *
     * @param isCollapsed {@code true} if the node is collapsed; {@code false} otherwise
     */
    public void setCollapsed(boolean isCollapsed) { this.isCollapsed = isCollapsed; }
}
