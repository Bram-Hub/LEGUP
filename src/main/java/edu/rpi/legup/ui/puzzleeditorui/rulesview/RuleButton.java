package edu.rpi.legup.ui.puzzleeditorui.rulesview;

import edu.rpi.legup.model.rules.Rule;

import javax.swing.*;

public class RuleButton extends JButton {
    private Rule rule;

    /**
     * RuleButton Constructor - creates a button for a rule
     *
     * @param rule rule to create the button
     */
    RuleButton(Rule rule) {
        super(rule.getImageIcon());
        this.rule = rule;
    }

    /**
     * Gets the Rule from this button
     *
     * @return Rule from this button
     */
    public Rule getRule() {
        return rule;
    }

    /**
     * Sets the Rule for this button
     *
     * @param rule Rule for this button
     */
    void setRule(Rule rule) {
        this.rule = rule;
    }
}
