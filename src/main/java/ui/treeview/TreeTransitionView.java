package ui.treeview;

import model.tree.TreeElementType;
import model.tree.TreeTransition;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import static java.lang.Math.*;

public class TreeTransitionView extends TreeElementView
{
    private static final int RADIUS = 10;
    private static final int DIAMETER = 2 * RADIUS;
    private static final int GAP = 5;

    private static final Stroke THIN_STROKE = new BasicStroke(1);
    private static final Stroke MEDIUM_STROKE = new BasicStroke(2);

    private static final Color OUTLINE_COLOR = Color.BLACK;
    private static final Color CORRECT_COLOR = Color.GREEN;
    private static final Color INCORRECT_COLOR = Color.RED;
    private static final Color DEFAULT_COLOR = Color.GRAY;
    private static final Color SELECTION_COLOR = Color.GREEN;
    private static final Color X_COLOR = Color.RED;

    private TreeNodeView childView;
    private TreeNodeView parentView;
    private Polygon arrowhead;
    private Point startPoint;
    private Point endPoint;

    private boolean isCollapsed;

    /**
     * TreeTransitionView Constructor - creates a transition arrow for display
     *
     * @param transition tree transition associated with this view
     * @param parentView TreeNodeView of the parent associated with this transition
     */
    public TreeTransitionView(TreeTransition transition, TreeNodeView parentView)
    {
        super(TreeElementType.TRANSITION, transition.getChildNode());
        this.treeElement = transition;
        this.parentView = parentView;
        this.isCollapsed = false;
    }

    private void constructArrowhead()
    {
        int radius = childView.getRadius();
        double ratio = (childView.getY() - parentView.getY()) / (childView.getX() - parentView.getX());
        double denom = Math.sqrt(Math.pow(ratio, 2) + 1);
        int cosTheta = (int)Math.round((radius + GAP) / denom);
        int sinTheta = (int)Math.round((radius + GAP) * ratio / denom);

        startPoint.x = parentView.getX() + cosTheta;
        startPoint.y = parentView.getY() + sinTheta;

        endPoint.x = childView.getX() - cosTheta;
        endPoint.y = childView.getY() + (childView.getY() >= parentView.getY() ? -sinTheta : -sinTheta);

        double thetaArrow = Math.toRadians(30);

        int point1X = endPoint.x;
        int point1Y = endPoint.y;

        int point2X = point1X - radius;
        int point2Y = point1Y + (int)Math.round(radius / (2 * cos(thetaArrow)));

        int point3X = point1X - radius;
        int point3Y = point1Y - (int)Math.round(radius / (2 * cos(thetaArrow)));

        double theta = Math.atan(ratio);

        AffineTransform rotation = new AffineTransform();
        rotation.rotate(theta, endPoint.x, endPoint.y);
        Point rightPoint = new Point();
        rotation.transform(new Point(point2X, point2Y), rightPoint);
        Point leftPoint = new Point();
        rotation.transform(new Point(point3X, point3Y), leftPoint);

        Point topPoint = new Point(point1X, point1Y);

        arrowhead = new Polygon();
        arrowhead.addPoint(topPoint.x, topPoint.y);
        arrowhead.addPoint(rightPoint.x, rightPoint.y);
        arrowhead.addPoint(leftPoint.x, leftPoint.y);
    }

    /**
     * Draws the TreeTransitionView
     *
     * @param graphics2D graphics2D used for drawing
     */
    public void draw(Graphics2D graphics2D)
    {
        constructArrowhead();

        graphics2D.setStroke(THIN_STROKE);

        graphics2D.setColor(OUTLINE_COLOR);
        graphics2D.setStroke(MEDIUM_STROKE);
        graphics2D.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);

        graphics2D.setColor(getTreeElement().isJustified() ? getTreeElement().isCorrect() ? CORRECT_COLOR : INCORRECT_COLOR : DEFAULT_COLOR );
        graphics2D.fillPolygon(arrowhead);

        graphics2D.setColor(OUTLINE_COLOR);
        graphics2D.drawPolygon(arrowhead);

        if(isSelected)
        {
            Rectangle rec = arrowhead.getBounds();
            graphics2D.setColor(SELECTION_COLOR);
            graphics2D.drawRect(rec.x - 10, rec.y - 10, rec.width + 20, rec.height + 20);
        }
    }

    public TreeTransition getTreeElement()
    {
        return (TreeTransition)treeElement;
    }

    public TreeNodeView getChildView()
    {
        return childView;
    }

    public void setChildView(TreeNodeView childView)
    {
        this.childView = childView;
    }

    public TreeNodeView getParentView()
    {
        return parentView;
    }

    public void setParentView(TreeNodeView parentView)
    {
        this.parentView = parentView;
    }

    public Point getStartPoint()
    {
        return startPoint;
    }

    public void setStartPoint(Point startPoint)
    {
        this.startPoint = startPoint;
    }

    public Point getEndPoint()
    {
        return endPoint;
    }

    public void setEndPoint(Point endPoint)
    {
        this.endPoint = endPoint;
    }

    public int getStartX()
    {
        return startPoint.x;
    }

    public void setStartX(int x)
    {
        this.startPoint.x = x;
    }

    public int getStartY()
    {
        return startPoint.y;
    }

    public void setStartY(int y)
    {
        this.startPoint.y = y;
    }

    public int getEndX()
    {
        return endPoint.x;
    }

    public void setEndX(int x)
    {
        this.endPoint.x = x;
    }

    public int getEndY()
    {
        return endPoint.y;
    }

    public void setEndY(int y)
    {
        this.endPoint.y = y;
    }

    public boolean isCollapsed()
    {
        return isCollapsed;
    }

    public void setCollapsed(boolean isCollapsed)
    {
        this.isCollapsed = isCollapsed;
    }

    @Override
    public Rectangle getBounds()
    {
        return arrowhead.getBounds();
    }

    @Override
    public Rectangle2D getBounds2D()
    {
        return arrowhead.getBounds2D();
    }

    @Override
    public boolean contains(double x, double y)
    {
        return arrowhead.contains(x, y);
    }

    @Override
    public boolean contains(Point2D p)
    {
        return arrowhead.contains(p);
    }

    @Override
    public boolean intersects(double x, double y, double w, double h)
    {
        return arrowhead.intersects(x, y, w, h);
    }

    @Override
    public boolean intersects(Rectangle2D r)
    {
        return arrowhead.intersects(r);
    }

    @Override
    public boolean contains(double x, double y, double w, double h)
    {
        return arrowhead.contains(x, y, w, h);
    }

    @Override
    public boolean contains(Rectangle2D r)
    {
        return arrowhead.contains(r);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at)
    {
        return arrowhead.getPathIterator(at);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at, double flatness)
    {
        return arrowhead.getPathIterator(at, flatness);
    }
}
