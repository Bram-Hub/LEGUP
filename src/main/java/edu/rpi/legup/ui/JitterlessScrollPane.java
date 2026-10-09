package edu.rpi.legup.ui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

/**
 * The {@code JitterlessScrollPane} class is a {@code JScrollPane} that combats jittering of
 * its contents caused by HiDPI screen scaling by translating its contents from Swing's
 * internal pixel coordinate system to the display's HiDPI-scaled pixel coordinate system.
 */
public class JitterlessScrollPane extends JScrollPane {

    // Store the sub-pixel offset from trackpad scrolling
    private double fracOffsetX = 0;
    private double fracOffsetY = 0;

    public JitterlessScrollPane() {
        super();
        addMouseWheelListener(new PreciseWheelListener());
    }

    public JitterlessScrollPane(Component view) {
        super(view);
        addMouseWheelListener(new PreciseWheelListener());
    }

    public void setFracOffsetX(double fracOffsetX) {
        this.fracOffsetX = fracOffsetX;
    }

    public double getFracOffsetX() {
        return fracOffsetX;
    }

    public void setFracOffsetY(double fracOffsetY) {
        this.fracOffsetY = fracOffsetY;
    }

    public double getFracOffsetY() {
        return fracOffsetY;
    }

    @Override
    public JViewport createViewport() {
        return new JitterlessViewport();
    }

    /**
     * Sets the viewport of the {@code JitterlessScrollPane} to the given viewport.
     *
     * @param viewport The new viewport to be used; Must be a {@code JitterlessViewport} or null.
     *
     * @see javax.swing.JScrollPane#setViewport
     * @see edu.rpi.legup.ui.JitterlessScrollPane.JitterlessViewport
     */
    @Override
    public void setViewport(JViewport viewport) {
        if (viewport == null || viewport instanceof JitterlessViewport) {
            super.setViewport(viewport);
        } else {
            throw new IllegalArgumentException("The viewport of a JitterlessScrollPane can only be " +
                    "set to a JitterlessViewport");
        }
    }

    /**
     * The {@code JitterlessViewport} class is a {@code JViewport} used by the
     * {@code JitterlessScrollPane} class to translate its contents to combat jittering
     * without causing clipping issues with the {@code JitterlessScrollPane}'s border.
     */
    public class JitterlessViewport extends JViewport {

        @Override
        protected void paintChildren(Graphics graphics) {

            Graphics2D g = (Graphics2D) graphics.create();

            // Get graphics environment scale from screen
            AffineTransform scaleDPI = GraphicsEnvironment
                    .getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice()
                    .getDefaultConfiguration()
                    .getDefaultTransform();

            // Translate graphics to snap contents to DPI-scaled screen pixel coordinates
            long scaledX = Math.round((horizontalScrollBar.getValue() + fracOffsetX) * scaleDPI.getScaleX());
            long scaledY = Math.round((verticalScrollBar.getValue() + fracOffsetY) * scaleDPI.getScaleY());
            g.translate(
                    horizontalScrollBar.getValue() - (scaledX / scaleDPI.getScaleX()),
                    verticalScrollBar.getValue() - (scaledY / scaleDPI.getScaleY())
            );

            super.paintChildren(g);
            g.dispose();
        }
    }

    /**
     * The {@code PreciseWheelListener} class is used by the {@code JitterlessScrollPane} class
     * to intercept scrolling events from precise inputs like trackpads and uses them to give
     * the {@code JitterlessScrollPane} sub-pixel scrolling accuracy and prevent "jumping".
     */
    protected class PreciseWheelListener implements MouseWheelListener {

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {

            // Only intercept scroll wheel events from precise inputs and if enabled
            if (
                    isWheelScrollingEnabled() &&
                            UIManager.getBoolean("ScrollPane.smoothScrolling") &&
                            e.getPreciseWheelRotation() != e.getWheelRotation() &&
                            viewport != null
            ) {
                // Modified code from com.formdev.flatlaf.ui.FlatScrollPaneUI#mouseWheelMovedSmooth
                JScrollBar scrollbar = verticalScrollBar;
                if (scrollbar == null || !scrollbar.isVisible() || e.isShiftDown()) {
                    scrollbar = horizontalScrollBar;
                    if (scrollbar == null || !scrollbar.isVisible()) {
                        return;
                    }
                }

                e.consume();

                double rotation = e.getPreciseWheelRotation();
                int unitIncrement;
                int orientation = scrollbar.getOrientation();
                Component view = viewport.getView();

                if (view instanceof Scrollable scrollable) {

                    Rectangle visibleRect = new Rectangle(viewport.getExtentSize());
                    unitIncrement = scrollable.getScrollableUnitIncrement(visibleRect, orientation, 1);

                    if (unitIncrement > 0) {

                        if (orientation == SwingConstants.VERTICAL) {
                            visibleRect.y += unitIncrement;
                            visibleRect.height -= unitIncrement;
                        } else {
                            visibleRect.x += unitIncrement;
                            visibleRect.width -= unitIncrement;
                        }

                        int unitIncrement2 = scrollable.getScrollableUnitIncrement(visibleRect, orientation, 1);
                        if (unitIncrement2 > 0) {
                            unitIncrement = Math.min(unitIncrement, unitIncrement2);
                        }
                    }
                } else {

                    int direction = rotation < 0 ? -1 : 1;
                    unitIncrement = scrollbar.getUnitIncrement(direction);
                }

                // Compute new position based on previous sub-pixel offset
                double delta = rotation * unitIncrement * e.getScrollAmount() +
                        ((orientation == SwingConstants.VERTICAL) ?
                                getFracOffsetY() : getFracOffsetX());
                int idelta = (int) Math.round(delta);

                int value = scrollbar.getValue();
                int minValue = scrollbar.getMinimum();
                int maxValue = scrollbar.getMaximum() - scrollbar.getModel().getExtent();
                int newValue = Math.max(minValue, Math.min(value + idelta, maxValue));

                if (newValue != value) {
                    scrollbar.setValue(newValue);
                }

                // Set new sub-pixel offset
                double newFracOffset =
                        (value + delta > minValue && value + delta < maxValue) ?
                                delta - idelta : 0.0;
                if (orientation == SwingConstants.VERTICAL) {
                    setFracOffsetY(newFracOffset);
                } else {
                    setFracOffsetX(newFracOffset);
                }
            }
        }
    }
}
