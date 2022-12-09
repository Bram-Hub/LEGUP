package edu.rpi.legup.ui.proofeditorui.rulesview;

import javax.swing.*;

public class SearchBarPanel extends RulePanel {
    /**
     * ContradictionRulePanel Constructor creates a ContradictionRulePanel
     *
     * @param ruleFrame rule frame that this ContradictionRulePanel is contained in
     */
    SearchBarPanel(RuleFrame ruleFrame) {
        super(ruleFrame);
        this.icon = new ImageIcon(ClassLoader.getSystemClassLoader().getResource("edu/rpi/legup/images/Legup/Zoom In.png"));
        this.name = "Search Rules";
        this.toolTip = "Search Rules";
    }
}
