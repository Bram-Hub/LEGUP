package ui.rulesview;

import javax.swing.ImageIcon;

public class CaseRulePanel extends RulePanel
{
    /**
     * CaseRulePanel Constructor - creates a CaseRulePanel
     *
     * @param ruleFrame rule frame that this CaseRulePanel is contained in
     */
    public CaseRulePanel(RuleFrame ruleFrame)
    {
        super(ruleFrame);
        this.icon = new ImageIcon(ClassLoader.getSystemClassLoader().getResource("images/Legup/Case Rules.gif"));
        this.name = "Case Rules";
        this.toolTip = "Case Rules";
    }
}