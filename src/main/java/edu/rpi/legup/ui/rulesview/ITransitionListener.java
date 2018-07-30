package edu.rpi.legup.ui.rulesview;

import edu.rpi.legup.model.gameboard.Board;

public interface ITransitionListener
{
    /**
     * The tree selection has changed
     *
     * @param board the new Selection
     */
    void treeSelectionChanged(Board board);
}
