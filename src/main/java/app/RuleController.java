package app;

import model.rules.Rule;
import model.rules.Tree;
import ui.rulesview.RuleButton;
import ui.rulesview.RuleFrame;
import ui.rulesview.RulePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
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
        tree.verifySelected(rule);
        GameBoardFacade.getInstance().getLegupUI().repaintTree();
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
}
