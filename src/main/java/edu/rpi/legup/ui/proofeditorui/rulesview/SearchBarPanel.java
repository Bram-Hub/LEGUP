package edu.rpi.legup.ui.proofeditorui.rulesview;

import javax.swing.*;

public class SearchBarPanel extends RulePanel {
    /**
     * SearchBarPanel Constructor creates a SearchBarPanel
     *
     * @param ruleFrame rule frame that this SearchBarPanel is contained in
     *     <p>This class is used to create a panel named "search bar"
     */
    SearchBarPanel(RuleFrame ruleFrame) {
        super(ruleFrame);
        this.icon =
                new ImageIcon(
                        ClassLoader.getSystemClassLoader()
                                .getResource("edu/rpi/legup/images/Legup/Zoom In.png"));
        this.name = "Search Rules";
        this.toolTip = "Search Rules";
    }
}
