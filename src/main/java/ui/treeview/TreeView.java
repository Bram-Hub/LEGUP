package ui.treeview;

import app.GameBoardFacade;
import app.TreeController;
import model.rules.Tree;
import model.rules.TreeNode;
import ui.DynamicViewer;
import ui.Selection;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class TreeView extends DynamicViewer
{
    private static final Color NODE_COLOR = new Color(255, 255, 155);
    private static final Color TRANS_CORRECT_COLOR = Color.GREEN;
    private static final Color TRANS_INCORRECT_COLOR = Color.RED;
    private static final int NODE_RADIUS = 10;
    private static final int SMALL_NODE_RADIUS = 7;
    private static final int COLLAPSED_DRAW_DELTA_X = 10;
    private static final int COLLAPSED_DRAW_DELTA_Y = 10;

    private static final int NODE_GAP_WIDTH = 50;
    private static final int NODE_GAP_HEIGHT = 50;

    private static final float floater[] = new float[]{(5.0f), (10.0f)};
    private static final float floater2[] = new float[]{(2.0f), (3.0f)};
    private static final Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10, floater, 0);
    private static final Stroke dotted = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10, floater2, 0);
    private static final Stroke medium = new BasicStroke(2);
    private static final Stroke thin = new BasicStroke(1);
    private static final String NodeImgs = "images/Legup/tree/smiley/";
    private static Selection mouseOver;

    private TreeNodeView nodeHover;

    private ArrayList<Rectangle> currentStateBoxes;
    private Point selectionOffset = null;
    private Point lastMovePoint = null;
    private Rectangle bounds = new Rectangle(0, 0, 0, 0);
    private int xOffset = 0;
    private int yOffset = 0;
    private Point mousePoint;
    private Map<TreeNode, Color> collapseColorHash;
    private Tree tree;
    private TreeNodeView rootNodeView;
    private Dimension dimension;

    public TreeView(TreeController treeController)
    {
        super(treeController);
        currentStateBoxes = new ArrayList<>();
        collapseColorHash = new HashMap<>();
        nodeHover = null;
        setSize(dimension = new Dimension(100, 200));
        setPreferredSize(new Dimension(640, 160));
    }

    /**
     * Gets the tree node element that the mouse is hovering over
     *
     * @return tree node element that the mouse is hovering over
     */
    public TreeNodeView getNodeHover()
    {
        return nodeHover;
    }

    /**
     * Sets the tree node element that the mouse is hovering over
     *
     * @param nodeHover tree node element the mouse is hovering over
     */
    public void setNodeHover(TreeNodeView nodeHover)
    {
        this.nodeHover = nodeHover;
    }

    public void actionPerformed(ActionEvent e)
    {
        // System.out.println("actionPerformed");
    }

    private TreeNodeView getLastCollapsed(TreeNodeView nodeView)
    {
        return getLastCollapsed(nodeView, null);
    }

    private TreeNodeView getLastCollapsed(TreeNodeView nodeView, int[] outptrNumTransitions)
    {
        ArrayList<TreeNodeView> children = nodeView.getChildrenViews();
        int numTransitions = 0;

        if(children.size() == 1)
        {
            TreeNodeView childView = children.get(0);

            if(childView.isCollapsed())
            {
                numTransitions++;
                nodeView = getLastCollapsed(childView);
            }
        }
        if(outptrNumTransitions != null)
        {
            outptrNumTransitions[0] = numTransitions;
        }
        return nodeView;
    }

    public TreeNodeView getTreeNodeView(Point point)
    {
        return getTreeNodeView(point, rootNodeView);
    }

    private TreeNodeView getTreeNodeView(Point point, TreeNodeView nodeView)
    {
        if(nodeView.contains(point))
        {
            return nodeView;
        }
        else
        {
            TreeNodeView treeNodeView = null;
            for(TreeNodeView view: nodeView.getChildrenViews())
            {
                treeNodeView = getTreeNodeView(point, view);
                if(treeNodeView != null)
                {
                    return treeNodeView;
                }
            }
            return treeNodeView;
        }
    }

    // recursively computes the bounding rectangle of the tree
    private Rectangle getTreeBounds(TreeNodeView nodeView)
    {
        // get the position of the current node and add padding
        Rectangle b = new Rectangle(nodeView.getLocation());
        b.grow(2 * NODE_RADIUS, 2 * NODE_RADIUS);
        // Adjust the rectangle so that rule popups aren't cut off
        float scale = (100 / (float) getZoom());
        b.setBounds((int) b.getX() - (int) (100 * scale), (int) b.getY(), (int) b.getWidth() + (int) (400 * scale), (int) b.getHeight() + (int) (200 * scale));
        // get the relevant child nodes
        ArrayList<TreeNodeView> childrenViews = nodeView.isCollapsed() ? getLastCollapsed(nodeView).getChildrenViews() : nodeView.getChildrenViews();
        // compute the union of the child bounding boxes recursively
        for(int c = 0; c < childrenViews.size(); c++)
        {
            b = b.union(getTreeBounds(childrenViews.get(c)));
        }
        return b;
    }

    public void updateTreeView(Tree tree)
    {
        this.tree = tree;
        repaint();
    }

    public void updateTreeSize()
    {
        if(GameBoardFacade.getInstance().getTree() == null)
        {
            return;
        }
        //bounds = getTreeBounds(GameBoardFacade.getInstance().getTree().getRootNode());
        setSize(bounds.getSize());
        TreeNode treeNode = GameBoardFacade.getInstance().getTree().getRootNode();
        if(bounds.y != 60)
        {
            //treeNode.adjustOffset(new Point(60 - bounds.y, 0));
        }
    }

    public void reset()
    {
        TreeNode state = GameBoardFacade.getInstance().getTree().getRootNode();
        if(bounds.x != 0 || bounds.y != 0)
        {
            //state.setOffset(new Point(state.getOffset().x - bounds.x, state.getOffset().y - bounds.y));
            updateTreeSize();
        }
    }

    public void zoomFit()
    {
        // find the ideal width and height scale
        zoomTo(1.0);
        updateTreeSize();
        double fitwidth = (viewport.getWidth() - 8.0) / (getSize().width - 200);
        double fitheight = (viewport.getHeight() - 8.0) / (getSize().height - 120);
        // choose the smaller of the two and zoom
        zoomTo((fitwidth < fitheight) ? fitwidth : fitheight);
        viewport.setViewPosition(new Point(0, 0));
    }

    public void draw(Graphics2D graphics2D)
    {
        currentStateBoxes.clear();
        Tree tree = GameBoardFacade.getInstance().getTree();
        if(tree != null)
        {
            //setSize(bounds.getSize());
            //setSize(dimension);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            drawTree(graphics2D, tree.getRootNode(), 0);
            //drawCurrentStateBoxes(graphics2D);

            if(mouseOver != null)
            {
                //drawMouseOver(graphics2D);
            }
        }
    }

    public void zoomReset()
    {
        zoomTo(1.0);
        viewport.setViewPosition(new Point(0, 0));
    }

    protected void mouseDraggedAt(Point point, MouseEvent e)
    {
        if(lastMovePoint == null)
        {
            lastMovePoint = new Point(point);
        }
    }
    /*
    protected void highlightSelectedTransition(Point p)
    {
        Selection sel = getSelectionAtPoint(GameBoardFacade.getInstance().getTree().getRootNode(), p);
        if(sel != null && sel.getState().isModifiable())
        {
            GameBoardFacade.getInstance().getGui().getJustificationFrame().
                    setSelectionByJustification(sel.getState().getJustification());
        }
    }*/

    public void mouseWheelMovedAt(MouseWheelEvent e)
    {
        updateTreeSize();
    }
    /*
    public TreeNode addChildAtCurrentState(Rule rule)
    {
        Selection selection = GameBoardFacade.getInstance().getSelections().getFirstSelection();
        TreeNode cur = selection.getState();
        if((cur.getChangedCells().size() > 0) || (cur.extraDataChanged()))
        {
            if(cur.isModifiable() && selection.isState())
            {
                GameBoardFacade.setCurrentState(cur.endTransition());
            }
        }
        updateTreeSize();
        return cur;
    }*/

    public boolean checkIfBranchIsContradiction(TreeNode state)
    {


        return false;
    }

    public boolean checkIfBranchesConverge(TreeNode node1, TreeNode node2)
    {
        TreeNode branch0 = node1;
        while(branch0.getChildren().size() == 1)
        {
            branch0 = branch0.getChildren().get(0);
            if(branch0.getParents().size() == 2)
            {
                break;
            }
        }

        TreeNode branch1 = node2;
        while(branch1.getChildren().size() == 1)
        {
            branch1 = branch1.getChildren().get(0);
            if(branch1.getParents().size() == 2)
            {
                break;
            }
        }

        if(branch0 == branch1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean handleAllBranchesContra(TreeNode treeNode)
    {
        return false;
    }

    public void handleAllBranchesMerged(TreeNode treeNode)
    {


        //This is the better looking solution!
        //Partially works//
        //However decollapse is broken. Also, the gap between nodes is too large.
        //parent0.toggleCollapseRecursiveMerge(parent0.getLocation().x, parent0.getLocation().y, true);
        //parent1.toggleCollapseRecursiveMerge(parent1.getLocation().x, parent1.getLocation().y, true);
    }

    /**
     * Gets the color of a collapsed tree node.
     * This function must be called before the game board collapsing takes place,
     * otherwise transition data will be hidden
     *
     * @param treeNode collapsed tree node
     */
    public void getCollapseColor(TreeNode treeNode)
    {
        boolean overallColor = treeNode.leadsToContradiction();
        if(overallColor)
        {
            this.collapseColorHash.put(treeNode, Color.GREEN);
        }
        else
        {
            this.collapseColorHash.put(treeNode, Color.RED);
        }
    }

    /**
     * Delete the current state and associated transition then fix the children
     */
    public void delCurrentState()
    {
        GameBoardFacade.getInstance().getTree().deleteSelected();
        updateTreeSize();
    }

    /**
     * Delete the child and child's subtree starting at the current state
     */
    public void delChildAtCurrentState()
    {
        Tree tree = GameBoardFacade.getInstance().getTree();
        HashSet<TreeNode> selected = tree.getSelected();
        ArrayList<TreeNode> selectedList = new ArrayList<>(selected);

        //updateTreeSize();
    }

    /**
     * Merge the two or more selected states
     */
    public void mergeStates()
    {
        Tree tree = GameBoardFacade.getInstance().getTree();
        if(tree.canMerge())
        {
            tree.mergeNodes();
        }
        updateTreeSize();
    }

    private void drawTree(Graphics2D graphics2D, TreeNode root, int v)
    {
        TreeNodeView rootView = new TreeNodeView(root);
        dimension = new Dimension();
        rootNodeView = rootView;
        int span = drawTree(graphics2D, rootView, 0, TreeNodeView.DIAMETER);
        dimension.height = span;
        ArrayList<TreeNodeView> children = rootView.getChildrenViews();
        rootView.setY(TreeNodeView.DIAMETER + span / 2);
        dimension.height = dimension.height + 2 * TreeNodeView.DIAMETER;
        dimension.width = dimension.width + TreeNodeView.DIAMETER;
        System.out.println("Dimension: " + dimension);
        setSize(dimension);
        rootView.draw(graphics2D);
    }

    private int drawTree(Graphics2D graphics2D, TreeNodeView nodeView, int depth, int rspan)
    {
        TreeNode node = nodeView.getTreeNode();
        int xLoc = (NODE_GAP_WIDTH + TreeNodeView.DIAMETER) * depth + TreeNodeView.DIAMETER;
        nodeView.setX(xLoc);
        dimension.width = Math.max(dimension.width, xLoc);
        if(node.getChildren().isEmpty())
        {
            nodeView.setY(rspan + TreeNodeView.RADIUS);
            graphics2D.drawString(nodeView.getLocation() + ": " + (TreeNodeView.DIAMETER + NODE_GAP_HEIGHT), nodeView.getX(), nodeView.getY());
            return TreeNodeView.DIAMETER;
        }
        else
        {
            ArrayList<TreeNode> children = node.getChildren();
            int size = children.size();
            int tspan = 0;
            for(int i = 0; i < size; i++)
            {
                TreeNode child = children.get(i);
                TreeNodeView childView = new TreeNodeView(child);

                int cspan = drawTree(graphics2D, childView, depth + 1, rspan + tspan);

                System.out.println(childView.getLocation());
                nodeView.addChildrenView(childView);
                childView.draw(graphics2D);
                tspan += i == size - 1 ? cspan : cspan + NODE_GAP_HEIGHT;
            }
            ArrayList<TreeNodeView> childrenViews = nodeView.getChildrenViews();
            nodeView.setY(rspan + tspan / 2);
            graphics2D.drawString(nodeView.getLocation() + ": " + tspan, nodeView.getX(), nodeView.getY());

            return tspan;
        }
    }

    /**
     * Draw the current transition (will make it blue if it's part of the selection)
     *
     * @param trans          the line of the transition we're drawing, starting at the source
     * @param g              the graphics to use
     * @param parent         the parent gameboard state of the transition we're drawing
     * @param collapsedChild is the child we're connecting to a collapsed state
     */
    private void drawTransition(Line2D.Float trans, Graphics g, TreeNode parent, boolean collapsedChild)
    {
        Graphics2D g2d = (Graphics2D) g;
        int nodeRadius = collapsedChild ? SMALL_NODE_RADIUS : NODE_RADIUS;

        g2d.setStroke(medium);
        //g.setColor(((sel.contains(theSelection)) ? Color.blue : Color.gray));

        g2d.draw(trans);

        // we also want to draw the arrowhead
        final int ARROW_SIZE = 8;

        // find the tip of the arrow, the point NODE_RADIUS away from the destination endpoint
        double theta = Math.atan2(trans.y2 - trans.y1, trans.x2 - trans.x1);

        double nx = nodeRadius * Math.cos(theta);
        double ny = nodeRadius * Math.sin(theta);

        int px = Math.round(trans.x2);
        int py = Math.round(trans.y2);

        Polygon arrowhead = new Polygon();
        arrowhead.addPoint(px, py);

        nx = (ARROW_SIZE) * Math.cos(theta);
        ny = (ARROW_SIZE) * Math.sin(theta);

        px = (int) Math.round(trans.x2 - nx);
        py = (int) Math.round(trans.y2 - ny);
        // px and py are now the "base" of the arrowhead

        theta += Math.PI / 2.0;
        double dx = (ARROW_SIZE / 2) * Math.cos(theta);
        double dy = (ARROW_SIZE / 2) * Math.sin(theta);

        arrowhead.addPoint((int) Math.round(px + dx), (int) Math.round(py + dy));

        theta -= Math.PI;
        dx = (ARROW_SIZE / 2) * Math.cos(theta);
        dy = (ARROW_SIZE / 2) * Math.sin(theta);

        arrowhead.addPoint((int) Math.round(px + dx), (int) Math.round(py + dy));

        g2d.fill(arrowhead);
    }

    /**
     * When the user collapses the nodes, find out which gameboard state was collapsed. The gameboard state can then be used to find out
     * the overall color for the collapsed transition(s)
     *
     * @param lastCollapsed TreeNode before the collapsed transition(s)
     *
     * @return OVerall color for the collapsed transition(s)
     */
    private Color getCollapsedTransitionColor(TreeNode lastCollapsed)
    {
        Color transitionColor = new Color(255, 255, 155);

        //get last node
        TreeNode iterBoard = lastCollapsed;
        while(iterBoard.getChildren().size() == 1 && iterBoard.getChildren().get(0).getParents().size() < 2)
        {
            iterBoard = iterBoard.getChildren().get(0);
        }

        transitionColor = new Color(255, 255, 155);
        if(collapseColorHash.containsKey(iterBoard))
        {
            transitionColor = collapseColorHash.get(iterBoard);
        }

        return transitionColor;
    }

    /**
     * Draw a collapsed node at the current location
     *
     * @param g the Graphics to draw with
     * @param x the x location to draw it on
     * @param y the y location to draw it on
     */
    private void drawCollapsedNode(Graphics g, int x, int y, TreeNode lastCollapsed)
    {
        x += 5;
        final int rad = SMALL_NODE_RADIUS;
        final int diam = 2 * rad;
        final int deltaX = -COLLAPSED_DRAW_DELTA_X + 2;
        final int deltaY = -COLLAPSED_DRAW_DELTA_Y;

        Color transitionColor = getCollapsedTransitionColor(lastCollapsed);

        Graphics2D g2D = (Graphics2D) g;
        g2D.setStroke(thin);
        g2D.setColor(Color.black);
        for(int c = 0; c < 3; ++c)
        {
            //Polygon tri = makeTriangle(x - rad + (c - 1) * deltaX, y, diam / 2);
//            g.setColor(transitionColor);
//            g.fillPolygon(tri);
//            g.setColor(Color.black);
//            g.drawPolygon(tri);
        }
    }

    /**
     * Draw the current state boxes (the cached selection)
     *
     * @param g the graphics to use to draw
     */
    private void drawCurrentStateBoxes(Graphics g)
    {
        if(currentStateBoxes != null)
        {
            Graphics2D g2d = (Graphics2D) g;

            g.setColor(Color.blue);
            g2d.setStroke(dashed);

            for(int x = 0; x < currentStateBoxes.size(); ++x)
            {
                g2d.draw(currentStateBoxes.get(x));
            }
        }
    }

    /**
     * When the user hovers over the transition, draws the corresponding rules image
     *
     * @param g the graphics to use to draw
     *//*
    public void drawMouseOver(Graphics2D g)
    {
        TreeNode B = mouseOver.getState();
        //J contains both basic rules and contradictions
        Rule rule = B.getJustification();
        int w, h;
        g.setStroke(thin);

        w = (int) (100 * (100 / (float) getZoom()));
        h = (int) (100 * (100 / (float) getZoom()));
        float scale = (100 / (float) getZoom());
        int offset = (int) (scale * 30);

        JViewport vp = getViewport();
        BufferedImage image = new BufferedImage(vp.getWidth(), vp.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g_tmp = image.createGraphics();
        int v_offset = 0;

        if((mouseOver.getState().getJustification() != null) || (mouseOver.getState().getCaseRuleJustification() != null))
        {
            if((mouseOver.getState().justificationText != null) && (mouseOver.getState().getColor() != TreeView.NODE_COLOR))
            {
                g_tmp.setColor(Color.black);
                String[] tmp = mouseOver.getState().justificationText.split("\n");
                v_offset = 10 + tmp.length * 14;
                for(int c1 = 0; c1 < tmp.length; c1++)
                {
                    g_tmp.drawString(tmp[c1], 0, (14 * c1) + 10);
                }
            }
            g_tmp.setColor(Color.gray);
            g_tmp.drawRect(0, v_offset, 100, 100);
        }

        if(rule != null)
        {
            g_tmp.drawImage(rule.getImageIcon().getImage(), 0, v_offset, null);
        }
        CaseRule CR = B.getCaseSplitJustification();
        if(CR != null)
        {
            g_tmp.drawImage(CR.getImageIcon().getImage(), 0, v_offset, null);
            return;
        }

        g.drawImage(image, mousePoint.x + (int) (scale * 30), mousePoint.y - (int) (scale * 30), (int) (scale * vp.getWidth()), (int) (scale * vp.getHeight()), null);
    }*/
}
