package edu.rpi.legup.ui.proofeditorui.treeview;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * {@code TreeViewSelection} manages the selection and hover state of tree element views in a tree
 * view. It maintains a list of selected views, tracks the currently hovered view, and manages the
 * mouse position.
 */
public class TreeViewSelection {
    private ArrayList<TreeElementView> selectedViews;
    private TreeElementView hover;
    private Point2D mousePoint;

    /** {@code TreeViewSelection} constructor creates a tree view selection. */
    public TreeViewSelection() {
        this.selectedViews = new ArrayList<>();
        this.hover = null;
        this.mousePoint = null;
    }

    /**
     * {@code TreeViewSelection} constructor creates a tree view selection with a selected view.
     *
     * @param view selected view
     */
    public TreeViewSelection(@NotNull TreeElementView view) {
        this();
        this.selectedViews.add(view);
    }

    /**
     * {@code TreeViewSelection} constructor creates a tree view selection with a list of selected views.
     *
     * @param views list of selected views
     */
    public TreeViewSelection(@NotNull List<TreeElementView> views) {
        this();
        this.selectedViews.addAll(views);
    }

    /**
     * Gets the list of selected tree element views.
     *
     * @return list of selected tree element views
     */
    public List<TreeElementView> getSelectedViews() { return selectedViews; }

    /**
     * Gets the first element view in the list of views.
     *
     * @return first element view in the list of views
     */
    public TreeElementView getFirstSelection() { return selectedViews.isEmpty() ? null : selectedViews.getFirst(); }

    /**
     * Toggles the selected state of an element view.
     *
     * @param treeElementView a tree element view to toggle
     */
    public void toggleSelection(@NotNull TreeElementView treeElementView) {
        if (selectedViews.contains(treeElementView)) {
            selectedViews.remove(treeElementView);
            treeElementView.setSelected(false);
        } else {
            selectedViews.add(treeElementView);
            treeElementView.setSelected(true);
        }
    }

    /**
     * Selects an element view.
     *
     * @param treeElementView a tree element view to select
     */
    public void addToSelection(@NotNull TreeElementView treeElementView) {
        if (!selectedViews.contains(treeElementView)) {
            selectedViews.add(treeElementView);
            treeElementView.setSelected(true);
        }
    }

    /**
     * Creates a new selection containing only the specified tree element view.
     *
     * @param treeElementView a tree element view to select
     */
    public void newSelection(@NotNull TreeElementView treeElementView) {
        clearSelection();
        selectedViews.add(treeElementView);
        treeElementView.setSelected(true);
    }

    /** Deselects all selected views. */
    public void clearSelection() {
        for (TreeElementView treeElementView : selectedViews) {
            treeElementView.setSelected(false);
        }
        selectedViews.clear();
    }

    /**
     * Gets tree element view that the mouse is hovering over or {@code null} is no such view exists.
     *
     * @return tree element view that the mouse is hovering over or {@code null} is no such view exists
     */
    public TreeElementView getHover() { return hover; }

    /**
     * Clears the previous hover and sets the specified tree puzzleElement view to the new hover.
     *
     * @param newHovered tree puzzleElement view for the new hover
     */
    public void newHover(@NotNull TreeElementView newHovered) {
        newHovered.setHover(true);
        if (hover != null) { hover.setHover(false); }
        hover = newHovered;
    }

    /** Clears the current hover tree element view. */
    public void clearHover() {
        if (hover != null) {
            hover.setHover(false);
            hover = null;
        }
    }

    /**
     * Gets the current mouse location relative to the tree view.
     *
     * @return the current mouse location relative to the tree view
     */
    public Point2D getMousePoint() { return mousePoint; }

    /**
     * Sets the current mouse location relative to the tree view.
     *
     * @param point the current mouse location relative to the tree view
     */
    public void setMousePoint(@Nullable Point2D point) { this.mousePoint = point; }

    /**
     * Copies the {@code TreeViewSelection}.
     *
     * @return a copy of this {@code TreeViewSelection}
     */
    public TreeViewSelection copy() {
        TreeViewSelection copy = new TreeViewSelection();
        copy.selectedViews = new ArrayList<>(selectedViews);
        copy.hover = hover;
        copy.mousePoint = mousePoint;
        return copy;
    }
}
