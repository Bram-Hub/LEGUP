package edu.rpi.legup.ui.zoompane;

import com.formdev.flatlaf.ui.FlatScrollPaneUI;
import com.formdev.flatlaf.ui.FlatStylingSupport;
import com.formdev.flatlaf.util.LoggingFacade;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ActionMapUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicScrollPaneUI;

/** Provides the Flat Laf UI delegate for {@link ZoomPane}. */
public class ZoomPaneUI extends FlatScrollPaneUI {

    /** Re-usable handler instance for event handling. */
    private Handler handler;

    /** {@code MouseWheelListener} installed on the viewport. */
    private MouseWheelListener mouseZoomListener;

    /** {@code ActionListener} installed on the zoom bar for fitting the view to the viewport. */
    protected ActionListener zbFitListener;

    /** {@code ChangeListener} installed on the zoom bar. */
    protected ChangeListener zbChangeListener;

    /** {@code PropertyChangeListener} installed on the zoom bar. */
    private PropertyChangeListener zbPropertyChangeListener;

    /** {@code PropertyChangeListener} installed on the viewport. */
    private PropertyChangeListener viewportPropertyChangeListener;

    /** {@code ComponentListener} installed on the viewport. */
    private ComponentListener viewportResizeListener;

    /**
     * Flag indicating that the viewport should fit its view as soon as it is possible.
     * @see Actions#readyToFit(ZoomPane)
     */
    protected boolean waitingToFit;

    /**
     * Flag indicating that the viewport has fit its view and should re-fit its view every time the view changes
     * until an action is taken.
     *
     * @see Actions#readyToFit(ZoomPane)
     */
    protected boolean isFit;

    /**
     * Flag indicating that state change events should be ignored (usually because they were triggered by
     * a call from this class).
     * @see #syncScrollPaneWithViewport()
     * @see Handler#actionPerformed(ActionEvent)
     */
    protected boolean ignoreStateChanges;

    public static ComponentUI createUI(@NotNull JComponent c) { return new ZoomPaneUI(); }

    /** @return the {@code viewPadding} client property or UI Default associated with the given {@code ZoomPane} */
    protected static Insets getViewPadding(ZoomPane zoomPane) {
        Object viewPadding = zoomPane.getClientProperty("ZoomPane.viewPadding");
        if (viewPadding instanceof Insets) { return (Insets) viewPadding; }
        viewPadding = UIManager.getInsets("ZoomPane.viewPadding");
        if (viewPadding != null) { return (Insets) viewPadding; }
        return new Insets(0, 0, 0, 0);
    }

    /**
     * {@inheritDoc}
     *
     * @param c {@inheritDoc}
     * @throws IllegalArgumentException if c is not a {@code ZoomPane}
     */
    @Override
    public void installUI(@NotNull JComponent c) {
        if (c instanceof ZoomPane) { super.installUI(c); }
        else { throw new IllegalArgumentException("ZoomPaneUI can only be applied to a ZoomPane."); }

        int focusWidth = UIManager.getInt("Component.focusWidth");
        int arc = getArc();
        LookAndFeel.installProperty(c, "opaque", focusWidth == 0 && arc == 0);

        if (Actions.readyToFit((ZoomPane) scrollpane)) {
            Actions.zoomToFit((ZoomPane) scrollpane);
            waitingToFit = false;
        }
        else { waitingToFit = true; }
    }

    @Override
    protected void installDefaults(@NotNull JScrollPane c) {
        super.installDefaults(scrollpane);

        UIDefaults defaults = UIManager.getDefaults();
        if (defaults.containsKey("ZoomPane.border")) {
            LookAndFeel.installBorder(scrollpane, "ZoomPane.border");
        }
        if (defaults.containsKey("ZoomPane.background")) {
            LookAndFeel.installColorsAndFont(scrollpane, "ZoomPane.background",
                    "ScrollPane.foreground", "ScrollPane.font");
        }

        Border vpBorder = scrollpane.getViewportBorder();
        if (defaults.containsKey("ZoomPane.viewportBorder") &&
                ((vpBorder == null) ||( vpBorder instanceof UIResource))) {
            scrollpane.setViewportBorder(UIManager.getBorder("ZoomPane.viewportBorder"));
        }
    }

    @Override
    protected void installListeners(@NotNull JScrollPane c) {
        super.installListeners(c);

        mouseZoomListener = createViewportMouseWheelListener();
        zbChangeListener = createZBChangeListener();
        zbFitListener = createZBFitListener();
        zbPropertyChangeListener = createZBPropertyChangeListener();
        viewportPropertyChangeListener = createViewportPropertyChangeListener();
        viewportResizeListener = createViewportResizeListener();

        JViewport viewport = scrollpane.getViewport();
        ZoomBar zoomBar = ((ZoomPane) scrollpane).getZoomBar();

        if (viewport != null) {
            viewport.addMouseWheelListener(mouseZoomListener);
            viewport.addPropertyChangeListener(viewportPropertyChangeListener);
            viewport.addComponentListener(viewportResizeListener);
        }
        if (zoomBar != null) {
            zoomBar.getModel().addChangeListener(zbChangeListener);
            JButton fit = zoomBar.getFitButton();
            if (fit != null) { fit.addActionListener(zbFitListener); }
            zoomBar.addPropertyChangeListener(zbPropertyChangeListener);
        }
    }

    @Override
    protected void uninstallListeners(@NotNull JComponent c) {
        super.uninstallListeners(c);

        JViewport viewport = scrollpane.getViewport();
        ZoomBar zoomBar = ((ZoomPane) scrollpane).getZoomBar();

        if (viewport != null) {
            viewport.removeMouseWheelListener(mouseZoomListener);
            viewport.removePropertyChangeListener(viewportPropertyChangeListener);
            viewport.removeComponentListener(viewportResizeListener);
        }
        if (zoomBar != null) {
            zoomBar.getModel().removeChangeListener(zbChangeListener);
            JButton fit = zoomBar.getFitButton();
            if (fit != null) { fit.removeActionListener(zbFitListener); }
            zoomBar.removePropertyChangeListener(zbPropertyChangeListener);
        }

        mouseZoomListener = null;
        zbChangeListener = null;
        zbFitListener = null;
        zbPropertyChangeListener = null;
        viewportPropertyChangeListener = null;
        viewportResizeListener = null;
        handler = null;
    }

    @Override
    protected void installKeyboardActions(@NotNull JScrollPane c) {
        SwingUtilities.replaceUIInputMap(c, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, getInputMap());
        SwingUtilities.replaceUIActionMap(c, getActionMap());
    }

    /** Attempts to retrieve the {@code InputMap} from the {@code UIManager} and creates one if none is found. */
    private InputMap getInputMap() {
        InputMap keyMap = (InputMap) UIManager.get("ZoomPane.ancestorInputMap");
        if (keyMap != null) { return keyMap; }

        keyMap = LookAndFeel.makeInputMap(new Object[] {
                "ctrl HOME", Actions.SCROLL_HOME,
                "UP", Actions.UNIT_SCROLL_UP,
                "KP_UP", Actions.UNIT_SCROLL_UP,
                "DOWN", Actions.UNIT_SCROLL_DOWN,
                "KP_DOWN", Actions.UNIT_SCROLL_DOWN,
                "LEFT", Actions.UNIT_SCROLL_LEFT,
                "KP_LEFT", Actions.UNIT_SCROLL_LEFT,
                "RIGHT", Actions.UNIT_SCROLL_RIGHT,
                "KP_RIGHT", Actions.UNIT_SCROLL_RIGHT,
                "PAGE_UP", Actions.SCROLL_UP,
                "PAGE_DOWN", Actions.SCROLL_DOWN,
                "ctrl PAGE_UP", Actions.SCROLL_LEFT,
                "ctrl PAGE_DOWN", Actions.SCROLL_RIGHT,
                "PLUS", Actions.UNIT_ZOOM_IN,
                "ADD", Actions.UNIT_ZOOM_IN,
                "ctrl PLUS", Actions.ZOOM_IN,
                "ctrl ADD", Actions.ZOOM_IN,
                "MINUS", Actions.UNIT_ZOOM_OUT,
                "SUBTRACT", Actions.UNIT_ZOOM_OUT,
                "ctrl MINUS", Actions.ZOOM_OUT,
                "ctrl SUBTRACT", Actions.ZOOM_OUT
        });
        UIManager.getLookAndFeelDefaults().put("ZoomPane.ancestorInputMap", keyMap);
        return keyMap;
    }

    /** Attempts to retrieve the {@code ActionMap} from the {@code UIManager} and creates one if none is found. */
    private ActionMap getActionMap() {
        ActionMap actionMap = (ActionMap) UIManager.get("ZoomPane.actionMap");
        if (actionMap != null) { return actionMap; }

        actionMap = new ActionMapUIResource();
        actionMap.put(Actions.SCROLL_HOME, new Actions(Actions.SCROLL_HOME));
        actionMap.put(Actions.UNIT_SCROLL_UP, new Actions(Actions.UNIT_SCROLL_UP));
        actionMap.put(Actions.UNIT_SCROLL_DOWN, new Actions(Actions.UNIT_SCROLL_DOWN));
        actionMap.put(Actions.UNIT_SCROLL_LEFT, new Actions(Actions.UNIT_SCROLL_LEFT));
        actionMap.put(Actions.UNIT_SCROLL_RIGHT, new Actions(Actions.UNIT_SCROLL_RIGHT));
        actionMap.put(Actions.SCROLL_UP, new Actions(Actions.SCROLL_UP));
        actionMap.put(Actions.SCROLL_DOWN, new Actions(Actions.SCROLL_DOWN));
        actionMap.put(Actions.SCROLL_LEFT, new Actions(Actions.SCROLL_LEFT));
        actionMap.put(Actions.SCROLL_RIGHT, new Actions(Actions.SCROLL_RIGHT));
        actionMap.put(Actions.UNIT_ZOOM_IN, new Actions(Actions.UNIT_ZOOM_IN));
        actionMap.put(Actions.UNIT_ZOOM_OUT, new Actions(Actions.UNIT_ZOOM_OUT));
        actionMap.put(Actions.ZOOM_IN, new Actions(Actions.ZOOM_IN));
        actionMap.put(Actions.ZOOM_OUT, new Actions(Actions.ZOOM_OUT));

        UIManager.getLookAndFeelDefaults().put("ZoomPane.actionMap", actionMap);
        return actionMap;
    }

    @Override
    protected ZoomPaneLayout createScrollPaneLayout() { return new ZoomPaneLayout(); }

    @Override
    protected void installStyle() {
        try {
            applyStyle(FlatStylingSupport.getResolvedStyle(scrollpane, "ZoomPane"));
        } catch (RuntimeException e) {
            LoggingFacade.INSTANCE.logSevere(null, e);
        }
    }

    @Override
    protected Object applyStyleProperty(@NotNull String key, @Nullable Object value) {
        Object toRet = super.applyStyleProperty(key, value);

        if (key.equals("focusWidth") || key.equals("arc")) {
            int focusWidth = (value instanceof Integer && key.equals("focusWidth")) ?
                    (int) value : UIManager.getInt("Component.focusWidth");
            int arc = (value instanceof Integer && key.equals("arc")) ? (int) value : getArc();

            LookAndFeel.installProperty(scrollpane, "opaque",
                    focusWidth == 0 && arc == 0);
        }

        return toRet;
    }

    @Override
    protected void syncScrollPaneWithViewport() {
        ZoomPane zoomPane = (ZoomPane) scrollpane;
        ZoomViewport viewport = (ZoomViewport) zoomPane.getViewport();
        JScrollBar vsb = zoomPane.getVerticalScrollBar();
        JScrollBar hsb = zoomPane.getHorizontalScrollBar();
        ZoomBar zb = zoomPane.getZoomBar();
        ZoomViewport rowHead = (ZoomViewport) zoomPane.getRowHeader();
        ZoomViewport colHead = (ZoomViewport) zoomPane.getColumnHeader();
        Insets padding = getViewPadding(zoomPane);

        // If view just changed size from 0, may need to fit it
        ignoreStateChanges = true;
        if ((isFit || waitingToFit) && Actions.readyToFit(zoomPane)) {
            Actions.zoomToFit(zoomPane);
            waitingToFit = false;
        }
        else { viewport.setViewPosition(Actions.clampViewPos(zoomPane, viewport.getPreciseViewPosition())); }

        if (viewport != null) {
            Dimension2D extentSize = viewport.getPreciseExtentSize();
            Dimension2D viewSize = viewport.getViewSize();
            Point2D viewPos = viewport.getPreciseViewPosition();
            double scaleFactor = viewport.getScaleFactor();

            if (vsb != null) {
                int extent = (int) Math.ceil(extentSize.getHeight() * scaleFactor);
                int min = -padding.top;
                int max = padding.bottom + (int) Math.ceil(viewSize.getHeight() * scaleFactor);
                int value = (int) Math.max(min, Math.min(max - extent, Math.round(
                        ((viewPos.getY() + padding.top / scaleFactor) / (viewSize.getHeight() - extentSize.getHeight()
                                + (padding.bottom + padding.top) / scaleFactor))
                                * (max - extent - min) + min
                )));
                vsb.setValues(value, extent, min, max);
            }
            if (hsb != null) {
                int extent = (int) Math.ceil(extentSize.getWidth() * scaleFactor);
                int min = -padding.left;
                int max = padding.right + (int) Math.ceil(viewSize.getWidth() * scaleFactor);
                int value = (int) Math.max(min, Math.min(max - extent, Math.round(
                        ((viewPos.getX() + padding.left / scaleFactor) / (viewSize.getWidth() - extentSize.getWidth()
                                + (padding.right + padding.left) / scaleFactor))
                                * (max - extent - min) + min
                )));
                hsb.setValues(value, extent, min, max);
            }
            if (zb != null) {
                int max = 1000;
                double zoom = Actions.scaleFactorToZoom(zoomPane, viewport.getScaleFactor());
                int value = Math.clamp(Math.round(zoom * max), 0, max);
                zb.setValues(value, 0, 0, max);
            }

            if (rowHead != null) {
                Point2D p = rowHead.getPreciseViewPosition();
                p.setLocation(0, viewPos.getY());
                rowHead.setViewPosition(p);
            }
            if (colHead != null) {
                Point2D p = colHead.getPreciseViewPosition();
                p.setLocation(0, viewPos.getX());
                colHead.setViewPosition(p);
            }
        }
        ignoreStateChanges = false;
    }

    /** @return the {@code Handler} for all AWT events this UI needs to process */
    private Handler getHandler() {
        if (handler == null) { handler = new Handler(); }
        return handler;
    }

    /** Returns a {@code PropertyChangeListener} that will be installed on the {@code ZoomViewport} */
    private PropertyChangeListener createViewportPropertyChangeListener() { return getHandler(); }

    /** Returns a {@code PropertyChangeListener} that will be installed on the {@code ZoomBar} */
    private PropertyChangeListener createZBPropertyChangeListener() { return getHandler(); }

    @Override
    protected ChangeListener createViewportChangeListener() { return getHandler(); }

    @Override
    protected ChangeListener createVSBChangeListener() { return getHandler(); }

    @Override
    protected ChangeListener createHSBChangeListener() { return getHandler(); }

    /**
     * Creates the {@code ChangeListener} for the {@code ZoomBar}'s model.
     *
     * @return an instance of the zoom bar {@code ChangeListener}
     */
    protected ChangeListener createZBChangeListener() { return getHandler(); }

    /**
     * Creates the {@code ActionListener} for the {@code ZoomBar}'s fit button.
     *
     * @return an instance of the zoom bar {@code ActionListener}
     */
    protected ActionListener createZBFitListener() { return getHandler(); }

    /**
     * {@link BasicScrollPaneUI} adds the returned value from this method to the scroll pane, but since
     * zooming logic may be relative to the viewport, {@link #createViewportMouseWheelListener()}
     * creates the listener with the zooming logic for the viewport while this method returns {@code null}.
     *
     * @return {@code null}
     * @see #installUI(JComponent)
     */
    @Override
    protected MouseWheelListener createMouseWheelListener() { return null; }

    /**
     * Creates an instance of {@code MouseWheelListener}, which is added to the {@code ZoomViewport}
     * by {@code installUI}. The returned {@code MouseWheelListener} is used to handle
     * mouse wheel-driven zooming.
     *
     * @return {@code MouseWheelListener} which implements wheel-driven zooming
     * @see #installUI(JComponent)
     */
    protected MouseWheelListener createViewportMouseWheelListener() { return getHandler(); }

    @Override
    protected boolean isSmoothScrollingEnabled() {
        Object smoothScrolling = scrollpane.getClientProperty("ZoomPane.smoothScrolling");
        if (smoothScrolling != null) { return Objects.equals(smoothScrolling, true); }
        smoothScrolling = UIManager.get("ZoomPane.smoothScrolling");
        if (smoothScrolling != null) { return Objects.equals(smoothScrolling, true); }
        return super.isSmoothScrollingEnabled();
    }

    /** @return UI Default {@code ZoomPane.arc} if an integer, otherwise fallback to {@code ScrollPane.arc} */
    protected int getArc() {
        Object arc = UIManager.get("ZoomPane.arc");
        return (arc instanceof Integer) ? (int) arc : UIManager.getInt("ScrollPane.arc");
    }

    @Override
    protected void updateViewport(@NotNull PropertyChangeEvent e) {
        ZoomViewport oldViewport = (ZoomViewport) e.getOldValue();
        ZoomViewport newViewport = (ZoomViewport) e.getNewValue();

        if (oldViewport != null) {
            oldViewport.removeMouseWheelListener(mouseZoomListener);
            oldViewport.removePropertyChangeListener(viewportPropertyChangeListener);
            oldViewport.removeComponentListener(viewportResizeListener);
        }

        super.updateViewport(e);
        if (newViewport != null) {
            newViewport.addMouseWheelListener(mouseZoomListener);
            newViewport.addPropertyChangeListener(viewportPropertyChangeListener);
            newViewport.addComponentListener(viewportResizeListener);

            if (Actions.readyToFit((ZoomPane) scrollpane)) {
                Actions.zoomToFit((ZoomPane) scrollpane);
                waitingToFit = false;
            }
            else { waitingToFit = true; }
        }
    }

    /**
     * Updates zoom bar.
     *
     * @param e the property change event
     */
    protected void updateZoomBar(@NotNull PropertyChangeEvent e) {
        ZoomBar oldZoomBar = (ZoomBar) e.getOldValue();
        ZoomBar newZoomBar = (ZoomBar) e.getNewValue();
        JButton fit;

        if (oldZoomBar != null) {
            oldZoomBar.getModel().removeChangeListener(zbChangeListener);
            fit = oldZoomBar.getFitButton();
            if (fit != null) { fit.removeActionListener(zbFitListener); }
            oldZoomBar.removePropertyChangeListener(zbPropertyChangeListener);
        }
        if (newZoomBar != null) {
            newZoomBar.getModel().addChangeListener(zbChangeListener);
            fit = newZoomBar.getFitButton();
            if (fit != null) { fit.addActionListener(zbFitListener); }
            newZoomBar.addPropertyChangeListener(zbPropertyChangeListener);
        }
    }

    /**
     * Creates an instance of {@code ComponentListener}, which is added to the {@code ZoomViewport}
     * by {@code installUI}. The returned {@code ComponentListener} adjusts the view position when
     * the viewport is resized. It also checks if the view is able and needs to be fit to the viewport.
     *
     * @return {@code MouseWheelListener} which handles viewport resizing
     * @see #installUI(JComponent)
     */
    protected ComponentListener createViewportResizeListener() { return getHandler(); }

    @Override
    protected PropertyChangeListener createPropertyChangeListener() {
        PropertyChangeListener oldPropertyChangeListener = super.createPropertyChangeListener();
        PropertyChangeListener newPropertyChangeListener = getHandler();

        return e -> {
            oldPropertyChangeListener.propertyChange(e);
            newPropertyChangeListener.propertyChange(e);
        };
    }

    /** Actions for navigation of the view. */
    protected static class Actions implements Action {
        
        protected static final String SCROLL_HOME = "scrollHome";
        protected static final String UNIT_SCROLL_UP = "unitScrollUp";
        protected static final String UNIT_SCROLL_DOWN = "unitScrollDown";
        protected static final String UNIT_SCROLL_LEFT = "unitScrollLeft";
        protected static final String UNIT_SCROLL_RIGHT = "unitScrollRight";
        protected static final String SCROLL_UP = "scrollUp";
        protected static final String SCROLL_DOWN = "scrollDown";
        protected static final String SCROLL_LEFT = "scrollLeft";
        protected static final String SCROLL_RIGHT = "scrollRight";
        protected static final String UNIT_ZOOM_IN = "unitZoomIn";
        protected static final String UNIT_ZOOM_OUT = "unitZoomOut";
        protected static final String ZOOM_IN = "zoomIn";
        protected static final String ZOOM_OUT = "zoomOut";

        /** The {@code Action}'s identifier. */
        protected String name;

        public Actions(@NotNull String name) { this.name = name; }

        /** @return the {@code Action}'s identifier */
        public final String getName() { return name; }

        @Override
        public Object getValue(@NotNull String key) {
            if (key.equals(NAME)) { return name; }
            return null;
        }

        @Override
        public boolean isEnabled() { return accept(null); }

        @Override
        public boolean accept(@Nullable Object sender) { return true; }

        // Mutator methods; Actions is immutable and therefore these methods have no implementation
        @Override public void putValue(String key, Object value) {}
        @Override public void setEnabled(boolean b) {}
        @Override public void addPropertyChangeListener(PropertyChangeListener listener) {}
        @Override public void removePropertyChangeListener(PropertyChangeListener listener) {}

        @Override
        public void actionPerformed(@NotNull ActionEvent e) {
            ZoomPane zoomPane = (ZoomPane) e.getSource();
            boolean ltr = zoomPane.getComponentOrientation().isLeftToRight();
            String key = getName();

            switch(key) {
                case SCROLL_HOME -> { zoomToFit(zoomPane); }
                case UNIT_SCROLL_UP -> { scroll(zoomPane, SwingConstants.VERTICAL, -1, false); }
                case UNIT_SCROLL_DOWN -> { scroll(zoomPane, SwingConstants.VERTICAL, 1, false); }
                case UNIT_SCROLL_LEFT -> { scroll(zoomPane, SwingConstants.HORIZONTAL, ltr ? -1 : 1, false); }
                case UNIT_SCROLL_RIGHT -> { scroll(zoomPane, SwingConstants.HORIZONTAL, ltr ? 1 : -1, false); }
                case SCROLL_UP -> { scroll(zoomPane, SwingConstants.VERTICAL, -1, true); }
                case SCROLL_DOWN -> { scroll(zoomPane, SwingConstants.VERTICAL, 1, true); }
                case SCROLL_LEFT -> { scroll(zoomPane, SwingConstants.HORIZONTAL, ltr ? -1 : 1, true); }
                case SCROLL_RIGHT -> { scroll(zoomPane, SwingConstants.HORIZONTAL, ltr ? 1 : -1, true); }
                case UNIT_ZOOM_IN -> { zoom(zoomPane, 1, null); }
                case UNIT_ZOOM_OUT -> { zoom(zoomPane, -1, null); }
                case ZOOM_IN -> { zoom(zoomPane, 2.5, null); }
                case ZOOM_OUT -> { zoom(zoomPane, -2.5, null); }
            }
        }

        // ---------------------------------------
        //           Viewport Navigation
        // ---------------------------------------

        /**
         * Scroll the view along the horizontal or vertical orientation in some direction.
         * <p>
         * <b>Note:</b> this method considers one block scroll to be the extent size of the viewport minus
         * the area defined by the {@code viewPadding} property, not the entire extent size of the viewport.
         *
         * @param zoomPane the {@code ZoomPane} performing the scroll
         * @param orientation axis along which to scroll: either {@code SwingConstants.VERTICAL} or
         *                    {@code SwingConstants.HORIZONTAL}
         * @param direction signed direction in which to scroll along the axis
         * @param block flag for whether to do a block scroll or a unit scroll
         */
        protected static void scroll(@NotNull ZoomPane zoomPane, int orientation, double direction, boolean block) {
            if (!viewportViewReady(zoomPane)) { return; }

            ((ZoomPaneUI) zoomPane.getUI()).isFit = false;
            ZoomViewport viewport = (ZoomViewport) zoomPane.getViewport();
            Rectangle2D visRect = viewport.getViewRect();
            Insets padding = getViewPadding(zoomPane);
            double scaleFactor = viewport.getScaleFactor();
            boolean vertical = orientation == SwingConstants.VERTICAL;
            double scrollAmount;

            scrollAmount = (vertical) ?
                    visRect.getHeight() - (padding.top + padding.bottom) / scaleFactor :
                    visRect.getWidth() - (padding.left + padding.right) / scaleFactor;
            if (!block) { scrollAmount /= 4.0; }

            Point2D viewPos = new Point2D.Double(
                    visRect.getX() + (vertical ? 0 : scrollAmount * direction),
                    visRect.getY() + (vertical ? scrollAmount * direction : 0)
            );
            viewport.setViewPosition(clampViewPos(zoomPane, viewPos));
        }

        /**
         * Zoom the view to a specified zoom level {@code [0, 1]} about a focus point.
         *
         * @param zoomPane the {@code ZoomPane} performing the zoom
         * @param zoom the zoom level to set
         * @param focus the focus point to zoom about in view coordinates. If {@code null}, it will be set to
         *              the center of the current display state
         */
        protected static void zoomTo(@NotNull ZoomPane zoomPane, double zoom, @Nullable Point2D focus) {
            if (!viewportViewReady(zoomPane)) { return; }

            ((ZoomPaneUI) zoomPane.getUI()).isFit = false;
            ZoomViewport viewport = (ZoomViewport) zoomPane.getViewport();
            double oldScaleFactor = viewport.getScaleFactor();
            double newScaleFactor = zoomToScaleFactor(zoomPane, Math.clamp(zoom, 0, 1));
            if (newScaleFactor != oldScaleFactor) {

                Rectangle2D viewRect = viewport.getPreciseViewRect();
                if (focus == null) { focus = new Point2D.Double(viewRect.getCenterX(), viewRect.getCenterY()); }

                viewport.setScaleFactor(newScaleFactor);

                double scaleRatio = newScaleFactor / oldScaleFactor;
                viewport.setViewPosition(clampViewPos(zoomPane, new Point2D.Double(
                        focus.getX() - (focus.getX() - viewRect.getX()) / scaleRatio,
                        focus.getY() - (focus.getY() - viewRect.getY()) / scaleRatio
                )));
            }
        }

        /**
         * Zoom into or out of the view by some amount. Since zoom level operates on a {@code double}-precision
         * {@code [0, 1]} scale, an {@code amount >= 1} will zoom all the way in and an {@code amount <= -1}
         * will zoom all the way out.
         * <p>
         * <b>Note:</b> this function does not take sensitivity into account. For an equivalent zooming method with
         * sensitivity, use {@link #zoom(ZoomPane, double, Point2D)}.
         *
         * @param zoomPane the {@code ZoomPane} performing the zoom
         * @param amount the amount {@code [-1, 1]} to zoom.
         * @param focus the focus point to zoom about in view coordinates.
         */
        public static void zoomBy(@NotNull ZoomPane zoomPane, double amount, @Nullable Point2D focus) {
            if (!viewportViewReady(zoomPane)) { return; }

            double currentZoom = scaleFactorToZoom(zoomPane, ((ZoomViewport) zoomPane.getViewport()).getScaleFactor());
            double newZoom = Math.clamp(currentZoom + amount, 0, 1);
            if (newZoom != currentZoom) { zoomTo(zoomPane, newZoom, focus); }
        }

        /**
         * Zoom into or out of the view by some number of "steps" or wheel ticks.
         *
         * @param zoomPane the {@code ZoomPane} performing the zoom
         * @param steps the number of steps to zoom by. Zoom in if {@code steps > 0},
         *              zoom out if {@code steps < 0}, unless the sensitivity is negative.
         * @param focus the focus point to zoom about in view coordinates.
         */
        public static void zoom(@NotNull ZoomPane zoomPane, double steps, @Nullable Point2D focus) {
            if (!viewportViewReady(zoomPane)) { return; }
            if (steps != 0) { zoomBy(zoomPane, steps * zoomPane.getSensitivity(), focus); }
        }

        /**
         * Zoom the view to fit the viewport on its relatively longest side.
         *
         * @param zoomPane the {@code ZoomPane} performing the fit
         */
        public static void zoomToFit(@NotNull ZoomPane zoomPane) {
            if (!readyToFit(zoomPane)) { return; }

            ((ZoomPaneUI) zoomPane.getUI()).isFit = true;
            ZoomViewport viewport = (ZoomViewport) zoomPane.getViewport();
            Insets padding = getViewPadding(zoomPane);
            double scaleFactor = clampScaleFactor(zoomPane, Math.min(
                    (viewport.getWidth() - padding.left - padding.right) / viewport.getViewSize().getWidth(),
                    (viewport.getHeight() - padding.top - padding.bottom) / viewport.getViewSize().getHeight()
            ));

            viewport.setScaleFactor(scaleFactor);
            viewport.setViewPosition(clampViewPos(zoomPane,
                    new Point2D.Double(-padding.left / scaleFactor, -padding.top / scaleFactor)));
        }

        // ---------------------------
        //           Helpers
        // ---------------------------

        /** @return {@code true} if the viewport view is ready to be manipulated, {@code false} otherwise */
        protected static boolean viewportViewReady(@NotNull ZoomPane zoomPane) {
            ZoomViewport viewport = (ZoomViewport) zoomPane.getViewport();
            if (viewport == null || viewport.getZoomView() == null) { return false; }
            Dimension viewSize = viewport.getViewSize();
            return viewSize.width > 0 && viewSize.height > 0;
        }

        /** @return {@code true} if viewport is ready to fit its view, {@code false} otherwise. */
        protected static boolean readyToFit(@NotNull ZoomPane zoomPane) {
            JViewport viewport = zoomPane.getViewport();
            return viewportViewReady(zoomPane)
                    && viewport.isValid() && viewport.getWidth() > 0 && viewport.getHeight() > 0;
        }

        /**
         * Convert a scale factor value to a zoom level based on the {@code ZoomPane}'s zoom limits.
         *
         * @param zoomPane the zoom pane doing the conversion
         * @param scaleFactor the scale factor to be converted
         * @return the zoom level of {@code scaleFactor} in {@code zoomPane}
         */
        protected static double scaleFactorToZoom(@NotNull ZoomPane zoomPane, double scaleFactor) {
            double min = getMinScaleFactor(zoomPane);
            double max = getMaxScaleFactor(zoomPane);
            if (max == min) { return 0; } // Avoid divide by 0
            return Math.log(scaleFactor / min) / Math.log(max / min);
        }

        /**
         * Convert a zoom level to a scale factor value based on the {@code ZoomPane}'s zoom limits.
         *
         * @param zoomPane the zoom pane doing the conversion
         * @param zoom the zoom level to be converted
         * @return the scale factor value of {@code zoom} in {@code zoomPane}
         */
        protected static double zoomToScaleFactor(@NotNull ZoomPane zoomPane, double zoom) {
            double min = getMinScaleFactor(zoomPane);
            double max = getMaxScaleFactor(zoomPane);
            return min * Math.pow(max / min, zoom);
        }

        /** @return the minimum scale factor value of some {@code ZoomPane} */
        protected static double getMinScaleFactor(@NotNull ZoomPane zoomPane) {
            double minScaleFactor = zoomPane.getMinScaleFactor();
            if (!viewportViewReady(zoomPane) || !zoomPane.isMinScaleRelative()) { return minScaleFactor; }

            ZoomViewport viewport = (ZoomViewport) zoomPane.getViewport();
            return minScaleFactor * Math.min(
                    viewport.getWidth() / viewport.getViewSize().getWidth(),
                    viewport.getHeight() / viewport.getViewSize().getHeight()
            );
        }

        /** @return the maximum scale factor value of some {@code ZoomPane} */
        protected static double getMaxScaleFactor(@NotNull ZoomPane zoomPane) {
            double maxScaleFactor = zoomPane.getMaxScaleFactor();
            if (!viewportViewReady(zoomPane) || zoomPane.isMaxScaleRelative()) { return maxScaleFactor; }

            ZoomViewport viewport = (ZoomViewport) zoomPane.getViewport();
            return maxScaleFactor * Math.max(
                    viewport.getWidth() / viewport.getViewSize().getWidth(),
                    viewport.getHeight() / viewport.getViewSize().getHeight()
            );
        }

        /** @return {@code scaleFactor} clamped to the zoom limits of some {@code ZoomPane} */
        protected static double clampScaleFactor(@NotNull ZoomPane zoomPane, double scaleFactor) {
            double min = getMinScaleFactor(zoomPane);
            double max = getMaxScaleFactor(zoomPane);
            return (min < max) ? Math.clamp(scaleFactor, min, max) : (min + max) / 2.0;
        }

        /** @return {@code viewPos} clamped to the valid view position space of some {@code ZoomPane} */
        protected static Point2D clampViewPos(@NotNull ZoomPane zoomPane, @NotNull Point2D viewPos) {
            ZoomViewport viewport = (ZoomViewport) zoomPane.getViewport();
            Dimension viewSize = viewport.getViewSize();
            Dimension2D extentSize = viewport.getPreciseExtentSize();
            Insets padding = getViewPadding(zoomPane);
            double scaleFactor = viewport.getScaleFactor();

            double minX = -padding.left / scaleFactor;
            double minY = -padding.top / scaleFactor;
            double maxX = viewSize.width - extentSize.getWidth() + padding.right / scaleFactor;
            double maxY = viewSize.height - extentSize.getHeight() + padding.bottom / scaleFactor;

            viewPos.setLocation(
                    (minX < maxX) ?
                            Math.clamp(viewPos.getX(), minX, maxX) :
                            (minX + maxX) / 2,
                    (minY < maxY) ?
                            Math.clamp(viewPos.getY(), minY, maxY) :
                            (minY + maxY) / 2
            );
            return viewPos;
        }
    }

    /** The handler for all AWT events this UI needs to process. */
    private class Handler implements ActionListener, ChangeListener, MouseWheelListener,
            PropertyChangeListener, ComponentListener {

        // Used by componentResized to remember old viewport size
        private Dimension oldSize = new Dimension();

        // MouseWheelListener: This is installed on the ZoomViewport.
        @Override
        public void mouseWheelMoved(@NotNull MouseWheelEvent e) {
            if (scrollpane.isWheelScrollingEnabled() && Actions.viewportViewReady((ZoomPane) scrollpane)
                    && ((ZoomPane) scrollpane).getSensitivity() != 0) {
                ZoomViewport viewport = (ZoomViewport) scrollpane.getViewport();

                double rotation = e.getWheelRotation();
                if (isSmoothScrollingEnabled()) { rotation = e.getPreciseWheelRotation(); }

                if (rotation == 0) { return; }
                e.consume();

                if (e.getScrollType() == MouseWheelEvent.WHEEL_BLOCK_SCROLL) { rotation *= 2.5; }
                Actions.zoom((ZoomPane) scrollpane, -rotation, viewport.toViewCoordinates((Point2D) e.getPoint()));

                viewport.getPeer().dispatchEvent(new MouseEvent(
                        viewport.getPeer(), MouseEvent.MOUSE_MOVED, e.getWhen(), 0,
                        e.getX(), e.getY(), 0, false
                ));
            }
        }

        // ChangeListener: This in installed on the ZoomBar and both ScrollBar models.
        @Override
        public void stateChanged(@NotNull ChangeEvent e) {

            if (scrollpane.getViewport() != null && !ignoreStateChanges) {

                if (e.getSource() == scrollpane.getViewport()) { syncScrollPaneWithViewport(); }

                else {
                    JScrollBar hsb = scrollpane.getHorizontalScrollBar();
                    if (hsb != null && e.getSource() == hsb.getModel()) { sbStateChanged(e, false); }

                    else {
                        JScrollBar vsb = scrollpane.getVerticalScrollBar();
                        if (vsb != null && e.getSource() == vsb.getModel()) { sbStateChanged(e, true); }

                        else {
                            ZoomBar zb = ((ZoomPane) scrollpane).getZoomBar();
                            if (zb != null && e.getSource() == zb.getModel()) { zbStateChanged(e); }
                        }
                    }
                }
            }
        }

        private void sbStateChanged(@NotNull ChangeEvent e, boolean isVertical) {
            ZoomViewport viewport = (ZoomViewport) scrollpane.getViewport();
            BoundedRangeModel model = (BoundedRangeModel) e.getSource();
            Dimension2D extentSize = viewport.getPreciseExtentSize();
            Dimension viewSize = viewport.getViewSize();
            Point2D p = viewport.getPreciseViewPosition();
            Insets padding = getViewPadding((ZoomPane) scrollpane);
            double scaleFactor = viewport.getScaleFactor();

            double normValue = model.getExtent() == model.getMaximum() - model.getMinimum() ? 0 :
                    (double) (model.getValue() - model.getMinimum()) /
                            (model.getMaximum() - model.getExtent() - model.getMinimum());
            double viewMin, viewMax;

            if (isVertical) {
                viewMin = -padding.top / scaleFactor;
                viewMax = viewSize.height - extentSize.getHeight() + padding.bottom / scaleFactor;
            }
            else {
                viewMin = -padding.left / scaleFactor;
                viewMax = viewSize.width - extentSize.getWidth() + padding.right / scaleFactor;
            }
            double viewValue = viewMin + normValue * (viewMax - viewMin);
            p.setLocation(isVertical ? p.getX() : viewValue, isVertical ? viewValue : p.getY());

            ignoreStateChanges = true;
            viewport.setViewPosition(Actions.clampViewPos((ZoomPane) scrollpane, p));
            ignoreStateChanges = false;
        }

        private void zbStateChanged(@NotNull ChangeEvent e) {
            BoundedRangeModel model = (BoundedRangeModel) e.getSource();
            double min = model.getMinimum();
            double max = model.getMaximum();
            double value = model.getValue();
            // Can't ignore state changes because scrollbars may need to be updated;
            // viewport sync will ignore for us
            Actions.zoomTo((ZoomPane) scrollpane, (value - min) / (max - min), null);
        }

        // ActionListener: This is installed on the ZoomBar fit button.
        @Override
        public void actionPerformed(@NotNull ActionEvent e) { Actions.zoomToFit((ZoomPane) scrollpane); }

        // PropertyChangeListener: this is installed on the ZoomPane and ZoomBar.
        @Override
        public void propertyChange(@NotNull PropertyChangeEvent e) {

            if (e.getSource() == scrollpane) {
                if (e.getPropertyName().equals("zoomBar")) { updateZoomBar(e); }
            }
            else if (e.getSource() == scrollpane.getViewport()) { vpPropertyChange(e); }
            else if (e.getSource() == ((ZoomPane) scrollpane).getZoomBar()) { zbPropertyChange(e); }
        }

        private void vpPropertyChange(@NotNull PropertyChangeEvent e) {
            if (e.getPropertyName().equals("view")) {
                if (e.getNewValue() != null) {
                    if (Actions.readyToFit((ZoomPane) scrollpane)) {
                        Actions.zoomToFit((ZoomPane) scrollpane);
                        waitingToFit = false;
                    }
                    else { waitingToFit = true; }
                }
            }
        }

        private void zbPropertyChange(@NotNull PropertyChangeEvent e) {

            if (e.getPropertyName().equals("model")) {

                BoundedRangeModel oldModel = (BoundedRangeModel) e.getOldValue();
                BoundedRangeModel newModel = (BoundedRangeModel) e.getNewValue();

                if (oldModel != null) { oldModel.removeChangeListener(zbChangeListener); }
                if (newModel != null) { newModel.addChangeListener(zbChangeListener); }
            }
            else if (e.getPropertyName().equals("fitButton")) {

                JButton oldButton = (JButton) e.getOldValue();
                JButton newButton = (JButton) e.getNewValue();

                if (oldButton != null) { oldButton.removeActionListener(zbFitListener); }
                if (newButton != null) { newButton.addActionListener(zbFitListener); }
            }
        }

        // ComponentListener: this is installed on the ZoomViewport.
        @Override
        public void componentResized(@NotNull ComponentEvent e) {
            ZoomViewport viewport = (ZoomViewport) scrollpane.getViewport();
            if (e.getComponent() == viewport) {

                // If viewport has been resized from 0, may need to fit the view
                if (waitingToFit && Actions.readyToFit((ZoomPane) scrollpane)) {
                    Actions.zoomToFit((ZoomPane) scrollpane);
                    waitingToFit = false;
                }

                Dimension newSize = e.getComponent().getSize();
                if (newSize.width > 0 && newSize.height > 0) {
                    if (oldSize.width > 0 && oldSize.height > 0) {

                        Rectangle2D viewRect = viewport.getPreciseViewRect();
                        double widthRatio = (double) newSize.width / oldSize.width;
                        double heightRatio = (double) newSize.height / oldSize.height;

                        viewport.setViewPosition(new Point2D.Double(
                                viewRect.getCenterX() - (viewRect.getCenterX() - viewRect.getX()) * widthRatio,
                                viewRect.getCenterY() - (viewRect.getCenterY() - viewRect.getY()) * heightRatio
                        ));
                    }

                    oldSize = newSize;
                }
            }
        }

        @Override public void componentMoved(ComponentEvent e) {}
        @Override public void componentShown(ComponentEvent e) {}
        @Override public void componentHidden(ComponentEvent e) {}
    }

    /** The layout manager for this UI. */
    protected static class ZoomPaneLayout extends FlatScrollPaneLayout implements ZoomPaneConstants {

        /**
         * The zoompane's zoombar child. Default is a {@code ZoomBar}.
         * @see ZoomPane#setZoomBar(ZoomBar)
         */
        protected ZoomBar zb;

        @Override
        public void syncWithScrollPane(@NotNull JScrollPane sp) {
            super.syncWithScrollPane(sp);
            zb = ((ZoomPane) sp).getZoomBar();
        }

        /**
         * {@inheritDoc}
         * <ul><li>ZoomPaneConstants.ZOOMBAR</li></ul>
         *
         * @param s {@inheritDoc}
         * @param c {@inheritDoc}
         * @throws IllegalArgumentException {@inheritDoc}
         */
        @Override
        public void addLayoutComponent(@NotNull String s, @Nullable Component c) {
            if (s.equals(ZOOMBAR)) { zb = (ZoomBar) addSingletonComponent(zb, c); }
            else { super.addLayoutComponent(s, c); }
        }

        @Override
        public void removeLayoutComponent(@NotNull Component c) {
            if (c == zb) { zb = null; }
            else { super.removeLayoutComponent(c); }
        }

        /**
         * {@inheritDoc}
         * <p>
         * Lastly, the preferred size of the zoombar is added given the current zoombar displayPolicy.
         *
         * @param parent {@inheritDoc}
         * @return {@inheritDoc}
         */
        @Override
        public Dimension preferredLayoutSize(@NotNull Container parent) {
            Dimension superSize = super.preferredLayoutSize(parent);
            int zbPolicy = ((ZoomPane) parent).getZoomBarPolicy();

            if (zb != null && zbPolicy == ZOOMBAR_ALWAYS) { superSize.height += zb.getPreferredSize().height; }
            return superSize;
        }

        /**
         * {@inheritDoc} Lastly, the minimum size of the zoombar is added given its displayPolicy isn't NEVER.
         *
         * @param parent {@inheritDoc}
         * @return {@inheritDoc}
         */
        @Override
        public Dimension minimumLayoutSize(@NotNull Container parent) {
            Dimension superSize = super.minimumLayoutSize(parent);
            int zbPolicy = ((ZoomPane) parent).getZoomBarPolicy();

            if (zb != null && zbPolicy != ZOOMBAR_NEVER) {
                Dimension size = zb.getMinimumSize();
                superSize.height += size.height;
                superSize.width = Math.max(superSize.width, size.width);
            }
            return superSize;
        }

        /**
         * {@inheritDoc}
         * <ul>
         *     <li>If a zoombar is needed, i.e. if the {@code displayPolicy} is ALWAYS, it gets its preferred
         *     height and the width of its parent. All previous components' dimensions and positions are then
         *     adjusted to account for these new constraints.</li>
         * </ul>
         *
         * @param parent {@inheritDoc}
         */
        @Override
        public void layoutContainer(@NotNull Container parent) {
            super.layoutContainer(parent);
            int zbPolicy = ((ZoomPane) parent).getZoomBarPolicy();

            Rectangle availR = parent.getBounds();
            Insets insets = parent.getInsets();
            availR.x = insets.left;
            availR.y = insets.top;
            availR.width -= insets.left + insets.right;
            availR.height -= insets.top + insets.bottom;

            if (zb != null && zbPolicy != ZOOMBAR_NEVER) {

                int zbHeight = zb.getPreferredSize().height;
                zb.setBounds(new Rectangle(availR.x, availR.height - zbHeight, availR.width, zbHeight));

                if (rowHead != null) {
                    Rectangle rowHeadBounds = rowHead.getBounds();
                    rowHeadBounds.height -= zbHeight;
                    rowHead.setBounds(rowHeadBounds);
                }
                if (viewport != null) {
                    Rectangle viewportBounds = viewport.getBounds();
                    viewportBounds.height -= zbHeight;
                    viewport.setBounds(viewportBounds);
                }
                if (vsb != null) {
                    Rectangle vsbBounds = vsb.getBounds();
                    vsbBounds.height -= zbHeight;
                    vsb.setBounds(vsbBounds);
                }
                if (hsb != null) {
                    Rectangle hsbBounds = hsb.getBounds();
                    hsbBounds.y -= zbHeight;
                    hsb.setBounds(hsbBounds);
                }
            }
        }
    }
}
