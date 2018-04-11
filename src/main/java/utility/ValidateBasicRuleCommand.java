package utility;

import app.GameBoardFacade;
import model.rules.Rule;
import model.tree.*;
import ui.treeview.*;

import java.util.ArrayList;
import java.util.HashMap;

public class ValidateBasicRuleCommand extends PuzzleCommand
{
    private ArrayList<TreeElementView> selectedViews;

    private HashMap<TreeElement, Rule> oldRules;
    private HashMap<TreeElement, TreeNode> addNode;
    private Rule newRule;

    public ValidateBasicRuleCommand(ArrayList<TreeElementView> selectedViews, Rule rule)
    {
        this.selectedViews = (ArrayList<TreeElementView>)selectedViews.clone();
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
        TreeSelection selection = treeView.getTreeSelection();
        Tree tree = GameBoardFacade.getInstance().getTree();

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
                        treeNode = new TreeNode(transition.getBoard().copy());
                        addNode.put(element, treeNode);
                        treeNode.addParent(transition);
                    }
                    else
                    {
                        treeNode = addNode.get(element);
                    }
                    transition.setChildNode(treeNode);

                    transitionView.getChildView().setTreeElement(treeNode);
                    transitionView.getChildView().setVisible(true);
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
                        treeNode = new TreeNode(transition.getBoard().copy());
                        addNode.put(element, treeNode);
                        treeNode.addParent(transition);
                    }
                    else
                    {
                        treeNode = addNode.get(element);
                    }
                    transition.setChildNode(treeNode);

                    transitionView.getChildView().setTreeElement(treeNode);
                    transitionView.getChildView().setVisible(true);
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
        TreeSelection selection = treeView.getTreeSelection();
        Tree tree = GameBoardFacade.getInstance().getTree();

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
                    transitionView.getChildView().setTreeElement(null);
                    transitionView.getChildView().setVisible(false);
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
                    transitionView.getChildView().setTreeElement(null);
                    transitionView.getChildView().setVisible(false);
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
