package edu.rpi.legup.ui.puzzleeditorui.treeview;

import edu.rpi.legup.model.tree.TreeElementType;
import edu.rpi.legup.model.tree.TreeTransition;

import java.awt.*;
import java.awt.geom.*;
import java.util.List;
import java.util.ArrayList;

import static java.lang.Math.*;

public class TreeTransitionView extends TreeElementView {
    static final int RADIUS = 25;
    static final int DIAMETER = 2 * RADIUS;
    static final int GAP = 5;

    private static final Stroke MAIN_STROKE = new BasicStroke(3);
    private static final Stroke SELECTION_STROKE = new BasicStroke(2);

    private static final Color OUTLINE_COLOR = Color.BLACK;
    private static final Color CORRECT_COLOR = Color.GREEN;
    private static final Color INCORRECT_COLOR = Color.RED;
    private static final Color DEFAULT_COLOR = Color.GRAY;
    private static final Color X_COLOR = Color.RED;

    private static final Color OUTLINE_SELECTION_COLOR = new Color(0x1976D2);

    private static final Color HOVER_COLOR = new Color(0x90CAF9);
    private static final Color OUTLINE_HOVER_COLOR = new Color(0xBDBDBD);

    private TreeNodeView childView;
    private ArrayList<TreeNodeView> parentViews;
    private Polygon arrowhead;

    private List<Point> lineStartPoints;
    private Point lineEndPoint;

    private Point endPoint;

    /**
     * TreeTransitionView Constructor creates a transition arrow for display
     *
     * @param transition tree transition associated with this view
     */
    public TreeTransitionView(TreeTransition transition) {
        super(TreeElementType.TRANSITION, transition);
        this.parentViews = new ArrayList<>();
        this.isCollapsed = false;
        this.endPoint = new Point();
        this.lineStartPoints = new ArrayList<>();
        this.lineEndPoint = new Point();
    }

    /**
     * TreeTransitionView Constructor creates a transition arrow for display
     *
     * @param transition tree transition associated with this view
     * @param parentView TreeNodeView of the parent associated with this transition
     */
    public TreeTransitionView(TreeTransition transition, TreeNodeView parentView) {
        this(transition);
        this.parentViews.add(parentView);
        this.lineStartPoints.add(new Point());
    }

    /**
     * Draws the TreeTransitionView
     *
     * @param graphics2D graphics2D used for drawing
     */
    public void draw(Graphics2D graphics2D) {
        arrowhead = createTransitionTriangle(RADIUS);

        graphics2D.setColor(OUTLINE_COLOR);
        graphics2D.setStroke(MAIN_STROKE);

        for (Point lineStartPoint : lineStartPoints) {
            CubicCurve2D c = new CubicCurve2D.Double();
            double ctrlx1 = lineEndPoint.x - 25;
            double ctrly1 = lineStartPoint.y;
            double ctrlx2 = lineEndPoint.x - 25;
            double ctrly2 = lineEndPoint.y;

            c.setCurve(lineStartPoint.x, lineStartPoint.y, ctrlx1,
                    ctrly1, ctrlx2, ctrly2, lineEndPoint.x, lineEndPoint.y);
            graphics2D.draw(c);
        }

        if (isSelected) {
            graphics2D.setColor(getTreeElement().isJustified() ? getTreeElement().isCorrect() ? CORRECT_COLOR : INCORRECT_COLOR : DEFAULT_COLOR);
            graphics2D.fillPolygon(arrowhead);

            graphics2D.setColor(OUTLINE_COLOR);
            graphics2D.drawPolygon(arrowhead);

            Polygon selection_triangle = createTransitionTriangle(RADIUS + 10);
            selection_triangle.translate(7, 0);

            graphics2D.setStroke(SELECTION_STROKE);
            graphics2D.setColor(OUTLINE_SELECTION_COLOR);
            graphics2D.drawPolygon(selection_triangle);
        } else if (isHover) {
            graphics2D.setColor(HOVER_COLOR);
            graphics2D.fillPolygon(arrowhead);

            graphics2D.setColor(OUTLINE_COLOR);
            graphics2D.drawPolygon(arrowhead);

            Polygon selection_triangle = createTransitionTriangle(RADIUS + 10);
            selection_triangle.translate(7, 0);

            graphics2D.setStroke(SELECTION_STROKE);
            graphics2D.setColor(OUTLINE_HOVER_COLOR);
            graphics2D.drawPolygon(selection_triangle);
        } else {
            graphics2D.setColor(getTreeElement().isJustified() ? getTreeElement().isCorrect() ? CORRECT_COLOR : INCORRECT_COLOR : DEFAULT_COLOR);
            graphics2D.fillPolygon(arrowhead);

            graphics2D.setColor(OUTLINE_COLOR);
            graphics2D.drawPolygon(arrowhead);
        }
    }

    /**
     * Constructs the arrowhead shape from the start and end points
     */
    private Polygon createTransitionTriangle(int radius) {
        double thetaArrow = Math.toRadians(30);

        int point1X = endPoint.x;
        int point1Y = endPoint.y;

        int point2X = point1X - radius;
        int point2Y = point1Y + (int) Math.round(radius / (2 * cos(thetaArrow)));

        int point3X = point1X - radius;
        int point3Y = point1Y - (int) Math.round(radius / (2 * cos(thetaArrow)));

        lineEndPoint.x = point2X;
        lineEndPoint.y = (point3Y - point2Y) / 2 + point2Y;

        Polygon tri = new Polygon();
        tri.addPoint(point1X, point1Y);
        tri.addPoint(point2X, point2Y);
        tri.addPoint(point3X, point3Y);

        return tri;
    }

    /**
     * Gets the TreeElement associated with this view
     *
     * @return the TreeElement associated with this view
     */
    public TreeTransition getTreeElement() {
        return (TreeTransition) treeElement;
    }

    /**
     * Gets the TreeNodeView child view
     *
     * @return TreeNodeView child view
     */
    public TreeNodeView getChildView() {
        return childView;
    }

    /**
     * Sets the TreeNodeView child view
     *
     * @param childView TreeNodeView child view
     */
    public void setChildView(TreeNodeView childView) {
        this.childView = childView;
    }

    /**
     * Gets the list of parent views associated with this tree transition view
     *
     * @return list of parent views for this tree transition view
     */
    public ArrayList<TreeNodeView> getParentViews() {
        return parentViews;
    }

    /**
     * Sets the list of parent views associated with this tree transition view
     *
     * @param parentViews list of parent views for this tree transition view
     */
    public void setParentViews(ArrayList<TreeNodeView> parentViews) {
        this.parentViews = parentViews;
        this.lineStartPoints.clear();
        for (TreeNodeView parentView : this.parentViews) {
            this.lineStartPoints.add(new Point());
        }
    }

    /**
     * Adds a TreeNodeView to the list of parent views
     *
     * @param nodeView TreeNodeView to add to the list of parent views
     */
    public void addParentView(TreeNodeView nodeView) {
        parentViews.add(nodeView);
        lineStartPoints.add(new Point());
    }

    /**
     * Removes a TreeNodeView from the list of parent views
     *
     * @param nodeView TreeNodeView to remove from the list of parent views
     */
    public void removeParentView(TreeNodeView nodeView) {
        int index = parentViews.indexOf(nodeView);
        parentViews.remove(nodeView);
        if (index != -1) {
            lineStartPoints.remove(index);
        }
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
    }

    public int getEndX() {
        return endPoint.x;
    }

    public void setEndX(int x) {
        this.endPoint.x = x;
    }

    public int getEndY() {
        return endPoint.y;
    }

    public void setEndY(int y) {
        this.endPoint.y = y;
    }

    public List<Point> getLineStartPoints() {
        return lineStartPoints;
    }

    public void setLineStartPoints(List<Point> lineStartPoints) {
        this.lineStartPoints = lineStartPoints;
    }

    public Point getLineStartPoint(int index) {
        return index < lineStartPoints.size() ? lineStartPoints.get(index) : null;
    }

    @Override
    public Rectangle getBounds() {
        return arrowhead.getBounds();
    }

    @Override
    public Rectangle2D getBounds2D() {
        return arrowhead.getBounds2D();
    }

    @Override
    public boolean contains(double x, double y) {
        return arrowhead.contains(x, y);
    }

    @Override
    public boolean contains(Point2D p) {
        return arrowhead != null && arrowhead.contains(p);
    }

    @Override
    public boolean intersects(double x, double y, double w, double h) {
        return arrowhead.intersects(x, y, w, h);
    }

    @Override
    public boolean intersects(Rectangle2D r) {
        return arrowhead.intersects(r);
    }

    @Override
    public boolean contains(double x, double y, double w, double h) {
        return arrowhead.contains(x, y, w, h);
    }

    @Override
    public boolean contains(Rectangle2D r) {
        return arrowhead.contains(r);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at) {
        return arrowhead.getPathIterator(at);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return arrowhead.getPathIterator(at, flatness);
    }
}
