package ui.treeview;

import model.rules.TreeElementType;

import java.util.ArrayList;

public class TreeSelection
{
    private ArrayList<TreeElementView> selection;

    public TreeSelection()
    {
        selection = new ArrayList<>();
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
}
