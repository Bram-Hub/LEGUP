package edu.rpi.legup.ui.zoompane;

import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.plaf.ScrollBarUI;
import java.beans.BeanProperty;

/** An implementation of a zoom bar with a fit button. */
public class ZoomBar extends JScrollBar {

    private static final String uiClassID = "ZoomBarUI";

    /** The button that users can press to zoom to fit. */
    protected JButton fitButton;

    /** Creates a horizontal {@code ZoomBar}. */
    public ZoomBar() {
        super(HORIZONTAL);
        unitIncrement = blockIncrement = 25;
    }

    /**
     * Sets the {@code ZoomBarUI} object that provides the look and feel (L&amp;F) for this component.
     *
     * @param ui the {@code ZoomBarUI} L&amp;F object
     * @see #getUI
     */
    public void setUI(@Nullable ZoomBarUI ui) { super.setUI(ui); }

    /**
     * {@inheritDoc}
     * @deprecated use {@link #setUI(ZoomBarUI)} instead
     *
     * @param ui {@inheritDoc}
     * @throws IllegalArgumentException if ui is not an instance of {@code ZoomBarUI}
     */
    @Deprecated
    @Override
    public void setUI(@Nullable ScrollBarUI ui) {
        if (ui instanceof ZoomBarUI || ui == null) { setUI((ZoomBarUI) ui); }
        else { throw new IllegalArgumentException("ZoomBar's UI delegate must be an instance of ZoomBarUI"); }
    }

    /**
     * Replaces the current {@code ZoomBarUI} object with a version from the current default look and feel.
     * To be called when the default look and feel changes.
     *
     * @see JComponent#updateUI()
     * @see UIManager#getUI(javax.swing.JComponent)
     */
    @Override
    public void updateUI() { setUI((ZoomBarUI) UIManager.getUI(this)); }

    /**
     * {@inheritDoc}
     * @return the string "ZoomPaneUI"
     * @see JScrollBar#getUIClassID()
     */
    @Override
    public String getUIClassID() { return uiClassID; }

    /**
     * Returns the button that users can press to zoom to fit the view that this {@code ZoomBar} controls.
     *
     * @return the fit button
     * @see #setFitButton(JButton)
     */
    public JButton getFitButton() { return fitButton; }

    /**
     * Sets the button that users can press to zoom to fit the view that this {@code ZoomBar} controls.
     *
     * @param newFitButton a new fit button
     * @see #getFitButton()
     */
    @BeanProperty(expert = true, description = "The zoom bar's fit button.")
    public void setFitButton(@Nullable JButton newFitButton) {

        if (fitButton != null) { remove(fitButton); }
        JButton oldFitButton = fitButton;
        fitButton = newFitButton;
        if (fitButton != null) { add(fitButton); }

        firePropertyChange("fitButton", oldFitButton, fitButton);
    }
}
