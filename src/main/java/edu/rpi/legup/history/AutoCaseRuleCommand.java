package edu.rpi.legup.history;

import static edu.rpi.legup.app.GameBoardFacade.getInstance;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.*;
import edu.rpi.legup.ui.boardview.ElementView;
import edu.rpi.legup.ui.proofeditorui.treeview.*;
import java.awt.event.MouseEvent;
import java.util.*;

public class AutoCaseRuleCommand extends PuzzleCommand {

    private ElementView elementView;
    private TreeViewSelection selection;
    private CaseRule caseRule;
    private CaseBoard caseBoard;
    private MouseEvent mouseEvent;

    private List<TreeTransition> caseTrans;

    /**
     * AutoCaseRuleCommand Constructor creates a command for validating a case rule
     *
     * @param elementView currently selected puzzle puzzleElement view that is being edited
     * @param selection currently selected tree puzzleElement views that is being edited
     * @param caseRule currently selected caseRule puzzleElement view that is being edited
     * @param caseBoard currently selected caseBoard puzzleElement view that is being edited
     * @param mouseEvent currently selected mouseEvent puzzleElement view that is being edited
     */
    public AutoCaseRuleCommand(
            ElementView elementView,
            TreeViewSelection selection,
            CaseRule caseRule,
            CaseBoard caseBoard,
            MouseEvent mouseEvent) {
        this.elementView = elementView;
        this.selection = selection.copy();
        this.caseRule = caseRule;
        this.caseBoard = caseBoard;
        this.mouseEvent = mouseEvent;
        this.caseTrans = new ArrayList<>();
    }

    /** Executes an command */
    @Override
    public void executeCommand() {
        Tree tree = getInstance().getTree();
        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
        final TreeViewSelection newSelection = new TreeViewSelection();

        TreeNode node = (TreeNode) selection.getFirstSelection().getTreeElement();
        if (caseTrans.isEmpty()) {
            List<Board> cases =
                    caseRule.getCases(caseBoard.getBaseBoard(), elementView.getPuzzleElement());
            for (Board board : cases) {
                final TreeTransition transition = (TreeTransition) tree.addTreeElement(node);
                board.setModifiable(false);
                transition.setBoard(board);
                transition.setRule(caseRule);
                transition.setSelection(elementView.getPuzzleElement().copy());
                caseTrans.add(transition);

                TreeNode childNode = (TreeNode) tree.addTreeElement(transition);

                puzzle.notifyTreeListeners(listener -> listener.onTreeElementAdded(transition));
                newSelection.addToSelection(treeView.getElementView(childNode));
            }
        } else {
            for (final TreeTransition transition : caseTrans) {
                tree.addTreeElement(node, transition);
                TreeNode childNode = transition.getChildNode();
                puzzle.notifyTreeListeners(listener -> listener.onTreeElementAdded(transition));
                newSelection.addToSelection(treeView.getElementView(childNode));
            }
        }

        final TreeElement finalTreeElement = node.getChildren().get(0).getChildNode();
        puzzle.notifyBoardListeners(listener -> listener.onTreeElementChanged(finalTreeElement));
        puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(newSelection));
    }

    /**
     * Gets the reason why the command cannot be executed
     *
     * @return if command cannot be executed, returns reason for why the command cannot be executed,
     *     otherwise null if command can be executed
     */
    @Override
    public String getErrorString() {
        if (selection.getSelectedViews().size() != 1) {
            return CommandError.ONE_SELECTED_VIEW.toString();
        }

        TreeElementView treeElementView = selection.getFirstSelection();
        if (treeElementView.getType() != TreeElementType.NODE) {
            return CommandError.SELECTION_CONTAINS_TRANSITION.toString();
        }

        TreeNodeView nodeView = (TreeNodeView) treeElementView;
        if (!nodeView.getChildrenViews().isEmpty()) {
            return CommandError.NO_CHILDREN.toString();
        }

        if (!caseBoard.isPickable(elementView.getPuzzleElement(), mouseEvent)) {
            return "The selected data element is not pickable with this case rule.";
        }

        if (caseRule.getCases(caseBoard.getBaseBoard(), elementView.getPuzzleElement()).size()
                == 0) {
            return "The selection must produce at least one case";
        }

        int numberOfCaseRules =
                caseRule.getCases(caseBoard.getBaseBoard(), elementView.getPuzzleElement()).size();
        System.out.println("Number of cases:" + numberOfCaseRules);
        if (numberOfCaseRules > caseRule.MAX_CASES) {
            return "The selection can produce a max of " + caseRule.MAX_CASES + " cases";
        }
        if (numberOfCaseRules < caseRule.MIN_CASES) {
            return "The selection must produce a minimum of " + caseRule.MIN_CASES + " cases";
        }

        return null;
    }

    /** Undoes an command */
    @Override
    public void undoCommand() {
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();

        TreeNode node = (TreeNode) selection.getFirstSelection().getTreeElement();

        for (Iterator<TreeTransition> it = node.getChildren().iterator(); it.hasNext(); ) {
            final TreeTransition finalTran = it.next();
            it.remove();
            puzzle.notifyTreeListeners(listener -> listener.onTreeElementRemoved(finalTran));
        }

        final TreeElement finalTreeElement = node;
        puzzle.notifyBoardListeners(listener -> listener.onTreeElementChanged(finalTreeElement));
        puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(selection));
    }
}
