package edu.rpi.legup.ui.proofeditorui.treeview;

import edu.rpi.legup.model.tree.TreeElementType;
import edu.rpi.legup.model.tree.TreeTransition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

/**
 * {@code TreeTransitionView} is a visual representation of a tree transition in the tree view. It
 * extends TreeElementView and displays a transition arrow between tree nodes and handles various
 * visual states such as selection, hover, and correctness.
 */
public class TreeTransitionView extends TreeElementView {

    /** The child node view of this transition. */
    private TreeNodeView childView;

    /** The list of parent node views of this transition. */
    private ArrayList<TreeNodeView> parentViews;

    /**
     * The node view responsible for reserving space for a transition from multiple parent node views.
     * This should remain {@code null} for a transition from one parent node view.
     * This node view should not have an arrow drawn from it.
     */
    private TreeNodeView layoutParentView;

    /** Arrowhead shape to draw. */
    private Polygon arrowhead;

    /** List of start points to draw arrow tails from. */
    private final List<Point> tailStartPoints;

    /** List of paths to draw tails along. */
    private final List<Path2D> tailPaths;

    /** End point to draw the tail of the arrows to. */
    private final Point tailEndPoint;

    /** End point to draw arrows to. */
    private final Point endPoint;

    /**
     * {@code TreeTransitionView} constructor creates a transition arrow for display.
     *
     * @param transition tree transition associated with this view
     */
    public TreeTransitionView(@NotNull TreeTransition transition) {
        super(TreeElementType.TRANSITION, transition);
        this.parentViews = new ArrayList<>();
        this.endPoint = new Point();
        this.tailStartPoints = new ArrayList<>();
        this.tailPaths = new ArrayList<>();
        this.tailEndPoint = new Point();
        updateArrowHead();
    }

    /**
     * {@code TreeTransitionView} constructor creates a transition arrow for display with a single parent
     * node view.
     *
     * @param transition tree transition associated with this view
     * @param parentView {@code TreeNodeView} of the single parent view associated with this transition
     */
    public TreeTransitionView(@NotNull TreeTransition transition, @NotNull TreeNodeView parentView) {
        this(transition);
        this.parentViews.add(parentView);
        this.tailStartPoints.add(new Point());
        this.tailPaths.add(null);
    }

    /**
     * {@code TreeTransitionView} constructor creates a transition arrow for display with multiple parent
     * node views and an additional parent for reserving space for the transition's subtree. It is recommended
     * to use the deepest common ancestor of the elements of {@code parentViews} for the {@code layoutParentView}.
     *
     * @param transition tree transition associated with this view
     * @param parentViews {@code TreeNodeView}s of the multiple parent views associated with this transition
     * @param layoutParentView {@code TreeNodeView} responsible for reserving space for the transition's subtree
     */
    public TreeTransitionView(@NotNull TreeTransition transition, @NotNull List<TreeNodeView> parentViews,
                              @NotNull TreeNodeView layoutParentView) {
        this(transition);
        this.parentViews.addAll(parentViews);
        for (TreeNodeView ignored : parentViews) {
            tailStartPoints.add(new Point());
            tailPaths.add(null);
        }
        this.layoutParentView = layoutParentView;
    }

    /**
     * Draws the {@code TreeTransitionView}.
     *
     * @param graphics2D {@code Graphics2D} used for drawing
     */
    public void draw(@NotNull Graphics2D graphics2D) {
        updateArrowHead();
        for (int i = 0; i < tailPaths.size(); ++i) { updateTailPath(i); }

        Graphics2D g = (Graphics2D) graphics2D.create();

        if (isSelected || isHover) {

            g.setColor(UIManager.getColor(isSelected ? "Tree.selectedOutline" : "Tree.hoverOutline"));
            g.setStroke(new BasicStroke(UIManager.getInt("Tree.selectedWidth") * 2
                    + UIManager.getInt("Tree.outlineWidth") * 2
                    + UIManager.getInt("Tree.transitionTailWeight"),
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (Path2D tailPath : tailPaths) { g.draw(tailPath); }

            g.setStroke(new BasicStroke(UIManager.getInt("Tree.selectedWidth") * 2
                    + UIManager.getInt("Tree.outlineWidth") * 2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.draw(arrowhead);
        }

        g.setColor(UIManager.getColor("Tree.outline"));
        g.setStroke(new BasicStroke(UIManager.getInt("Tree.outlineWidth") * 2
                + UIManager.getInt("Tree.transitionTailWeight"), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (Path2D tailPath : tailPaths) { g.draw(tailPath); }

        g.setStroke(new BasicStroke(UIManager.getInt("Tree.outlineWidth") * 2,
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.draw(arrowhead);

        g.setColor(UIManager.getColor(
                isHover && !isSelected ? "Tree.hover" : (
                        !getTreeElement().isJustified() ? "Tree.arrowDefault" : (
                                getTreeElement().isCorrect() ? "Tree.valid" :
                                        "Tree.invalid"
        ))));
        g.setStroke(new BasicStroke(UIManager.getInt("Tree.transitionTailWeight"),
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        for (Path2D tailPath : tailPaths) { g.draw(tailPath); }
        g.fill(arrowhead);

        g.dispose();
    }

    /** Update arrow head if {@code null} or desired dimensions have changed. */
    private void updateArrowHead() {
        int base = UIManager.getInt("Tree.transitionHeadBase");
        int height = UIManager.getInt("Tree.transitionHeadHeight");

        if (arrowhead != null) {
            Rectangle board = arrowhead.getBounds();
            if (board.width == height && board.height == base) { return; }
        }

        arrowhead = new Polygon();
        arrowhead.addPoint(endPoint.x, endPoint.y);
        arrowhead.addPoint(endPoint.x - height, endPoint.y + base / 2);
        arrowhead.addPoint(endPoint.x - height, endPoint.y - base / 2);
    }

    /**
     * Update arrow tail at {@code index} if desired end points have changed.
     *
     * @param index index of tail path to update
     */
    private void updateTailPath(int index) {
        if (tailPaths.get(index) != null) { return; }

        Path2D.Double tailPath = new Path2D.Double();
        Point tailStartPoint = tailStartPoints.get(index);
        tailPath.moveTo(tailStartPoint.x, tailStartPoint.y);

        if (tailStartPoint.y != tailEndPoint.y) {
            // If there is enough space, use two arcs of radius 'arc' to get from starting y to end y
            int dir = (int) Math.signum(tailEndPoint.y - tailStartPoint.y);

            int halfCurveSpace = UIManager.getInt("Tree.transitionTailGap") / 2;
            int arc = Math.min(UIManager.getInt("Tree.transitionArc"), halfCurveSpace);

            if (dir * (tailEndPoint.y - tailStartPoint.y) >= arc * 2) {

                tailPath.append(new Arc2D.Double(
                        tailEndPoint.x - halfCurveSpace - arc * 2,
                        tailStartPoint.y - (dir < 0 ? arc * 2 : 0),
                        arc * 2,
                        arc * 2,
                        dir * 90,
                        dir * -90,
                        Arc2D.OPEN
                ), true);
                tailPath.append(new Arc2D.Double(
                        tailEndPoint.x - halfCurveSpace,
                        tailEndPoint.y - (dir > 0 ? arc * 2 : 0),
                        arc * 2,
                        arc * 2,
                        180,
                        dir * 90,
                        Arc2D.OPEN
                ), true);
            }
            else {
                // If there is not enough space, use two arcs of whatever radius smoothly connects points
                // Visual aid at https://www.desmos.com/geometry/lvpyacqoeq
                int x1 = tailEndPoint.x - halfCurveSpace - arc;
                int y1 = tailStartPoint.y;
                int x2 = tailEndPoint.x - halfCurveSpace + arc;
                int y2 = tailEndPoint.y;
                double x3 = (x1 + x2) / 2.0;
                double y3 = (y1 + y2) / 2.0;
                double radius = Math.abs(((2 * x1 * x3) + (y1 * y1) - (x1 * x1) - (x3 * x3) - (y3 * y3))
                        / (2 * (y1 - y3)) - y1);
                double extent = 90 - Math.toDegrees(Math.asin((radius - Math.abs(y1 - y3)) / radius));

                tailPath.append(new Arc2D.Double(
                        x1 - radius,
                        y1 - (dir < 0 ? radius * 2 : 0),
                        radius * 2,
                        radius * 2,
                        dir * 90,
                        dir * -extent,
                        Arc2D.OPEN
                ), true);
                tailPath.append(new Arc2D.Double(
                        x3 + arc - radius,
                        y2 - (dir > 0 ? radius * 2 : 0) ,
                        radius * 2,
                        radius * 2,
                        dir * (-90 - extent),
                        dir * extent,
                        Arc2D.OPEN
                ), false);
            }
        }

        tailPath.lineTo(tailEndPoint.x, tailEndPoint.y);
        tailPaths.set(index, tailPath);
    }

    /**
     * Gets the {@code TreeElement} associated with this view.
     *
     * @return the {@code TreeElement} associated with this view
     */
    public TreeTransition getTreeElement() { return (TreeTransition) treeElement; }

    /**
     * Gets the {@code TreeNodeView} child view.
     *
     * @return {@code TreeNodeView} child view
     */
    public TreeNodeView getChildView() { return childView; }

    /**
     * Sets the {@code TreeNodeView} child view.
     *
     * @param childView {@code TreeNodeView} child view
     */
    public void setChildView(@Nullable TreeNodeView childView) { this.childView = childView; }

    /**
     * Gets the list of parent views associated with this tree transition view.
     *
     * @return list of parent views for this tree transition view
     */
    public ArrayList<TreeNodeView> getParentViews() { return parentViews; }

    /**
     * Sets the list of parent views associated with this tree transition view.
     *
     * @param parentViews list of parent views for this tree transition view
     */
    public void setParentViews(@NotNull ArrayList<TreeNodeView> parentViews) {
        this.parentViews = parentViews;
        tailStartPoints.clear();
        tailPaths.clear();
        for (TreeNodeView ignored : parentViews) {
            tailStartPoints.add(new Point());
            tailPaths.add(null);
        }
    }

    /**
     * Adds a {@code TreeNodeView} to the list of parent views.
     *
     * @param nodeView {@code TreeNodeView} to add to the list of parent views
     */
    public void addParentView(@NotNull TreeNodeView nodeView) {
        parentViews.add(nodeView);
        tailStartPoints.add(new Point());
        tailPaths.add(null);
    }

    /**
     * Removes a {@code TreeNodeView} from the list of parent views.
     *
     * @param nodeView {@code TreeNodeView} to remove from the list of parent views
     */
    public void removeParentView(@NotNull TreeNodeView nodeView) {
        int index = parentViews.indexOf(nodeView);
        parentViews.remove(nodeView);
        if (index != -1) {
            tailStartPoints.remove(index);
            tailPaths.remove(index);
        }
    }

    /**
     * Gets the {@code layoutParentView} that reserves space for this transition's subtree.
     *
     * @return the current {@code layoutParentView}
     */
    public TreeNodeView getLayoutParentView() { return layoutParentView; }

    /**
     * Sets the {@code layoutParentView} that reserves space for this transition's subtree.
     *
     * @param nodeView the new {@code layoutParentView}
     */
    public void setLayoutParentView(@Nullable TreeNodeView nodeView) { layoutParentView = nodeView; }

    /**
     * Gets the x-coordinate of the end point of the transition arrow.
     *
     * @return the x-coordinate of the end point
     */
    public int getEndX() { return endPoint.x; }

    /**
     * Sets the x-coordinate of the end point of the transition arrow.
     *
     * @param x the new x-coordinate of the end point
     */
    public void setEndX(int x) {
        arrowhead.translate(x - endPoint.x, 0);
        endPoint.x = x;
        tailEndPoint.x = x - UIManager.getInt("Tree.transitionHeadHeight")
                - UIManager.getInt("Tree.outlineWidth") / 2;
        tailPaths.replaceAll(ignored -> null);
    }

    /**
     * Gets the y-coordinate of the end point of the transition arrow.
     *
     * @return the y-coordinate of the end point
     */
    public int getEndY() { return endPoint.y; }

    /**
     * Sets the y-coordinate of the end point of the transition arrow.
     *
     * @param y the new y-coordinate of the end point
     */
    public void setEndY(int y) {
        arrowhead.translate(0, y - endPoint.y);
        endPoint.y = y;
        tailEndPoint.y = y;
        tailPaths.replaceAll(ignored -> null);
    }

    /**
     * Gets the start point at the specified index from the list of start points.
     *
     * @param index the index of the start point to retrieve
     * @return the start point at the specified index, or {@code null} if the index is out of range
     */
    public Point getTailStartPoint(int index) {
        return 0 <= index && index < tailStartPoints.size() ? tailStartPoints.get(index) : null;
    }

    /**
     * Sets the start point at the specified index from the list of start points.
     *
     * @param index the index of the start point to set
     * @param startPoint the new start point
     */
    public void setTailStartPoint(int index, @NotNull Point startPoint) {
        tailStartPoints.set(index, startPoint);
        tailPaths.set(index, null);
    }

    /**
     * Returns the bounding rectangle of this {@code TreeTransitionView}.
     *
     * @return a {@code Rectangle} representing the bounding box of this {@code TreeTransitionView}
     */
    @Override
    public Rectangle getBounds() { return arrowhead.getBounds(); }

    /**
     * Returns the bounding rectangle of this {@code TreeTransitionView} as a {@code Rectangle2D}.
     *
     * @return a {@code Rectangle2D} representing the bounding box of this {@code TreeTransitionView}
     */
    @Override
    public Rectangle2D getBounds2D() { return arrowhead.getBounds2D(); }

    /**
     * Determines if the specified point (x, y) is within the bounds of this {@code TreeTransitionView}.
     *
     * @param x the x-coordinate of the point to check
     * @param y the y-coordinate of the point to check
     * @return {@code true} if the point is within the bounds of this {@code TreeTransitionView}; {@code
     *     false} otherwise
     */
    @Override
    public boolean contains(double x, double y) { return arrowhead.contains(x, y); }

    /**
     * Determines if the specified {@code Point2D} object is within the bounds of this {@code TreeTransitionView}.
     *
     * @param p the {@code Point2D} object representing the point to check
     * @return {@code true} if the point is within the bounds of this {@code TreeTransitionView}; {@code
     *     false} otherwise
     */
    @Override
    public boolean contains(@NotNull Point2D p) { return arrowhead != null && arrowhead.contains(p); }

    /**
     * Determines if the specified rectangle defined by (x, y, width, height) intersects with the
     * bounds of this {@code TreeTransitionView}.
     *
     * @param x The x-coordinate of the rectangle to check
     * @param y The y-coordinate of the rectangle to check
     * @param w The width of the rectangle to check
     * @param h The height of the rectangle to check
     * @return {@code true} if the rectangle intersects with the bounds of this {@code TreeTransitionView};
     *     {@code false} otherwise
     */
    @Override
    public boolean intersects(double x, double y, double w, double h) { return arrowhead.intersects(x, y, w, h); }

    /**
     * Determines if the specified {@code Rectangle2D} object intersects with the bounds of this
     * {@code TreeTransitionView}.
     *
     * @param r the {@code Rectangle2D} object representing the rectangle to check
     * @return {@code true} if the rectangle intersects with the bounds of this {@code TreeTransitionView};
     *     {@code false} otherwise
     */
    @Override
    public boolean intersects(@NotNull Rectangle2D r) { return arrowhead.intersects(r); }

    /**
     * Determines if the specified rectangle defined by (x, y, width, height) is entirely contained
     * within the bounds of this {@code TreeTransitionView}.
     *
     * @param x the x-coordinate of the rectangle to check
     * @param y the y-coordinate of the rectangle to check
     * @param w the width of the rectangle to check
     * @param h the height of the rectangle to check
     * @return {@code true} if the rectangle is entirely contained within the bounds of this
     *     {@code TreeTransitionView}; {@code false} otherwise
     */
    @Override
    public boolean contains(double x, double y, double w, double h) { return arrowhead.contains(x, y, w, h); }

    /**
     * Determines if the specified {@code Rectangle2D} object is entirely contained within the bounds of
     * this {@code TreeTransitionView}.
     *
     * @param r the {@code Rectangle2D} object representing the rectangle to check
     * @return {@code true} if the rectangle is entirely contained within the bounds of this
     *     {@code TreeTransitionView}; {@code false} otherwise
     */
    @Override
    public boolean contains(@NotNull Rectangle2D r) { return arrowhead.contains(r); }

    /**
     * Returns an iterator over the path geometry of this {@code TreeTransitionView}. The iterator provides
     * access to the path's segments and their coordinates, which can be used for rendering or hit
     * testing.
     *
     * @param at the {@code AffineTransform} to apply to the path geometry
     * @return a {@code PathIterator} that iterates over the path geometry of this {@code TreeTransitionView}
     */
    @Override
    public PathIterator getPathIterator(@NotNull AffineTransform at) { return arrowhead.getPathIterator(at); }

    /**
     * Returns an iterator over the path geometry of this {@code TreeTransitionView} with the specified
     * flatness. The iterator provides access to the path's segments and their coordinates, which
     * can be used for rendering or hit testing.
     *
     * @param at the {@code AffineTransform} to apply to the path geometry
     * @param flatness the maximum distance that the line segments can deviate from the true path
     * @return a {@code PathIterator} that iterates over the path geometry of this {@code TreeTransitionView}
     */
    @Override
    public PathIterator getPathIterator(@NotNull AffineTransform at, double flatness) {
        return arrowhead.getPathIterator(at, flatness);
    }
}
