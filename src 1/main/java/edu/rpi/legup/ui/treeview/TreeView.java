package edu.rpi.legup.ui.treeview;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.controller.TreeController;
import edu.rpi.legup.model.observer.ITreeListener;
import edu.rpi.legup.model.tree.Tree;
import edu.rpi.legup.model.tree.TreeElement;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.ui.ScrollView;
import edu.rpi.legup.utility.DisjointSets;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static edu.rpi.legup.model.tree.TreeElementType.NODE;
import static edu.rpi.legup.model.tree.TreeElementType.TRANSITION;
import static edu.rpi.legup.ui.treeview.TreeNodeView.DIAMETER;
import static edu.rpi.legup.ui.treeview.TreeNodeView.RADIUS;

public class TreeView extends ScrollView implements ITreeListener {
    private final static Logger LOGGER = LogManager.getLogger(TreeView.class.getName());

    private static final int TRANS_GAP = 5;

    private static final int NODE_GAP_WIDTH = 70;
    private static final int NODE_GAP_HEIGHT = 15;

    private static final int BORDER_GAP_HEIGHT = 20;
    private static final int BORDER_GAP_WIDTH = 20;

    private static final int BORDER_SPACING = 100;

    private TreeNodeView nodeHover;

    private ArrayList<Rectangle> currentStateBoxes;
    private Rectangle bounds = new Rectangle(0, 0, 0, 0);

    private Tree tree;
    private TreeNodeView rootNodeView;
    private Map<TreeElement, TreeElementView> viewMap;
    private Dimension dimension;

    private TreeViewSelection selection;

    public TreeView(TreeController treeController) {
        super(treeController);
        currentStateBoxes = new ArrayList<>();
        setSize(dimension = new Dimension(100, 200));
        setPreferredSize(new Dimension(640, 160));

        viewMap = new HashMap<>();

        selection = new TreeViewSelection();
    }

    public TreeViewSelection getSelection() {
        return selection;
    }

    /**
     * Gets the tree node puzzleElement that the mouse is hovering over
     *
     * @return tree node puzzleElement that the mouse is hovering over
     */
    public TreeNodeView getNodeHover() {
        return nodeHover;
    }

    /**
     * Sets the tree node puzzleElement that the mouse is hovering over
     *
     * @param nodeHover tree node puzzleElement the mouse is hovering over
     */
    public void setNodeHover(TreeNodeView nodeHover) {
        this.nodeHover = nodeHover;
    }

    /**
     * Gets the TreeElementView by the specified point or null if no view exists at the specified point
     *
     * @param point location to query for a view
     * @return TreeElementView at the point specified, otherwise null
     */
    public TreeElementView getTreeElementView(Point point) {
        return getTreeElementView(point, rootNodeView);
    }

    /**
     * Recursively gets the TreeElementView by the specified point or null if no view exists at the specified point or
     * the view specified is null
     *
     * @param point       location to query for a view
     * @param elementView view to determine if the point is contained within it
     * @return TreeElementView at the point specified, otherwise null
     */
    private TreeElementView getTreeElementView(Point point, TreeElementView elementView) {
        if (elementView == null) {
            return null;
        } else if (elementView.contains(point) && elementView.isVisible()) {
            if (elementView.getType() == NODE && ((TreeNodeView) elementView).isContradictoryState()) {
                return null;
            }
            return elementView;
        } else {
            if (elementView.getType() == NODE) {
                TreeNodeView nodeView = (TreeNodeView) elementView;
                for (TreeTransitionView transitionView : nodeView.getChildrenViews()) {
                    TreeElementView view = getTreeElementView(point, transitionView);
                    if (view != null) {
                        return view;
                    }
                }
            } else {
                TreeTransitionView transitionView = (TreeTransitionView) elementView;
                return getTreeElementView(point, transitionView.getChildView());
            }
        }
        return null;
    }

    public void updateTreeView(Tree tree) {
        this.tree = tree;
        if (selection.getSelectedViews().size() == 0) {
            selection.newSelection(new TreeNodeView(tree.getRootNode()));
        }
        repaint();
    }

    /**
     * Sets the tree associated with this TreeView
     *
     * @param tree tree
     */
    public void setTree(Tree tree) {
        this.tree = tree;
    }

    public void updateTreeSize() {
        if (GameBoardFacade.getInstance().getTree() == null) {
            return;
        }
        setSize(bounds.getSize());
    }

    public void reset() {
        if (bounds.x != 0 || bounds.y != 0) {
            updateTreeSize();
        }
    }

    public void zoomFit() {
        zoomTo(1.0);
        updateTreeSize();
        double fitwidth = (viewport.getWidth() - 8.0) / (getSize().width - 200);
        double fitheight = (viewport.getHeight() - 8.0) / (getSize().height - 120);
        // choose the smaller of the two and zoom
        zoomTo((fitwidth < fitheight) ? fitwidth : fitheight);
        viewport.setViewPosition(new Point(0, 0));
    }

    /**
     * Creates a customized viewport for the scroll pane
     *
     * @return viewport for the scroll pane
     */
    @Override
    protected JViewport createViewport() {
        return new JViewport() {
            @Override
            protected LayoutManager createLayoutManager() {
                return new ViewportLayout() {
                    @Override
                    public void layoutContainer(Container parent) {
                        Point point = viewport.getViewPosition();
                        // determine the maximum x and y view positions
                        int mx = getCanvas().getWidth() - viewport.getWidth();
                        int my = getCanvas().getHeight() - viewport.getHeight();
                        // obey edge boundaries
                        if (point.x < 0) {
                            point.x = 0;
                        }
                        if (point.x > mx) {
                            point.x = mx;
                        }
                        if (point.y < 0) {
                            point.y = 0;
                        }
                        if (point.y > my) {
                            point.y = my;
                        }
                        // center margins
                        if (mx < 0) {
                            point.x = 0;
                        }
                        if (my < 0) {
                            point.y = my / 2;
                        }
                        viewport.setViewPosition(point);
                    }
                };
            }
        };
    }

    public void draw(Graphics2D graphics2D) {
        currentStateBoxes.clear();
        Tree tree = GameBoardFacade.getInstance().getTree();
        if (tree != null) {
            //setSize(bounds.getDimension());
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            drawTree(graphics2D);

            dimension.width += BORDER_SPACING;
            setSize(dimension);
//            graphics2D.drawRect(0,0, dimension.width, dimension.height);

            if (selection.getHover() != null) {
                drawMouseOver(graphics2D);
            }
        }
    }

    public void zoomReset() {
        zoomTo(1.0);
        viewport.setViewPosition(new Point(0, 0));
    }

    private void redrawTree(Graphics2D graphics2D, TreeNodeView nodeView) {
        if (nodeView != null) {
            nodeView.draw(graphics2D);
            for (TreeTransitionView transitionView : nodeView.getChildrenViews()) {
                transitionView.draw(graphics2D);
                redrawTree(graphics2D, transitionView.getChildView());
            }
        }
    }

    public void removeTreeElement(TreeElementView view) {
        if (view.getType() == NODE) {
            TreeNodeView nodeView = (TreeNodeView) view;
            nodeView.getParentView().setChildView(null);
        } else {
            TreeTransitionView transitionView = (TreeTransitionView) view;
            transitionView.getParentViews().forEach((TreeNodeView n) -> n.removeChildrenView(transitionView));
        }
    }

    /**
     * When the edu.rpi.legup.user hovers over the transition, draws the corresponding rules image
     *
     * @param g the graphics to use to draw
     */
    public void drawMouseOver(Graphics2D g) {
        if (selection.getHover().getType() == TRANSITION && ((TreeTransitionView) selection.getHover()).getTreeElement().isJustified()) {
            TreeTransition transition = (TreeTransition) selection.getHover().treeElement;
            int imgWidth = 100;
            int imgHeight = 100;

            BufferedImage image = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB);
            image.createGraphics().drawImage(transition.getRule().getImageIcon().getImage(), 0, 0, null);
            Point mousePoint = selection.getMousePoint();
            g.drawImage(image, mousePoint.x, mousePoint.y - 50, imgWidth, imgHeight, null);
        }
    }

    public void resetView() {
        this.tree = null;
        this.rootNodeView = null;
        this.selection.clearSelection();
        this.selection.clearHover();
    }

    /**
     * Called when a tree puzzleElement is added to the tree
     *
     * @param treeElement TreeElement that was added to the tree
     */
    @Override
    public void onTreeElementAdded(TreeElement treeElement) {
        if (treeElement.getType() == NODE) {
            addTreeNode((TreeNode) treeElement);
        } else {
            addTreeTransition((TreeTransition) treeElement);
        }
        repaint();
    }

    /**
     * Called when a tree puzzleElement is removed from the tree
     *
     * @param element TreeElement that was removed to the tree
     */
    @Override
    public void onTreeElementRemoved(TreeElement element) {
        if (element.getType() == NODE) {
            TreeNode node = (TreeNode) element;
            TreeNodeView nodeView = (TreeNodeView) viewMap.get(node);

            nodeView.getParentView().setChildView(null);
            removeTreeNode(node);
        } else {
            TreeTransition trans = (TreeTransition) element;
            TreeTransitionView transView = (TreeTransitionView) viewMap.get(trans);

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
    public void onTreeSelectionChanged(TreeViewSelection selection) {
        this.selection.getSelectedViews().forEach(v -> v.setSelected(false));
        selection.getSelectedViews().forEach(v -> v.setSelected(true));

        this.selection = selection;

//        List<TreeElementView> selectedViews = selection.getSelectedViews();
//        Point avg = new Point();
//        for(TreeElementView elementView : selectedViews) {
//            if(elementView.getType() == NODE) {
//                TreeNodeView nodeView = (TreeNodeView)elementView;
//                avg.x += nodeView.getX();
//                avg.y += nodeView.getY();
//            } else {
//                TreeTransitionView transitionView = (TreeTransitionView) elementView;
//                avg.x += transitionView.getEndX();
//                avg.y += transitionView.getEndY();
//            }
//        }
//        avg.x /= selectedViews.size();
//        avg.y /= selectedViews.size();
//
//        Point pos =  viewport.getViewPosition();
//        if(pos.x < avg.x) {
//            pos.x = avg.x - pos.x;
//        }
//        if(pos.y < avg.y) {
//            pos.y = avg.y - pos.y;
//        }
//
//        viewport.setViewPosition(pos);

        repaint();
    }

    /**
     * Called when the model has finished updating the tree.
     */
    @Override
    public void onUpdateTree() {
        repaint();
    }

    /**
     * Gets the TreeElementView by the corresponding TreeElement associated with it
     *
     * @param element TreeElement of the view
     * @return TreeElementView of the TreeElement associated with it
     */
    public TreeElementView getElementView(TreeElement element) {
        return viewMap.get(element);
    }

    private void removeTreeNode(TreeNode node) {
        viewMap.remove(node);
        node.getChildren().forEach(t -> removeTreeTransition(t));
    }

    private void removeTreeTransition(TreeTransition trans) {
        viewMap.remove(trans);
        if (trans.getChildNode() != null) {
            removeTreeNode(trans.getChildNode());
        }
    }

    private void addTreeNode(TreeNode node) {
        TreeTransition parent = node.getParent();

        TreeNodeView nodeView = new TreeNodeView(node);
        TreeTransitionView parentView = (TreeTransitionView) viewMap.get(parent);

        nodeView.setParentView(parentView);
        parentView.setChildView(nodeView);

        viewMap.put(node, nodeView);

        if (!node.getChildren().isEmpty()) {
            node.getChildren().forEach(t -> addTreeTransition(t));
        }
    }

    private void addTreeTransition(TreeTransition trans) {
        List<TreeNode> parents = trans.getParents();

        TreeTransitionView transView = new TreeTransitionView(trans);
        for (TreeNode parent : parents) {
            TreeNodeView parentNodeView = (TreeNodeView) viewMap.get(parent);
            transView.addParentView(parentNodeView);
            parentNodeView.addChildrenView(transView);
        }

        viewMap.put(trans, transView);

        if (trans.getChildNode() != null) {
            addTreeNode(trans.getChildNode());
        }
    }

    ///New Draw Methods

    public void drawTree(Graphics2D graphics2D) {
        if (tree == null) {
            LOGGER.error("Unable to draw tree.");
        } else {
            if (rootNodeView == null) {
                rootNodeView = new TreeNodeView(tree.getRootNode());

                LOGGER.debug("Creating new views for tree view.");
                createViews(rootNodeView);

                selection.newSelection(rootNodeView);
            }

            dimension = new Dimension(0, 0);
            calcSpan(rootNodeView);
            rootNodeView.setSpan(rootNodeView.getSpan() + DIAMETER + BORDER_SPACING);

            calculateViewLocations(rootNodeView, 0);
            dimension.height = (int) rootNodeView.getSpan();

            redrawTree(graphics2D, rootNodeView);
            LOGGER.debug("DrawTree: dimensions - " + dimension.width + "x" + dimension.height);
        }
    }

    public void createViews(TreeNodeView nodeView) {
        if (nodeView != null) {
            viewMap.put(nodeView.getTreeElement(), nodeView);

            TreeNode node = nodeView.getTreeElement();
            for (TreeTransition trans : node.getChildren()) {
                TreeTransitionView transView = (TreeTransitionView) viewMap.get(trans);
                if(transView != null) {
                    nodeView.addChildrenView(transView);
                    transView.addParentView(nodeView);
                    break;
                }
                transView = new TreeTransitionView(trans);

                viewMap.put(transView.getTreeElement(), transView);

                transView.addParentView(nodeView);
                nodeView.addChildrenView(transView);

                TreeNode childNode = trans.getChildNode();
                if (childNode != null) {
                    TreeNodeView childNodeView = new TreeNodeView(childNode);
                    viewMap.put(childNodeView.getTreeElement(), childNodeView);

                    childNodeView.setParentView(transView);
                    transView.setChildView(childNodeView);

                    createViews(childNodeView);
                }
            }
        }
    }

    public void calculateViewLocations(TreeNodeView nodeView, int depth) {
        nodeView.setDepth(depth);
        int xLoc = (NODE_GAP_WIDTH + DIAMETER) * depth + DIAMETER;
        nodeView.setX(xLoc);
        dimension.width = Math.max(dimension.width, xLoc);

        TreeTransitionView parentTransView = nodeView.getParentView();
        int yLoc = parentTransView == null ? (int) nodeView.getSpan() / 2 : parentTransView.getEndY();
        nodeView.setY(yLoc);

        ArrayList<TreeTransitionView> children = nodeView.getChildrenViews();
        switch (children.size()) {
            case 0:
                break;
            case 1: {
                TreeTransitionView childView = children.get(0);

                List<TreeNodeView> parentsViews = childView.getParentViews();
                if (parentsViews.size() == 1) {
                    childView.setEndY(yLoc);

                    childView.setDepth(depth);

                    Point lineStartPoint = childView.getLineStartPoint(0);
                    lineStartPoint.x = xLoc + RADIUS + TRANS_GAP / 2;
                    lineStartPoint.y = yLoc;
                    childView.setEndX((NODE_GAP_WIDTH + DIAMETER) * (depth + 1) + RADIUS - TRANS_GAP / 2);

                    dimension.width = Math.max(dimension.width, childView.getEndX());

                    TreeNodeView childNodeView = childView.getChildView();
                    if (childNodeView != null) {
                        calculateViewLocations(childNodeView, depth + 1);
                    }
                } else if (parentsViews.size() > 1 && parentsViews.get(parentsViews.size() - 1) == nodeView) {
                    int yAvg = 0;
                    for (int i = 0; i < parentsViews.size(); i++) {
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

                    dimension.width = Math.max(dimension.width, childView.getEndX());

                    TreeNodeView childNodeView = childView.getChildView();
                    if (childNodeView != null) {
                        calculateViewLocations(childNodeView, depth + 1);
                    }
                }
                break;
            }
            default: {
                int span = 0;
                for (TreeTransitionView childView : children) {
                    span += childView.getSpan();
                }

                span = (int) ((nodeView.getSpan() - span) / 2);
                for (int i = 0; i < children.size(); i++) {
                    TreeTransitionView childView = children.get(i);

                    childView.setDepth(depth);

                    Point lineStartPoint = childView.getLineStartPoint(0);
                    lineStartPoint.x = xLoc + RADIUS + TRANS_GAP / 2;
                    lineStartPoint.y = yLoc;
                    childView.setEndX((NODE_GAP_WIDTH + DIAMETER) * (depth + 1) + RADIUS - TRANS_GAP / 2);
                    childView.setEndY(yLoc - (int) (nodeView.getSpan() / 2) + span + (int) (childView.getSpan() / 2));

                    span += childView.getSpan();
                    TreeNodeView childNodeView = childView.getChildView();
                    if (childNodeView != null) {
                        calculateViewLocations(childNodeView, depth + 1);
                    }
                }
                break;
            }
        }
    }

    public void calcSpan(TreeElementView view) {
        if (view.getType() == NODE) {
            TreeNodeView nodeView = (TreeNodeView) view;
            TreeNode node = nodeView.getTreeElement();
            if (nodeView.getChildrenViews().size() == 0) {
                nodeView.setSpan(DIAMETER + NODE_GAP_HEIGHT);
            } else if (nodeView.getChildrenViews().size() == 1) {
                TreeTransitionView childView = nodeView.getChildrenViews().get(0);
                calcSpan(childView);
                if (childView.getParentViews().size() > 1) {
                    nodeView.setSpan(DIAMETER + NODE_GAP_HEIGHT);
                } else {
                    nodeView.setSpan(childView.getSpan());
                }
            } else {
                DisjointSets<TreeTransition> branches = node.findMergingBranches();
                List<TreeTransition> children = node.getChildren();

                if (node == children.get(0).getParents().get(0)) {
                    reorderBranches(node, branches);
                    ArrayList<TreeTransitionView> newChildrenViews = new ArrayList<>();
                    for (TreeTransition trans : node.getChildren()) {
                        newChildrenViews.add((TreeTransitionView) viewMap.get(trans));
                    }
                    nodeView.setChildrenViews(newChildrenViews);
                }

                List<Set<TreeTransition>> mergingSets = branches.getAllSets();

                double span = 0.0;
                for (Set<TreeTransition> mergeSet : mergingSets) {
                    if (mergeSet.size() > 1) {
                        TreeTransition mergePoint = TreeNode.findMergingPoint(mergeSet);
                        TreeTransitionView mergePointView = (TreeTransitionView) viewMap.get(mergePoint);
                        double subSpan = 0.0;
                        for (TreeTransition branch : mergeSet) {
                            TreeTransitionView branchView = (TreeTransitionView) viewMap.get(branch);
                            subCalcSpan(branchView, mergePointView);
                            subSpan += branchView.getSpan();
                        }
                        calcSpan(mergePointView);
                        span += Math.max(mergePointView.getSpan(), subSpan);
                    } else {
                        TreeTransition trans = mergeSet.iterator().next();
                        TreeTransitionView transView = (TreeTransitionView) viewMap.get(trans);
                        calcSpan(transView);
                        span += transView.getSpan();
                    }
                }
                nodeView.setSpan(span);
            }
        } else {
            TreeTransitionView transView = (TreeTransitionView) view;
            TreeNodeView nodeView = transView.getChildView();
            if (nodeView == null) {
                transView.setSpan(DIAMETER + NODE_GAP_HEIGHT);
            } else {
                calcSpan(nodeView);
                transView.setSpan(nodeView.getSpan());
            }
        }
    }

    /**
     * Calculates the sub span of a given sub tree rooted at the specified view and stops at the tree puzzleElement view
     * specified as stop. Stop tree puzzleElement is NOT included in the span calculation
     *
     * @param view
     * @param stop
     */
    private void subCalcSpan(TreeElementView view, TreeElementView stop) {
        //safe-guard for infinite loop
        if (view == stop) {
            return;
        }

        if (view.getType() == NODE) {
            TreeNodeView nodeView = (TreeNodeView) view;
            TreeNode node = nodeView.getTreeElement();
            if (nodeView.getChildrenViews().size() == 0) {
                nodeView.setSpan(DIAMETER + NODE_GAP_HEIGHT);
            } else if (nodeView.getChildrenViews().size() == 1) {
                TreeTransitionView childView = nodeView.getChildrenViews().get(0);
                if (childView == stop) {
                    nodeView.setSpan(DIAMETER + NODE_GAP_HEIGHT);
                } else {
                    subCalcSpan(childView, stop);
                    if (childView.getParentViews().size() > 1) {
                        nodeView.setSpan(DIAMETER + NODE_GAP_HEIGHT);
                    } else {
                        nodeView.setSpan(childView.getSpan());
                    }
                }
            } else {
                DisjointSets<TreeTransition> branches = node.findMergingBranches();
                List<TreeTransition> children = node.getChildren();

                if (node == children.get(0).getParents().get(0)) {
                    reorderBranches(node, branches);
                }

                List<Set<TreeTransition>> mergingSets = branches.getAllSets();

                double span = 0.0;
                for (Set<TreeTransition> mergeSet : mergingSets) {
                    if (mergeSet.size() > 1) {
                        TreeTransition mergePoint = TreeNode.findMergingPoint(mergeSet);
                        TreeTransitionView mergePointView = (TreeTransitionView) viewMap.get(mergePoint);
                        double subSpan = 0.0;
                        for (TreeTransition branch : mergeSet) {
                            TreeTransitionView branchView = (TreeTransitionView) viewMap.get(branch);
                            subCalcSpan(branchView, mergePointView);
                            subSpan += branchView.getSpan();
                        }
                        subCalcSpan(mergePointView, stop);
                        span += Math.max(mergePointView.getSpan(), subSpan);
                    } else {
                        TreeTransition trans = mergeSet.iterator().next();
                        TreeTransitionView transView = (TreeTransitionView) viewMap.get(trans);
                        subCalcSpan(transView, stop);
                        span += transView.getSpan();
                    }
                }

                nodeView.setSpan(span);
            }
        } else {
            TreeTransitionView transView = (TreeTransitionView) view;
            TreeNodeView nodeView = transView.getChildView();
            if (nodeView == null || nodeView == stop) {
                transView.setSpan(DIAMETER + NODE_GAP_HEIGHT);
            } else {
                calcSpan(nodeView);
                transView.setSpan(nodeView.getSpan());
            }
        }
    }

    /**
     * Reorders branches such that merging branches are sequentially grouped together and transitions are kept in
     * relative order in the list of child transitions of the specified node
     *
     * @param node     root node of the branches
     * @param branches DisjointSets of the child branches of the specified node which determine which branches merge
     */
    private void reorderBranches(TreeNode node, DisjointSets<TreeTransition> branches) {
        List<TreeTransition> children = node.getChildren();
        List<Set<TreeTransition>> mergingSets = branches.getAllSets();

        List<List<TreeTransition>> newOrder = new ArrayList<>();
        for (Set<TreeTransition> set : mergingSets) {
            List<TreeTransition> mergeBranch = new ArrayList<>();
            newOrder.add(mergeBranch);
            children.forEach(t ->
            {
                if (set.contains(t)) {
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
            for (TreeTransition t1 : b1) {
                int curIndex = children.indexOf(t1);
                if (low1 == -1 || curIndex < low1) {
                    low1 = curIndex;
                }
            }
            for (TreeTransition t1 : b2) {
                int curIndex = children.indexOf(t1);
                if (low1 == -1 || curIndex < low1) {
                    low1 = curIndex;
                }
            }
            return low1 < low2 ? -1 : 1;
        });

        List<TreeTransition> newChildren = new ArrayList<>();
        newOrder.forEach(l -> newChildren.addAll(l));
        node.setChildren(newChildren);
    }
}