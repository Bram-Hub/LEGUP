package edu.rpi.legup.app;

import edu.rpi.legup.history.ICommand;
import edu.rpi.legup.history.ValidateBasicRuleCommand;
import edu.rpi.legup.history.ValidateContradictionRuleCommand;
import edu.rpi.legup.model.rules.BasicRule;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.rules.Rule;
import edu.rpi.legup.model.rules.RuleType;
import edu.rpi.legup.ui.treeview.TreeView;
import edu.rpi.legup.ui.treeview.TreeViewSelection;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static edu.rpi.legup.app.GameBoardFacade.getInstance;

public class PuzzleKeyAccelerator implements KeyListener {

    private Map<KeyStroke, Rule> keyStrokeMap;

    public PuzzleKeyAccelerator() {
        this.keyStrokeMap = new HashMap<>();
    }

    public void addKeyAccelerator(KeyStroke keyStroke, Rule rule) {
        keyStrokeMap.put(keyStroke, rule);
    }

    public Map<KeyStroke, Rule> getKeyStrokeMap() {
        return this.keyStrokeMap;
    }

    public void clearKeyMap() {
        this.keyStrokeMap.clear();
    }

    /**
     * Invoked when a key has been typed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key typed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * Invoked when a key has been pressed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key pressed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        KeyStroke keyStroke = KeyStroke.getKeyStrokeForEvent(e);
        Rule rule = keyStrokeMap.get(keyStroke);
        if (rule != null) {
            TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();

            String update = "";
            if (rule.getRuleType() == RuleType.CASE) {
//                handleCaseRule((CaseRule)rule);
            } else if (rule.getRuleType() == RuleType.CONTRADICTION) {
                TreeViewSelection selection = treeView.getSelection();

                ICommand validate = new ValidateContradictionRuleCommand(selection, (ContradictionRule) rule);
                if (validate.canExecute()) {
                    getInstance().getHistory().pushChange(validate);
                    validate.execute();
                } else {
                    update = validate.getError();
                }
            } else {
                TreeViewSelection selection = treeView.getSelection();

                ICommand validate = new ValidateBasicRuleCommand(selection, (BasicRule) rule);
                if (validate.canExecute()) {
                    getInstance().getHistory().pushChange(validate);
                    validate.execute();
                } else {
                    update = validate.getError();
                }
            }
            GameBoardFacade.getInstance().getLegupUI().getTreePanel().updateError(update);
        }
    }

    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {

    }
}
