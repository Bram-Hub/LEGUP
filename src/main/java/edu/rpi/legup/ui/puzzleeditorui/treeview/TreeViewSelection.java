package edu.rpi.legup.ui.puzzleeditorui.treeview;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class TreeViewSelection {
    private ArrayList<TreeElementView> selectedViews;
    private TreeElementView hover;
    private Point mousePoint;

    /**
     * TreeViewSelection Constructor creates a tree view selection
     */
    public TreeViewSelection() {
        this.selectedViews = new ArrayList<>();
        this.hover = null;
        this.mousePoint = null;
    }

    /**
     * TreeViewSelection Constructor creates a tree view selection with a selected view
     *
     * @param view selected view
     */
    public TreeViewSelection(TreeElementView view) {
        this();
        this.selectedViews.add(view);
    }

    /**
     * TreeViewSelection Constructor creates a tree view selection with a list of selected views
     *
     * @param views list of selected views
     */
    public TreeViewSelection(List<TreeElementView> views) {
        this();
        this.selectedViews.addAll(views);
    }


    /**
     * Gets the list of selected tree puzzleElement views
     *
     * @return list of selected tree puzzleElement views
     */
    public List<TreeElementView> getSelectedViews() {
        return selectedViews;
    }

    /**
     * Gets the first selectedViews in the list of views
     *
     * @return first selectedViews in the list of views
     */
    public TreeElementView getFirstSelection() {
        return selectedViews.size() == 0 ? null : selectedViews.get(0);
    }

    /**
     * Toggles a tree puzzleElement view selectedViews
     *
     * @param treeElementView a tree puzzleElement view to toggle
     */
    public void toggleSelection(TreeElementView treeElementView) {
        if (selectedViews.contains(treeElementView)) {
            selectedViews.remove(treeElementView);
            treeElementView.setSelected(false);
        } else {
            selectedViews.add(treeElementView);
            treeElementView.setSelected(true);
        }
    }

    /**
     * Adds a tree puzzleElement view selectedViews
     *
     * @param treeElementView a tree puzzleElement view to add
     */
    public void addToSelection(TreeElementView treeElementView) {
        if (!selectedViews.contains(treeElementView)) {
            selectedViews.add(treeElementView);
            treeElementView.setSelected(true);
        }
    }

    /**
     * Creates a new selectedViews and add the specified tree puzzleElement view
     *
     * @param treeElementView tree puzzleElement view
     */
    public void newSelection(TreeElementView treeElementView) {
        clearSelection();
        selectedViews.add(treeElementView);
        treeElementView.setSelected(true);
    }

    /**
     * Clears all selected views
     */
    public void clearSelection() {
        for (TreeElementView treeElementView : selectedViews) {
            treeElementView.setSelected(false);
        }
        selectedViews.clear();
    }

    /**
     * Gets tree puzzleElement view that the mouse is hovering over or null is no such view exists
     *
     * @return tree puzzleElement view that the mouse is hovering over or null is no such view exists
     */
    public TreeElementView getHover() {
        return hover;
    }

    /**
     * Clears the previous hover and sets the specified tree puzzleElement view to the new hover
     *
     * @param newHovered tree puzzleElement view for the new hover
     */
    public void newHover(TreeElementView newHovered) {
        newHovered.setHover(true);
        if (hover != null) {
            hover.setHover(false);
        }
        hover = newHovered;
    }

    /**
     * Clears the current hover tree puzzleElement view
     */
    public void clearHover() {
        if (hover != null) {
            hover.setHover(false);
            hover = null;
        }
    }

    /**
     * Gets the current mouse location relative to the tree view
     *
     * @return the current mouse location relative to the tree view
     */
    public Point getMousePoint() {
        return mousePoint;
    }

    /**
     * Sets the current mouse location relative to the tree view
     *
     * @param point the current mouse location relative to the tree view
     */
    public void setMousePoint(Point point) {
        this.mousePoint = point;
    }

    /**
     * Copies the TreeViewSelection
     *
     * @return a copy of this TreeViewSelection
     */
    @SuppressWarnings("unchecked")
    public TreeViewSelection copy() {
        TreeViewSelection cpy = new TreeViewSelection();
        cpy.selectedViews = (ArrayList<TreeElementView>) selectedViews.clone();
        cpy.hover = hover;
        cpy.mousePoint = mousePoint;
        return cpy;
    }
}
