package edu.rpi.legup.ui.proofeditorui.treeview;

import edu.rpi.legup.controller.TreeController;
import edu.rpi.legup.model.observer.ITreeListener;
import edu.rpi.legup.model.tree.Tree;
import edu.rpi.legup.model.tree.TreeElement;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.ui.zoompane.ZoomView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.rpi.legup.model.tree.TreeElementType.NODE;
import static edu.rpi.legup.model.tree.TreeElementType.TRANSITION;

/**
 * The {@code TreeView} class provides a graphical representation of a {@code Tree} structure,
 * allowing interaction and visualization of tree elements, transitions, and selections. It extends
 * {@code ScrollView} and implements {@code ITreeListener} to respond to updates in the tree
 * structure.
 */
public class TreeView extends ZoomView implements ITreeListener {

    private Tree tree;
    private TreeNodeView rootNodeView;
    private final Map<TreeElement, TreeElementView> viewMap;
    private int maxDepth;

    private TreeViewSelection selection;

    /**
     * Constructs a {@code TreeView} with the specified {@code TreeController}.
     *
     * @param treeController the {@code TreeController} used to manage tree operations
     */
    public TreeView(@Nullable TreeController treeController) {
        super(treeController);

        viewMap = new HashMap<>();
        selection = new TreeViewSelection();
    }

    /**
     * Gets the current tree view selection.
     *
     * @return the {@code TreeViewSelection} object representing the current selection
     */
    public TreeViewSelection getSelection() { return selection; }

    /**
     * Gets the {@code TreeElementView} by the specified point or {@code null} if no view exists at the
     * specified point.
     *
     * @param point location to query for a view
     * @return {@code TreeElementView} at the point specified, otherwise {@code null}
     */
    public TreeElementView getTreeElementView(@NotNull Point2D point) {
        return getTreeElementView(point, rootNodeView);
    }

    /**
     * Recursively gets the {@code TreeElementView} by the specified point or {@code null} if no view exists
     * at the specified point or the view specified is null.
     *
     * @param point location to query for a view
     * @param elementView view to determine if the point is contained within it
     * @return {@code TreeElementView} at the point specified, otherwise {@code null}
     */
    private TreeElementView getTreeElementView(@NotNull Point2D point, @Nullable TreeElementView elementView) {
        if (elementView == null) { return null; }

        if (elementView.contains(point) && elementView.isVisible()) {
            if (elementView.getType() == NODE && ((TreeNodeView) elementView).isContradictoryState()) {
                return null;
            }
            return elementView;
        }
        else if (elementView.getType() == NODE) {
            for (TreeTransitionView transitionView : ((TreeNodeView) elementView).getChildViews()) {

                TreeElementView view = getTreeElementView(point, transitionView);
                if (view != null) { return view; }
            }
        }
        else {
            TreeTransitionView transitionView = (TreeTransitionView) elementView;
            return getTreeElementView(point, transitionView.getChildView());
        }

        return null;
    }

    /**
     * Updates the tree view with the specified {@code Tree}.
     *
     * @param tree the {@code Tree} to display in the view
     */
    public void setTree(@NotNull Tree tree) {
        if (tree != this.tree) {
            this.tree = tree;

            if (tree.getRootNode() != null) {
                addTreeNode(tree.getRootNode());
                rootNodeView = (TreeNodeView) viewMap.get(tree.getRootNode());
                selection.newSelection(rootNodeView);
                calculateSpans(rootNodeView);
                calculatePositions(rootNodeView);

                updateSize();
            }
            else { setSize(new Dimension()); }
        }
    }

    /**
     * Draws the tree view on the provided {@code Graphics} context.
     *
     * @param graphics the {@code Graphics} context to draw on
     */
    @Override
    public void draw(@NotNull Graphics graphics) {
        if (rootNodeView != null) {
            Graphics2D g = (Graphics2D) graphics.create();

            drawSubtree(g, rootNodeView);
            if (selection.getHover() != null) { drawRuleHover(g); }

            g.dispose();
        }
    }

    /**
     * Recursively redraws the tree starting from the specified node view.
     *
     * @param graphics2D the {@code Graphics2D} context to draw on
     * @param nodeView the {@code TreeNodeView} to start drawing from
     */
    private void drawSubtree(@NotNull Graphics2D graphics2D, @NotNull TreeNodeView nodeView) {
        for (TreeTransitionView transitionView : nodeView.getChildViews()) {
            if (transitionView.getParentViews().getFirst() == nodeView) { transitionView.draw(graphics2D); }
            if (transitionView.getChildView() != null) { drawSubtree(graphics2D, transitionView.getChildView()); }
        }
        nodeView.draw(graphics2D);
    }

    /**
     * When the user hovers over the transition, draws the corresponding rules image.
     *
     * @param graphics2D the graphics context to draw on
     */
    public void drawRuleHover(@NotNull Graphics2D graphics2D) {
        if (selection.getHover().getType() == TRANSITION
                && ((TreeTransitionView) selection.getHover()).getTreeElement().isJustified()) {

            TreeTransition transition = (TreeTransition) selection.getHover().getTreeElement();
            Point2D mousePoint = selection.getMousePoint();

            transition.getRule().getImageIcon().paintIcon(null, graphics2D,
                    (int) mousePoint.getX() + 25, (int) mousePoint.getY() - 50);
        }
    }

    /** Resets the view by clearing the current tree, root node view, and selection. */
    public void resetView() {
        this.tree = null;
        this.rootNodeView = null;
        this.selection.clearSelection();
        this.selection.clearHover();
    }

    @Override
    public void onTreeElementAdded(@NotNull TreeElement treeElement) {
        if (treeElement.getType() == NODE) { addTreeNode((TreeNode) treeElement); }
        else { addTreeTransition((TreeTransition) treeElement); }
        TreeElementView elementView = viewMap.get(treeElement);

        calculateSpans(elementView);
        propagateSpan(elementView);
        calculatePositions(rootNodeView);
        updateSize();

        repaint();
    }

    @Override
    public void onTreeElementRemoved(@NotNull TreeElement element) {
        TreeElementView parentView, elementView = viewMap.get(element);

        if (element.getType() == NODE) {
            TreeNodeView nodeView = (TreeNodeView) elementView;
            parentView = ((TreeNodeView) elementView).getParentView();
            removeTreeNode(nodeView);
            if (((TreeNode) element).isRoot()) { rootNodeView = null; }
        }
        else {
            TreeTransitionView transitionView = (TreeTransitionView) elementView;
            if (transitionView.getLayoutParentView() != null) { parentView = transitionView.getLayoutParentView(); }
            else { parentView = transitionView.getParentViews().getFirst(); }
            removeTreeTransition(transitionView);
        }

        propagateSpan(parentView);
        calculatePositions(rootNodeView);
        updateSize();

        repaint();
    }

    @Override
    public void onTreeSelectionChanged(@NotNull TreeViewSelection selection) {
        this.selection.getSelectedViews().forEach(v -> v.setSelected(false));
        selection.getSelectedViews().forEach(v -> v.setSelected(true));
        this.selection = selection;
        repaint();
    }

    /**
     * Gets the {@code TreeElementView} by the corresponding {@code TreeElement} associated with it.
     *
     * @param element {@code TreeElement} of the view
     * @return {@code TreeElementView} of the {@code TreeElement} associated with it
     */
    public TreeElementView getElementView(@NotNull TreeElement element) { return viewMap.get(element); }

    /**
     * Recursively removes the specified {@code TreeNodeView}.
     *
     * @param nodeView the {@code TreeNodeView} to be removed
     */
    private void removeTreeNode(@NotNull TreeNodeView nodeView) {
        while (!nodeView.getChildViews().isEmpty()) { removeTreeTransition(nodeView.getChildViews().getFirst()); }
        viewMap.remove(nodeView.getTreeElement());

        if (nodeView.getParentView() != null) {
            nodeView.getParentView().setChildView(null);
            nodeView.setParentView(null);
        }
        if (nodeView.getDepth() == maxDepth) { updateMaxDepth(); }
    }

    /**
     * Removes the specified {@code TreeTransitionView}.
     *
     * @param transitionView the {@code TreeTransitionView} to be removed
     */
    private void removeTreeTransition(@NotNull TreeTransitionView transitionView) {
        if (transitionView.getChildView() != null) { removeTreeNode(transitionView.getChildView()); }
        viewMap.remove(transitionView.getTreeElement());

        for (TreeNodeView parentView : transitionView.getParentViews()) { parentView.removeChildView(transitionView); }
        transitionView.setParentViews(new ArrayList<>());
        if (transitionView.getLayoutParentView() != null) {
            transitionView.getLayoutParentView().removeChildView(transitionView);
        }
        transitionView.setLayoutParentView(null);

        if (transitionView.getDepth() == maxDepth) { updateMaxDepth(); }
    }

    /**
     * Adds the specified {@code TreeNode} and its associated views.
     *
     * @param node the {@code TreeNode} to be added
     */
    private void addTreeNode(@NotNull TreeNode node) {
        TreeTransition parent = node.getParent();
        TreeNodeView nodeView = new TreeNodeView(node);

        if (parent != null) {
            TreeTransitionView parentView = (TreeTransitionView) viewMap.get(parent);
            parentView.setChildView(nodeView);
            nodeView.setParentView(parentView);

            int newDepth = parentView.getDepth() + 1;
            nodeView.setDepth(newDepth);
            maxDepth = Math.max(maxDepth, newDepth);
        }

        viewMap.put(node, nodeView);

        // Add transition children only once all their parent views have been created
        for (TreeTransition transition : node.getChildren()) {

            boolean allParentViewsExist = true;
            for (TreeNode parentNode : transition.getParents()) {

                if (parentNode != node && viewMap.get(parentNode) == null) {
                    allParentViewsExist = false;
                    break;
                }
            }

            if (allParentViewsExist) { addTreeTransition(transition); }
        }
    }

    /**
     * Adds the specified {@code TreeTransition} and its associated views.
     * <p>
     * If this transition is a merge, this operation will do nothing if any of {@code TreeNode} parents' views
     * have not yet been created. It will also take the additional step of assigning the new
     * {@code TreeTransitionView} to be the layout child of the deepest common ancestor of all of its parent
     * node views.
     *
     * @param transition The {@code TreeTransition} to be added
     */
    private void addTreeTransition(@NotNull TreeTransition transition) {

        TreeTransitionView transitionView;
        if (transition.getParents().size() > 1) {

            List<TreeNodeView> parentViews = new ArrayList<>();

            for (TreeNode parentNode : transition.getParents()) {
                TreeNodeView parentView = (TreeNodeView) viewMap.get(parentNode);

                if (parentView == null) { return; }
                else { parentViews.add(parentView); }
            }

            ArrayList<Integer> indexTotal = new ArrayList<>();
            ArrayList<TreeNodeView> ancestors = new ArrayList<>();

            // Find the deepest common ancestor of all elements of parentViews
            TreeNodeView currentNodeView = parentViews.getFirst();
            TreeTransitionView currentTransitionView;
            while ((currentTransitionView = currentNodeView.getParentView()) != null) {

                currentNodeView = currentTransitionView.getParentViews().getFirst();
                if (currentTransitionView.getParentViews().size() > 1) {
                    currentNodeView = currentTransitionView.getLayoutParentView();
                }

                indexTotal.addFirst(currentNodeView.getChildViews().indexOf(currentTransitionView));
                ancestors.addFirst(currentNodeView);
            }

            // Remove non-shared ancestors and add up indices to average for placement later
            for (int i = 1; i < parentViews.size(); ++i) {

                currentNodeView = parentViews.get(i);
                int j = 0;

                while ((currentTransitionView = currentNodeView.getParentView()) != null) {

                    currentNodeView = currentTransitionView.getParentViews().getFirst();
                    if (currentTransitionView.getParentViews().size() > 1) {
                        currentNodeView = currentTransitionView.getLayoutParentView();
                    }

                    if ((j = ancestors.indexOf(currentNodeView)) != -1) {
                        while (ancestors.size() > j + 1) {
                            ancestors.removeLast();
                            indexTotal.removeLast();
                        }

                        indexTotal.set(j, indexTotal.get(j) +
                                currentNodeView.getChildViews().indexOf(currentTransitionView));
                        break;
                    }
                }

                // All parent views to this point must have had same ancestor chain so no need to manually check
                while (--j > 0) { indexTotal.set(j, indexTotal.get(j) * (i + 1) / i); }
            }

            transitionView = new TreeTransitionView(transition, parentViews, ancestors.getLast());
            int depth = 0;
            for (TreeNodeView parentView : parentViews) {
                parentView.addChildView(transitionView);
                depth = Math.max(depth, parentView.getDepth());
            }
            transitionView.setDepth(depth + 1);
            ancestors.getLast().insertChildView((int) Math.round((double) indexTotal.getLast() /
                    parentViews.size()), transitionView);
        }
        else {
            TreeNodeView nodeView = (TreeNodeView) viewMap.get(transition.getParents().getFirst());
            transitionView = new TreeTransitionView(transition, nodeView);
            nodeView.addChildView(transitionView);
            transitionView.setDepth(nodeView.getDepth() + 1);
        }

        maxDepth = Math.max(maxDepth, transitionView.getDepth());
        viewMap.put(transition, transitionView);
        if (transition.getChildNode() != null) { addTreeNode(transition.getChildNode()); }
    }

    /**
     * Recursively calculates the spans of element views starting from the specified element view.
     *
     * @param elementView the element view to start calculating spans from
     */
    private void calculateSpans(@NotNull TreeElementView elementView) {

        if (elementView.getType() == NODE) {
            TreeNodeView nodeView = (TreeNodeView) elementView;
            if (nodeView.getChildViews().isEmpty()) { elementView.setSpan(getMinSpan()); }
            else {
                int span = 0;
                for (TreeTransitionView transitionView : nodeView.getChildViews()) {

                    if (transitionView.getParentViews().size() == 1 ||
                            transitionView.getLayoutParentView() == nodeView) {
                        calculateSpans(transitionView);
                        span += transitionView.getSpan();
                    }
                    else { span += getMinSpan(); }
                }
                nodeView.setSpan(span);
            }
        }
        else {
            TreeTransitionView transitionView = (TreeTransitionView) elementView;
            if (transitionView.getChildView() != null) {
                calculateSpans(transitionView.getChildView());
                transitionView.setSpan(Math.max(transitionView.getChildView().getSpan(), getMinSpan()));
            }
            else { transitionView.setSpan(getMinSpan()); }
        }
    }

    /**
     * Recursively propagates a span change in {@code elementView}'s children up the tree.
     *
     * @param elementView the element view to propagate a span change from
     */
    private void propagateSpan(@NotNull TreeElementView elementView) {

        if (elementView.getType() == NODE) {
            TreeNodeView nodeView = (TreeNodeView) elementView;
            int span = 0;

            for (TreeTransitionView transitionView : nodeView.getChildViews()) {
                if (transitionView.getParentViews().size() == 1 || transitionView.getLayoutParentView() == nodeView) {
                    span += transitionView.getSpan();
                }
            }
            nodeView.setSpan(Math.max(span, getMinSpan()));
            if (nodeView.getParentView() != null) { propagateSpan(nodeView.getParentView()); }
        }
        else {
            TreeTransitionView transitionView = (TreeTransitionView) elementView;
            if (transitionView.getChildView() != null) {
                transitionView.setSpan(transitionView.getChildView().getSpan());
            }
            else { transitionView.setSpan(getMinSpan()); }

            if (transitionView.getParentViews().size() == 1) {
                propagateSpan(transitionView.getParentViews().getFirst());
            }
            else { propagateSpan(transitionView.getLayoutParentView()); }
        }
    }

    /**
     * Recursively lays out the positions to draw the nodes and transitions starting from the specified node view.
     *
     * @param nodeView the {@code TreeNodeView} to start laying out from
     */
    private void calculatePositions(@NotNull TreeNodeView nodeView) {
        int xPos = getNodeX(nodeView.getDepth());
        nodeView.setX(xPos);

        int yPos = (nodeView.getParentView() == null) ? nodeView.getSpan() / 2 : nodeView.getParentView().getEndY();
        nodeView.setY(yPos);

        List<TreeTransitionView> childViews = nodeView.getChildViews();
        int spanPos = yPos - nodeView.getSpan() / 2;
        for (TreeTransitionView transitionView : childViews) {

            if (transitionView.getLayoutParentView() != nodeView) {
                transitionView.setTailStartPoint(
                        transitionView.getParentViews().indexOf(nodeView),
                        new Point(
                                xPos + getNodeWidth() / 2 + getGapWidth()
                                        + (UIManager.getInt("Tree.outlineWidth")
                                        + UIManager.getInt("Tree.transitionTailWeight")) / 2,
                                yPos
                        )
                );
            }
            if (transitionView.getParentViews().size() == 1 || transitionView.getLayoutParentView() == nodeView) {
                transitionView.setEndX(
                        getNodeX(transitionView.getDepth() + 1) - getNodeWidth() / 2 - getGapWidth()
                                - UIManager.getInt("Tree.outlineWidth") / 2
                );
                transitionView.setEndY(spanPos + transitionView.getSpan() / 2);
                spanPos += transitionView.getSpan();

                if (transitionView.getChildView() != null) { calculatePositions(transitionView.getChildView()); }
            }
        }
    }

    /** Updates size of the view based on the max depth of the tree and the span of the root node. */
    private void updateSize() {
        setSize(new Dimension(
                getNodeWidth() * (maxDepth / 2 + 1) + getNodeGapWidth() * ((maxDepth + 1) / 2),
                rootNodeView.getSpan()
        ));
    }

    /** Updates the max depth of the tree after an element has been removed. */
    private void updateMaxDepth() {
        int newMaxDepth = 0;
        for (TreeElementView elementView : viewMap.values()) {
            newMaxDepth = Math.max(elementView.getDepth(), newMaxDepth);
            if (newMaxDepth == maxDepth) { return; }
        }
        maxDepth = newMaxDepth;
    }

    /**
     * Gets the x coordinate of a node view based on its depth.
     *
     * @param depth the depth of the node view
     * @return the x coordinate to place the node view
     */
    private int getNodeX(int depth) {
        int nodeWidth = getNodeWidth();
        return (nodeWidth + getNodeGapWidth()) * (depth / 2) + (nodeWidth / 2);
    }

    /** @return the width in view coordinates of each node */
    private int getNodeWidth() {
        return UIManager.getInt("Tree.nodeRadius") * 2
                + UIManager.getInt("Tree.outlineWidth");
    }

    /** @return the width of the gap between nodes horizontally */
    private int getNodeGapWidth() {
        return UIManager.getInt("Tree.transitionTailGap")
                + UIManager.getInt("Tree.transitionHeadHeight")
                + (UIManager.getInt("Tree.transitionTailWeight") / 2)
                + UIManager.getInt("Tree.outlineWidth") + getGapWidth() * 2;
    }

    /** @return the width of the gap between connected nodes and transitions */
    private int getGapWidth() { return UIManager.getInt("Tree.horizontalGap"); }

    /**
     * Gets the minimum span of any {@code TreeElementView}.
     *
     * @return the minimum span
     */
    private int getMinSpan() { return UIManager.getInt("Tree.nodeRadius") * 2
            + UIManager.getInt("Tree.outlineWidth") + UIManager.getInt("Tree.verticalGap"); }
}
