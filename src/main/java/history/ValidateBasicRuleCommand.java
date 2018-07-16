package history;

import app.GameBoardFacade;
import model.Puzzle;
import model.gameboard.Board;
import model.observer.ITreeListener;
import model.rules.Rule;
import model.tree.*;
import ui.treeview.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ValidateBasicRuleCommand extends PuzzleCommand
{
    private TreeViewSelection selection;

    private HashMap<TreeElement, Rule> oldRules;
    private HashMap<TreeElement, TreeNode> addNode;
    private Rule newRule;

    public ValidateBasicRuleCommand(TreeViewSelection selection, Rule rule)
    {
        this.selection = selection.copy();
        this.newRule = rule;
        this.oldRules = new HashMap<>();
        this.addNode = new HashMap<>();
    }

    /**
     * Executes an command
     */
    @Override
    public void execute()
    {
        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
        TreeViewSelection selection = treeView.getTreeViewSelection();
        Tree tree = GameBoardFacade.getInstance().getTree();
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();

        List<TreeElementView> selectedViews = selection.getSelection();
        for(TreeElementView selectedView : selectedViews)
        {
            TreeElement element = selectedView.getTreeElement();
            if(element.getType() == TreeElementType.TRANSITION)
            {
                TreeTransition transition = (TreeTransition)element;
                TreeTransitionView transitionView = (TreeTransitionView)selectedView;
                oldRules.put(transition, transition.getRule());
                transition.setRule(newRule);

                if(transition.getChildNode() == null)
                {
                    TreeNode treeNode;
                    if(addNode.get(element) == null)
                    {
                        Board copyBoard = transition.getBoard().copy();
                        copyBoard.setModifiable(false);
                        treeNode = new TreeNode(copyBoard);
                        addNode.put(element, treeNode);
                        treeNode.setParent(transition);
                    }
                    else
                    {
                        treeNode = addNode.get(element);
                    }
                    transition.setChildNode(treeNode);

                    if(transitionView.getChildView() == null)
                    {
                        puzzle.notifyTreeListeners((ITreeListener listener) -> listener.onTreeElementAdded(treeNode));
                        //treeView.addNodeView(transitionView, treeNode);
                    }
                }
                else
                {
                    addNode.put(element, null);
                }
                selection.newSelection(transitionView.getChildView());
            }
            else
            {
                TreeNode node = (TreeNode)element;
                TreeNodeView nodeView = (TreeNodeView)selectedView;

                TreeTransitionView transitionView = nodeView.getChildrenViews().get(0);
                TreeTransition transition = transitionView.getTreeElement();
                oldRules.put(transition, transition.getRule());
                transition.setRule(newRule);

                if(transition.getChildNode() == null)
                {
                    TreeNode treeNode;
                    if(addNode.get(element) == null)
                    {
                        Board copyBoard = transition.getBoard().copy();
                        copyBoard.setModifiable(false);
                        treeNode = new TreeNode(copyBoard);
                        addNode.put(element, treeNode);
                        treeNode.setParent(transition);
                    }
                    else
                    {
                        treeNode = addNode.get(element);
                    }
                    transition.setChildNode(treeNode);

                    if(transitionView.getChildView() == null)
                    {
                        puzzle.notifyTreeListeners((ITreeListener listener) -> listener.onTreeElementAdded(treeNode));
                        //treeView.addNodeView(transitionView, treeNode);
                    }
                }
                else
                {
                    addNode.put(element, null);
                }
                selection.newSelection(transitionView.getChildView());
            }
        }
        GameBoardFacade.getInstance().setBoard(selection.getFirstSelection().getTreeElement().getBoard());
        GameBoardFacade.getInstance().getLegupUI().repaintBoard();
        GameBoardFacade.getInstance().getLegupUI().repaintTree();
    }

    /**
     * Determines whether this command can be executed
     */
    @Override
    public boolean canExecute()
    {
        List<TreeElementView> selectedViews = selection.getSelection();
        for(TreeElementView view : selectedViews)
        {
            if(view.getType() == TreeElementType.NODE)
            {
                TreeNodeView nodeView = (TreeNodeView)view;
                TreeNode node = nodeView.getTreeElement();
                if(node.getChildren().size() != 1)
                {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Gets the reason why the command cannot be executed
     *
     * @return if command cannot be executed, returns reason for why the command cannot be executed,
     * otherwise null if command can be executed
     */
    @Override
    public String getExecutionError()
    {
        List<TreeElementView> selectedViews = selection.getSelection();
        for(TreeElementView view : selectedViews)
        {
            if(view.getType() == TreeElementType.NODE)
            {
                TreeNodeView nodeView = (TreeNodeView)view;
                TreeNode node = nodeView.getTreeElement();
                if(node.getChildren().size() == 1)
                {
                    return "Nodes must must have 1 children transition to be able to justified with a basic rule";
                }
            }
        }
        return null;
    }

    /**
     * Undoes an command
     */
    @Override
    public void undo()
    {
        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
        TreeViewSelection selection = treeView.getTreeViewSelection();
        Tree tree = GameBoardFacade.getInstance().getTree();

        List<TreeElementView> selectedViews = selection.getSelection();
        for(TreeElementView selectedView : selectedViews)
        {
            TreeElement element = selectedView.getTreeElement();
            if(element.getType() == TreeElementType.TRANSITION)
            {
                TreeTransition transition = (TreeTransition)element;
                TreeTransitionView transitionView = (TreeTransitionView)selectedView;
                transition.setRule(oldRules.get(element));

                if(addNode.get(element) != null)
                {
                    tree.removeTreeElement(transition.getChildNode());
                    treeView.removeTreeElement(transitionView.getChildView());
                }
            }
            else
            {
                TreeNode node = (TreeNode)element;
                TreeNodeView nodeView = (TreeNodeView)selectedView;

                TreeTransitionView transitionView = nodeView.getChildrenViews().get(0);
                TreeTransition transition = transitionView.getTreeElement();

                transition.setRule(oldRules.get(element));

                if(addNode.get(element) != null)
                {
                    tree.removeTreeElement(transition.getChildNode());
                    treeView.removeTreeElement(transitionView.getChildView());
                }
            }
        }
        selection.clearSelection();
        selection.getSelection().addAll(selectedViews);

        GameBoardFacade.getInstance().setBoard(selection.getFirstSelection().getTreeElement().getBoard());
        GameBoardFacade.getInstance().getLegupUI().repaintBoard();
        GameBoardFacade.getInstance().getLegupUI().repaintTree();
    }
}
