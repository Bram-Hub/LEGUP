package edu.rpi.legup.ui;

import javax.swing.ImageIcon;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 * The {@code SmoothImageIcon} class is a {@link ImageIcon} that is rendered with the
 * {@link RenderingHints#VALUE_INTERPOLATION_BILINEAR} rendering hint on, counteracting
 * the pixel snapping caused by scaling or HiDPI displays.
 */
public class SmoothImageIcon extends ImageIcon {

    public SmoothImageIcon(URL url) { super(url); }

    public SmoothImageIcon(BufferedImage image) { super(image); }

    public synchronized void paintIcon(Component c, Graphics graphics, int x, int y) {

        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        super.paintIcon(c, g, x, y);
        g.dispose();
    }
}
