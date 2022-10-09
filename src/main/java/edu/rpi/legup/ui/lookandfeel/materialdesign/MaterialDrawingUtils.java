package edu.rpi.legup.ui.lookandfeel.materialdesign;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.util.Map;

public class MaterialDrawingUtils {

    static {
        System.setProperty ("awt.useSystemAAFontSettings", "on");
        System.setProperty ("swing.aatext", "true");
        System.setProperty ("sun.java2d.xrender", "true");
    }

    public static Graphics getAliasedGraphics (Graphics g) {
        Map<RenderingHints.Key, Object> hints = (Map<RenderingHints.Key, Object>) Toolkit.getDefaultToolkit ().getDesktopProperty ("awt.font.desktophints");
        if (hints != null) {
          hints.put (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

          Graphics2D g2d = (Graphics2D) g;
          g2d.addRenderingHints (hints);

          //g2d.addRenderingHints (new RenderingHints (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
          return g2d;
        } else {
          // Desktop hints not supported on this platform
          return g;
        }
    }
}
