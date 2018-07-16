package ui.treeview;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class TreeViewSelection
{
    private ArrayList<TreeElementView> selection;
    private TreeElementView hover;
    private Point mousePoint;

    /**
     * TreeViewSelection Constructor - creates a tree view selection
     */
    public TreeViewSelection()
    {
        this.selection = new ArrayList<>();
        this.hover = null;
        this.mousePoint = null;
    }

    /**
     * Gets the list of selected tree element views
     *
     * @return list of selected tree element views
     */
    public List<TreeElementView> getSelection()
    {
        return selection;
    }

    /**
     * Gets the first selection in the list of views
     *
     * @return first selection in the list of views
     */
    public TreeElementView getFirstSelection()
    {
        return selection.size() == 0 ? null : selection.get(0);
    }

    /**
     * Toggles a tree element view selection
     *
     * @param treeElementView a tree element view to toggle
     */
    public void toggleSelection(TreeElementView treeElementView)
    {
        if(selection.contains(treeElementView))
        {
            selection.remove(treeElementView);
            treeElementView.setSelected(false);
        }
        else
        {
            selection.add(treeElementView);
            treeElementView.setSelected(true);
        }
    }

    /**
     * Adds a tree element view selection
     *
     * @param treeElementView a tree element view to add
     */
    public void addToSelection(TreeElementView treeElementView)
    {
        if(!selection.contains(treeElementView))
        {
            selection.add(treeElementView);
            treeElementView.setSelected(true);
        }
    }

    /**
     * Creates a new selection and add the specified tree element view
     *
     * @param treeElementView tree element view
     */
    public void newSelection(TreeElementView treeElementView)
    {
        clearSelection();
        selection.add(treeElementView);
        treeElementView.setSelected(true);
    }

    /**
     * Clears all selected views
     */
    public void clearSelection()
    {
        for(TreeElementView treeElementView : selection)
        {
            treeElementView.setSelected(false);
        }
        selection.clear();
    }

    /**
     * Gets tree element view that the mouse is hovering over or null is no such view exists
     *
     * @return tree element view that the mouse is hovering over or null is no such view exists
     */
    public TreeElementView getHover()
    {
        return hover;
    }

    /**
     * Clears the previous hover and sets the specified tree element view to the new hover
     *
     * @param newHovered tree element view for the new hover
     */
    public void newHover(TreeElementView newHovered)
    {
        newHovered.setHover(true);
        if(hover != null)
        {
            hover.setHover(false);
        }
        hover = newHovered;
    }

    /**
     * Clears the current hover tree element view
     */
    public void clearHover()
    {
        if(hover != null)
        {
            hover.setHover(false);
            hover = null;
        }
    }

    /**
     * Gets the current mouse location relative to the tree view
     *
     * @return the current mouse location relative to the tree view
     */
    public Point getMousePoint()
    {
        return mousePoint;
    }

    /**
     * Sets the current mouse location relative to the tree view
     *
     * @param point the current mouse location relative to the tree view
     */
    public void setMousePoint(Point point)
    {
        this.mousePoint = point;
    }

    /**
     * Copies the TreeViewSelection
     *
     * @return a copy of this TreeViewSelection
     */
    @SuppressWarnings("unchecked")
    public TreeViewSelection copy()
    {
        TreeViewSelection cpy = new TreeViewSelection();
        cpy.selection = (ArrayList<TreeElementView>) selection.clone();
        cpy.hover = hover;
        cpy.mousePoint = mousePoint;
        return cpy;
    }
}
