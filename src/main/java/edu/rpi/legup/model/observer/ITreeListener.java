package edu.rpi.legup.model.observer;

import edu.rpi.legup.model.tree.TreeElement;
import edu.rpi.legup.ui.proofeditorui.treeview.TreeViewSelection;

public interface ITreeListener {
  /**
   * Called when a {@link TreeElement} is added to the tree.
   *
   * @param treeElement tree element that was added to the tree
   */
  void onTreeElementAdded(TreeElement treeElement);

  /**
   * Called when a {@link TreeElement} is removed from the tree.
   *
   * @param element TreeElement that was removed to the tree
   */
  void onTreeElementRemoved(TreeElement element);

  /**
   * Called when the {@link TreeViewSelection} was changed.
   *
   * @param selection tree view selection that was changed
   */
  void onTreeSelectionChanged(TreeViewSelection selection);

  /** Called when the model has finished updating the tree. */
  void onUpdateTree();
}
