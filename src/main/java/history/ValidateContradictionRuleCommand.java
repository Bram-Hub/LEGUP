package history;

import app.GameBoardFacade;
import model.rules.Rule;
import model.tree.*;
import ui.treeview.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ValidateContradictionRuleCommand extends PuzzleCommand
{
    private TreeViewSelection selection;

    private HashMap<TreeElement, Rule> oldRules;
    private HashMap<TreeNode, ArrayList<TreeTransition>> saveElements;
    private HashMap<TreeNodeView, ArrayList<TreeTransitionView>> saveElementViews;
    private Rule newRule;

    /**
     * ValidateContradictionRuleCommand Constructor - creates a puzzle command for validating a contradiction rule
     *
     * @param selection currently selected tree element views
     * @param rule contradiction rule to set to all of the tree elements
     */
    public ValidateContradictionRuleCommand(TreeViewSelection selection, Rule rule)
    {
        this.selection = selection;
        this.newRule = rule;
        this.oldRules = new HashMap<>();
        this.saveElements = new HashMap<>();
        this.saveElementViews = new HashMap<>();
    }

    /**
     * Executes an command
     */
    @Override
    public void execute()
    {
        Tree tree = GameBoardFacade.getInstance().getTree();
        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
        TreeViewSelection treeViewSelection = treeView.getTreeViewSelection();
        treeViewSelection.clearSelection();

        List<TreeElementView> selectedViews = selection.getSelection();
        for(TreeElementView view : selectedViews)
        {
            TreeElement element = view.getTreeElement();
            TreeNode node;
            TreeNodeView nodeView;
            if(element.getType() == TreeElementType.TRANSITION)
            {
                TreeTransition transition = (TreeTransition) element;
                TreeTransitionView treeTransitionView = (TreeTransitionView)view;
                node = transition.getParents().get(0);
                nodeView = treeTransitionView.getParentViews().get(0);
            }
            else
            {
                node = (TreeNode) element;
                nodeView = (TreeNodeView)view;
            }

            if(!node.getChildren().isEmpty())
            {
                ArrayList<TreeTransition> save = new ArrayList<>(node.getChildren());
                saveElements.put(node, save);

                ArrayList<TreeTransitionView> saveView = new ArrayList<>(nodeView.getChildrenViews());
                saveElementViews.put(nodeView, saveView);
            }

            node.getChildren().clear();
            nodeView.getChildrenViews().clear();

            TreeTransition transition = tree.addNewTransition(node);
            TreeNode treeNode = new TreeNode(transition.getBoard().copy());
            transition.setChildNode(treeNode);
            treeNode.setParent(transition);

            transition.setRule(newRule);

            TreeTransitionView transitionView = treeView.addTransitionView(nodeView, transition);
            TreeNodeView newNodeView = treeView.addNodeView(transitionView, treeNode);

            treeViewSelection.addToSelection(transitionView);
        }
        GameBoardFacade.getInstance().setBoard(treeViewSelection.getFirstSelection().getTreeElement().getBoard());
        GameBoardFacade.getInstance().getLegupUI().repaintTree();
    }

    /**
     * Determines whether this command can be executed
     */
    @Override
    public boolean canExecute()
    {
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
        return null;
    }

    /**
     * Undoes an command
     */
    @Override
    public void undo()
    {
        Tree tree = GameBoardFacade.getInstance().getTree();
        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
        TreeViewSelection treeViewSelection = treeView.getTreeViewSelection();

        List<TreeElementView> selectedViews = selection.getSelection();
        for(TreeElementView view : selectedViews)
        {
            TreeElement element = view.getTreeElement();
            TreeNode node;
            TreeNodeView nodeView;
            if(element.getType() == TreeElementType.TRANSITION)
            {
                TreeTransition transition = (TreeTransition) element;
                TreeTransitionView treeTransitionView = (TreeTransitionView)view;
                node = transition.getParents().get(0);
                nodeView = treeTransitionView.getParentViews().get(0);
            }
            else
            {
                node = (TreeNode) element;
                nodeView = (TreeNodeView)view;
            }
            node.getChildren().clear();
            nodeView.getChildrenViews().clear();

            ArrayList<TreeTransition> save = saveElements.get(element);
            ArrayList<TreeTransitionView> saveView = saveElementViews.get(nodeView);

            if(save != null && !save.isEmpty())
            {
                node.getChildren().addAll(save);
                nodeView.getChildrenViews().addAll(saveView);
            }
        }
        treeViewSelection.clearSelection();
        treeViewSelection.getSelection().addAll(selectedViews);

        GameBoardFacade.getInstance().setBoard(treeViewSelection.getFirstSelection().getTreeElement().getBoard());
        GameBoardFacade.getInstance().getLegupUI().repaintTree();
    }
}
