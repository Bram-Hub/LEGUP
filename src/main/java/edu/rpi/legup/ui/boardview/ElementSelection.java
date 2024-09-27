package edu.rpi.legup.ui.boardview;

import java.awt.*;
import java.util.ArrayList;

/**
 * ElementSelection manages the selection and hover states of ElementViews.
 * It maintains a list of selected elements, the currently hovered element, and the mouse point location.
 */
public class ElementSelection {
    private ArrayList<ElementView> selection;
    private ElementView hover;
    private Point mousePoint;

    /**
     * Constructs an ElementSelection instance with an empty selection and no hover or mouse point
     */
    public ElementSelection() {
        this.selection = new ArrayList<>();
        this.hover = null;
        this.mousePoint = null;
    }

    /**
     * Gets the list of currently selected ElementViews.
     *
     * @return the list of selected ElementViews
     */
    public ArrayList<ElementView> getSelection() {
        return selection;
    }

    /**
     * Gets the first ElementView in the selection, or null if the selection is empty.
     *
     * @return the first selected ElementView, or null if there are no selections
     */
    public ElementView getFirstSelection() {
        return selection.size() == 0 ? null : selection.get(0);
    }

    /**
     * Toggles the selection state of an ElementView.
     * If the ElementView is currently selected, it is deselected. Otherwise, it is selected.
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
     * Sets a new selection, clearing the previous selection and selecting the specified ElementView.
     *
     * @param elementView the ElementView to select
     */
    public void newSelection(ElementView elementView) {
        clearSelection();
        selection.add(elementView);
        elementView.setSelected(true);
    }

    /**
     * Clears the selection and deselects all ElementViews
     */
    public void clearSelection() {
        for (ElementView elementView : selection) {
            elementView.setSelected(false);
        }
        selection.clear();
    }

    /**
     * Gets the currently hovered ElementView.
     *
     * @return the currently hovered ElementView, or null if no element is hovered
     */
    public ElementView getHover() {
        return hover;
    }

    /**
     * Sets a new hovered ElementView, updating the hover state of the previous and new elements.
     *
     * @param newHovered the new ElementView to be hovered
     */
    public void newHover(ElementView newHovered) {
        newHovered.setHover(true);
        if (hover != null) {
            hover.setHover(false);
        }
        hover = newHovered;
    }

    /**
     * Clears the current hover state if there exists one
     */
    public void clearHover() {
        if (hover != null) {
            hover.setHover(false);
            hover = null;
        }
    }

    /**
     * Gets the current mouse point location.
     *
     * @return the current mouse point location
     */
    public Point getMousePoint() {
        return mousePoint;
    }

    /**
     * Sets the mouse point location.
     *
     * @param point the new mouse point location
     */
    public void setMousePoint(Point point) {
        this.mousePoint = point;
    }
}
