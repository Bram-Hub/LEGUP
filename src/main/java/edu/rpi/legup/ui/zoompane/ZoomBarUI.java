package edu.rpi.legup.ui.zoompane;

import com.formdev.flatlaf.ui.FlatScrollBarUI;
import com.formdev.flatlaf.ui.FlatStylingSupport;
import com.formdev.flatlaf.util.LoggingFacade;
import com.formdev.flatlaf.util.UIScale;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;
import java.beans.PropertyChangeListener;
import java.util.Objects;

/** Provides the Flat Laf UI Delegate for {@link ZoomBar}. */
public class ZoomBarUI extends FlatScrollBarUI {

    /** Fit button. */
    protected JButton fitButton;

    /**
     * Distance between the fit button and the adjacent button. This may be a negative number.
     * If negative, then an overlap between the buttons will occur.
     */
    protected int fitGap;

    /**
     * Creates the UI.
     *
     * @param c the component
     * @return the UI
     */
    public static ComponentUI createUI(@NotNull JComponent c) { return new ZoomBarUI(); }

    @Override
    @SuppressWarnings("RedundantCollectionOperation")
    protected void configureScrollBarColors() {
        super.configureScrollBarColors();

        if (UIManager.getDefaults().keySet().contains("ZoomBar.background")) {
            LookAndFeel.installColors(scrollbar, "ZoomBar.background", "ScrollBar.foreground");
        }
        thumbColor = getDefaultIfDefined("ZoomBar.thumb", thumbColor, Color.class);
        trackColor = getDefaultIfDefined("ZoomBar.track", trackColor, Color.class);
    }

    @Override
    protected void installDefaults() {
        super.installDefaults();

        // All defaults should try to use ZoomBar values and fallback to ScrollBar values
        int width = getDefaultIfDefined("ZoomBar.width", scrollBarWidth, Integer.class);
        scrollBarWidth = width >= 0 ? width : scrollBarWidth;

        minimumThumbSize = getDefaultIfDefined("ZoomBar.minimumThumbSize", minimumThumbSize, Dimension.class);
        maximumThumbSize = getDefaultIfDefined("ZoomBar.maximumThumbSize", maximumThumbSize, Dimension.class);
        allowsAbsolutePositioning = getDefaultIfDefined("ZoomBar.allowsAbsolutePositioning", allowsAbsolutePositioning, Boolean.class);
        if (UIManager.getDefaults().containsKey("ZoomBar.border")) {
            LookAndFeel.installBorder(scrollbar, "ZoomBar.border");
        }
        incrGap = getDefaultIfDefined("ZoomBar.incrementButtonGap", incrGap, Integer.class);
        decrGap = getDefaultIfDefined("ZoomBar.decrementButtonGap", decrGap, Integer.class);
        fitGap = UIManager.getInt("ZoomBar.fitButtonGap");

        minimumButtonSize = getDefaultIfDefined("ZoomBar.minimumButtonSize", minimumButtonSize, Dimension.class);
        trackInsets = getDefaultIfDefined("ZoomBar.trackInsets", trackInsets, Insets.class);
        thumbInsets = getDefaultIfDefined("ZoomBar.thumbInsets", thumbInsets, Insets.class);
        trackArc = getDefaultIfDefined("ZoomBar.trackArc", trackArc, Integer.class);
        thumbArc = getDefaultIfDefined("ZoomBar.thumbArc", thumbArc, Integer.class);
        hoverTrackColor = getDefaultIfDefined("ZoomBar.hoverTrackColor", hoverTrackColor, Color.class);
        hoverThumbColor = getDefaultIfDefined("ZoomBar.hoverThumbColor", hoverThumbColor, Color.class);
        hoverThumbWithTrack = getDefaultIfDefined("ZoomBar.hoverThumbWithTrack", hoverThumbWithTrack, Boolean.class);
        pressedTrackColor = getDefaultIfDefined("ZoomBar.pressedTrackColor", pressedTrackColor, Color.class);
        pressedThumbColor = getDefaultIfDefined("ZoomBar.pressedThumbColor", pressedThumbColor, Color.class);
        pressedThumbWithTrack = getDefaultIfDefined("ZoomBar.pressedThumbWithTrack", pressedThumbWithTrack, Boolean.class);

        showButtons = getDefaultIfDefined("ZoomBar.showButtons", showButtons, Boolean.class);
        buttonArrowColor = getDefaultIfDefined("ZoomBar.buttonArrowColor", buttonArrowColor, Color.class);
        buttonDisabledArrowColor = getDefaultIfDefined("ZoomBar.buttonDisabledArrowColor", buttonDisabledArrowColor, Color.class);
        hoverButtonBackground = getDefaultIfDefined("ZoomBar.hoverButtonBackground", hoverButtonBackground, Color.class);
        pressedButtonBackground = getDefaultIfDefined("ZoomBar.pressedButtonBackground", pressedButtonBackground, Color.class);
    }

    /**
     * Returns the UI Default specified by {@code key} if it has been defined, otherwise {@code defaultValue}.
     *
     * @param key the key to look for
     * @param defaultValue the default value to fall back to
     * @param type the expected type of the value
     * @return UI Default {@code key} if defined, otherwise {@code defaultValue}
     * @param <T> the expected type of the value
     */
    @SuppressWarnings("RedundantCollectionOperation")
    private static <T> T getDefaultIfDefined(@NotNull String key, @Nullable T defaultValue, @NotNull Class<T> type) {
        if (!UIManager.getDefaults().keySet().contains(key)) { return defaultValue; }
        Object value = UIManager.get(key);
        return type.isInstance(value) ? type.cast(value) : defaultValue;
    }

    @Override
    protected void installStyle() {
        try {
            applyStyle(FlatStylingSupport.getResolvedStyle(scrollbar, "ZoomBar"));
        } catch (RuntimeException ex) {
            LoggingFacade.INSTANCE.logSevere(null, ex);
        }
    }

    @Override
    protected void installComponents() {

        fitButton = ((ZoomBar) scrollbar).getFitButton();
        if (fitButton == null || fitButton instanceof UIResource) {
            fitButton = createFitButton();
            ((ZoomBar) scrollbar).setFitButton(fitButton);
        }

        super.installComponents();
    }

    @Override
    protected void uninstallComponents() {
        if (fitButton instanceof UIResource) { ((ZoomBar) scrollbar).setFitButton(null); }
        super.uninstallComponents();
    }

    @Override
    protected PropertyChangeListener createPropertyChangeListener() {
        PropertyChangeListener superListener = super.createPropertyChangeListener();

        return e -> {
            superListener.propertyChange(e);
            if (e.getPropertyName().equals("fitButton")) { fitButton = (JButton) e.getNewValue(); }
        };
    }

    @Override
    protected boolean isShowButtons() {
        Object showButtons = scrollbar.getClientProperty("ZoomBar.showButtons");
        if (showButtons == null && scrollbar.getParent() instanceof ZoomPane) {
            showButtons = ((ZoomPane) scrollbar.getParent()).getClientProperty("ZoomPane.showButtons");
        }
        return (showButtons != null) ? Objects.equals(showButtons, true) : this.showButtons;
    }

    /** @return {@code true} if the {@code ZoomBar} should display the fit button, {@code false} otherwise */
    protected boolean isShowFitButton() {
        Object showFitButton = scrollbar.getClientProperty("ZoomBar.showFitButton");
        if (showFitButton == null && scrollbar.getParent() instanceof ZoomPane) {
            showFitButton = ((ZoomPane) scrollbar.getParent()).getClientProperty("ZoomPane.showFitButton");
        }
        return (showFitButton != null) ? Objects.equals(showFitButton, true) :
                getDefaultIfDefined("ZoomBar.showFitButton", isShowButtons(), Boolean.class);
    }

    @Override
    protected void layoutVScrollbar(@NotNull JScrollBar sb) {
        Dimension sbSize = sb.getSize();
        Insets sbInsets = sb.getInsets();

        int itemW = sbSize.width - (sbInsets.left + sbInsets.right);
        int itemX = sbInsets.left;

        boolean squareButtons = getDefaultIfDefined("ZoomBar.squareButtons",
                UIManager.getBoolean("ScrollPane.squareButtons"), Boolean.class);
        int decrButtonH = squareButtons ? itemW : decrButton.getPreferredSize().height;
        int decrButtonY = sbInsets.top;
        int fitButtonH = squareButtons ? itemW : fitButton.getPreferredSize().height;
        int fitButtonY = sbSize.height - (sbInsets.bottom + fitButtonH);
        int incrButtonH = squareButtons ? itemW : incrButton.getPreferredSize().height;
        int incrButtonY = fitButtonY - incrButtonH - fitGap;

        int sbInsetsH = sbInsets.top + sbInsets.bottom;
        int sbButtonsH = decrButtonH + incrButtonH + fitButtonH;
        int gaps = decrGap + incrGap + fitGap;
        float trackH = sbSize.height - (sbInsetsH + sbButtonsH) - gaps;

        float min = sb.getMinimum();
        float extent = sb.getVisibleAmount();
        float range = sb.getMaximum() - min;
        float value = sb.getValue();

        int thumbH = (range <= 0) ? getMaximumThumbSize().height : (int)(trackH * (extent / range));
        thumbH = Math.min(Math.max(thumbH, getMinimumThumbSize().height), getMaximumThumbSize().height);

        int thumbY = incrButtonY - incrGap - thumbH;
        if (value < (sb.getMaximum() - sb.getVisibleAmount())) {
            float thumbRange = trackH - thumbH;
            thumbY = (int) (0.5f + (thumbRange * ((value - min) / (range - extent))));
            thumbY += decrButtonY + decrButtonH + decrGap;
        }

        int sbAvailButtonH = (sbSize.height - sbInsetsH);
        if (sbAvailButtonH < sbButtonsH) {
            fitButtonH = incrButtonH = decrButtonH = sbAvailButtonH / 3;
            fitButtonY = sbSize.height - (sbInsets.bottom + fitButtonH);
            incrButtonY = fitButtonY - incrButtonH - fitGap;
        }
        decrButton.setBounds(itemX, decrButtonY, itemW, decrButtonH);
        incrButton.setBounds(itemX, incrButtonY, itemW, incrButtonH);
        fitButton.setBounds(itemX, fitButtonY, itemW, fitButtonH);

        int itrackY = decrButtonY + decrButtonH + decrGap;
        int itrackH = incrButtonY - incrGap - itrackY;
        trackRect.setBounds(itemX, itrackY, itemW, itrackH);

        if (thumbH >= (int) trackH)       {
            if (getDefaultIfDefined("ZoomBar.alwaysShowThumb",
                    UIManager.getBoolean("ScrollBar.alwaysShowThumb"), Boolean.class)) {
                setThumbBounds(itemX, itrackY, itemW, itrackH);
            }
            else { setThumbBounds(0, 0, 0, 0); }
        }
        else {
            if ((thumbY + thumbH) > incrButtonY - incrGap) { thumbY = incrButtonY - incrGap - thumbH; }
            if (thumbY  < (decrButtonY + decrButtonH + decrGap)) { thumbY = decrButtonY + decrButtonH + decrGap + 1; }
            setThumbBounds(itemX, thumbY, itemW, thumbH);
        }
    }

    @Override
    protected void layoutHScrollbar(@NotNull JScrollBar sb) {
        Dimension sbSize = sb.getSize();
        Insets sbInsets = sb.getInsets();

        int itemH = sbSize.height - (sbInsets.top + sbInsets.bottom);
        int itemY = sbInsets.top;
        boolean ltr = sb.getComponentOrientation().isLeftToRight();

        boolean squareButtons = getDefaultIfDefined("ZoomBar.squareButtons",
                UIManager.getBoolean("ScrollPane.squareButtons"), Boolean.class);
        int leftButtonW = squareButtons ? itemH : decrButton.getPreferredSize().width;
        int rightButtonW = squareButtons ? itemH : incrButton.getPreferredSize().width;
        int fitButtonW = squareButtons ? itemH : fitButton.getPreferredSize().width;
        if (!ltr) {
            int temp = leftButtonW;
            leftButtonW = rightButtonW;
            rightButtonW = temp;
        }
        int leftButtonX = sbInsets.left;
        int fitButtonX = sbSize.width - (sbInsets.right + fitButtonW);
        int rightButtonX = fitButtonX - rightButtonW - fitGap;
        int leftGap = ltr ? decrGap : incrGap;
        int rightGap = ltr ? incrGap : decrGap;

        int sbInsetsW = sbInsets.left + sbInsets.right;
        int sbButtonsW = leftButtonW + rightButtonW + fitButtonW;
        float trackW = sbSize.width - (sbInsetsW + sbButtonsW) - (leftGap + rightGap);

        float min = sb.getMinimum();
        float max = sb.getMaximum();
        float extent = sb.getVisibleAmount();
        float range = max - min;
        float value = sb.getValue();

        int thumbW = (range <= 0) ? getMaximumThumbSize().width : (int) (trackW * (extent / range));
        thumbW = Math.min(Math.max(thumbW, getMinimumThumbSize().width), getMaximumThumbSize().width);

        int thumbX = ltr ? rightButtonX - rightGap - thumbW : leftButtonX + leftButtonW + leftGap;
        if (value < (max - sb.getVisibleAmount())) {
            float thumbRange = trackW - thumbW;
            if (ltr) { thumbX = (int)(0.5f + (thumbRange * ((value - min) / (range - extent)))); }
            else { thumbX = (int)(0.5f + (thumbRange * ((max - extent - value) / (range - extent)))); }
            thumbX += leftButtonX + leftButtonW + leftGap;
        }

        int sbAvailButtonW = (sbSize.width - sbInsetsW);
        if (sbAvailButtonW < sbButtonsW) {
            fitButtonW = rightButtonW = leftButtonW = sbAvailButtonW / 3;
            fitButtonX = sbSize.width - (sbInsets.right + fitButtonW);
            rightButtonX = fitButtonX - rightButtonW - rightGap;
        }

        (ltr ? decrButton : incrButton).setBounds(leftButtonX, itemY, leftButtonW, itemH);
        (ltr ? incrButton : decrButton).setBounds(rightButtonX, itemY, rightButtonW, itemH);
        fitButton.setBounds(fitButtonX, itemY, fitButtonW, itemH);

        int itrackX = leftButtonX + leftButtonW + leftGap;
        int itrackW = rightButtonX - rightGap - itrackX;
        trackRect.setBounds(itrackX, itemY, itrackW, itemH);

        if (thumbW >= (int) trackW) {
            if (getDefaultIfDefined("ZoomBar.alwaysShowThumb",
                    UIManager.getBoolean("ScrollBar.alwaysShowThumb"), Boolean.class)) {
                setThumbBounds(itrackX, itemY, itrackW, itemH);
            }
            else { setThumbBounds(0, 0, 0, 0); }
        }
        else {
            if (thumbX + thumbW > rightButtonX - rightGap) { thumbX = rightButtonX - rightGap - thumbW; }
            if (thumbX < leftButtonX + leftButtonW + leftGap) { thumbX = leftButtonX + leftButtonW + leftGap + 1; }
            setThumbBounds(thumbX, itemY, thumbW, itemH);
        }
    }

    @Override
    protected JButton createIncreaseButton(int orientation) { return new ZoomBarButton("increase"); }

    @Override
    protected JButton createDecreaseButton(int orientation) { return new ZoomBarButton("decrease"); }

    /**
     * Creates a fit button.
     *
     * @return a fit button
     */
    protected JButton createFitButton() { return new ZoomBarButton("fit"); }

    /**
     * A button displaying plus, minus, and fit icons based on its type.
     */
    protected class ZoomBarButton extends FlatScrollBarButton {

        /** Type of the button. */
        protected String type;

        /** Path of the icon to display based on type. */
        protected Path2D iconPath;

        protected ZoomBarButton(@NotNull String type) {
            super(0);
            this.type = type;
        }

        @Override
        protected void paintArrow(@NotNull Graphics2D graphics) {
            Graphics2D g = (Graphics2D) graphics.create();

            float size = UIScale.scale(getArrowWidth()) - 1;
            float thickness = UIScale.scale(getArrowThickness());
            int width = getWidth();
            int height = getHeight();
            float xOffset = UIScale.scale(getXOffset());
            float yOffset = UIScale.scale(getYOffset());

            g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g.translate(
                    (width - size) / 2.0 + UIScale.scale(xOffset),
                    (height - size) / 2.0 + UIScale.scale(yOffset)
            );
            g.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            Path2D path = getPath();
            path.transform(AffineTransform.getScaleInstance(size, size));
            g.draw(path);

            g.dispose();
        }

        /**
         * Gets a deep copy of the path of the icon for this type of button.
         *
         * @return the path of the icon to draw
         */
        protected Path2D getPath() {
            if (iconPath != null) { return (Path2D) iconPath.clone(); }
            return (Path2D) (iconPath = loadPath()).clone();
        }

        /** @return a new icon path for a button of this type */
        protected Path2D loadPath() {
            Path2D path = new Path2D.Double();

            switch(type) {
                case "increase" -> {
                    path.moveTo(0, 0.5);
                    path.lineTo(1, 0.5);
                    path.moveTo(0.5, 0);
                    path.lineTo(0.5, 1);
                }
                case "decrease" -> {
                    path.moveTo(0.125, 0.5);
                    path.lineTo(0.875, 0.5);
                }
                case "fit" -> {
                    path.moveTo(0, 0.25);
                    path.append(new Arc2D.Double(0, 0, 0.25, 0.25, 180, -90, Arc2D.OPEN), true);
                    path.lineTo(0.25, 0);

                    path.moveTo(0.75, 0);
                    path.append(new Arc2D.Double(0.75, 0, 0.25, 0.25, 90, -90, Arc2D.OPEN), true);
                    path.lineTo(1, 0.25);

                    path.moveTo(1, 0.75);
                    path.append(new Arc2D.Double(0.75, 0.75, 0.25, 0.25, 0, -90, Arc2D.OPEN), true);
                    path.lineTo(0.75, 1);

                    path.moveTo(0.25, 1);
                    path.append(new Arc2D.Double(0, 0.75, 0.25, 0.25, 270, -90, Arc2D.OPEN), true);
                    path.lineTo(0, 0.75);
                }
                default -> {}
            }

            return path;
        }

        @Override
        public Dimension getPreferredSize() {
            if (type.equals("fit") ? isShowFitButton() : isShowButtons()) {
                return new Dimension(
                        UIScale.scale(Math.max(scrollBarWidth, (minimumButtonSize != null) ? minimumButtonSize.width : 0)),
                        UIScale.scale(Math.max(scrollBarWidth, (minimumButtonSize != null) ? minimumButtonSize.height : 0))
                );
            }
            else { return new Dimension(); }
        }

        @Override
        public Dimension getMinimumSize() {
            return (type.equals("fit") ? isShowFitButton() : isShowButtons())
                    ? super.getMinimumSize() : new Dimension();
        }

        @Override
        public Dimension getMaximumSize() {
            return (type.equals("fit") ? isShowFitButton() : isShowButtons())
                    ? super.getMaximumSize() : new Dimension();
        }
    }
}
