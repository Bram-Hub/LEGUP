package app;

import model.rules.*;
import model.tree.*;
import ui.rulesview.CaseRuleSelectionView;
import ui.rulesview.RuleButton;
import ui.treeview.*;

import java.awt.*;
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
                    treeSelection.newSelection(transitionView.getChildView());
                }
                else
                {
                    TreeNode node = (TreeNode)element;
                    TreeNodeView nodeView = (TreeNodeView)elementView;
                    if(node.getChildren().size() == 1)
                    {
                        TreeTransitionView transitionView = nodeView.getChildrenViews().get(0);
                        TreeTransition transition = transitionView.getTreeElement();
                        transition.setRule(rule);
                        if(transition.getChildNode() == null)
                        {
                            TreeNode treeNode = new TreeNode(transition.getBoard().copy());
                            transitionView.getChildView().setTreeElement(treeNode);
                            transitionView.getChildView().setVisible(true);
                        }
                        treeSelection.newSelection(transitionView.getChildView());
                    }
                }
            }
        }
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
        TreeSelection treeSelection = treeView.getTreeSelection();
        ArrayList<TreeElementView> selection = treeSelection.getSelection();
        if(selection.size() == 1)
        {
            TreeElementView elementView = treeSelection.getFirstSelection();
            TreeElement element = elementView.getTreeElement();
            if(element.getType() == TreeElementType.TRANSITION)
            {

            }
            else
            {
                TreeNode node = (TreeNode)element;
                TreeNodeView nodeView = (TreeNodeView)elementView;
                //ArrayList<Board> cases = caseRule.getCases(node.getBoard(), e);

                for(int i = 0; i < 9; i++)
                {
                    TreeTransition transition = tree.addNewTransition(node);
                    transition.setRule(caseRule);
                    TreeTransitionView transitionView = treeView.addNewTransitionView(nodeView, transition);
                }
                GameBoardFacade.getInstance().getLegupUI().repaintTree();
            }
        }
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
        GameBoardFacade.getInstance().getLegupUI().getRuleFrame().add(new CaseRuleSelectionView(button) ,BorderLayout.EAST);
        GameBoardFacade.getInstance().getLegupUI().getRuleFrame().revalidate();
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
