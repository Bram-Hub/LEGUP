package edu.rpi.legup.ui.rulesview;

import javax.swing.ImageIcon;

public class CaseRulePanel extends RulePanel {
    /**
     * CaseRulePanel Constructor creates a CaseRulePanel
     *
     * @param ruleFrame rule frame that this CaseRulePanel is contained in
     */
    CaseRulePanel(RuleFrame ruleFrame) {
        super(ruleFrame);
        this.icon = new ImageIcon(ClassLoader.getSystemClassLoader().getResource("edu/rpi/legup/images/Legup/Case Rules.gif"));
        this.name = "Case Rules";
        this.toolTip = "Case Rules";
    }
}