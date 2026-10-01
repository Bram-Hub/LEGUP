package edu.rpi.legup.ui.zoompane;

import javax.swing.ScrollPaneConstants;

/** Constants used with the ZoomPane component. */
public interface ZoomPaneConstants extends ScrollPaneConstants {

    /** Identifies a horizontal scrollbar. */
    String ZOOMBAR = "ZOOMBAR";

    /** Identifies the zoom bar policy property. */
    String ZOOMBAR_POLICY = "ZOOMBAR_POLICY";

    /** Used to set the zoom bar policy so that zoom bars are never displayed. */
    int ZOOMBAR_NEVER = 41;

    /** Used to set the zoom bar policy so that zoom bars are always displayed. */
    int ZOOMBAR_ALWAYS = 42;
}
