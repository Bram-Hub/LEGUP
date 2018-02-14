package ui.rulesview;

import model.rules.Rule;

import javax.swing.*;

public class RuleButton extends JButton
{
    private Rule rule;

    public RuleButton(Rule rule)
    {
        super(rule.getImageIcon());
        this.rule = rule;
    }

    public Rule getRule()
    {
        return rule;
    }

    public void setRule(Rule rule)
    {
        this.rule = rule;
    }
}
