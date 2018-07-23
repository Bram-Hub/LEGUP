package history;

import app.GameBoardFacade;
import model.Puzzle;
import model.gameboard.Board;
import model.gameboard.CaseBoard;
import model.rules.CaseRule;
import model.rules.Rule;
import model.tree.*;
import ui.boardview.BoardView;
import ui.boardview.ElementView;
import ui.treeview.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static app.GameBoardFacade.getInstance;

public class CaseRuleCommand extends PuzzleCommand
{
    private ElementView elementView;
    private TreeViewSelection selection;
    private Rule rule;

    private Map<TreeElement, Rule> oldRule;
    private Map<TreeElement, TreeElement> addNode;

    /**
     * CaseRuleCommand Constructor - creates a command for validating a case rule
     *
     * @param elementView currently selected puzzle element view that is being edited
     * @param selection currently selected tree element views that is being edited
     */
    public CaseRuleCommand(ElementView elementView, TreeViewSelection selection, Rule rule)
    {
        this.elementView = elementView;
        this.selection = selection;
        this.rule = rule;
        this.oldRule = new HashMap<>();
        this.addNode = new HashMap<>();
    }

    /**
     * Executes an command
     */
    @Override
    public void execute()
    {
        Tree tree = getInstance().getTree();
        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
        BoardView boardView = GameBoardFacade.getInstance().getLegupUI().getBoardView();
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
        TreeViewSelection newSelection = new TreeViewSelection();

        List<TreeElementView> selectedViews = selection.getSelectedViews();
        for(TreeElementView view : selectedViews)
        {
            TreeElement element = view.getTreeElement();
            if(element.getType() == TreeElementType.NODE)
            {
                TreeNode node = (TreeNode)element;
                CaseBoard caseBoard = (CaseBoard)boardView.getBoard();
                CaseRule caseRule = caseBoard.getCaseRule();
                List<Board> cases = caseRule.getCases(caseBoard.getBaseBoard(), elementView.getElement());

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
     * Determines whether this command can be executed
     */
    @Override
    public boolean canExecute()
    {
        for(TreeElementView view : selection.getSelectedViews())
        {
            TreeElement element = view.getTreeElement();
            if(element.getType() == TreeElementType.NODE)
            {
                TreeNode node = (TreeNode)element;
                if(!node.getChildren().isEmpty())
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
        if(selection.getSelectedViews().size() > 1)
        {
            return "Tree Selection must exactly 1 Tree Node";
        }
        return null;
    }

    /**
     * Undoes an command
     */
    @Override
    public void undo()
    {

    }
}
