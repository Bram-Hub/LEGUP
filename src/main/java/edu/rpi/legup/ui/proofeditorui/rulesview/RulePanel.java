package edu.rpi.legup.ui.proofeditorui.rulesview;

import edu.rpi.legup.model.rules.Rule;
import edu.rpi.legup.ui.WrapLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class RulePanel extends JPanel {
    protected ImageIcon icon;
    protected String name;
    protected String toolTip;

    protected RuleButton[] ruleButtons;
    protected RuleFrame ruleFrame;
    protected List<? extends Rule> rules;

    /**
     * RulePanel Constructor creates a RulePanel
     *
     * @param ruleFrame rule frame that this RulePanel is contained in
     */
    public RulePanel(RuleFrame ruleFrame) {
        this.ruleFrame = ruleFrame;
        this.rules = new ArrayList<>();
        setLayout(new WrapLayout());
    }

    /**
     * Gets the rule rule buttons
     *
     * @return rule ruleButtons
     */
    public RuleButton[] getRuleButtons() {
        return ruleButtons;
    }

    /**
     * Sets the rules for this rule panel
     *
     * @param rules list of the rules
     */
    public void setRules(List<? extends Rule> rules) {
        this.rules = rules;
        clearButtons();

        ruleButtons = new RuleButton[rules.size()];

        for (int i = 0; i < rules.size(); i++) {
            Rule rule = rules.get(i);
            ruleButtons[i] = new RuleButton(rule);
            ruleFrame.getButtonGroup().add(ruleButtons[i]);

            ruleButtons[i].setToolTipText(rule.getRuleName() + ": " + rule.getDescription());
            ruleButtons[i].addActionListener(ruleFrame.getController());

            String rulename = rule.getRuleName();
            JLabel name_lable= new JLabel(rulename);
            name_lable.setBounds(120,120,150,30);
            name_lable.setVerticalTextPosition(SwingConstants.BOTTOM);
            name_lable.setLayout(new GridLayout(1,1));

            ruleButtons[i].setLayout(new BoxLayout(ruleButtons[i],BoxLayout.Y_AXIS));
            ruleButtons[i].add(name_lable);
            ruleButtons[i].setPreferredSize(new Dimension(150,150));
            //ruleButtons[i].setIcon(new ImageIcon("Desktop/pic" ));
            add(ruleButtons[i]);

        }
        revalidate();
    }

    /**
     * Sets the selection by the specified rule
     *
     * @param rule rule to set the selection to
     */
    public void setSelectionByRule(Rule rule) {
        if (ruleButtons != null) {
            for (int i = 0; i < ruleButtons.length; i++) {
                if (rules.get(i).equals(rule)) {
                    ruleButtons[i].setSelected(true);
                    break;
                }
            }
        }
    }

    /**
     * Clears the rule buttons off this panel
     */
    protected void clearButtons() {
        if (ruleButtons != null) {
            removeAll();
            for (int x = 0; x < ruleButtons.length; ++x) {
                ruleButtons[x].removeActionListener(ruleFrame.getController());
            }
        }
    }

    /**
     * Gets the rules associated with this panel
     *
     * @return the rules
     */
    public List<? extends Rule> getRules() {
        return rules;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getToolTip() {
        return toolTip;
    }

    public void setToolTip(String toolTip) {
        this.toolTip = toolTip;
    }
}