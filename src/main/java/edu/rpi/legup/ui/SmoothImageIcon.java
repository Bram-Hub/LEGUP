package edu.rpi.legup.ui;

import com.formdev.flatlaf.FlatLaf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Image;
import java.net.URL;

/**
 * The {@code SmoothImageIcon} class is a {@link ImageIcon} that is rendered with the
 * {@link RenderingHints#VALUE_INTERPOLATION_BILINEAR} rendering hint on, counteracting
 * the pixel snapping caused by scaling or HiDPI displays.
 */
public class SmoothImageIcon extends ImageIcon implements FlatLaf.DisabledIconProvider {

    public SmoothImageIcon(@NotNull URL url) { super(url); }

    public SmoothImageIcon(@NotNull Image image) { super(image); }

    @Override
    public synchronized void paintIcon(@Nullable Component c, @NotNull Graphics graphics, int x, int y) {

        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        super.paintIcon(c, g, x, y);
        g.dispose();
    }

    @Override
    public Icon getDisabledIcon() {
        ImageIcon disabledIcon = (ImageIcon) UIManager.getLookAndFeel().getDisabledIcon(
                null, new ImageIcon(getImage()));
        return new SmoothImageIcon(disabledIcon.getImage());
    }
}
