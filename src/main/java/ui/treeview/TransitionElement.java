package ui.treeview;

import app.GameBoardFacade;
import model.rules.ContradictionRule;
import model.rules.TreeNode;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import static java.lang.Math.*;

public class TransitionElement implements Shape
{
    private static final int RADIUS = 10;
    private static final int DIAMETER = 2 * RADIUS;
    private static final int GAP = 5;

    private static final Stroke THIN_STROKE = new BasicStroke(1);
    private static final Stroke MEDIUM_STROKE = new BasicStroke(2);
    private static final Stroke THICK_STROKE = new BasicStroke(3);

    private static final Color OUTLINE_COLOR = Color.BLACK;
    private static final Color SELECTION_COLOR = Color.GREEN;
    private static final Color X_COLOR = Color.RED;

    private TreeNodeView nodeView;
    private TreeNodeView parentView;
    private Polygon arrowhead;
    private Point tailStartPoint;
    private Point tailEndPoint;


    /**
     * TransitionElement Constructor - creates a transition arrow for display
     *
     * @param nodeView treeNodeView associated with this transition
     * @param parentView treeNodeView of the parent associated with this transition
     */
    public TransitionElement(TreeNodeView nodeView, TreeNodeView parentView)
    {
        this.nodeView = nodeView;
        this.parentView = parentView;

        constructArrowtail();
        constructArrowhead();

        // System.err.println("Parent: ("+parentView.getX()+", "+parentView.getY()+")   Child: ("+nodeView.getX()+", "+nodeView.getY()+")");
    }

    private void constructArrowtail() {
        double ratio = (nodeView.getY() - parentView.getY())/(nodeView.getX() - parentView.getX());
        double radius = nodeView.getRadius();
        double denominator = sqrt((ratio*ratio)+1);
        double xMagnitude = (radius+GAP)/denominator;
        double yMagnitude = (radius+GAP)*ratio/denominator;

        int startPointX = parentView.getX() + (int) round(xMagnitude);
        int startPointY = parentView.getY() + (int) round(yMagnitude);
        tailStartPoint = new Point(startPointX, startPointY);

        int endPointX = nodeView.getX() - (int) round(xMagnitude);
        int endPointY = nodeView.getY() - (int) round(yMagnitude);
        tailEndPoint = new Point(endPointX, endPointY);
    }

    private void constructArrowhead() {

        Point topPoint = tailEndPoint;
        int radius = nodeView.getRadius();
        double theta = Math.toRadians(30);

        int rightPointX = topPoint.x - radius;
        int rightPointY = topPoint.y + (int)(radius/(2*cos(theta)));

        int leftPointX = topPoint.x - radius;
        int leftPointY = topPoint.y - (int)(radius/(2*cos(theta)));

        double phi = 0;
        if (nodeView.getY()-parentView.getY() != 0) {
            phi = atan((nodeView.getX()-parentView.getX())/(nodeView.getY()-parentView.getY()));
        }

        AffineTransform rotation = new AffineTransform();
        rotation.rotate(phi, tailEndPoint.x, tailEndPoint.y);
        Point rightPoint = new Point();
        rotation.transform(new Point(rightPointX, rightPointY), rightPoint);
        Point leftPoint = new Point();
        rotation.transform(new Point(leftPointX, leftPointY), leftPoint);

        arrowhead = new Polygon();
        arrowhead.addPoint(tailEndPoint.x, tailEndPoint.y);
        arrowhead.addPoint(rightPoint.x, rightPoint.y);
        arrowhead.addPoint(leftPoint.x, leftPoint.y);
    }

    /**
     * Draws the TransitionElement
     *
     * @param graphics2D graphics2D used for drawing
     */
    public void draw(Graphics2D graphics2D)
    {
        graphics2D.setStroke(THIN_STROKE);
        /*
        Polygon transition = new Polygon();
        transition.addPoint(location.x + RADIUS, location.y);
        transition.addPoint((int)(location.x + RADIUS * Math.cos(2 * Math.PI / 3)), (int)(location.y * Math.sin(2 * Math.PI / 3)));
        transition.addPoint((int)(location.x + RADIUS * Math.cos(4 * Math.PI / 3)), (int)(location.y * Math.sin(4 * Math.PI / 3)));

        graphics2D.fill(transition);

        graphics2D.setColor(OUTLINE_COLOR);
        graphics2D.drawPolygon(transition);

        if(GameBoardFacade.getInstance().getTree().isSelected(treeNode))
        {
            graphics2D.setColor(SELECTION_COLOR);
            graphics2D.setStroke(MEDIUM_STROKE);
            graphics2D.drawRect(location.x - DIAMETER, location.y - DIAMETER, DIAMETER * 2, DIAMETER * 2);
        }

        if(treeNode.getRule() instanceof ContradictionRule)
        {
            graphics2D.setColor(X_COLOR);
            graphics2D.drawLine(location.x - RADIUS + 3 * RADIUS,location.y - RADIUS,
                    location.x + RADIUS + 3 * RADIUS,location.y + RADIUS);
            graphics2D.drawLine(location.x + RADIUS + 3 * RADIUS,location.y - RADIUS,
                    location.x - RADIUS + 3 * RADIUS,location.y + RADIUS);
        }
        */
        graphics2D.setColor(OUTLINE_COLOR);
        graphics2D.setStroke(MEDIUM_STROKE);
        graphics2D.drawLine(tailStartPoint.x, tailStartPoint.y, tailEndPoint.x, tailEndPoint.y);

        graphics2D.setColor(SELECTION_COLOR);
        graphics2D.fillPolygon(arrowhead);

        graphics2D.setColor(OUTLINE_COLOR);
        graphics2D.drawPolygon(arrowhead);
    }

    @Override
    public Rectangle getBounds()
    {
        return null;
    }

    @Override
    public Rectangle2D getBounds2D()
    {
        return null;
    }

    @Override
    public boolean contains(double x, double y)
    {
        return false;
    }

    @Override
    public boolean contains(Point2D p)
    {
        return false;
    }

    @Override
    public boolean intersects(double x, double y, double w, double h)
    {
        return false;
    }

    @Override
    public boolean intersects(Rectangle2D r)
    {
        return false;
    }

    @Override
    public boolean contains(double x, double y, double w, double h)
    {
        return false;
    }

    @Override
    public boolean contains(Rectangle2D r)
    {
        return false;
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at)
    {
        return null;
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at, double flatness)
    {
        return null;
    }
}
