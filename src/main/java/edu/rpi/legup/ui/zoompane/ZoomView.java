package edu.rpi.legup.ui.zoompane;

import com.formdev.flatlaf.util.HiDPIUtils;
import edu.rpi.legup.controller.ZoomViewController;
import edu.rpi.legup.ui.zoompane.ZoomViewport.ZoomPeer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 * The canvas object that draws an image or environment in a zoomable and interactive environment like a
 * {@code ZoomPane}. This class maintains its base size and fires a {@code ChangeEvent} when the base size
 * changes, or it otherwise needs to be repainted. Transformations to the graphics context such as translations
 * and scaling factors are left to the {@code ZoomView}'s container.
 */
public abstract class ZoomView {

    /** A list of event listeners for this {@code ZoomView}. */
    private final EventListenerList listenerList = new EventListenerList();

    /** Re-usable {@code ChangeEvent} to be fired to {@link ChangeListener}s. */
    private ChangeEvent changeEvent;

    /** The area needed to draw this view. */
    private Dimension size = new Dimension();

    /** {@code Component} peer that can be used for displaying popups, directing AWT events, and requesting focus.  */
    private ZoomPeer peer;

    /** {@code ZoomViewController} defining interactive behavior of the view. */
    private ZoomViewController controller;

    /**
     * Helper for {@code ZoomView}s that want to draw strings and cannot use
     * {@link Graphics#drawString} because of its kerning issues.
     *
     * @param graphics the graphics context to draw to
     * @param text the string to be drawn
     * @param x the x location from which to draw the string
     * @param y the y location from which to draw the string
     */
    public static void drawStringSmooth(@NotNull Graphics2D graphics, @NotNull String text, float x, float y) {
        graphics.fill(graphics.getFont().createGlyphVector(graphics.getFontRenderContext(), text).getOutline(x, y));
    }

    /**
     * Helper for {@code ZoomView}s that want to draw strings and cannot use
     * {@link Graphics#drawString} because of its kerning issues.
     *
     * @param graphics the graphics context to draw to
     * @param text the string to be drawn
     * @param cx the center x location from which to draw the string
     * @param cy the center y location from which to draw the string
     */
    public static void drawStringSmoothCentered(@NotNull Graphics2D graphics, @NotNull String text, float cx, float cy) {
        GlyphVector vector = graphics.getFont().createGlyphVector(graphics.getFontRenderContext(), text);
        Rectangle2D bounds = vector.getVisualBounds();
        graphics.fill(vector.getOutline((float) (cx - bounds.getWidth() / 2), (float) (cy - bounds.getHeight() / 2)));
    }

    /** Creates a {@code ZoomView} with a given controller. */
    public ZoomView(@Nullable ZoomViewController controller) { setController(controller); }

    /** Gets the area needed to draw this view. */
    public Dimension getSize() { return size; }

    /**
     * Updates the size of the view and triggers a repaint.
     *
     * @param newSize the new size of the view
     * @see #fireStateChanged
     */
    public void setSize(@NotNull Dimension newSize) {
        if (!size.equals(newSize)) {
            size = newSize;
            fireStateChanged();
        }
    }

    /**
     * Sets the {@code ZoomView}'s {@code Component} peer. The peer is used for creating popups and
     * capturing user input and focus.
     *
     * @param newPeer the new peer to use
     */
    public void setPeer(@Nullable ZoomPeer newPeer) {

        if (peer != null && controller != null) {
            peer.removeZoomViewController(controller);
            peer.removeKeyListener(controller);
        }
        peer = newPeer;
        if (peer != null && controller != null) {
            peer.addZoomViewController(controller);
            peer.addKeyListener(controller);
        }
    }

    /** @return the {@code ZoomView}'s {@code Component} peer */
    public ZoomPeer getPeer() { return peer; }

    /**
     * Sets the {@code ZoomViewController} used by the {@code ZoomView} to handle user input.
     *
     * @param newController the new controller to use
     */
    public void setController(@Nullable ZoomViewController newController) {

        if (controller != null) {
            controller.setView(null);

            if (peer != null) {
                peer.removeZoomViewController(controller);
                peer.removeKeyListener(controller);
            }
        }

        controller = newController;

        if (controller != null) {
            controller.setView(this);

            if (peer != null) {
                peer.addZoomViewController(controller);
                peer.addKeyListener(controller);
            }
        }
    }

    /** @return the {@code ZoomView}'s controller */
    public ZoomViewController getController() { return controller; }

    /**
     * Adds a {@code ChangeListener} to the list that is notified each time the view's size changes.
     * If listener {@code l} is {@code null}, no exception is thrown and no action is performed.
     *
     * @param l the {@code ChangeListener} to add
     * @see #removeChangeListener
     */
    public void addChangeListener(@Nullable ChangeListener l) {
        if (l != null) { listenerList.add(ChangeListener.class, l); }
    }

    /**
     * Removes a {@code ChangeListener} from the list that's notified each time the view's size changes.
     * If listener {@code l} has not previously been added to this view or is {@code null}, no exception
     * is thrown and no action is performed.
     *
     * @param l the {@code ChangeListener} to remove
     * @see #addChangeListener
     */
    public void removeChangeListener(ChangeListener l) {
        if (l != null) { listenerList.remove(ChangeListener.class, l); }
    }

    /**
     * Returns an array of all the {@code ChangeListener}s added to this {@code ZoomView} with
     * {@link #addChangeListener}.
     *
     * @return all of the {@code ChangeListener}s added or an empty array if no listeners have been added
     */
    public ChangeListener[] getChangeListeners() { return listenerList.getListeners(ChangeListener.class); }

    /** Alerts all {@link ChangeListener}s that the view's state has changed. */
    private void fireStateChanged() {
        if (changeEvent == null) { changeEvent = new ChangeEvent(this); }
        for (ChangeListener listener : getChangeListeners()) { listener.stateChanged(changeEvent); }
    }

    /** Trigger a repaint of the {@code ZoomView} as soon as possible. */
    public void repaint() { getPeer().repaint(); }

    /** Trigger a repaint of the {@code ZoomView} contained to the given {@code Rectangle2D} as soon as possible. */
    public void repaint(@NotNull Rectangle2D r) { repaint(r.getX(), r.getY(), r.getWidth(), r.getHeight()); }

    /** Trigger a repaint of the {@code ZoomView} contained to the given bounds as soon as possible. */
    public void repaint(double x, double y, double width, double height) {
        Point2D p = getPeer().fromViewCoordinates(new Point2D.Double(x, y));
        Dimension2D d = getPeer().fromViewCoordinates(new ZoomViewport.Dimension2DDouble(width, height));

        HiDPIUtils.repaint(
                getPeer(),
                (int) Math.floor(p.getX()),
                (int) Math.floor(p.getY()),
                (int) (Math.ceil(p.getX() + d.getWidth()) - Math.floor(p.getX())),
                (int) (Math.ceil(p.getY() + d.getHeight()) - Math.floor(p.getY()))
        );
    }

    /**
     * Draws this view's content to the given graphics context at its base scale.
     *
     * @param graphics the zoomed graphics context to draw to
     */
    public abstract void draw(@NotNull Graphics graphics);
}