package edu.rpi.legup.model.observer;

import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.tree.TreeElement;

/**
 * IBoardListener defines methods for receiving notifications about changes to the board, including
 * updates to tree elements, case boards, and puzzle elements.
 */
public interface IBoardListener {
    /**
     * Called when the tree element has changed.
     *
     * @param treeElement tree element
     */
    void onTreeElementChanged(TreeElement treeElement);

    /**
     * Called when the a case board has been added to the view.
     *
     * @param caseBoard case board to be added
     */
    void onCaseBoardAdded(CaseBoard caseBoard);

    /**
     * Called when a {@link PuzzleElement}'s data on the board has changed.
     *
     * @param puzzleElement puzzleElement that has changed
     */
    void onBoardDataChanged(PuzzleElement puzzleElement);
}
