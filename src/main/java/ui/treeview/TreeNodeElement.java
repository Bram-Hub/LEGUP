package ui.treeview;

import app.GameBoardFacade;
import model.rules.TreeNode;

import javax.swing.*;
import java.awt.*;

public class TreeNodeElement extends JPanel
{
    private static final int RADIUS = 10;
    private static final int DIAMETER = 2 * RADIUS;

    private static final Stroke THIN_STROKE = new BasicStroke(1);
    private static final Stroke MEDIUM_STROKE = new BasicStroke(2);

    private static final Color OUTLINE_COLOR = Color.BLACK;
    private static final Color SELECTION_COLOR = Color.GREEN;

    private TreeNode treeNode;
    private Point location;

    /**
     * TreeNodeElement Constructor - creates a node for display
     *
     * @param treeNode treeNode associated with this transition
     * @param location location of the transition on the screen
     */
    public TreeNodeElement(TreeNode treeNode, Point location)
    {
        this.treeNode = treeNode;
        this.location = location;
    }

    /**
     * Draws the TreeNodeElement
     *
     * @param graphics2D graphics2D used for drawing
     */
    public void draw(Graphics2D graphics2D)
    {
        graphics2D.setStroke(THIN_STROKE);
        if(treeNode.getBoard().isModifiable())
        {
            graphics2D.fillOval(location.x - RADIUS, location.y - RADIUS, DIAMETER, DIAMETER);
            graphics2D.setColor(OUTLINE_COLOR);
            graphics2D.drawOval(location.x - RADIUS, location.y - RADIUS, DIAMETER, DIAMETER);

            if(GameBoardFacade.getInstance().getTree().isSelected(treeNode))
            {
                graphics2D.setColor(SELECTION_COLOR);
                graphics2D.setStroke(MEDIUM_STROKE);
                graphics2D.drawRect(location.x - DIAMETER, location.y - DIAMETER, DIAMETER * 2, DIAMETER * 2);
            }
        }
        else
        {

        }
    }

    /**
     * Gets the tree node associated with this view
     *
     * @return tree node
     */
    public TreeNode getTreeNode()
    {
        return treeNode;
    }
}