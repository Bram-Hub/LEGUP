package edu.rpi.legup.ui.lookandfeel.components;

import edu.rpi.legup.ui.lookandfeel.materialdesign.MaterialDrawingUtils;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicCheckBoxMenuItemUI;

/**
 * A Material Design UI for {@link JCheckBoxMenuItem}.
 * It customizes the painting of the checkbox menu item to align with Material Design aesthetics,
 * including using specific icons for selected and unselected states and enabling anti-aliasing for smoother rendering.
 */
public class MaterialCheckBoxMenuItemUI extends BasicCheckBoxMenuItemUI {
    /**
     * Creates a new UI delegate for a {@link JCheckBoxMenuItem}.
     *
     * @param c the component for which to create the UI delegate
     * @return a new instance of {@code MaterialCheckBoxMenuItemUI}
     */
    public static ComponentUI createUI(JComponent c) {
        return new MaterialCheckBoxMenuItemUI();
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(MaterialDrawingUtils.getAliasedGraphics(g), c);
    }

    @Override
    protected void paintMenuItem(
            Graphics g,
            JComponent c,
            Icon checkIcon,
            Icon arrowIcon,
            Color background,
            Color foreground,
            int defaultTextIconGap) {
        JCheckBoxMenuItem checkBoxMenuItem = (JCheckBoxMenuItem) c;
        if (checkBoxMenuItem.isSelected()) {
            super.paintMenuItem(
                    MaterialDrawingUtils.getAliasedGraphics(g),
                    checkBoxMenuItem,
                    UIManager.getIcon("CheckBoxMenuItem.selectedCheckIcon"),
                    arrowIcon,
                    background,
                    foreground,
                    defaultTextIconGap);
            return;
        }
        super.paintMenuItem(
                MaterialDrawingUtils.getAliasedGraphics(g),
                checkBoxMenuItem,
                UIManager.getIcon("CheckBoxMenuItem.checkIcon"),
                arrowIcon,
                background,
                foreground,
                defaultTextIconGap);
    }
}
