package edu.rpi.legup.ui.proofeditorui.treeview;

import edu.rpi.legup.model.rules.RuleType;
import edu.rpi.legup.model.tree.TreeElementType;
import edu.rpi.legup.model.tree.TreeNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

/**
 * Represents a view of a tree node in the tree structure. This class extends {@link
 * TreeElementView} and provides specific rendering and interaction functionality for tree nodes. It
 * includes visual properties and methods to manage the node's appearance, location, and its
 * relationships with other nodes.
 */
public class TreeNodeView extends TreeElementView {

    private Point location;

    private TreeTransitionView parentView;
    private ArrayList<TreeTransitionView> childViews;

    private boolean isContradictoryState;

    /** @return the radius of node views in the tree view */
    public static int getRadius() { return UIManager.getInt("Tree.nodeRadius"); }

    /** @return the diameter of node views in the tree view */
    public static int getDiameter() { return getRadius() * 2; }

    /**
     * {@code TreeNodeView} constructor creates a node for display.
     *
     * @param treeNode tree node associated with this transition
     */
    public TreeNodeView(TreeNode treeNode) {
        super(TreeElementType.NODE, treeNode);
        this.treeElement = treeNode;
        this.location = new Point();
        this.parentView = null;
        this.childViews = new ArrayList<>();
        this.isCollapsed = false;
        this.isContradictoryState = false;
        this.isVisible = true;
    }

    /**
     * Draws the {@code TreeNodeView}.
     *
     * @param graphics2D {@code Graphics2D} used for drawing
     */
    public void draw(@NotNull Graphics2D graphics2D) {
        if (isVisible()) {
            Graphics2D g = (Graphics2D) graphics2D.create();
            int radius = getRadius();
            int diameter = getDiameter();

            if (getTreeElement().getParent() != null
                    && getTreeElement().getParent().isJustified()
                    && getTreeElement().getParent().getRule().getRuleType()
                            == RuleType.CONTRADICTION) {

                isContradictoryState = true;
                double r = (radius - UIManager.getInt("Tree.contradictionWidth") / 2.0) / Math.sqrt(2);
                Line2D l1 = new Line2D.Double(location.x - r, location.y - r,
                        location.x + r, location.y + r);
                Line2D l2 = new Line2D.Double(location.x + - r, location.y + r,
                        location.x + r, location.y - r);

                g.setColor(UIManager.getColor("Tree.outline"));
                g.setStroke(new BasicStroke(UIManager.getInt("Tree.contradictionWidth")
                        + UIManager.getInt("Tree.outlineWidth") * 2,
                        BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g.draw(l1);
                g.draw(l2);

                g.setColor(UIManager.getColor("Tree.contradiction"));
                g.setStroke(new BasicStroke(UIManager.getInt("Tree.contradictionWidth"),
                        BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g.draw(l1);
                g.draw(l2);

            } else {

                if (isSelected || isHover) {
                    g.setStroke(new BasicStroke(UIManager.getInt("Tree.selectedWidth") * 2
                            + UIManager.getInt("Tree.outlineWidth") * 2));
                    g.setColor(UIManager.getColor(isSelected ? "Tree.selectedOutline" : "Tree.hoverOutline"));

                    g.drawOval(location.x - radius, location.y - radius, diameter, diameter);
                }

                isContradictoryState = false;
                g.setStroke(new BasicStroke(UIManager.getInt("Tree.outlineWidth") * 2));

                g.setColor(UIManager.getColor("Tree.outline"));
                g.drawOval(location.x - radius, location.y - radius, diameter, diameter);

                g.setColor(UIManager.getColor(
                        isSelected ? "Tree.selected" : (
                                isHover ? "Tree.hover" : (
                                        getTreeElement().isContradictoryBranch() ? "Tree.contradiction" :
                                                "Tree.default"
                ))));
                g.fillOval(location.x - radius, location.y - radius, diameter, diameter);
            }
            g.dispose();
        }
    }

    /** @return {@code true} if view is in a contradictory state; {@code false} otherwise */
    public boolean isContradictoryState() { return isContradictoryState; }

    /**
     * Gets the list of child views associated with this tree node.
     *
     * @return list of child views for this tree node
     */
    public ArrayList<TreeTransitionView> getChildViews() { return childViews; }

    /**
     * Sets the list of child views associated with this tree node.
     *
     * @param childViews list of child views for this tree node
     */
    public void setChildViews(@NotNull ArrayList<TreeTransitionView> childViews) { this.childViews = childViews; }

    /**
     * Adds a {@code TreeTransitionView} to the list of child views.
     *
     * @param nodeView {@code TreeTransitionView} to add to the list of child views
     */
    public void addChildView(@NotNull TreeTransitionView nodeView) { childViews.add(nodeView); }

    /**
     * Inserts a {@code TreeTransitionView} to the list of child views at a specified index.
     *
     * @param index index at which to add {@code nodeView}
     * @param nodeView {@code TreeTransitionView} to add to the list of child views
     */
    public void insertChildView(int index, @NotNull TreeTransitionView nodeView) { childViews.add(index, nodeView); }

    /**
     * Removes a {@code TreeTransitionView} from the list of child views.
     *
     * @param nodeView {@code TreeTransitionView} to remove from the list of child views
     */
    public void removeChildView(@NotNull TreeTransitionView nodeView) { childViews.remove(nodeView); }

    /**
     * Sets the parent tree transition view.
     *
     * @param parentView parent tree transition view
     */
    public void setParentView(@Nullable TreeTransitionView parentView) { this.parentView = parentView; }

    /**
     * Gets the parent tree transition view.
     *
     * @return parent tree transition view
     */
    public TreeTransitionView getParentView() { return parentView; }

    /**
     * Gets the tree node associated with this view.
     *
     * @return tree node
     */
    public TreeNode getTreeElement() { return (TreeNode) treeElement; }

    /**
     * Gets the location of the tree node.
     *
     * @return location of the tree node
     */
    public Point getLocation() { return location; }

    /**
     * Sets the location of the tree node.
     *
     * @param location location of the tree node
     */
    public void setLocation(@NotNull Point location) { this.location = location; }

    /**
     * Gets the x location of the tree node.
     *
     * @return x location
     */
    public int getX() { return location.x; }

    /**
     * Sets the x location of the tree node.
     *
     * @param x x location
     */
    public void setX(int x) { location.x = x; }

    /**
     * Gets the y location of the tree node.
     *
     * @return y location
     */
    public int getY() { return location.y; }

    /**
     * Sets the y location of the tree node.
     *
     * @param y y location
     */
    public void setY(int y) { location.y = y; }

    /**
     * Returns the bounding rectangle of this {@code TreeNodeView}.
     *
     * @return a {@code Rectangle} representing the bounding box of this {@code TreeNodeView}
     */
    @Override
    public Rectangle getBounds() { return new Rectangle(location.x, location.y, getDiameter(), getDiameter()); }

    /**
     * Returns the bounding rectangle of this {@code TreeNodeView} as a {@code Rectangle2D}.
     *
     * @return a {@code Rectangle2D} representing the bounding box of this {@code TreeNodeView}
     */
    @Override
    public Rectangle2D getBounds2D() { return new Rectangle(location.x, location.y, getDiameter(), getDiameter()); }

    /**
     * Determines if the specified point (x, y) is within the bounds of this {@code TreeNodeView}.
     *
     * @param x the x-coordinate of the point to check
     * @param y the y-coordinate of the point to check
     * @return {@code true} if the point is within the bounds of this {@code TreeNodeView}; {@code false} otherwise
     */
    @Override
    public boolean contains(double x, double y) { return Math.hypot(x - location.x, y - location.y) <= getRadius(); }

    /**
     * Determines if the specified {@code Point2D} object is within the bounds of this {@code TreeNodeView}.
     *
     * @param p the {@code Point2D} object representing the point to check
     * @return {@code true} if the point is within the bounds of this {@code TreeNodeView}; {@code false} otherwise
     */
    @Override
    public boolean contains(@NotNull Point2D p) { return contains(p.getX(), p.getY()); }

    /**
     * Determines if the specified rectangle defined by (x, y, width, height) intersects with the
     * bounds of this {@code TreeNodeView}.
     *
     * @param x The x-coordinate of the rectangle to check
     * @param y The y-coordinate of the rectangle to check
     * @param w The width of the rectangle to check
     * @param h The height of the rectangle to check
     * @return {@code true} if the rectangle intersects with the bounds of this {@code TreeNodeView};
     * {@code false} otherwise
     */
    @Override
    public boolean intersects(double x, double y, double w, double h) { return false; }

    /**
     * Determines if the specified {@code Rectangle2D} object intersects with the bounds of this
     * {@code TreeNodeView}.
     *
     * @param r the {@code Rectangle2D} object representing the rectangle to check
     * @return {@code true} if the rectangle intersects with the bounds of this {@code TreeNodeView};
     * {@code false} otherwise
     */
    @Override
    public boolean intersects(Rectangle2D r) { return intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight()); }

    /**
     * Determines if the specified rectangle defined by (x, y, width, height) is entirely contained
     * within the bounds of this {@code TreeNodeView}.
     *
     * @param x the x-coordinate of the rectangle to check
     * @param y the y-coordinate of the rectangle to check
     * @param w the width of the rectangle to check
     * @param h the height of the rectangle to check
     * @return {@code true} if the rectangle is entirely contained within the bounds of this {@code TreeNodeView};
     * {@code false} otherwise
     */
    @Override
    public boolean contains(double x, double y, double w, double h) { return false; }

    /**
     * Determines if the specified {@code Rectangle2D} object is entirely contained within the bounds of
     * this {@code TreeNodeView}.
     *
     * @param r the {@code Rectangle2D} object representing the rectangle to check
     * @return {@code true} if the rectangle is entirely contained within the bounds of this {@code TreeNodeView};
     * {@code false} otherwise
     */
    @Override
    public boolean contains(@NotNull Rectangle2D r) { return false; }

    /**
     * Returns an iterator over the path geometry of this {@code TreeNodeView}. The iterator provides access
     * to the path's segments and their coordinates, which can be used for rendering or hit testing.
     *
     * @param at the {@code AffineTransform} to apply to the path geometry
     * @return a {@code PathIterator} that iterates over the path geometry of this {@code TreeNodeView}
     */
    @Override
    public PathIterator getPathIterator(@NotNull AffineTransform at) { return null; }

    /**
     * Returns an iterator over the path geometry of this {@code TreeNodeView} with the specified flatness.
     * The iterator provides access to the path's segments and their coordinates, which can be used
     * for rendering or hit testing.
     *
     * @param at the {@code AffineTransform} to apply to the path geometry
     * @param flatness the maximum distance that the line segments can deviate from the true path
     * @return a {@code PathIterator} that iterates over the path geometry of this {@code TreeNodeView}
     */
    @Override
    public PathIterator getPathIterator(@NotNull AffineTransform at, double flatness) { return null; }
}
