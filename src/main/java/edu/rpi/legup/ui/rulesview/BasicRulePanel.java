package edu.rpi.legup.ui.rulesview;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.ImageIcon;

public class BasicRulePanel extends RulePanel {
    private static final Logger LOGGER = LogManager.getLogger(BasicRulePanel.class.getName());

    /**
     * BasicRulePanel Constructor creates a basic rule panel
     *
     * @param ruleFrame rule frame that this basic rule panel is contained in
     */
    BasicRulePanel(RuleFrame ruleFrame) {
        super(ruleFrame);
        this.icon = new ImageIcon(ClassLoader.getSystemClassLoader().getResource("edu/rpi/legup/images/Legup/Basic Rules.gif"));
        this.name = "Basic Rules";
        this.toolTip = "Basic Rules";
    }
}