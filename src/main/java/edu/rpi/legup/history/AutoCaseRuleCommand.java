package edu.rpi.legup.history;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.rules.Rule;
import edu.rpi.legup.model.tree.*;
import edu.rpi.legup.ui.boardview.ElementView;
import edu.rpi.legup.ui.treeview.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static edu.rpi.legup.app.GameBoardFacade.getInstance;

public class AutoCaseRuleCommand extends PuzzleCommand
{
    private ElementView elementView;
    private TreeViewSelection selection;
    private Rule rule;

    private Map<TreeElement, Rule> oldRule;
    private Map<TreeElement, TreeElement> addNode;
    private CaseBoard caseBoard;

    /**
     * AutoCaseRuleCommand Constructor - creates a command for validating a case rule
     *
     * @param elementView currently selected edu.rpi.legup.puzzle puzzleElement view that is being edited
     * @param selection currently selected tree puzzleElement views that is being edited
     */
    public AutoCaseRuleCommand(ElementView elementView, TreeViewSelection selection, Rule rule, CaseBoard caseBoard)
    {
        this.elementView = elementView;
        this.selection = selection;
        this.rule = rule;
        this.caseBoard = caseBoard;
        this.oldRule = new HashMap<>();
        this.addNode = new HashMap<>();
    }

    /**
     * Executes an command
     */
    @Override
    public void executeCommand()
    {
        Tree tree = getInstance().getTree();
        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
        TreeViewSelection newSelection = new TreeViewSelection();

        List<TreeElementView> selectedViews = selection.getSelectedViews();
        for(TreeElementView view : selectedViews)
        {
            TreeElement element = view.getTreeElement();
            if(element.getType() == TreeElementType.NODE)
            {
                TreeNode node = (TreeNode)element;
                CaseRule caseRule = caseBoard.getCaseRule();
                List<Board> cases = caseRule.getCases(caseBoard.getBaseBoard(), elementView.getPuzzleElement());

                for(Board b: cases)
                {
                    TreeTransition transition = tree.addNewTransition(node);
                    b.setModifiable(false);
                    transition.setBoard(b);
                    transition.setRule(caseRule);

                    puzzle.notifyTreeListeners(listener -> listener.onTreeElementAdded(transition));

                    TreeNode n = tree.addNode(transition);
                    puzzle.notifyTreeListeners(listener -> listener.onTreeElementAdded(n));
                    newSelection.addToSelection(treeView.getElementView(n));
                }
            }
            else
            {
                TreeTransition transition = (TreeTransition)element;
                oldRule.put(transition, transition.getRule());

                transition.setRule(rule);

                TreeNode childNode = transition.getChildNode();
                if(childNode == null)
                {
                    childNode = (TreeNode)addNode.get(transition);
                    if(childNode == null)
                    {
                        childNode = new TreeNode(transition.getBoard().copy());
                        addNode.put(transition, childNode);
                    }
                    childNode.setParent(transition);
                    transition.setChildNode(childNode);

                    TreeNode finalChildNode = childNode;
                    puzzle.notifyTreeListeners(listener -> listener.onTreeElementAdded(finalChildNode));
                }
                newSelection.addToSelection(treeView.getElementView(childNode));
            }
        }
        TreeElementView firstSelectedView = selection.getFirstSelection();
        Board newBoardView;
        if(firstSelectedView.getType() == TreeElementType.NODE)
        {
            TreeNodeView nodeView = (TreeNodeView)firstSelectedView;
            newBoardView = nodeView.getChildrenViews().get(0).getTreeElement().getBoard();
        }
        else
        {
            TreeTransitionView transitionView = (TreeTransitionView)firstSelectedView;
            newBoardView = transitionView.getChildView().getTreeElement().getBoard();
        }
        puzzle.notifyBoardListeners(listener -> listener.onBoardChanged(newBoardView));
        puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(newSelection));
    }

    /**
     * Gets the reason why the command cannot be executed
     *
     * @return if command cannot be executed, returns reason for why the command cannot be executed,
     * otherwise null if command can be executed
     */
    @Override
    public String getErrorString()
    {
        if(selection.getSelectedViews().size() == 1) {
            TreeElement element = selection.getFirstSelection().getTreeElement();
            if(element.getType() == TreeElementType.NODE) {
                TreeNode node = (TreeNode)element;
                if(!node.getChildren().isEmpty()) {
                    return "Selected nodes must not have any children to be justified with a case rule.";
                }
            }
        } else {
            for(TreeElementView view : selection.getSelectedViews()) {
                TreeElement element = view.getTreeElement();
                if(element.getType() == TreeElementType.NODE) {
                    return "You cannot justify multiple nodes or a node and transitions at the same time.";
                }
            }
        }
        return null;
    }

    /**
     * Undoes an command
     */
    @Override
    public void undoCommand()
    {
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
        Tree tree = GameBoardFacade.getInstance().getTree();

        List<TreeElementView> selectedViews = selection.getSelectedViews();
        for(TreeElementView view : selectedViews)
        {
            TreeElement element = view.getTreeElement();
            if(element.getType() == TreeElementType.NODE)
            {
                TreeNode node = (TreeNode)element;
                while(!node.getChildren().isEmpty())
                {
                    TreeTransition transition = node.getChildren().get(0);
                    tree.removeTreeElement(transition);
                    puzzle.notifyTreeListeners(listener -> listener.onTreeElementRemoved(transition));
                }
            }
            else
            {
                TreeTransition transition = (TreeTransition)element;
                transition.setRule(oldRule.get(transition));
                TreeNode childNode = (TreeNode)addNode.get(transition);
                if(childNode != null)
                {
                    tree.removeTreeElement(childNode);
                    puzzle.notifyTreeListeners(listener -> listener.onTreeElementRemoved(childNode));
                }
            }
        }
        TreeElementView firstSelectedView = selection.getFirstSelection();
        Board newBoard = firstSelectedView.getTreeElement().getBoard();

        puzzle.notifyBoardListeners(listener -> listener.onBoardChanged(newBoard));
        puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(selection));
    }
}
