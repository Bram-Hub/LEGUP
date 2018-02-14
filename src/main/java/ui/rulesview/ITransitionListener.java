package ui.rulesview;

import model.gameboard.Board;

public interface ITransitionListener
{
    /**
     * The tree selection has changed
     *
     * @param board the new Selection
     */
    void treeSelectionChanged(Board board);
}
