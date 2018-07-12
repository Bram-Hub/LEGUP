package ui.treeview;

import app.GameBoardFacade;
import controller.TreeController;
import model.observer.ITreeListener;
import model.rules.Rule;
import model.tree.Tree;
import model.tree.TreeElement;
import model.tree.TreeNode;
import model.tree.TreeTransition;
import ui.DynamicViewer;
import utility.DisjointSets;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

import static model.tree.TreeElementType.NODE;
import static model.tree.TreeElementType.TRANSITION;
import static ui.treeview.TreeNodeView.DIAMETER;
import static ui.treeview.TreeNodeView.RADIUS;

public class TreeView extends DynamicViewer implements ITreeListener
{
    private static final Color NODE_COLOR = new Color(255, 255, 155);
    private static final Color TRANS_CORRECT_COLOR = Color.GREEN;
    private static final Color TRANS_INCORRECT_COLOR = Color.RED;

    private static final int NODE_RADIUS = 10;
    private static final int SMALL_NODE_RADIUS = 7;
    private static final int COLLAPSED_DRAW_DELTA_X = 10;
    private static final int COLLAPSED_DRAW_DELTA_Y = 10;
    private static final int TRANS_GAP = 5;

    private static final int NODE_GAP_WIDTH = 70;
    private static final int NODE_GAP_HEIGHT = 25;

    private static final int BORDER_GAP_HEIGHT = 20;
    private static final int BORDER_GAP_WIDTH = 20;

    private static final float floater[] = new float[]{(5.0f), (10.0f)};
    private static final float floater2[] = new float[]{(2.0f), (3.0f)};
    private static final Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10, floater, 0);
    private static final Stroke dotted = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10, floater2, 0);
    private static final Stroke medium = new BasicStroke(2);
    private static final Stroke thin = new BasicStroke(1);
    private static final String NodeImgs = "images/Legup/tree/smiley/";

    private TreeNodeView nodeHover;

    private ArrayList<Rectangle> currentStateBoxes;
    private Point selectionOffset = null;
    private Point lastMovePoint = null;
    private Rectangle bounds = new Rectangle(0, 0, 0, 0);
    private int xOffset = 0;
    private int yOffset = 0;
    private Map<TreeNode, Color> collapseColorHash;

    private Tree tree;
    private TreeNodeView rootNodeView;
    private Map<TreeElement, TreeElementView> viewMap;
    private Dimension dimension;

    private TreeSelection treeSelection;

    public TreeView(TreeController treeController)
    {
        super(treeController);
        currentStateBoxes = new ArrayList<>();
        collapseColorHash = new HashMap<>();
        setSize(dimension = new Dimension(100, 200));
        setPreferredSize(new Dimension(640, 160));

        viewMap = new HashMap<>();

        treeSelection = new TreeSelection();
    }

    public TreeSelection getTreeSelection()
    {
        return treeSelection;
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

    public Map<TreeElement, TreeElementView> getViewMap()
    {
        return viewMap;
    }

    public void actionPerformed(ActionEvent e)
    {
        // System.out.println("actionPerformed");
    }

    public TreeElementView getTreeElementView(Point point)
    {
        return rootNodeView == null ? null : getTreeElementView(point, rootNodeView);
    }

    private TreeElementView getTreeElementView(Point point, TreeElementView elementView)
    {

        if(elementView == null)
        {
            return null;
        }
        else if(elementView.contains(point) && elementView.isVisible())
        {
            if(elementView.getType() == NODE && ((TreeNodeView)elementView).isContradictoryState())
            {
                return null;
            }
            return elementView;
        }
        else
        {
            if(elementView.getType() == NODE)
            {
                TreeNodeView nodeView = (TreeNodeView)elementView;
                for(TreeTransitionView transitionView: nodeView.getChildrenViews())
                {
                    TreeElementView view = getTreeElementView(point, transitionView);
                    if(view != null)
                    {
                        return view;
                    }
                }
            }
            else
            {
                TreeTransitionView transitionView = (TreeTransitionView)elementView;
                return getTreeElementView(point, transitionView.getChildView());
            }
        }
        return null;
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
        //ArrayList<TreeTransitionView> childrenViews = nodeView.isCollapsed() ? getLastCollapsed(nodeView).getChildrenViews() : nodeView.getChildrenViews();
        // compute the union of the child bounding boxes recursively
//        for(int c = 0; c < childrenViews.size(); c++)
//        {
//            b = b.union(getTreeBounds(childrenViews.get(c).getChildView()));
//        }
        return b;
    }

    public void updateTreeView(Tree tree)
    {
        this.tree = tree;
        if(treeSelection.getSelection().size() == 0)
        {
            treeSelection.newSelection(new TreeNodeView(tree.getRootNode()));
        }
        repaint();
    }

    public void setTree(Tree tree)
    {
        this.tree = tree;
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
            //treeElement.adjustOffset(new Point(60 - bounds.y, 0));
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
            //setSize(bounds.getDimension());
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            newReDraw(graphics2D);
            //drawTree(graphics2D, tree);
            setSize(dimension);

            //graphics2D.drawRect(0,0, dimension.width, dimension.height);

            if(treeSelection.getHover() != null)
            {
                drawMouseOver(graphics2D);
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

    public void mouseWheelMovedAt(MouseWheelEvent e)
    {
        updateTreeSize();
    }

    public boolean checkIfBranchesConverge(TreeNode node1, TreeNode node2)
    {
        TreeNode branch0 = node1;
        while(branch0.getChildren().size() == 1)
        {
            //branch0 = branch0.getChildren().get(0);
//            if(branch0.getParents().size() == 2)
//            {
//                break;
//            }
        }

        TreeNode branch1 = node2;
        while(branch1.getChildren().size() == 1)
        {
           // branch1 = branch1.getChildren().get(0);
//            if(branch1.getParents().size() == 2)
//            {
//                break;
//            }
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
        boolean overallColor = treeNode.isContradictoryBranch();
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
     * Delete the child and child's subtree starting at the current state
     */
    public void delChildAtCurrentState()
    {
        Tree tree = GameBoardFacade.getInstance().getTree();
//        HashSet<TreeNode> selected = tree.getSelected(
//        ArrayList<TreeNode> selectedList = new ArrayList<>(selected);

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

    private void drawTree(Graphics2D graphics2D, Tree tree)
    {
        if(rootNodeView == null)
        {
            viewMap.clear();
            redrawTree(graphics2D, tree);
        }
        else
        {
            recalculateTreeLocations();
            drawTree(graphics2D, rootNodeView);
            graphics2D.setColor(Color.BLACK);
            graphics2D.setStroke(new BasicStroke(3));
            //graphics2D.drawRect(0, 0, dimension.width, dimension.height);
        }
    }

    private void drawTree(Graphics2D graphics2D, TreeElementView elementView)
    {
        if(elementView == null)
        {
            return;
        }
        elementView.draw(graphics2D);
        if(elementView.getType() == NODE)
        {
            TreeNodeView nodeView = (TreeNodeView)elementView;
            for(TreeTransitionView childView: nodeView.getChildrenViews())
            {
                drawTree(graphics2D, childView);
            }
        }
        else
        {
            TreeTransitionView transitionView = (TreeTransitionView)elementView;
            drawTree(graphics2D, transitionView.getChildView());
        }
    }

    private void recalculateTreeLocations()
    {
        viewMap.clear();
        dimension.height = recalculateNodeViewLocations(rootNodeView, 0, 0) + 2 * BORDER_GAP_HEIGHT;
        for(TreeTransitionView transitionView : rootNodeView.getChildrenViews())
        {
            calculateTransitionViewLocations(transitionView);
        }
    }

    /**
     * Recursively calculates the locations of the tree from scratch using deep first search
     *
     * @param nodeView current view object
     * @param depth current depth of the tree view
     * @param rspan current breath of the tree view
     * @return span of the branch (bounded y distance of the branch center at the view)
     */
    private int recalculateNodeViewLocations(TreeNodeView nodeView, int depth, int rspan)
    {
//        int xLoc = (NODE_GAP_WIDTH + DIAMETER) * depth + DIAMETER;
//        dimension.width = Math.max(dimension.width, xLoc + DIAMETER + BORDER_GAP_WIDTH);
//
//        TreeNode node = nodeView.getTreeElement();
//        nodeView.setX(xLoc);
//
//        viewMap.put(node, nodeView);
//
//        if(node.getChildren().isEmpty())
//        {
//            nodeView.setY(rspan + RADIUS + BORDER_GAP_HEIGHT);
//            return DIAMETER;
//        }
//        else
//        {
//            ArrayList<TreeTransitionView> children = nodeView.getChildrenViews();
//            int size = children.size();
//            int tspan = 0;
//            for(int i = 0; i < size; i++)
//            {
//                TreeTransitionView childView = children.get(i);
//                TreeTransition childTrans = childView.getTreeElement();
//                TreeNodeView childNodeView = childView.getChildView();
//                viewMap.put(childTrans, childView);
//
//                int cspan = DIAMETER;
//                if(childNodeView != null)
//                {
//                    if(!viewMap.containsKey(childNodeView.getTreeElement()))
//                    {
//                        cspan = recalculateNodeViewLocations(childView.getChildView(), depth + 1, rspan + tspan);
//
//                        childView.setEndX((NODE_GAP_WIDTH + DIAMETER) * (depth + 1) + RADIUS - TRANS_GAP / 2);
//                        childView.setEndY(rspan + tspan + cspan / 2 + BORDER_GAP_HEIGHT);
//                    }
//                    else
//                    {
//                        childView.setEndX((NODE_GAP_WIDTH + DIAMETER) * (depth + 1) + RADIUS - TRANS_GAP / 2);
//                        childView.setEndY(rspan + tspan + cspan / 2 + BORDER_GAP_HEIGHT);
//
//                        TreeNode childNode = childNodeView.getTreeElement();
////                        ArrayList<TreeTransition> parentsTrans = childNode..getParents();
////                        int calcTrans = 0;
////                        for(TreeTransition tran : parentsTrans)
////                        {
////                            if(viewMap.containsKey(tran))
////                            {
////                                calcTrans++;
////                            }
////                        }
////                        if(calcTrans == parentsTrans.size())
////                        {
////                            //System.err.println("Seen : " + viewMap.containsKey(childNodeView.getTreeElement()));
//////                            int mergeY = (childNodeView.getParentViews().get(0).getEndY() + childNodeView.getParentViews().get(childNodeView.getParentViews().size() - 1).getEndY()) / 2;
//////                            childNodeView.setY(mergeY);
////
////                            for(TreeTransitionView transView : childNodeView.getParentViews())
////                            {
////                                transView.setEndX(childNodeView.getX() - RADIUS - TRANS_GAP / 2);
////                                transView.setEndY(childNodeView.getY());
////                            }
////                        }
//                    }
//                }
//                else
//                {
//                    childView.setEndX((NODE_GAP_WIDTH + DIAMETER) * (depth + 1) + RADIUS - TRANS_GAP / 2);
//                    childView.setEndY(rspan + tspan + cspan / 2 + BORDER_GAP_HEIGHT);
//                }
//
//                dimension.width = Math.max(dimension.width, (NODE_GAP_WIDTH + DIAMETER) * (depth + 1) + RADIUS - TRANS_GAP / 2 + BORDER_GAP_WIDTH);
//
//                childView.setStartX(xLoc + RADIUS + TRANS_GAP / 2);
//
//                tspan += i == size - 1 ? cspan : cspan + NODE_GAP_HEIGHT;
//            }
//            for(TreeTransitionView view : nodeView.getChildrenViews())
//            {
//                view.setStartY(rspan + tspan / 2 + BORDER_GAP_HEIGHT);
//            }
//            nodeView.setY(rspan + tspan / 2 + BORDER_GAP_HEIGHT);
//            return tspan;
//        }
        return 0;
    }

    /**
     * Recursively calculates the locations of the transition views in the tree view.
     * Assumes the locations of the nodes have already been calculated by createTreeViews
     *
     * @param transitionView transition view
     */
    private void calculateTransitionViewLocations(TreeTransitionView transitionView)
    {
//        TreeNodeView childView = transitionView.getChildView();
//        TreeNodeView parentView = transitionView.getParentView();
//
//        double ratio = (childView.getY() - parentView.getY()) / (childView.getX() - parentView.getX());
//        double radius = childView.getRadius();
//        double denominator = sqrt((ratio * ratio) + 1);
//        double xMagnitude = (radius + TRANS_GAP) / denominator;
//        double yMagnitude = (radius + TRANS_GAP) * ratio / denominator;
//
//        int startPointX = parentView.getX() + (int) round(xMagnitude);
//        int startPointY = parentView.getY() + (int) round(yMagnitude);
//        transitionView.setStartPoint(new Point(startPointX, startPointY));
//
//        int endPointX = childView.getX() - (int) round(xMagnitude);
//        int endPointY = childView.getY() - (int) round(yMagnitude);
//        transitionView.setEndPoint(new Point(endPointX, endPointY));
//        for(TreeTransitionView child: childView.getChildrenViews())
//        {
//            calculateTransitionViewLocations(child);
//        }
    }

    /**
     * Redraws the tree completely from scratch
     *
     * @param graphics2D graphics object used for drawing
     * @param tree tree
     */
    private void redrawTree(Graphics2D graphics2D, Tree tree)
    {
        if(tree == null)
        {
            //TODO add error
        }
        else
        {
            rootNodeView = new TreeNodeView(tree.getRootNode());
            viewMap.put(tree.getRootNode(), rootNodeView);
            dimension.height = createTreeViews(rootNodeView, 0, 0) + DIAMETER + BORDER_GAP_HEIGHT;
            for(TreeTransitionView transitionView: rootNodeView.getChildrenViews())
            {
                calculateTransitionViewLocations(transitionView);
            }
            redrawTree(graphics2D, rootNodeView);
            treeSelection.newSelection(rootNodeView);
            viewport.setViewPosition(new Point(0, dimension.height / 2 + viewport.getHeight() / 2 + rootNodeView.getRadius()));
        }
    }

    private void redrawTree(Graphics2D graphics2D, TreeNodeView nodeView)
    {
        if(nodeView != null)
        {
            nodeView.draw(graphics2D);
            for(TreeTransitionView transitionView : nodeView.getChildrenViews())
            {
                transitionView.draw(graphics2D);
                redrawTree(graphics2D, transitionView.getChildView());
            }
        }
    }

    /**
     * Recursively calculates the locations of the tree from scratch using deep first search
     *
     * @param nodeView current view object
     * @param depth current depth of the tree view
     * @param rspan current breath of the tree view
     * @return span of the branch (bounded y distance of the branch center at the view)
     */
    private int createTreeViews(TreeNodeView nodeView, int depth, int rspan)
    {
//        int xLoc = (NODE_GAP_WIDTH + DIAMETER) * depth + DIAMETER;
//        dimension.width = Math.max(dimension.width, xLoc);
//
//        TreeNode node = nodeView.getTreeElement();
//        nodeView.setX(xLoc);
//
//        if(node.getChildren().isEmpty())
//        {
//            nodeView.setY(rspan + RADIUS + BORDER_GAP_HEIGHT);
//            return DIAMETER;
//        }
//        else
//        {
//            List<TreeTransition> children = node.getChildren();
//            int size = children.size();
//            int tspan = 0;
//            for(int i = 0; i < size; i++)
//            {
//                TreeTransition child = children.get(i);
//                TreeTransitionView transitionView = new TreeTransitionView(child, nodeView);
//                viewMap.put(child, transitionView);
//
//                TreeNodeView childView = null;
//                if(child.getChildNode() != null && !viewMap.containsKey(child.getChildNode()))
//                {
//                    childView = new TreeNodeView(child.getChildNode());
//                    transitionView.setChildView(childView);
//                    childView.setParentView(transitionView);
//                }
//
//                int cspan = createTreeViews(childView, depth + 1, rspan + tspan);
//
//                transitionView.setStartX(xLoc + RADIUS + TRANS_GAP / 2);
//                transitionView.setEndX((NODE_GAP_WIDTH + DIAMETER) * (depth + 1) + RADIUS - TRANS_GAP / 2);
//
//                transitionView.setEndY(rspan + tspan + cspan / 2 + BORDER_GAP_HEIGHT);
//
//                nodeView.addChildrenView(transitionView);
//                tspan += i == size - 1 ? cspan : cspan + NODE_GAP_HEIGHT;
//            }
//            for(TreeTransitionView view : nodeView.getChildrenViews())
//            {
//                view.setStartY(rspan + tspan / 2 + BORDER_GAP_HEIGHT);
//            }
//            nodeView.setY(rspan + tspan / 2 + BORDER_GAP_HEIGHT);
//            return tspan;
//        }
        return 0;
    }

    public TreeTransitionView addTransitionView(TreeNodeView nodeView, TreeTransition transition)
    {
        TreeTransitionView transitionView = new TreeTransitionView(transition, nodeView);
        nodeView.getChildrenViews().add(transitionView);
        return transitionView;
    }

    public TreeNodeView addNodeView(TreeTransitionView transitionView, TreeNode node)
    {
        TreeNodeView newNodeView = new TreeNodeView(node);
        transitionView.setChildView(newNodeView);
        newNodeView.setParentView(transitionView);
        return newNodeView;
    }

    public void removeTreeElement(TreeElementView view)
    {
        if(view.getType() == NODE)
        {
            TreeNodeView nodeView = (TreeNodeView)view;
            nodeView.getParentView().setChildView(null);
        }
        else
        {
            TreeTransitionView transitionView = (TreeTransitionView)view;
            transitionView.getParentViews().forEach((TreeNodeView n) -> n.removeChildrenView(transitionView));
        }
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
//        while(iterBoard.getChildren().dimension() == 1 && iterBoard.getChildren().get(0).getParents().dimension() < 2)
//        {
//            //iterBoard = iterBoard.getChildren().get(0);
//        }

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
     */
    public void drawMouseOver(Graphics2D g)
    {
        if(treeSelection.getHover().getType() == TRANSITION && ((TreeTransitionView)treeSelection.getHover()).getTreeElement().isJustified())
        {
            TreeTransitionView transitionView = (TreeTransitionView) treeSelection.getHover();
            TreeTransition transition = transitionView.getTreeElement();
            Rule rule = transitionView.getTreeElement().getRule();
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

            if(transition.isJustified())
            {
                g_tmp.setColor(Color.black);
                String[] tmp = {rule.getRuleName()};
                v_offset = 10 + tmp.length * 14;
                for(int c1 = 0; c1 < tmp.length; c1++)
                {
                    g_tmp.drawString(tmp[c1], 0, (14 * c1) + 10);
                }
                g_tmp.setColor(Color.gray);
                g_tmp.drawRect(0, v_offset, 100, 100);
            }

            if(rule != null)
            {
                g_tmp.drawImage(rule.getImageIcon().getImage(), 0, v_offset, null);
            }
            Point mousePoint = treeSelection.getMousePoint();
            int scaledWidth = (int) (scale * vp.getWidth());
            int scaledHeight = (int) (scale * vp.getHeight());
            g.drawImage(image, mousePoint.x, mousePoint.y, scaledWidth, scaledHeight, null);
        }
    }

    public void resetView()
    {
        this.tree = null;
        this.rootNodeView = null;
        this.treeSelection.clearSelection();
        this.treeSelection.clearHover();
    }

    /**
     * Called when a tree element is added to the tree
     *
     * @param element TreeElement that was added to the tree
     */
    @Override
    public void onTreeElementAdded(TreeElement element)
    {
        if(element.getType() == NODE)
        {
            addTreeNode((TreeNode)element);
        }
        else
        {
            addTreeTransition((TreeTransition)element);
        }
        repaint();
    }

    /**
     * Called when a tree element is removed from the tree
     *
     * @param element TreeElement that was removed to the tree
     */
    @Override
    public void onTreeElementRemoved(TreeElement element)
    {
        if(element.getType() == NODE)
        {
            TreeNode node = (TreeNode)element;
            TreeNodeView nodeView = (TreeNodeView)viewMap.get(node);

            nodeView.getParentView().setChildView(null);
            removeTreeNode(node);
        }
        else
        {
            TreeTransition trans = (TreeTransition)element;
            TreeTransitionView transView = (TreeTransitionView)viewMap.get(trans);

            transView.getParentViews().forEach(n -> n.removeChildrenView(transView));
            removeTreeTransition(trans);
        }
        repaint();
    }

    /**
     * Called when the tree selection was changed
     *
     * @param selection tree selection that was changed
     */
    @Override
    public void onTreeSelectionChanged(TreeSelection selection)
    {
        repaint();
    }

    public TreeElementView getElementView(TreeElement element)
    {
        return viewMap.get(element);
    }

    public void removeTreeNode(TreeNode node)
    {
        viewMap.remove(node);
        node.getChildren().forEach(t -> removeTreeTransition(t));
    }

    public void removeTreeTransition(TreeTransition trans)
    {
        viewMap.remove(trans);
        if(trans.getChildNode() != null)
        {
            removeTreeNode(trans.getChildNode());
        }
    }

    public void addTreeNode(TreeNode node)
    {
        TreeTransition parent = node.getParent();

        TreeNodeView nodeView = new TreeNodeView(node);
        TreeTransitionView parentView = (TreeTransitionView)viewMap.get(parent);

        nodeView.setParentView(parentView);
        parentView.setChildView(nodeView);

        viewMap.put(node, nodeView);

        if(!node.getChildren().isEmpty())
        {
            node.getChildren().forEach(t -> addTreeTransition(t));
        }
    }

    public void addTreeTransition(TreeTransition trans)
    {
        List<TreeNode> parents = trans.getParents();

        TreeTransitionView transView = new TreeTransitionView(trans);
        for(TreeNode parent : parents)
        {
            TreeNodeView parentNodeView = (TreeNodeView)viewMap.get(parent);
            transView.addParentView(parentNodeView);
            parentNodeView.addChildrenView(transView);
        }

        viewMap.put(trans, transView);

        if(trans.getChildNode() != null)
        {
            addTreeNode(trans.getChildNode());
        }
    }

    ///New Draw Methods

    public void newReDraw(Graphics2D graphics2D)
    {
        if(tree == null)
        {
            //TODO add error
            System.err.println("CreateViews: Null tree");
        }
        else
        {
            if(rootNodeView == null)
            {
                rootNodeView = new TreeNodeView(tree.getRootNode());

                createViews(rootNodeView);
                System.err.println("newReDraw: Created Views");

                treeSelection.newSelection(rootNodeView);
            }

            calcSpan(rootNodeView);
            System.err.println("newReDraw: Calculated span: " + rootNodeView.getSpan());

            calculateViewLocations(rootNodeView, 0);
            dimension.height = (int)rootNodeView.getSpan() + DIAMETER + BORDER_GAP_HEIGHT;
            System.err.println("newReDraw: Calculated view positions");

            //dimension = new Dimension(800, 400);

            redrawTree(graphics2D, rootNodeView);
            System.err.println("newReDraw: redrawTree");

        }
    }

    public void createViews(TreeNodeView nodeView)
    {
        if(nodeView != null)
        {
            viewMap.put(nodeView.getTreeElement(), nodeView);

            TreeNode node = nodeView.getTreeElement();
            for(TreeTransition trans : node.getChildren())
            {
                TreeTransitionView transView = new TreeTransitionView(trans);
                viewMap.put(transView.getTreeElement(), transView);

                transView.addParentView(nodeView);
                nodeView.addChildrenView(transView);

                TreeNode childNode = trans.getChildNode();
                if(childNode != null)
                {
                    TreeNodeView childNodeView = new TreeNodeView(childNode);
                    viewMap.put(childNodeView.getTreeElement(), childNodeView);

                    childNodeView.setParentView(transView);
                    transView.setChildView(childNodeView);

                    createViews(childNodeView);
                }
            }
        }
    }

    public void calculateViewLocations(TreeNodeView nodeView, int depth)
    {
        nodeView.setDepth(depth);
        int xLoc = (NODE_GAP_WIDTH + DIAMETER) * depth + DIAMETER;
        nodeView.setX(xLoc);
        dimension.width = Math.max(dimension.width, xLoc + DIAMETER);

        TreeTransitionView parentTransView = nodeView.getParentView();
        int yLoc = parentTransView == null ? (int)nodeView.getSpan() / 2 : parentTransView.getEndY() ;
        nodeView.setY(yLoc);

        ArrayList<TreeTransitionView> children = nodeView.getChildrenViews();
        switch(children.size())
        {
            case 0:
                break;
            case 1:
            {
                TreeTransitionView childView = children.get(0);

                List<TreeNodeView> parentsViews = childView.getParentViews();
                if(parentsViews.size() == 1)
                {
                    childView.setEndY(yLoc);

                    childView.setDepth(depth);

                    Point lineStartPoint = childView.getLineStartPoint(0);
                    lineStartPoint.x = xLoc + RADIUS + TRANS_GAP / 2;
                    lineStartPoint.y = yLoc;
                    childView.setEndX((NODE_GAP_WIDTH + DIAMETER) * (depth + 1) + RADIUS - TRANS_GAP / 2);

                    dimension.width = Math.max(dimension.width, childView.getEndX() + DIAMETER);

                    TreeNodeView childNodeView = childView.getChildView();
                    if(childNodeView != null)
                    {
                        calculateViewLocations(childNodeView, depth + 1);
                    }
                }
                else if(parentsViews.size() > 1 && parentsViews.get(parentsViews.size() - 1) == nodeView)
                {
                    int yAvg = 0;
                    for(int i = 0; i < parentsViews.size(); i++)
                    {
                        TreeNodeView parentNodeView = parentsViews.get(i);
                        depth = Math.max(depth, parentNodeView.getDepth());
                        yAvg += parentNodeView.getY();

                        Point lineStartPoint = childView.getLineStartPoint(i);
                        lineStartPoint.x = parentNodeView.getX() + RADIUS + TRANS_GAP / 2;
                        lineStartPoint.y = parentNodeView.getY();
                    }
                    yAvg /= parentsViews.size();
                    childView.setEndY(yAvg);

                    childView.setDepth(depth);

                    childView.setEndX((NODE_GAP_WIDTH + DIAMETER) * (depth + 1) + RADIUS - TRANS_GAP / 2);

                    dimension.width = Math.max(dimension.width, childView.getEndX() + DIAMETER);

                    TreeNodeView childNodeView = childView.getChildView();
                    if(childNodeView != null)
                    {
                        calculateViewLocations(childNodeView, depth + 1);
                    }
                }
                break;
            }
            default:
            {
                int span = 0;
                for(TreeTransitionView childView : children)
                {
                    span += childView.getSpan();
                }

                span = (int)((nodeView.getSpan() - span) / 2);
                for(int i = 0; i < children.size(); i++)
                {
                    TreeTransitionView childView = children.get(i);

                    childView.setDepth(depth);

                    Point lineStartPoint = childView.getLineStartPoint(0);
                    lineStartPoint.x = xLoc + RADIUS + TRANS_GAP / 2;
                    lineStartPoint.y = yLoc;
                    childView.setEndX((NODE_GAP_WIDTH + DIAMETER) * (depth + 1) + RADIUS - TRANS_GAP / 2);
                    childView.setEndY(yLoc - (int)(nodeView.getSpan() / 2) + span + (int)(childView.getSpan() / 2));

                    span += childView.getSpan();
                    TreeNodeView childNodeView = childView.getChildView();
                    if(childNodeView != null)
                    {
                        calculateViewLocations(childNodeView, depth + 1);
                    }
                }
                break;
            }
        }
    }

    public void calcSpan(TreeElementView view)
    {
        if(view.getType() == NODE)
        {
            TreeNodeView nodeView = (TreeNodeView)view;
            TreeNode node = nodeView.getTreeElement();
            if(nodeView.getChildrenViews().size() == 0)
            {
                nodeView.setSpan(DIAMETER);
            }
            else if(nodeView.getChildrenViews().size() == 1)
            {
                TreeTransitionView childView = nodeView.getChildrenViews().get(0);
                calcSpan(childView);
                if(childView.getParentViews().size() > 1)
                {
                    nodeView.setSpan(DIAMETER);
                }
                else
                {
                    nodeView.setSpan(childView.getSpan());
                }
            }
            else
            {
                DisjointSets<TreeTransition> branches = node.findMergingBranches();
                List<TreeTransition> children = node.getChildren();

                if(node == children.get(0).getParents().get(0))
                {
                    reorderBranches(node, branches);
                }

                List<Set<TreeTransition>> mergingSets = branches.getAllSets();

                double span = 0.0;
                for(Set<TreeTransition> mergeSet : mergingSets)
                {
                    if(mergeSet.size() > 1)
                    {
                        TreeTransition mergePoint = TreeNode.findMergingPoint(mergeSet);
                        TreeTransitionView mergePointView = (TreeTransitionView) viewMap.get(mergePoint);
                        double subSpan = 0.0;
                        for(TreeTransition branch: mergeSet)
                        {
                            TreeTransitionView branchView = (TreeTransitionView) viewMap.get(branch);
                            subCalcSpan(branchView, mergePointView);
                            subSpan += branchView.getSpan();
                        }
                        calcSpan(mergePointView);
                        span += Math.max(mergePointView.getSpan(), subSpan);
                    }
                    else
                    {
                        TreeTransition trans = mergeSet.iterator().next();
                        TreeTransitionView transView = (TreeTransitionView) viewMap.get(trans);
                        calcSpan(transView);
                        span += transView.getSpan();
                    }
                }

                nodeView.setSpan(span);
            }
        }
        else
        {
            TreeTransitionView transView = (TreeTransitionView)view;
            TreeNodeView nodeView = transView.getChildView();
            if(nodeView == null)
            {
                transView.setSpan(DIAMETER);
            }
            else
            {
                calcSpan(nodeView);
                transView.setSpan(nodeView.getSpan());
            }
        }
    }

    /**
     * Calculates the sub span of a given sub tree rooted at the specified view and stops at the tree element view
     * specified as stop. Stop tree element is NOT included in the span calculation
     *
     * @param view
     * @param stop
     */
    private void subCalcSpan(TreeElementView view, TreeElementView stop)
    {
        //safe-guard for infinite loop
        if(view == stop)
        {
            return;
        }

        if(view.getType() == NODE)
        {
            TreeNodeView nodeView = (TreeNodeView)view;
            TreeNode node = nodeView.getTreeElement();
            if(nodeView.getChildrenViews().size() == 0)
            {
                nodeView.setSpan(DIAMETER);
            }
            else if(nodeView.getChildrenViews().size() == 1)
            {
                TreeTransitionView childView = nodeView.getChildrenViews().get(0);
                if(childView == stop)
                {
                    nodeView.setSpan(DIAMETER);
                }
                else
                {
                    subCalcSpan(childView, stop);
                    if(childView.getParentViews().size() > 1)
                    {
                        nodeView.setSpan(DIAMETER);
                    }
                    else
                    {
                        nodeView.setSpan(childView.getSpan());
                    }
                }
            }
            else
            {
                DisjointSets<TreeTransition> branches = node.findMergingBranches();
                List<TreeTransition> children = node.getChildren();

                if(node == children.get(0).getParents().get(0))
                {
                    reorderBranches(node, branches);
                }

                List<Set<TreeTransition>> mergingSets = branches.getAllSets();

                double span = 0.0;
                for(Set<TreeTransition> mergeSet : mergingSets)
                {
                    if(mergeSet.size() > 1)
                    {
                        TreeTransition mergePoint = TreeNode.findMergingPoint(mergeSet);
                        TreeTransitionView mergePointView = (TreeTransitionView) viewMap.get(mergePoint);
                        double subSpan = 0.0;
                        for(TreeTransition branch: mergeSet)
                        {
                            TreeTransitionView branchView = (TreeTransitionView) viewMap.get(branch);
                            subCalcSpan(branchView, mergePointView);
                            subSpan += branchView.getSpan();
                        }
                        subCalcSpan(mergePointView, stop);
                        span += Math.max(mergePointView.getSpan(), subSpan);
                    }
                    else
                    {
                        TreeTransition trans = mergeSet.iterator().next();
                        TreeTransitionView transView = (TreeTransitionView) viewMap.get(trans);
                        subCalcSpan(transView, stop);
                        span += transView.getSpan();
                    }
                }

                nodeView.setSpan(span);
            }
        }
        else
        {
            TreeTransitionView transView = (TreeTransitionView)view;
            TreeNodeView nodeView = transView.getChildView();
            if(nodeView == null || nodeView == stop)
            {
                transView.setSpan(DIAMETER);
            }
            else
            {
                calcSpan(nodeView);
                transView.setSpan(nodeView.getSpan());
            }
        }
    }

    /**
     * Reorders branches such that merging branches are sequentially grouped together and transitions are kept in
     * relative order in the list of child transitions of the specified node
     *
     * @param node root node of the branches
     * @param branches DisjointSets of the child branches of the specified node which determine which branches merge
     */
    private void reorderBranches(TreeNode node, DisjointSets<TreeTransition> branches)
    {
        List<TreeTransition> children = node.getChildren();
        List<Set<TreeTransition>> mergingSets = branches.getAllSets();

        List<List<TreeTransition>> newOrder = new ArrayList<>();
        for(Set<TreeTransition> set : mergingSets)
        {
            List<TreeTransition> mergeBranch = new ArrayList<>();
            newOrder.add(mergeBranch);
            children.forEach((TreeTransition t) ->
            {
                if(set.contains(t))
                {
                    mergeBranch.add(t);
                }
            });
            mergeBranch.sort((TreeTransition t1, TreeTransition t2) ->
                    children.indexOf(t1) <= children.indexOf(t2) ? -1 : 1);
        }

        newOrder.sort((List<TreeTransition> b1, List<TreeTransition> b2) ->
        {
            int low1 = -1;
            int low2 = -1;
            for(TreeTransition t1 : b1)
            {
                int curIndex = children.indexOf(t1);
                if(low1 == -1 || curIndex < low1 )
                {
                    low1 = curIndex;
                }
            }
            for(TreeTransition t1 : b2)
            {
                int curIndex = children.indexOf(t1);
                if(low1 == -1 || curIndex < low1 )
                {
                    low1 = curIndex;
                }
            }
            return low1 < low2 ? -1 : 1;
        });

        List<TreeTransition> newChildren = new ArrayList<>();
        newOrder.forEach(l -> l.forEach(t -> newChildren.add(t)));
        node.setChildren(newChildren);
    }
}