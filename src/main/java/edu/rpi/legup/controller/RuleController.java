package edu.rpi.legup.controller;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.history.CaseRuleCommand;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.rules.*;
import edu.rpi.legup.model.tree.*;
import edu.rpi.legup.ui.rulesview.RuleButton;
import edu.rpi.legup.ui.treeview.*;
import edu.rpi.legup.history.ICommand;
import edu.rpi.legup.history.ValidateBasicRuleCommand;
import edu.rpi.legup.history.ValidateContradictionRuleCommand;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import static edu.rpi.legup.app.GameBoardFacade.getInstance;

public class RuleController implements ActionListener
{
    protected Object lastSource;

    /**
     * RuleController Constructor - creates a edu.rpi.legup.controller object to listen
     * to edu.rpi.legup.ui events from a RulePanelView
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
    }

    /**
     * Handles the logic when a case rule has been pressed.
     *
     * @param caseRule case rule that is associated with the case rule button pressed
     */
    public void handleCaseRule(CaseRule caseRule)
    {
        Tree tree = GameBoardFacade.getInstance().getTree();
        Puzzle puzzle = getInstance().getPuzzleModule();
        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
        TreeViewSelection treeViewSelection = treeView.getSelection();
        List<TreeElementView> selection = treeViewSelection.getSelectedViews();
        if(selection.size() == 1)
        {
            TreeElementView elementView = treeViewSelection.getFirstSelection();
            TreeElement element = elementView.getTreeElement();
            if(element.getType() == TreeElementType.TRANSITION)
            {
                CaseRuleCommand caseRuleCommand = new CaseRuleCommand(null, treeViewSelection, caseRule, null);
                if(caseRuleCommand.canExecute())
                {
                    caseRuleCommand.execute();
                    getInstance().getHistory().pushChange(caseRuleCommand);
                }
            }
            else
            {
                TreeNode node = (TreeNode)element;
                TreeNodeView nodeView = (TreeNodeView)elementView;
                CaseBoard caseBoard = caseRule.getCaseBoard(node.getBoard());

                caseBoard.setCaseRule(caseRule);
                puzzle.notifyBoardListeners(listener -> listener.onBoardChanged(caseBoard));
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
     * This is called when a edu.rpi.legup.user presses a rule button on a selected node
     *
     * @param rule the rule to verify the selected node
     */
    public void verifySelected(Rule rule)
    {

    }
}
