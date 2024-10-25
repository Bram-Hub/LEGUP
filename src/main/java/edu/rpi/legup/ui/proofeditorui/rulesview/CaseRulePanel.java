package edu.rpi.legup.ui.proofeditorui.rulesview;

import javax.swing.ImageIcon;

/**
 * The {@code CaseRulePanel} class is a specialized panel that represents case rules
 * within a {@link RuleFrame}. It extends the {@link RulePanel} and provides
 * specific functionality and UI components related to case rules.
 * This class initializes with an icon and name that are specific to case rules.
 */
public class CaseRulePanel extends RulePanel {
    /**
     * CaseRulePanel Constructor creates a CaseRulePanel
     *
     * @param ruleFrame rule frame that this CaseRulePanel is contained in
     */
    CaseRulePanel(RuleFrame ruleFrame) {
        super(ruleFrame);
        this.icon =
                new ImageIcon(
                        ClassLoader.getSystemClassLoader()
                                .getResource("edu/rpi/legup/images/Legup/Case Rules.gif"));
        this.name = "Case Rules";
        this.toolTip = "Case Rules";
    }
}
