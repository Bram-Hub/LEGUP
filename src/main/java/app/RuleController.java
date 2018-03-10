package app;

import model.rules.*;
import ui.rulesview.RuleButton;
import ui.treeview.TreeElementView;
import ui.treeview.TreeSelection;
import ui.treeview.TreeTransitionView;
import ui.treeview.TreeView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
        if(rule.getRuleType() == RuleType.CASE)
        {
            handleCaseRule((CaseRule)rule);
        }
        else
        {
            TreeSelection treeSelection = treeView.getTreeSelection();
            ArrayList<TreeElementView> selection = treeSelection.getSelection();
            if(selection.size() == 1)
            {
                TreeElementView elementView = treeSelection.getFirstSelection();
                TreeElement element = elementView.getTreeElement();
                if(element.getType() == TreeElementType.TRANSITION)
                {
                    TreeTransition transition = (TreeTransition)element;
                    TreeTransitionView transitionView = (TreeTransitionView)elementView;
                    transition.setRule(rule);
                    if(transition.getChildNode() == null)
                    {
                        TreeNode treeNode = new TreeNode(transition.getBoard().copy());
                        transitionView.getChildView().setTreeElement(treeNode);
                        transitionView.getChildView().setVisible(true);
                    }
                }
                else
                {
                    TreeNode node = (TreeNode)element;
                    //TODO Write node stuff
                }
            }
        }
        GameBoardFacade.getInstance().getLegupUI().repaintTree();
    }

    /**
     * Handles the logic when a case rule has been pressed.
     *
     * @param caseRule case rule that is associated with the case rule button pressed
     */
    public void handleCaseRule(CaseRule caseRule)
    {

    }

    /**
     * Action Performed event - occurs when a rule button has been pressed
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
