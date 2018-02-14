package ui.treeview;

import app.GameBoardFacade;
import model.rules.ContradictionRule;
import model.rules.TreeNode;

import javax.swing.*;
import java.awt.*;

public class TransitionElement extends JPanel
{
    private static final int RADIUS = 10;
    private static final int DIAMETER = 2 * RADIUS;

    private static final Stroke THIN_STROKE = new BasicStroke(1);
    private static final Stroke MEDIUM_STROKE = new BasicStroke(2);

    private static final Color OUTLINE_COLOR = Color.BLACK;
    private static final Color SELECTION_COLOR = Color.GREEN;
    private static final Color X_COLOR = Color.RED;

    private TreeNode treeNode;
    private Point location;

    /**
     * TransitionElement Constructor - creates a transition arrow for display
     *
     * @param treeNode treeNode associated with this transition
     * @param location location of the transition on the screen
     */
    public TransitionElement(TreeNode treeNode, Point location)
    {
        this.treeNode = treeNode;
        this.location = location;
    }

    /**
     * Draws the TransitionElement
     *
     * @param graphics2D graphics2D used for drawing
     */
    public void draw(Graphics2D graphics2D)
    {
        graphics2D.setStroke(THIN_STROKE);
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
    }
}
