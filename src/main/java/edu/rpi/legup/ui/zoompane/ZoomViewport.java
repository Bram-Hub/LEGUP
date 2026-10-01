package edu.rpi.legup.ui.zoompane;

import edu.rpi.legup.controller.ZoomViewController;
import edu.rpi.legup.controller.ZoomViewController.ControllerMouseEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.accessibility.Accessible;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.BeanProperty;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The "viewport" through which you can see the underlying {@link ZoomView}. When you pan and zoom, what
 * performs the translation and scaling is the {@code ZoomViewport}. It is like peering through a camera's
 * viewfinder: the {@code ZoomViewport}'s position and size stay grounded within its parent while the
 * {@link ZoomView} displayed within its bounds is shifted around and scaled.
 * <p>
 * <b>Note:</b> Since the size of and detail required for drawing the {@link ZoomView} is constantly changing,
 * the entire view is repainted in its entirety every time the size or position of the view within the
 * viewport changes. Performance conscious applications should keep these updates to a minimum.
 * <pre>
 *     zoomPane.getZoomBar().putClientProperty("ZoomBar.fastWheelScrolling", Boolean.FALSE);
 * </pre>
 *
 * @see JViewport
 * @see ZoomView
 * @see ZoomPane
 */
public class ZoomViewport extends JViewport implements Accessible {

    /** Listener that is notified each time the view changes size. */
    private ChangeListener viewListener = null;

    /** The view element that is being displayed by the {@code ZoomViewport}. */
    protected ZoomView view;

    /** The scale factor being applied to the drawing of the view. */
    protected double scaleFactor;

    /** The current position of the view in viewport coordinates. */
    protected Point2D viewPosition;

    /** Creates a {@code ZoomViewport}. */
    public ZoomViewport() {
        super();
        setScrollMode(SIMPLE_SCROLL_MODE);
        setPeer(createPeer());
    }

    /**
     * Scrolls the view so that the {@code Rectangle2D} in view coordinates becomes visible. If
     * {@code contentRect} is bigger than the current view area, the viewport will zoom out to
     * accommodate the {@code contentRect}.
     *
     * @param contentRect the {@code Rectangle2D} to display.
     * @see #setViewRect
     */
    public void scrollRectToVisible(@NotNull Rectangle2D contentRect) {
        if (view == null) { return; }

        Rectangle2D viewRect = getViewRect();
        if (viewRect.contains(contentRect)) { return; }

        double widthRatio = viewRect.getWidth() / contentRect.getWidth();
        double heightRatio = viewRect.getHeight() / contentRect.getHeight();
        double minRatio = Math.min(widthRatio, heightRatio);

        // Zoom out if necessary
        if (minRatio < 1.0) { scaleFactor *= minRatio; }

        // Adjust view position to fit as much of the contentRect inside as possible
        Point2D viewPos = new Point2D.Double(viewRect.getX(), viewRect.getY());
        if ((viewRect.getX() < contentRect.getX()) !=
                (viewRect.getX() + viewRect.getWidth() > contentRect.getX() + contentRect.getWidth())) {
            double leftEdgesDist = contentRect.getX() - viewRect.getX();
            double rightEdgesDist = (contentRect.getX() + contentRect.getWidth()) -
                    (viewRect.getX() + viewRect.getWidth());
            viewPos.setLocation(
                    viewPos.getX() +
                            Math.abs(leftEdgesDist) < Math.abs(rightEdgesDist) ? leftEdgesDist : rightEdgesDist,
                    viewPos.getY());
        }
        if ((viewRect.getY() < contentRect.getY()) !=
                (viewRect.getY() + viewRect.getHeight() > contentRect.getY() + contentRect.getHeight())) {
            double topEdgesDist = contentRect.getY() - viewRect.getY();
            double bottomEdgesDist = (contentRect.getY() + contentRect.getHeight()) -
                    (viewRect.getY() + viewRect.getHeight());
            viewPos.setLocation(
                    viewPos.getY() +
                            Math.abs(topEdgesDist) < Math.abs(bottomEdgesDist) ? topEdgesDist : bottomEdgesDist,
                    viewPos.getY());
        }

        setViewPosition(viewPos);
    }

    /**
     * Scrolls the view so that the {@code Rectangle} in view coordinates becomes visible. If
     * {@code contentRect} is bigger than the current view area, the viewport will zoom
     * out to accommodate the {@code contentRect}.
     *
     * @param contentRect {@inheritDoc}
     * @see #setViewRect(Rectangle2D)
     */
    @Override
    public void scrollRectToVisible(@NotNull Rectangle contentRect) { scrollRectToVisible((Rectangle2D) contentRect); }

    /**
     * Paints this {@code ZoomViewport}'s children. Since {@code ZoomViewport} is intended to be the
     * viewport to a {@link ZoomView}, the view is drawn here, before its peer.
     *
     * @param graphics the {@code Graphics} context to draw in
     */
    public void paintChildren(@NotNull Graphics graphics) {

        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        g.scale(scaleFactor, scaleFactor);
        g.translate(-viewPosition.getX(), -viewPosition.getY());

        view.draw(g);
        g.dispose();

        super.paintChildren(graphics);
    }

    @Override
    public void validate() {
        super.validate();
        fireStateChanged();
    }

    /**
     * {@inheritDoc}
     *
     * @param mode must have value {@code ZoomViewport.SIMPLE_SCROLL_MODE}
     * @throws IllegalArgumentException if mode is not {@code ZoomViewport.SIMPLE_SCROLL_MODE}
     */
    @Override
    @BeanProperty(bound = false, enumerationValues = {"ZoomViewport.SIMPLE_SCROLL_MODE"},
            description = "Method of moving contents for incremental scrolls.")
    public void setScrollMode(int mode) {
        if (mode == SIMPLE_SCROLL_MODE) { super.setScrollMode(mode); }
        else {
            throw new IllegalArgumentException("ZoomViewport does not support BLIT and BACKING_STORE scroll modes.");
        }
    }

    /**
     * Returns the {@code ZoomViewport}'s component view or {@code null}.
     *
     * @return {@inheritDoc}
     * @see #setView(Component)
     * @see #getPeer()
     */
    @Override
    public Component getView() { return super.getView(); }

    /**
     * Returns the {@code ZoomViewport}'s view or {@code null}.
     *
     * @return the viewport's view, or {@code null} if none exists
     * @see #setZoomView(ZoomView)
     */
    public ZoomView getZoomView() { return view; }

    /**
     * Sets the {@code ZoomViewport}'s component view, which can be {@code null}.
     *
     * @param view the viewport's new component view
     * @throws IllegalArgumentException if view is not a {@code ZoomPeer}
     * @see ZoomPeer
     * @see #setPeer(ZoomPeer)
     */
    @Override
    public void setView(@Nullable Component view) {
        if (view instanceof ZoomPeer || view == null) { super.setView(view); }
        else { throw new IllegalArgumentException("ZoomViewport only permits a ZoomPeer view."); }
    }

    /**
     * Set the {@code ZoomViewport}'s view, which can be {@code null}.
     *
     * @param newView the {@code ZoomView} to be displayed, or {@code null}
     * @see #getView()
     */
    @BeanProperty(preferred = true, description = "The viewport view.")
    public void setZoomView(@Nullable ZoomView newView) {
        if (view == newView) { return; }

        if (view != null) {
            view.removeChangeListener(viewListener);
            view.setPeer(null);
        }

        ZoomView oldView = view;
        view = newView;
        scaleFactor = 1.0;
        viewPosition = new Point2D.Double();

        if (view != null) {
            viewListener = (ChangeListener) createViewListener();
            view.addChangeListener(viewListener);
            view.setPeer(getPeer());
        }

        firePropertyChange("view", oldView, newView);
        repaint();
    }

    /**
     * Returns the current scale factor being applied to the view's drawing.
     *
     * @return the current scale factor
     */
    public double getScaleFactor() { return scaleFactor; }

    /**
     * Sets the current scale factor being applied to the view's drawing, then fires a state change.
     *
     * @param newScaleFactor the new scale factor to be applied to the view's drawing
     * @throws IllegalArgumentException if {@code newScaleFactor <= 0}
     */
    public void setScaleFactor(double newScaleFactor) {
        if (newScaleFactor <= 0) { throw new IllegalArgumentException("newScaleFactor must be greater than 0."); }
        if (view == null || newScaleFactor == scaleFactor) { return; }

        scaleFactor = newScaleFactor;
        fireStateChanged();
        repaint();
    }

    /**
     * Returns the size of the view in view coordinates. If there is no view, return {@code (0,0)}.
     *
     * @return {@inheritDoc}
     */
    @Override
    public Dimension getViewSize() {
        if (view == null) { return new Dimension(); }
        return view.getSize();
    }

    @Override
    public void setViewSize(@NotNull Dimension newSize) {
        ZoomView view = getZoomView();
        if (view != null && !view.getSize().equals(newSize)) { view.setSize(newSize); }
    }

    /**
     * Returns the view coordinates that appear in the upper left hand corner of the viewport in view
     * coordinates, or {@code (0,0)} if there's no view.
     *
     * @return {@inheritDoc}
     * @see #getPreciseViewPosition()
     */
    @Override
    public Point getViewPosition() {
        Point2D precisePos = getPreciseViewPosition();
        return new Point((int) Math.floor(precisePos.getX()), (int) Math.floor(precisePos.getY()));
    }

    /**
     * Returns the view coordinates that appear in the upper left hand corner of the viewport in view
     * coordinates, or {@code (0,0)} if there's no view.
     *
     * @return a {@code Point2D} object giving the upper left coordinates
     */
    public Point2D getPreciseViewPosition() {
        if (view == null) { return new Point2D.Double(); }
        return toViewCoordinates(new Point2D.Double());
    }

    /**
     * {@inheritDoc}
     * <p>
     * It is recommended to use {@link #setViewPosition(Point2D)} instead for more precision.
     *
     * @param newViewPosition {@inheritDoc}
     */
    @Override
    public void setViewPosition(@NotNull Point newViewPosition) { setViewPosition((Point2D) newViewPosition); }

    /**
     * Sets the view coordinates that appear in the upper left hand corner of the viewport, does nothing
     * if there's no view.
     *
     * @param newViewPosition a {@code Point2D} object giving the upper left coordinates
     */
    public void setViewPosition(@NotNull Point2D newViewPosition) {
        if (view == null || newViewPosition.equals(viewPosition)) { return; }
        viewPosition.setLocation(newViewPosition);
        fireStateChanged();
        repaint();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @see #getPreciseViewRect()
     */
    @Override
    public @NotNull Rectangle getViewRect() { return super.getViewRect(); }

    /**
     * Returns a rectangle whose origin is {@link #getPreciseViewPosition} and size is
     * {@link #getPreciseExtentSize}. This is the visible part of the view in view coordinates.
     *
     * @return a {@code Rectangle2D} giving the visible part of the view using view coordinates
     */
    public Rectangle2D getPreciseViewRect() {
        Point2D pos = getPreciseViewPosition();
        Dimension2D size = getPreciseExtentSize();
        return new Rectangle2D.Double(pos.getX(), pos.getY(), size.getWidth(), size.getHeight());
    }

    /**
     * Sets the view area of the viewport to the given {@code Rectangle2D} after first adjusting it
     * to match the viewport's aspect ratio.
     *
     * @param viewRect the view area to set.
     */
    public void setViewRect(@NotNull Rectangle2D viewRect) {
        if (view == null) { return; }

        double widthRatio = getWidth() / viewRect.getWidth();
        double heightRatio = getHeight() / viewRect.getHeight();
        double minRatio = Math.min(widthRatio, heightRatio);
        widthRatio /= minRatio;
        heightRatio /= minRatio;

        double oldScaleFactor = scaleFactor;
        Point2D oldViewPosition = viewPosition;
        scaleFactor *= minRatio;
        viewPosition = new Point2D.Double(
                viewRect.getCenterX() - viewRect.getWidth() * widthRatio / 2,
                viewRect.getCenterY() - viewRect.getHeight() * heightRatio / 2
        );

        if (scaleFactor != oldScaleFactor || !viewPosition.equals(oldViewPosition)) {
            fireStateChanged();
            repaint();
        }
    }

    /**
     * {@inheritDoc} Since the view inhabits space in double-precision, this method returns the size of the
     * bounding box on the integer grid.
     *
     * @return {@inheritDoc}
     */
    @Override
    public Dimension getExtentSize() {
        Point2D precisePos = getPreciseViewPosition();
        Dimension2D preciseSize = getPreciseExtentSize();
        return new Dimension(
                (int) (Math.ceil(precisePos.getX() + preciseSize.getWidth()) - Math.floor(precisePos.getX())),
                (int) (Math.ceil(precisePos.getY() + preciseSize.getHeight() - Math.floor(precisePos.getY())))
        );
    }

    /**
     * Returns the size of the visible part of the view in view coordinates.
     *
     * @return a {@code Dimension2D} object giving the viewport's coverage of the view
     */
    public Dimension2D getPreciseExtentSize() {
        if (view == null) { return new Dimension2DDouble(); }
        return toViewCoordinates((Dimension2D) getSize());
    }

    /**
     * {@inheritDoc}
     * <p>
     * It is recommended to use {@link #toViewCoordinates(Dimension2D)} instead for more precision.
     *
     * @param size {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Dimension toViewCoordinates(@NotNull Dimension size) {
        size.setSize(toViewCoordinates((Dimension2D) size));
        return size;
    }

    /**
     * Converts a size in pixel coordinates to view coordinates.
     *
     * @param size a {@code Dimension2D} object using pixel coordinates
     * @return a {@code Dimension2D} object converted to view coordinates
     */
    public Dimension2D toViewCoordinates(@NotNull Dimension2D size) {
        return new Dimension2DDouble(size.getWidth() / scaleFactor, size.getHeight() / scaleFactor);
    }

    /**
     * {@inheritDoc}
     * <p>
     * It is recommended to use {@link #toViewCoordinates(Point2D)} instead for more precision.
     *
     * @param p {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Point toViewCoordinates(@NotNull Point p) {
        p.setLocation(toViewCoordinates((Point2D) p));
        return p;
    }

    /**
     * Converts a point in pixel coordinates to view coordinates.
     *
     * @param p a {@code Point2D} object using pixel coordinates
     * @return a {@code Point2D} object converted to view coordinates
     */
    public Point2D toViewCoordinates(@NotNull Point2D p) {
        return new Point2D.Double(
                viewPosition.getX() + p.getX() / scaleFactor,
                viewPosition.getY() + p.getY() / scaleFactor
        );
    }

    /**
     * {@inheritDoc}
     * <p>
     * It is recommended to use {@link #setExtentSize(Dimension2D)} instead for more precision.
     *
     * @param newExtent {@inheritDoc}
     */
    @Override
    public void setExtentSize(@NotNull Dimension newExtent) { setExtentSize((Dimension2D) newExtent); }

    /**
     * Sets the size of the visible part of the view using view coordinates.
     *
     * @param newExtent a {@code Dimension2D} object specifying the size of the view
     */
    public void setExtentSize(@NotNull Dimension2D newExtent) {
        if (view == null) { return; }
        double widthRatio = getWidth() / newExtent.getWidth();
        double heightRatio = getHeight() / newExtent.getHeight();
        setScaleFactor(scaleFactor * Math.min(widthRatio, heightRatio));
    }

    /** A listener for the view. */
    protected class ZoomViewListener extends ViewListener implements ChangeListener {

        /** Constructs a {@code ZoomViewListener}. */
        protected ZoomViewListener() {}

        public void stateChanged(@NotNull ChangeEvent e) {
            fireStateChanged();
            repaint();
        }
    }

    /**
     * Creates a listener for the view.
     *
     * @return a {@code ViewListener}.
     */
    @Override
    protected ViewListener createViewListener() { return new ZoomViewListener(); }

    @Override
    protected LayoutManager createLayoutManager() {
        return new LayoutManager() {

            public void addLayoutComponent(String name, Component comp) {}
            public void removeLayoutComponent(Component comp) {}

            public Dimension preferredLayoutSize(Container parent) { return parent.getSize(); }
            public Dimension minimumLayoutSize(Container parent) { return parent.getSize(); }

            public void layoutContainer(Container parent) {
                ZoomViewport viewport = (ZoomViewport) parent;
                viewport.getView().setBounds(0, 0, viewport.getWidth(), viewport.getHeight());
            }
        };
    }

    /**
     * Subclassers can override this to install a different peer in the constructor. Returns the
     * {@code ZoomPeer} to use as the view of the {@code ZoomViewport}.
     *
     * @return a {@code ZoomPeer}
     */
    protected ZoomPeer createPeer() { return new ZoomPeer(); }

    /**
     * Sets the {@code ZoomViewport}'s component peer, which can be {@code null}.
     *
     * @param newPeer the viewport's new component peer
     */
    public void setPeer(ZoomPeer newPeer) { setView(newPeer); }

    /**
     * Returns the {@code ZoomViewport}'s component peer or {@code null}.
     *
     * @return this viewport's peer, or {@code null} if none exists
     */
    public ZoomPeer getPeer() { return (ZoomPeer) getView(); }

    /**
     * The component child of {@code ZoomViewport} that acts as a peer to the {@code ZoomView}. It provides
     * the view with an unscaled surface on which to add popups and menus in addition to having installed the
     * view's controllers.
     */
    public class ZoomPeer extends JPanel {

        /** Identifies a component that should be laid out as a notification. */
        public static final String NOTIFICATION = "NOTIFICATION";

        /**
         * Mapping of controllers to the {@code MouseInputListeners} installed on this component that create
         * {@code ControllerMouseEvent}s for the controllers.
         */
        private final HashMap<ZoomViewController, MouseInputListener> transformers;

        public ZoomPeer() {
            setOpaque(false);
            setFocusable(true);
            setLayout(new ZoomPeerLayout());
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    requestFocusInWindow();
                }
            });
            transformers = new HashMap<>();
        }

        /**
         * Converts a point in view coordinates to pixel coordinates.
         *
         * @param p a {@code Point2D} object using view coordinates
         * @return a {@code Point2D} object converted to pixel coordinates
         */
        public Point2D fromViewCoordinates(@NotNull Point2D p) {
            return new Point2D.Double(
                    (p.getX() - viewPosition.getX()) * scaleFactor,
                    (p.getY() - viewPosition.getY()) * scaleFactor
            );
        }

        /**
         * Converts a dimension in view coordinates to pixel coordinates.
         *
         * @param d a {@code Dimension2D} object using view coordinates
         * @return a {@code Dimension2D} object converted to pixel coordinates
         */
        public Dimension2D fromViewCoordinates(@NotNull Dimension2D d) {
            return new Dimension2DDouble(
                    d.getWidth() * scaleFactor,
                    d.getHeight() * scaleFactor
            );
        }

        /** @return {@code true} if the given point is inside the view, {@code false} otherwise */
        protected boolean pointInsideView(@NotNull Point2D p) {
            Dimension viewSize = getViewSize();
            return p.getX() >= 0 && p.getX() <= viewSize.width && p.getY() >= 0 && p.getY() <= viewSize.height;
        }

        /**
         * Creates a {@code ControllerMouseEvent} with the location of the original transformed
         * to view coordinates.
         *
         * @param e the base mouse event
         * @return a controller mouse event in view coordinates
         */
        private ControllerMouseEvent createControllerMouseEvent(@NotNull MouseEvent e) {
            return new ControllerMouseEvent(this, e.getID(), e.getWhen(), e.getModifiersEx(),
                    toViewCoordinates((Point2D) e.getPoint()), e.getClickCount(), e.isPopupTrigger(), e.getButton());
        }

        /**
         * Creates a {@code MouseInputListener} that transforms the location of mouse events into view
         * coordinates before passing them to the supplied {@code ZoomViewController}. The controller-listener
         * pair is added to the {@code transformers} field.
         *
         * @param c the controller to be given transformed mouse events
         * @return a listener that supplies {@code c} with transformed mouse events
         */
        private MouseInputListener transformController(ZoomViewController c) {
            if (c == null) { return null; }

            MouseInputListener transformer = new MouseInputAdapter() {

                private boolean justInside;

                @Override
                public void mouseClicked(MouseEvent e) {
                    ControllerMouseEvent cme = createControllerMouseEvent(e);
                    if (pointInsideView(cme.getPrecisePoint())) { c.mouseClicked(cme); }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    ControllerMouseEvent cme = createControllerMouseEvent(e);
                    if (pointInsideView(cme.getPrecisePoint())) { c.mousePressed(cme); }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    ControllerMouseEvent cme = createControllerMouseEvent(e);
                    if (pointInsideView(cme.getPrecisePoint())) { c.mouseReleased(cme); }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (justInside) {
                        c.mouseExited(createControllerMouseEvent(e));
                        justInside = false;
                    }
                }

                @Override
                public void mouseDragged(MouseEvent e) { c.mouseDragged(createControllerMouseEvent(e)); }

                @Override
                public void mouseMoved(MouseEvent e) {
                    ControllerMouseEvent cme = createControllerMouseEvent(e);
                    c.mouseMoved(cme);

                    if (pointInsideView(cme.getPrecisePoint())) {
                        if (!justInside) {
                            c.mouseEntered(cme);
                            justInside = true;
                        }
                    }
                    else if (justInside) {
                        c.mouseExited(cme);
                        justInside = false;
                    }
                }
            };

            transformers.put(c, transformer);
            return transformer;
        }

        /**
         * Adds the specified {@code ZoomViewController} to receive mouse input and motion events from this
         * component. The position of events added via this method will be given in view coordinates.
         *
         * @param c the controller
         */
        public void addZoomViewController(ZoomViewController c) {
            MouseInputListener l = transformController(c);
            addMouseListener(l);
            addMouseMotionListener(l);
        }

        /**
         * Removes the specified {@code ZoomViewController} so that it no longer receives mouse input
         * and motion events from this component.
         *
         * @param c the controller
         */
        public void removeZoomViewController(ZoomViewController c) {
            MouseInputListener l = transformers.remove(c);
            removeMouseListener(l);
            removeMouseMotionListener(l);
        }

        /**
         * Returns an array of all {@code ZoomViewController}s registered on this component.
         *
         * @return all of this component's {@code ZoomViewController}s
         */
        public ZoomViewController[] getZoomViewControllers() {
            return transformers.keySet().toArray(new ZoomViewController[0]);
        }

        /**
         * Layout manager for {@code ZoomPeer} that lays out components as notifications if they are added
         * with {@link ZoomPeer#NOTIFICATION}.
         */
        private static class ZoomPeerLayout implements LayoutManager {

            /** List of components to lay out as notifications. */
            private final List<Component> notifications = new ArrayList<>();

            /** X coordinate to place each notification at. */
            private static final int NOTIFICATION_X = 10;

            /** Y coordinate to place the first notification at. */
            private static final int NOTIFICATION_Y = 10;

            /** Vertical gap between every notification. */
            private static final int NOTIFICATION_GAP = 5;

            @Override
            public void addLayoutComponent(@Nullable String name, @NotNull Component comp) {
                if (NOTIFICATION.equals(name)) { notifications.add(comp); }
            }

            @Override
            public void removeLayoutComponent(@NotNull Component comp) { notifications.remove(comp); }

            @Override
            public Dimension preferredLayoutSize(@NotNull Container parent) {
                return new Dimension(0, 0);
            }

            @Override
            public Dimension minimumLayoutSize(@NotNull Container parent) {
                return new Dimension(0, 0);
            }

            @Override
            public void layoutContainer(@NotNull Container parent) {

                int y = NOTIFICATION_Y;
                for (Component notification : notifications) {
                    if (notification.isVisible()) {

                        Dimension size = notification.getPreferredSize();
                        notification.setBounds(NOTIFICATION_X, y, size.width, size.height);

                        y += size.height + NOTIFICATION_GAP;
                    }
                }
            }
        }
    }

    /**
     * A {@code double}-precision {@code Dimension2D}.
     * <p>
     * Unlike other classes in {@link java.awt.geom}, the {@code Dimension2D} class does not provide inner
     * {@code Float} and {@code Double} classes. This class is a necessary {@code Dimension2D.Double}
     * implementation.
     */
    public static class Dimension2DDouble extends Dimension2D implements Serializable {

        /** The width of this {@code Dimension2D}.*/
        public double width;

        /** The height of this {@code Dimension2D}.*/
        public double height;

        /** Constructs and initializes a {@code Dimension2D} with lengths (0,&nbsp;0). */
        public Dimension2DDouble() { setSize(0, 0); }

        /**
         * Constructs and initializes a {@code Dimension2D} with the specified lengths.
         *
         * @param width the width of the newly constructed {@code Dimension2D}.
         * @param height the height of the newly constructed {@code Dimension2D}.
         */
        public Dimension2DDouble(double width, double height) { setSize(width, height); }


        @Override
        public double getWidth() { return width; }

        @Override
        public double getHeight() { return height; }

        @Override
        public void setSize(double width, double height) {
            this.width = width;
            this.height = height;
        }

        /**
         * Returns a {@code String} that represents the value of this {@code Dimension2D}.
         *
         * @return a string representation of this {@code Dimension2D}.
         */
        public String toString() { return "Dimension2DDouble[" + width + ", " + height + "]"; }

        /** Use serialVersionUID from JDK 1.6 for interoperability. */
        @Serial
        private static final long serialVersionUID = 6150783262733311327L;
    }

    /**
     * Returns a string representation of this {@code ZoomViewport}. This method is intended to be used only
     * for debugging purposes, and the content and format of the returned string may vary between implementations.
     * The returned string may be empty but may not be {@code null}.
     *
     * @return a string representation of this {@code ZoomViewport}
     */
    protected String paramString() {
        String paramString = super.paramString();
        paramString = paramString.substring(0, paramString.indexOf(",isViewSizeSet="));
        return paramString +
                ",scaleFactor=" + scaleFactor +
                ",viewPosition=" + viewPosition +
                ",extentSize=" + getPreciseExtentSize() +
                ",view=" + view;
    }
}
