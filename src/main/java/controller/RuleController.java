package controller;

import app.GameBoardFacade;
import model.gameboard.Board;
import model.rules.*;
import model.tree.*;
import ui.rulesview.RuleButton;
import ui.treeview.*;
import history.ICommand;
import history.ValidateBasicRuleCommand;
import history.ValidateContradictionRuleCommand;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import static app.GameBoardFacade.getInstance;

public class RuleController implements ActionListener
{
    protected Object lastSource;

    /**
     * RuleController Constructor - creates a controller object to listen
     * to ui events from a RulePanelView
     */
    public RuleController()
    {
        super();
    }

    /**
     * Button Pressed event - occurs a when a rule button has been pressed
     *
     * @param rule rule of the button that was pressed
     */
    public void buttonPressed(Rule rule)
    {
        Tree tree = GameBoardFacade.getInstance().getTree();
        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();

        String update = "";
        if(rule.getRuleType() == RuleType.CASE)
        {
            handleCaseRule((CaseRule)rule);
        }
        else if(rule.getRuleType() == RuleType.CONTRADICTION)
        {
            TreeViewSelection selection = treeView.getSelection();

            ICommand validate = new ValidateContradictionRuleCommand(selection, rule);
            if(validate.canExecute())
            {
                getInstance().getHistory().pushChange(validate);
                validate.execute();
            }
            else
            {
                update = validate.getExecutionError();
            }
        }
        else
        {
            TreeViewSelection selection = treeView.getSelection();

            ICommand validate = new ValidateBasicRuleCommand(selection, rule);
            if(validate.canExecute())
            {
                getInstance().getHistory().pushChange(validate);
                validate.execute();
            }
            else
            {
                update = validate.getExecutionError();
            }
        }
        GameBoardFacade.getInstance().getLegupUI().getTreePanel().updateError(update);
        GameBoardFacade.getInstance().getLegupUI().repaintBoard();
        GameBoardFacade.getInstance().getLegupUI().repaintTree();
    }

    /**
     * Handles the logic when a case rule has been pressed.
     *
     * @param caseRule case rule that is associated with the case rule button pressed
     */
    public void handleCaseRule(CaseRule caseRule)
    {
        Tree tree = GameBoardFacade.getInstance().getTree();
        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
        TreeViewSelection treeViewSelection = treeView.getSelection();
        List<TreeElementView> selection = treeViewSelection.getSelectedViews();
        if(selection.size() == 1)
        {
            TreeElementView elementView = treeViewSelection.getFirstSelection();
            TreeElement element = elementView.getTreeElement();
            if(element.getType() == TreeElementType.TRANSITION)
            {

            }
            else
            {
                TreeNode node = (TreeNode)element;
                TreeNodeView nodeView = (TreeNodeView)elementView;
                Board caseBoard = caseRule.getCaseBoard(node.getBoard());

                GameBoardFacade.getInstance().setBoard(caseBoard);
                GameBoardFacade.getInstance().getLegupUI().repaintTree();
            }
        }
    }

    public void handleContradictionRule(ContradictionRule rule)
    {
        Tree tree = GameBoardFacade.getInstance().getTree();
        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
        TreeViewSelection treeViewSelection = treeView.getSelection();
        List<TreeElementView> selection = treeViewSelection.getSelectedViews();
        if(selection.size() == 1)
        {
            TreeElementView elementView = treeViewSelection.getFirstSelection();
            TreeElement element = elementView.getTreeElement();
            if(element.getType() == TreeElementType.TRANSITION)
            {
                TreeTransition transition = (TreeTransition)element;
                if(transition.getChildNode() == null)
                {
                    transition.setRule(rule);
                    GameBoardFacade.getInstance().getLegupUI().repaintTree();
                }
            }
            else
            {
                TreeNode node = (TreeNode) element;
                TreeNodeView nodeView = (TreeNodeView) elementView;
                if(node.getChildren().size() == 0)
                {
                    TreeTransition transition = tree.addNewTransition(node);
                    transition.setRule(rule);
                    TreeTransitionView transitionView = treeView.addTransitionView(nodeView, transition);

                    tree.addNode(transition);
                    TreeNodeView newNodeView = transitionView.getChildView();
                    newNodeView.setTreeElement(transition.getChildNode());

                    GameBoardFacade.getInstance().setBoard(transition.getBoard());
                    GameBoardFacade.getInstance().getLegupUI().repaintTree();
                }
            }
        }
    }

    /**
     * ICommand Performed event - occurs when a rule button has been pressed
     *
     * @param e action event object
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        lastSource = e.getSource();
        RuleButton button = (RuleButton) lastSource;
        buttonPressed(button.getRule());
    }

    /**
     * Verifies that the selected tree node correctly uses the rule applied to it
     * This is called when a user presses a rule button on a selected node
     *
     * @param rule the rule to verify the selected node
     */
    public void verifySelected(Rule rule)
    {

    }
}
