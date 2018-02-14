package ui.rulesview;

import ui.Selection;

import java.util.ArrayList;

public interface ITreeSelectionListener
{
    /**
     * The tree selection has changed
     *
     * @param newSelection the new Selection
     */
    void treeSelectionChanged(ArrayList<Selection> newSelection);
}
