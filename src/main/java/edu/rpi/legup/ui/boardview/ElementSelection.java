package edu.rpi.legup.ui.boardview;

import java.awt.*;
import java.util.ArrayList;

/**
 * ElementSelection manages the selection and hover states of ElementViews. It maintains a list of
 * selected elements, the currently hovered element, and the mouse point location.
 */
public class ElementSelection {
    private ArrayList<ElementView> selection;
    private ElementView hover;
    private Point mousePoint;

    public ElementSelection() {
        this.selection = new ArrayList<>();
        this.hover = null;
        this.mousePoint = null;
    }

    public ArrayList<ElementView> getSelection() {
        return selection;
    }

    public ElementView getFirstSelection() {
        return selection.size() == 0 ? null : selection.get(0);
    }

    /**
     * Toggles the selection state of an ElementView. If the ElementView is currently selected, it
     * is deselected. Otherwise, it is selected.
     *
     * @param elementView the ElementView to toggle
     */
    public void toggleSelection(ElementView elementView) {
        if (selection.contains(elementView)) {
            selection.remove(elementView);
            elementView.setSelected(false);
        } else {
            selection.add(elementView);
            elementView.setSelected(true);
        }
    }

    /**
     * Sets a new selection, clearing the previous selection and selecting the specified
     * ElementView.
     *
     * @param elementView the ElementView to select
     */
    public void newSelection(ElementView elementView) {
        clearSelection();
        selection.add(elementView);
        elementView.setSelected(true);
    }

    /** Clears the selection and deselects all ElementViews */
    public void clearSelection() {
        for (ElementView elementView : selection) {
            elementView.setSelected(false);
        }
        selection.clear();
    }

    public ElementView getHover() {
        return hover;
    }

    public void newHover(ElementView newHovered) {
        newHovered.setHover(true);
        if (hover != null) {
            hover.setHover(false);
        }
        hover = newHovered;
    }

    /** Clears the current hover state if there exists one */
    public void clearHover() {
        if (hover != null) {
            hover.setHover(false);
            hover = null;
        }
    }

    public Point getMousePoint() {
        return mousePoint;
    }

    public void setMousePoint(Point point) {
        this.mousePoint = point;
    }
}
