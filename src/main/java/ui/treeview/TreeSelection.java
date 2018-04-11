package ui.treeview;

import java.awt.*;
import java.util.ArrayList;

public class TreeSelection
{
    private ArrayList<TreeElementView> selection;
    private TreeElementView hover;
    private Point mousePoint;

    public TreeSelection()
    {
        this.selection = new ArrayList<>();
        this.hover = null;
        this.mousePoint = null;
    }

    public ArrayList<TreeElementView> getSelection()
    {
        return selection;
    }

    public TreeElementView getFirstSelection()
    {
        return selection.size() == 0 ? null : selection.get(0);
    }

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

    public void addToSelection(TreeElementView treeElementView)
    {
        if(!selection.contains(treeElementView))
        {
            selection.add(treeElementView);
            treeElementView.setSelected(true);
        }
    }

    public void newSelection(TreeElementView treeElementView)
    {
        clearSelection();
        selection.add(treeElementView);
        treeElementView.setSelected(true);
    }

    public void clearSelection()
    {
        for(TreeElementView treeElementView : selection)
        {
            treeElementView.setSelected(false);
        }
        selection.clear();
    }

    public TreeElementView getHover()
    {
        return hover;
    }

    public void newHover(TreeElementView newHovered)
    {
        newHovered.setHover(true);
        if(hover != null)
        {
            hover.setHover(false);
        }
        hover = newHovered;
    }

    public void clearHover()
    {
        if(hover != null)
        {
            hover.setHover(false);
            hover = null;
        }
    }

    public Point getMousePoint()
    {
        return mousePoint;
    }

    public void setMousePoint(Point point)
    {
        this.mousePoint = point;
    }
}
