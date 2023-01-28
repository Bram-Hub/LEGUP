package edu.rpi.legup.ui.proofeditorui.rulesview;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.ImageIcon;

public class DirectRulePanel extends RulePanel {
    private static final Logger LOGGER = LogManager.getLogger(DirectRulePanel.class.getName());

    /**
     * DirectRulePanel Constructor creates a basic rule panel
     *
     * @param ruleFrame rule frame that this basic rule panel is contained in
     */
    DirectRulePanel(RuleFrame ruleFrame) {
        super(ruleFrame);
        this.icon = new ImageIcon(ClassLoader.getSystemClassLoader().getResource("edu/rpi/legup/images/Legup/Basic Rules.gif"));
        this.name = "Direct Rules";
        this.toolTip = "Direct Rules";
    }
}