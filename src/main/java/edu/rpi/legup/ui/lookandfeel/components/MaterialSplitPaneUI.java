package edu.rpi.legup.ui.lookandfeel.components;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneUI;

public class MaterialSplitPaneUI extends BasicSplitPaneUI {
    @Override
    public void installUI(JComponent c) {
        super.installUI(c);

        JSplitPane splitPane = (JSplitPane) c;
        splitPane.setOpaque(false);
        splitPane.setBorder(UIManager.getBorder("SplitPane.border"));
        splitPane.setBackground(UIManager.getColor("SplitPane.background"));
        splitPane.setDividerSize(UIManager.getInt("SplitPane.dividerSize"));
    }

    //    /**
    //     * Creates the default divider.
    //     *
    //     * @return the default divider
    //     */
    //    @Override
    //    public BasicSplitPaneDivider createDefaultDivider() {
    //        return new MaterialSplitPaneDivider(this);
    //    }
}
