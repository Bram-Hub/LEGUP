package ui.treeview;

import model.rules.RuleType;
import model.tree.TreeElementType;
import model.tree.TreeNode;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

public class TreeNodeView extends TreeElementView
{
    static final int RADIUS = 25;
    static final int DIAMETER = 2 * RADIUS;

    private static final Stroke THIN_STROKE = new BasicStroke(1);
    private static final Stroke MEDIUM_STROKE = new BasicStroke(2);

    private static final Color NODE_COLOR_ROOT = new Color(100, 100, 100);
    private static final Color NODE_MINOR_COLOR_ROOT = new Color(75, 75, 75);

    private static final Color NODE_COLOR_DEFAULT = new Color(244, 223, 66);
    private static final Color NODE_MINOR_COLOR_DEFAULT = new Color(216, 197, 52);

    private static final Color NODE_COLOR_CONTRADICTION = new Color(178, 10, 16);
    private static final Color NODE_MINOR_COLOR_CONTRADICTION = new Color(119, 13, 16);

    private static final Color OUTLINE_COLOR = Color.BLACK;
    private static final Color SELECTION_COLOR = Color.GREEN;

    private Point location;

    private ArrayList<TreeTransitionView> parentViews;
    private ArrayList<TreeTransitionView> childrenViews;
    private TreeTransitionView transitionView;

    private boolean isCollapsed;
    private boolean isContradictoryState;

    /**
     * TreeNodeView Constructor - creates a node for display
     *
     * @param treeNode treeElement associated with this transition
     */
    public TreeNodeView(TreeNode treeNode)
    {
        super(TreeElementType.NODE, treeNode);
        this.treeElement = treeNode;
        this.location = new Point();
        this.parentViews = new ArrayList<>();
        this.childrenViews = new ArrayList<>();
        this.isCollapsed = false;
        this.isContradictoryState = false;
    }

    /**
     * Draws the TreeNodeView
     *
     * @param graphics2D graphics2D used for drawing
     */
    public void draw(Graphics2D graphics2D)
    {
        if(isVisible() && treeElement != null)
        {
            if(getTreeElement().getParents().size() == 1 &&
                    getTreeElement().getParents().get(0).isJustified() &&
                    getTreeElement().getParents().get(0).getRule().getRuleType() == RuleType.CONTRADICTION)
            {
                isContradictoryState = true;
                graphics2D.setColor(NODE_COLOR_CONTRADICTION);
                graphics2D.drawLine(location.x - RADIUS, location.y - RADIUS, location.x + RADIUS, location.y + RADIUS);
                graphics2D.drawLine(location.x + RADIUS, location.y - RADIUS, location.x - RADIUS, location.y + RADIUS);
            }
            else
            {
                isContradictoryState = false;
                graphics2D.setStroke(THIN_STROKE);
                boolean isContraBranch = getTreeElement().leadsToContradiction();

                graphics2D.setColor(isContraBranch ? NODE_COLOR_CONTRADICTION : NODE_COLOR_DEFAULT);
                graphics2D.fillOval(location.x - RADIUS, location.y - RADIUS, DIAMETER, DIAMETER);

                graphics2D.setColor(OUTLINE_COLOR);
                graphics2D.drawOval(location.x - RADIUS, location.y - RADIUS, DIAMETER, DIAMETER);

                graphics2D.setColor(isContraBranch ? NODE_MINOR_COLOR_CONTRADICTION : NODE_MINOR_COLOR_DEFAULT);
                graphics2D.fillOval(location.x - RADIUS + 5, location.y - RADIUS + 5, DIAMETER - 10, DIAMETER - 10);

                graphics2D.setColor(OUTLINE_COLOR);
                graphics2D.drawOval(location.x - RADIUS + 5, location.y - RADIUS + 5, DIAMETER - 10, DIAMETER - 10);

                //graphics2D.fillOval(location.x - RADIUS, location.y - RADIUS, DIAMETER, DIAMETER);

                if(isSelected)
                {
                    graphics2D.setColor(SELECTION_COLOR);
                    graphics2D.setStroke(MEDIUM_STROKE);
                    graphics2D.drawRect(location.x - (int) (DIAMETER * 0.75), location.y - (int) (DIAMETER * 0.75), (int) (DIAMETER * 1.5), (int) (DIAMETER * 1.5));
                }
            }
        }
    }

    public boolean isContradictoryState()
    {
        return isContradictoryState;
    }

    /**
     * Gets the list of children views associated with this tree node
     *
     * @return list of children views for this tree node
     */
    public ArrayList<TreeTransitionView> getChildrenViews()
    {
        return childrenViews;
    }

    /**
     * Sets the list of children views associated with this tree node
     *
     * @param childrenViews list of children views for this tree node
     */
    public void setChildrenViews(ArrayList<TreeTransitionView> childrenViews)
    {
        this.childrenViews = childrenViews;
    }

    /**
     * Adds a TreeTransitionView to the list of children views
     *
     * @param nodeView TreeTransitionView to add to the list of children views
     */
    public void addChildrenView(TreeTransitionView nodeView)
    {
        childrenViews.add(nodeView);
    }

    /**
     * Removes a TreeTransitionView from the list of children views
     *
     * @param nodeView TreeTransitionView to remove from the list of children views
     */
    public void removeChildrenView(TreeTransitionView nodeView)
    {
        childrenViews.remove(nodeView);
    }

    /**
     * Gets the list of parent views associated with this tree node
     *
     * @return list of parent views for this tree node
     */
    public ArrayList<TreeTransitionView> getParentViews()
    {
        return parentViews;
    }

    /**
     * Sets the list of parent views associated with this tree node
     *
     * @param parentViews list of parent views for this tree node
     */
    public void setParentViews(ArrayList<TreeTransitionView> parentViews)
    {
        this.parentViews = parentViews;
    }

    /**
     * Adds a TreeTransitionView to the list of parent views
     *
     * @param nodeView TreeTransitionView to add to the list of parent views
     */
    public void addParentView(TreeTransitionView nodeView)
    {
        parentViews.add(nodeView);
    }

    /**
     * Removes a TreeTransitionView from the list of parent views
     *
     * @param nodeView TreeTransitionView to remove from the list of parent views
     */
    public void removeParentView(TreeTransitionView nodeView)
    {
        parentViews.remove(nodeView);
    }

    /**
     * Gets the tree node associated with this view
     *
     * @return tree node
     */
    public TreeNode getTreeElement()
    {
        return (TreeNode)treeElement;
    }

    /**
     * Gets the location of the tree node
     *
     * @return location of the tree node
     */
    public Point getLocation()
    {
        return location;
    }

    /**
     * Sets the location of the tree node
     *
     * @param location location of the tree node
     */
    public void setLocation(Point location)
    {
        this.location = location;
    }

    /**
     * Gets the x location of the tree node
     *
     * @return x location
     */
    public int getX()
    {
        return location.x;
    }

    /**
     * Sets the x location of the tree node
     *
     * @param x x location
     */
    public void setX(int x)
    {
        location.x = x;
    }

    /**
     * Gets the y location of the tree node
     *
     * @return y location
     */
    public int getY()
    {
        return location.y;
    }

    /**
     * Sets the y location of the tree node
     *
     * @param y y location
     */
    public void setY(int y)
    {
        location.y = y;
    }

    /**
     * Gets the radius of the tree node
     *
     * @return radius
     */
    public int getRadius() {return RADIUS; }

    /**
     * Is this tree node view collapsed in the view
     *
     * @return true if the node is collapsed, false otherwise
     */
    public boolean isCollapsed()
    {
        return isCollapsed;
    }

    /**
     * Sets the tree node view collapsed field
     *
     * @param isCollapsed true if the node is collapsed, false otherwise
     */
    public void setCollapsed(boolean isCollapsed)
    {
        this.isCollapsed = isCollapsed;
    }

    @Override
    public Rectangle getBounds()
    {
        return new Rectangle(location.x, location.y, DIAMETER, DIAMETER);
    }

    @Override
    public Rectangle2D getBounds2D()
    {
        return new Rectangle(location.x, location.y, DIAMETER, DIAMETER);
    }

    @Override
    public boolean contains(double x, double y)
    {
        return Math.sqrt(Math.pow(x - location.x, 2) + Math.pow(y - location.y, 2)) <= RADIUS;
    }

    @Override
    public boolean contains(Point2D p)
    {
        return contains(p.getX(), p.getY());
    }

    @Override
    public boolean intersects(double x, double y, double w, double h)
    {
        return false;
    }

    @Override
    public boolean intersects(Rectangle2D r)
    {
        return intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight());
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