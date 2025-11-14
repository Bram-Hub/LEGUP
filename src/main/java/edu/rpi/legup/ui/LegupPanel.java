package edu.rpi.legup.ui;

import javax.swing.*;

/**
 * An abstract base class for panels in the LEGUP application. This class extends {@link JPanel} and
 * defines common properties and methods for all panels in the LEGUP user interface (currently only
 * implements toolbar scale)
 */
public abstract class LegupPanel extends JPanel {
    /** Alerts panel that it will be going visible now */
    protected final int TOOLBAR_ICON_SCALE = 40;

    /** Abstract method to make the panel visible */
    public abstract void makeVisible();
}
