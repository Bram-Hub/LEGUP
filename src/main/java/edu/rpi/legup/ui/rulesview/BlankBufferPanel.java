package edu.rpi.legup.ui.rulesview;

import javax.swing.*;

public class BlankBufferPanel extends RulePanel {
    /**
     *Creates a blank rule panel for spacing issues
     */
    BlankBufferPanel(RuleFrame ruleFrame) {
        super(ruleFrame);

        this.name = "                                          ";
        this.toolTip = "                                      ";
    }
}
