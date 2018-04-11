package ui.rulesview;

import javax.swing.ImageIcon;

public class BasicRulePanel extends RulePanel
{
    /**
     * BasicRulePanel Constructor - creates a BasicRulePanel
     *
     * @param ruleFrame rule frame that this BasicRulePanel is contained in
     */
    public BasicRulePanel(RuleFrame ruleFrame)
    {
        super(ruleFrame);
        this.icon = new ImageIcon(ClassLoader.getSystemClassLoader().getResource("images/Legup/Basic Rules.gif"));
        this.name = "Basic Rules";
        this.toolTip = "Basic Rules";
    }
}