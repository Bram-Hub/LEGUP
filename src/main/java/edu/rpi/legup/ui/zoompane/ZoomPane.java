package edu.rpi.legup.ui.zoompane;

import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.beans.BeanProperty;
import java.beans.PropertyChangeEvent;
import java.beans.Transient;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRelation;
import javax.swing.*;
import javax.swing.plaf.ScrollPaneUI;
import javax.swing.plaf.UIResource;

/**
 * Provides an interactive window for the display of and interaction with {@link ZoomView} objects. A
 * {@code ZoomPane} manages a {@link ZoomViewport}, vertical and horizontal {@link JScrollBar}s, and
 * a {@link ZoomBar}.
 * <p>
 * Due to AWT and Swing limitations, only {@link ZoomView} and its subclasses can be used as the view of
 * a {@code ZoomPane}, whereas {@link JScrollPane} and {@link JViewport} typically display {@link Component}s
 * and {@link JComponent}s. {@code ZoomPane} and {@link ZoomViewport} are still subclasses of
 * those classes despite these fundamental differences to preserve behavioral and styling consistency.
 * <p>
 * <b>Note:</b> if this component's performance impact is of concern, smooth scrolling can be disabled
 * by setting the {@code ZoomPane.smoothScrolling} client property or UI default to false.
 *
 * @see JScrollPane
 * @see ZoomViewport
 * @see ZoomView
 */
public class ZoomPane extends JScrollPane implements ZoomPaneConstants {

    private static final String uiClassID = "ZoomPaneUI";

    /**
     * The display policy for the zoombar. The default is {@code ZoomPaneConstants.ZOOMBAR_ALWAYS}.
     * @see #setZoomBarPolicy
     */
    protected int zoomBarPolicy = ZOOMBAR_ALWAYS;

    /** The zoom pane's zoom bar child. Default is a {@code ZoomBar}. */
    protected ZoomBar zoomBar;

    // ---------- Zoom behavior settings ----------

    /** Zooming sensitivity: {@code (1 / # steps to cover entire zoom range)}. */
    protected double sensitivity = 0.1;

    /** The minimum scale factor that can be applied to the viewport's view. */
    protected double minScaleFactor = 1.0 / 3.0;

    /**
     * Flag for whether the minimum scale factor is a direct scale factor value or a ratio for how many
     * multiples of the view's size should be able to fit into the viewport on its relatively longest side.
     */
    protected boolean minScaleRelative = true;

    /** The maximum scale factor that can be applied to the viewport's view. */
    protected double maxScaleFactor = 5.0;

    /**
     * Flag for whether the maximum scale factor is a direct scale factor value or a ratio for how many
     * multiples of the view's size should be able to fit into the viewport on its relatively shortest side.
     */
    protected boolean maxScaleRelative = false;

    /**
     * Creates a {@code ZoomPane} that displays the zoom view in a zoom viewport whose view
     * position can be controlled with a pair of scrollbars and whose zoom level can be controlled
     * with a zoom bar.
     *
     * @param view the zoomable view to display in the zoom pane's viewport
     *
     * @see #setViewportView(ZoomView)
     */
    public ZoomPane(@Nullable ZoomView view) {
        setLayout(new ScrollPaneLayout.UIResource());
        setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
        setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_ALWAYS);
        setZoomBarPolicy(ZOOMBAR_ALWAYS);
        setViewport((ZoomViewport) createViewport());
        setVerticalScrollBar(createVerticalScrollBar());
        setHorizontalScrollBar(createHorizontalScrollBar());
        setZoomBar(createZoomBar());
        if (view != null) { setViewportView(view); }
        setOpaque(true);
        updateUI();
    }

    /** Creates an empty (no view) {@code ZoomPane}. */
    public ZoomPane() { this(null); }

    /**
     * Sets the {@code ZoomPaneUI} object that provides the look and feel (L&amp;F) for this component.
     *
     * @param ui the {@code ZoomPaneUI} L&amp;F object
     * @see #getUI
     */
    public void setUI(@Nullable ZoomPaneUI ui) { super.setUI(ui); }

    /**
     * {@inheritDoc}
     * @deprecated use {@link #setUI(ZoomPaneUI)} instead
     *
     * @param ui {@inheritDoc}
     * @throws IllegalArgumentException if ui is not an instance of {@code ZoomPaneUI}
     */
    @Deprecated
    @Override
    public void setUI(@Nullable ScrollPaneUI ui) {
        if (ui instanceof ZoomPaneUI) { setUI((ZoomPaneUI) ui); }
        else { throw new IllegalArgumentException("ZoomPane's UI delegate must be an instance of ZoomPaneUI"); }
    }

    /**
     * Replaces the current {@code ZoomPaneUI} object with a version from the current default look and feel.
     * To be called when the default look and feel changes.
     *
     * @see JComponent#updateUI()
     * @see UIManager#getUI(javax.swing.JComponent)
     */
    @Override
    public void updateUI() { setUI((ZoomPaneUI) UIManager.getUI(this)); }

    /**
     * {@inheritDoc}
     * @return the string "ZoomPaneUI"
     * @see JScrollPane#getUIClassID()
     */
    @Override
    public String getUIClassID() { return uiClassID; }

    /**
     * Determines when the vertical scrollbar appears in the {@code ZoomPane}.
     * Legal values are:
     * <ul>
     * <li>{@code ZoomPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED}
     * <li>{@code ZoomPaneConstants.VERTICAL_SCROLLBAR_NEVER}
     * <li>{@code ZoomPaneConstants.VERTICAL_SCROLLBAR_ALWAYS}
     * </ul>
     * <p>
     * <b>Warning:</b> using {@code ZoomPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED} may result in jumps in
     * view size and position as a result of creating space for the scroll bars.
     *
     * @param policy {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     * @see JScrollPane#setVerticalScrollBarPolicy(int)
     */
    @Override
    public void setVerticalScrollBarPolicy(int policy) { super.setVerticalScrollBarPolicy(policy); }

    /**
     * Determines when the horizontal scrollbar appears in the {@code ZoomPane}.
     * Legal values are:
     * <ul>
     * <li>{@code ZoomPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED}
     * <li>{@code ZoomPaneConstants.HORIZONTAL_SCROLLBAR_NEVER}
     * <li>{@code ZoomPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS}
     * </ul>
     * <p>
     * <b>Warning:</b> using {@code ZoomPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED} may result in jumps in
     * view size and position as a result of creating space for the scroll bars.
     *
     * @param policy {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     * @see JScrollPane#setHorizontalScrollBarPolicy(int)
     */
    @Override
    public void setHorizontalScrollBarPolicy(int policy) { super.setHorizontalScrollBarPolicy(policy); }

    @Override
    public Rectangle getViewportBorderBounds() {

        Rectangle borderR = super.getViewportBorderBounds();
        ZoomBar zb = getZoomBar();
        if (zb != null && zb.isVisible()) { borderR.height -= zb.getHeight(); }

        return borderR;
    }

    /** Scroll bar implementing {@code UIResource}. */
    private static class ScrollBarUIResource extends JScrollBar implements UIResource {

        public ScrollBarUIResource(@MagicConstant(intValues =
                {Adjustable.HORIZONTAL, Adjustable.VERTICAL}) int policy) {
            super(policy);
        }
    }

    /**
     * Returns a new {@code JScrollBar} by default. Subclasses may override this method to force
     * {@code ZoomPaneUI} implementations to use a {@code JScrollBar} subclass. Used by
     * {@code ZoomPaneUI} implementations to create the vertical scrollbar.
     *
     * @return {@inheritDoc}
     * @see JScrollBar
     */
    @Override
    public JScrollBar createVerticalScrollBar() { return new ScrollBarUIResource(JScrollBar.VERTICAL); }

    /**
     * Returns a new {@code JScrollBar} by default. Subclasses may override this method to force
     * {@code ZoomPaneUI} implementations to use a {@code JScrollBar} subclass. Used by
     * {@code ZoomPaneUI} implementations to create the horizontal scrollbar.
     *
     * @return {@inheritDoc}
     * @see JScrollBar
     */
    @Override
    public JScrollBar createHorizontalScrollBar() { return new ScrollBarUIResource(JScrollBar.HORIZONTAL); }

    /**
     * Returns the zoom bar policy value.
     *
     * @return the {@code zoomBarPolicy} property
     * @see #setZoomBarPolicy(int)
     */
    public int getZoomBarPolicy() { return zoomBarPolicy; }

    /**
     * Determines when the zoombar appears in the zoompane. The options are:
     * <ul>]
     *     <li>{@code ZoomPaneConstants.HORIZONTAL_SCROLLBAR_NEVER}
     *     <li>{@code ZoomPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS}
     * </ul>
     *
     * @param policy one of the two values listed above
     * @throws IllegalArgumentException if {@code policy} is not one of the legal values shown above
     * @see #getZoomBarPolicy
     */
    @BeanProperty(preferred = true,
            enumerationValues = {"ZoomPaneConstants.ZOOMBAR_NEVER", "ZoomPaneConstants.ZOOMBAR_ALWAYS"},
            description = "The ZoomPane ZoomBar policy")
    public void setZoomBarPolicy(int policy) {
        if (policy != ZOOMBAR_NEVER && policy != ZOOMBAR_ALWAYS) {
            throw new IllegalArgumentException("invalid zoomBarPolicy");
        }
        int old = zoomBarPolicy;
        zoomBarPolicy = policy;
        firePropertyChange("zoomBarPolicy", old, policy);
        revalidate();
        repaint();
    }

    private static class ZoomBarUIResource extends ZoomBar implements UIResource {}

    /**
     * Returns a new {@code ZoomBar} by default. Subclasses may override this method to force
     * {@code ZoomPaneUI} implementations to use a {@code ZoomBar} subclass. Used by
     * {@code ZoomPaneUI} implementations to create the zoom bar.
     *
     * @return a {@code ZoomBar}
     * @see ZoomBar
     */
    public ZoomBar createZoomBar() { return new ZoomBarUIResource(); }

    /**
     * Returns the zoom bar that controls the viewport's zoom level.
     *
     * @return the {@code zoomBar} property
     * @see #setZoomBar(ZoomBar)
     */
    @Transient
    public ZoomBar getZoomBar() { return zoomBar; }

    /**
     * Adds the zoom bar that controls the viewport's zoom to the zoom pane. This is usually unnecessary,
     * as {@code ZoomPane} creates a zoom bar by default.
     *
     * @param zoomBar the zoom bar to be added
     * @see #createZoomBar()
     * @see #getZoomBar()
     */
    @BeanProperty(expert = true, description = "The zoom bar.")
    public void setZoomBar(@Nullable ZoomBar zoomBar) {
        ZoomBar old = getZoomBar();
        this.zoomBar = zoomBar;
        if (zoomBar != null) { try { add(zoomBar, ZOOMBAR); } catch (IllegalArgumentException ignored) {;} }
        else if (old != null) { remove(old); }
        firePropertyChange("zoomBar", old, zoomBar);

        revalidate();
        repaint();
    }

    /**
     * Returns a new {@code ZoomViewport} by default. Used to create the viewport (as needed)
     * in {@link #setViewportView(ZoomView)}. Subclasses may override this method to return a subclass of
     * {@code JViewport}.
     *
     * @return a new {@code ZoomViewport}
     */
    @Override
    protected JViewport createViewport() { return new ZoomViewport(); }

    /**
     * Removes the old viewport (if there is one); syncs the scroll bars and zoom bar with the new viewport.
     * <p>
     * Most applications will find it more convenient to use {@link #setViewportView(ZoomView)} to add a viewport
     * and a view to the zoompane.
     *
     * @param viewport the new viewport to be used; if viewport is {@code null}, the old viewport is still
     *                 removed and the new viewport is set to {@code null}
     * @see #createViewport()
     * @see #getViewport()
     * @see #setViewportView(ZoomView)
     */
    public void setViewport(@Nullable ZoomViewport viewport) { super.setViewport(viewport); }

    /**
     * Removes the old viewport (if there is one); syncs the scroll bars and zoom bar with the new viewport.
     * <p>
     * Most applications will find it more convenient to use {@link #setViewportView(ZoomView)} to add a viewport
     * and a view to the zoompane.
     * @deprecated use {@link #setViewport(ZoomViewport)} instead
     *
     * @param viewport the new viewport to be used; if viewport is {@code null}, the old viewport is still
     *                 removed and the new viewport is set to {@code null}
     * @throws IllegalArgumentException if viewport is not an instance of {@code ZoomViewport} or {@code null}
     * @see #createViewport()
     * @see #getViewport()
     * @see #setViewportView(ZoomView)
     */
    @Deprecated
    @Override
    public void setViewport(@Nullable JViewport viewport) {
        if (viewport instanceof ZoomViewport || viewport == null) { setViewport((ZoomViewport) viewport); }
        else { throw new IllegalArgumentException("ZoomPane's viewport must be an instance of ZoomViewport"); }
    }

    /**
     * Returns the current {@code ZoomViewport}.
     *
     * @return the viewport property
     * @see #setViewport(ZoomViewport)
     */
    public ZoomViewport getZoomViewport() { return (ZoomViewport) getViewport(); }

    /**
     * Creates a viewport if necessary and then sets its view. Applications that don't provide the view
     * directly to the {@code ZoomPane} constructor should use this method to specify the {@code ZoomView}
     * child that's going to be displayed in the {@code ZoomPane}. For example:
     * <pre>
     * ZoomPane zoompane = new ZoomPane();
     * zoompane.setViewportView(zoomViewObject);
     * </pre>
     * Applications should not add children directly to the {@code ZoomPane}.
     *
     * @param view the view to add to the viewport
     * @see #setViewport(ZoomViewport)
     * @see ZoomViewport#setZoomView(ZoomView)
     */
    public void setViewportView(@Nullable ZoomView view) {
        if (getViewport() == null) { setViewport((ZoomViewport) createViewport()); }
        ((ZoomViewport) getViewport()).setZoomView(view);
    }

    /**
     * Creates a viewport if necessary and then sets its view.  Applications that don't provide the view
     * directly to the {@code ZoomPane} constructor should use this method to specify the {@code ZoomView}
     * child that's going to be displayed in the {@code ZoomPane}. For example:
     * <pre>
     * ZoomPane zoompane = new ZoomPane();
     * zoompane.setViewportView(zoomViewObject);
     * </pre>
     * Applications should not add children directly to the {@code ZoomPane}.
     * @deprecated use {@link #setViewportView(ZoomView)} instead; {@code ZoomViewport} accepts a
     * {@code ZoomView} as its view rather than a {@code Component}
     *
     * @param view {@inheritDoc}
     * @see #setViewport(ZoomViewport)
     * @see ZoomViewport#setZoomView(ZoomView)
     */
    @Deprecated
    @Override
    public void setViewportView(@Nullable Component view) {
        if (view == null) { setViewportView((ZoomView) null); }
        else { throw new IllegalArgumentException("ZoomPane's view cannot be set to a Component"); }
    }

    /**
     * Removes the old rowHeader (if it exists).
     * @deprecated implementing a rowHeader for {@code ZoomPane} is difficult and so far unnecessary, so
     * attempting to set the rowHeader to a non-{@code null} value will throw an exception.
     *
     * @param rowHeader {@inheritDoc}
     * @throws IllegalArgumentException if rowHeader is non-{@code null}
     * @see #getRowHeader()
     * @see #setRowHeaderView(Component)
     */
    @Deprecated
    @Override
    public void setRowHeader(JViewport rowHeader) {
        if (rowHeader == null) { super.setRowHeader(null); }
        else { throw new IllegalArgumentException("rowHeader for ZoomPane has not yet been implemented."); }
    }

    /**
     * Removes the old columnHeader (if it exists).
     * @deprecated implementing a columnHeader for {@code ZoomPane} is difficult and so far unnecessary,
     * so attempting to set the columnHeader to a non-{@code null} value will throw an exception.
     *
     * @param columnHeader {@inheritDoc}
     * @throws IllegalArgumentException if columnHeader is non-{@code null}
     * @see #getColumnHeader()
     * @see #setColumnHeaderView(Component)
     */
    @Deprecated
    @Override
    public void setColumnHeader(JViewport columnHeader) {
        if (columnHeader == null) { super.setColumnHeader(null); }
        else { throw new IllegalArgumentException("columnHeader for ZoomPane has not yet been implemented."); }
    }

    /**
     * Sets the orientation for the vertical and horizontal scroll bars and the zoom bar as determined
     * by the {@code ComponentOrientation} argument.
     *
     * @param co {@inheritDoc}
     * @see java.awt.ComponentOrientation
     */
    @Override
    public void setComponentOrientation(ComponentOrientation co) {
        super.setComponentOrientation(co);
        if (zoomBar != null) { zoomBar.setComponentOrientation(co); }
    }

    /**
     * Returns the sensitivity of zooming on this {@code ZoomPane}.
     *
     * @return zooming sensitivity
     * @see #setSensitivity(double)
     */
    @BeanProperty(description = "Sensitivity of zooming")
    public double getSensitivity() { return sensitivity; }

    /**
     * Sets the sensitivity of zooming on this {@code ZoomPane}. To gauge a good value use the formula
     * {@code sensitivity = 1 / steps} where steps is the number of scroll wheel ticks to cover the zoom range.
     *
     * @param newSensitivity the new sensitivity value to set
     * @see #getSensitivity()
     */
    @BeanProperty(description = "Sensitivity of zooming")
    public void setSensitivity(double newSensitivity) {
        double old = sensitivity;
        sensitivity = newSensitivity;
        firePropertyChange("sensitivity", old, sensitivity);
    }

    /**
     * Returns the minimum scale factor to be applied to the viewport's view. If {@code minScaleRelative}
     * is {@code true}, this value should instead be used as the ratio of how many of the view's size
     * should be able to fit into the viewport on its relatively longest size.
     *
     * @return the minimum scale factor
     * @see #setMinScaleFactor(double)
     * @see #isMinScaleRelative()
     * @see #getMaxScaleFactor()
     */
    @BeanProperty(description = "Minimum scale factor")
    public double getMinScaleFactor() { return minScaleFactor; }

    /**
     * Sets the minimum scale factor to be applied to the viewport's view. If {@code minScaleRelative}
     * is {@code true}, this value will instead be used as the ratio of how many of the view's size
     * should be able to fit into the viewport on its relatively longest size.
     *
     * @param newMinScaleFactor the new minimum scale factor to set
     * @see #getMinScaleFactor()
     * @see #setMinScaleRelative(boolean)
     * @see #setMaxScaleFactor(double)
     */
    @BeanProperty(description = "Minimum scale factor")
    public void setMinScaleFactor(double newMinScaleFactor) {
        double old = minScaleFactor;
        minScaleFactor = newMinScaleFactor;
        firePropertyChange("minScaleFactor", old, minScaleFactor);
    }

    /**
     * Returns whether {@code minScaleFactor} is a direct scale factor value or a ratio for how many
     * multiples of the view's size should be able to fit into the viewport on its relatively longest side.
     *
     * @return {@code true} if the {@code minScaleFactor} is relative, {@code false} otherwise
     * @see #getMinScaleFactor()
     * @see #setMinScaleRelative(boolean)
     */
    @BeanProperty(description = "Flag for whether the minimum scale factor is relative to its fit")
    public boolean isMinScaleRelative() { return minScaleRelative; }

    /**
     * Sets whether {@code minScaleFactor} is a direct scale factor value or a ratio for how many
     * multiples of the view's size should be able to fit into the viewport on its relatively longest side.
     *
     * @param newMinScaleRelative {@code true} if the {@code minScaleFactor} should be relative,
     * {@code false} otherwise
     * @see #setMinScaleFactor(double)
     * @see #isMinScaleRelative()
     */
    @BeanProperty(description = "Flag for whether the minimum scale factor is relative to its fit")
    public void setMinScaleRelative(boolean newMinScaleRelative) {
        boolean old = minScaleRelative;
        minScaleRelative = newMinScaleRelative;
        firePropertyChange("minScaleRelative", old, minScaleRelative);
    }

    /**
     * Returns the maximum scale factor to be applied to the viewport's view. If {@code maxScaleRelative}
     * is {@code true}, this value should instead be used as the ratio of how many of the view's size
     * should be able to fit into the viewport on its relatively shortest size.
     *
     * @return the maximum scale factor
     * @see #setMaxScaleFactor(double)
     * @see #isMaxScaleRelative()
     * @see #getMinScaleFactor()
     */
    @BeanProperty(description = "Maximum scale factor")
    public double getMaxScaleFactor() { return maxScaleFactor; }

    /**
     * Sets the maximum scale factor to be applied to the viewport's view. If {@code maxScaleRelative}
     * is {@code true}, this value will instead be used as the ratio of how many of the view's size
     * should be able to fit into the viewport on its relatively shortest size.
     *
     * @param newMaxScaleFactor the new maximum scale factor to set
     * @see #getMaxScaleFactor()
     * @see #setMaxScaleRelative(boolean)
     * @see #setMinScaleFactor(double)
     */
    @BeanProperty(description = "Maximum scale factor")
    public void setMaxScaleFactor(double newMaxScaleFactor) {
        double old = maxScaleFactor;
        maxScaleFactor = newMaxScaleFactor;
        firePropertyChange("maxScaleFactor", old, maxScaleFactor);
    }

    /**
     * Returns whether {@code maxScaleFactor} is a direct scale factor value or a ratio for how many
     * multiples of the view's size should be able to fit into the viewport on its relatively shortest side.
     *
     * @return {@code true} if the {@code maxScaleFactor} is relative, {@code false} otherwise
     * @see #getMaxScaleFactor()
     * @see #setMaxScaleRelative(boolean)
     */
    @BeanProperty(description = "Flag for whether the maximum scale factor is relative to its fit")
    public boolean isMaxScaleRelative() { return maxScaleRelative; }

    /**
     * Sets whether {@code maxScaleFactor} is a direct scale factor value or a ratio for how many
     * multiples of the view's size should be able to fit into the viewport on its relatively shortest side.
     *
     * @param newMaxScaleRelative {@code true} if the {@code maxScaleFactor} should be relative,
     * {@code false} otherwise
     * @see #setMaxScaleFactor(double)
     * @see #isMaxScaleRelative()
     */
    @BeanProperty(description = "Flag for whether the maximum scale factor is relative to its fit")
    public void setMaxScaleRelative(boolean newMaxScaleRelative) {
        boolean old = maxScaleRelative;
        maxScaleRelative = newMaxScaleRelative;
        firePropertyChange("maxScaleRelative", old, maxScaleRelative);
    }

    /**
     * Returns a string representation of this {@code ZoomPane}. This method is intended to be used
     * only for debugging purposes, and the content and format of the returned string may vary between
     * implementations. The returned string may be empty but may not be {@code null}.
     *
     * @return a string representation of this {@code ZoomPane}
     */
    @Override
    public String paramString() {
        return super.paramString() +
                ",zoomBar=" + (zoomBar != null ? zoomBar : "") +
                ",sensitivity=" + sensitivity +
                ",minScaleFactor=" + minScaleFactor +
                ",minScaleRelative=" + minScaleRelative +
                ",maxScaleFactor=" + maxScaleFactor +
                ",maxScaleRelative=" + maxScaleRelative;
    }

/////////////////
// Accessibility support
////////////////

    /**
     * Gets the {@code AccessibleContext} associated with this {@code ZoomPane}. For zoom panes, this
     * takes the form of an {@code AccessibleZoomPane}, of which a new instance is created if necessary.
     *
     * @return an {@code AccessibleZoomPane} that serves as the {@code AccessibleContext} of this
     * {@code ZoomPane}
     */
    @Override
    public AccessibleContext getAccessibleContext() {
        if (accessibleContext == null) { accessibleContext = new AccessibleZoomPane(); }
        return accessibleContext;
    }

    /**
     * This class implements accessibility support for the <code>ZoomPane</code> class. It provides
     * an implementation of the Java Accessibility API appropriate to scroll pane user-interface elements.
     */
    protected class AccessibleZoomPane extends AccessibleJScrollPane {

        /**
         * AccessibleZoomPane constructor.
         */
        public AccessibleZoomPane() {
            super();
            ZoomBar zoomBar = getZoomBar();
            if (zoomBar != null) { setZoomBarRelations(zoomBar); }
        }

        @Override
        public void propertyChange(PropertyChangeEvent e) {
            super.propertyChange(e);
            if (e.getPropertyName().equals("zoomBar") && e.getNewValue() instanceof ZoomBar) {
                setZoomBarRelations((ZoomBar) e.getNewValue());
            }
        }

        /**
         * Sets the {@code CONTROLLER_FOR} and {@code CONTROLLED_BY} AccessibleRelations for the
         * {@code ZoomPane} and {@code ZoomBar}.
         *
         * @param zoomBar the {@code ZoomBar} to set relations with. Must not be {@code null}
         */
        void setZoomBarRelations(ZoomBar zoomBar) {
            // The ZoomBar is a CONTROLLER_FOR the ZoomPane. The ZoomPane is CONTROLLED_BY the ZoomBar.
            AccessibleRelation controlledBy =
                    new AccessibleRelation(AccessibleRelation.CONTROLLED_BY, zoomBar);
            AccessibleRelation controllerFor =
                    new AccessibleRelation(AccessibleRelation.CONTROLLER_FOR, ZoomPane.this);

            AccessibleContext ac = zoomBar.getAccessibleContext();
            ac.getAccessibleRelationSet().add(controllerFor);
            getAccessibleRelationSet().add(controlledBy);
        }
    }
}