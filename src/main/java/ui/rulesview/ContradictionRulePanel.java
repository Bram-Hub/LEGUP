package ui.rulesview;

import javax.swing.*;
import java.util.Vector;

public class ContradictionRulePanel extends RulePanel
{
    /**
     * ContradictionRulePanel Constructor - creates a ContradictionRulePanel
     *
     * @param ruleFrame rule frame that this ContradictionRulePanel is contained in
     */
    public ContradictionRulePanel(RuleFrame ruleFrame)
    {
        super(ruleFrame);
        this.icon = new ImageIcon("images/Contradiction Rules.gif");
        this.name = "Contradiction Rules";
        this.toolTip = "Contradiction Rules";
    }
}
