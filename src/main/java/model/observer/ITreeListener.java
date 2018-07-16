package model.observer;

import model.tree.TreeElement;
import ui.treeview.TreeViewSelection;

public interface ITreeListener
{
    /**
     * Called when a tree element is added to the tree
     *
     * @param element TreeElement that was added to the tree
     */
    void onTreeElementAdded(TreeElement element);

    /**
     * Called when a tree element is removed from the tree
     *
     * @param element TreeElement that was removed to the tree
     */
    void onTreeElementRemoved(TreeElement element);

    /**
     * Called when the tree selection was changed
     *
     * @param selection tree selection that was changed
     */
    void onTreeSelectionChanged(TreeViewSelection selection);
}
