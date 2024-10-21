package edu.rpi.legup.ui.proofeditorui.rulesview;

import javax.swing.*;

/**
 * The {@code ContradictionRulePanel} class is a specialized panel that represents contradiction
 * rules within a {@link RuleFrame}. It extends the {@link RulePanel} and provides specific
 * functionality and UI components related to contradiction rules. This class initializes with an
 * icon and name that are specific to contradiction rules.
 */
public class ContradictionRulePanel extends RulePanel {
    /**
     * ContradictionRulePanel Constructor creates a ContradictionRulePanel
     *
     * @param ruleFrame rule frame that this ContradictionRulePanel is contained in
     */
    ContradictionRulePanel(RuleFrame ruleFrame) {
        super(ruleFrame);
        this.icon =
                new ImageIcon(
                        ClassLoader.getSystemClassLoader()
                                .getResource("edu/rpi/legup/images/Legup/Contradictions.gif"));
        this.name = "Contradiction Rules";
        this.toolTip = "Contradiction Rules";
    }
}
