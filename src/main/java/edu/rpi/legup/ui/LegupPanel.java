package edu.rpi.legup.ui;

import javax.swing.*;

public abstract class LegupPanel extends JPanel {
    /**
     * Alerts panel that it will be going visible now
     */

    protected final int TOOLBAR_ICON_SCALE = 40;
    public abstract void makeVisible();
}
