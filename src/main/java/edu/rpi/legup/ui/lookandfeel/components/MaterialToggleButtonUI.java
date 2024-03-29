package edu.rpi.legup.ui.lookandfeel.components;

import edu.rpi.legup.ui.lookandfeel.materialdesign.MaterialDrawingUtils;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToggleButtonUI;

public class MaterialToggleButtonUI extends BasicToggleButtonUI {

    public static ComponentUI createUI(JComponent c) {
        return new MaterialToggleButtonUI();
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);

        JToggleButton toggleButton = (JToggleButton) c;
        toggleButton.setBorder(UIManager.getBorder("ToggleButton.border"));
        toggleButton.setFont(UIManager.getFont("ToggleButton.font"));
        toggleButton.setBackground(UIManager.getColor("ToggleButton.background"));
        toggleButton.setForeground(UIManager.getColor("ToggleButton.foreground"));

        if (toggleButton.getIcon() == null && toggleButton.getSelectedIcon() == null) {
            toggleButton.setIcon(UIManager.getIcon("ToggleButton.icon"));
            toggleButton.setSelectedIcon(UIManager.getIcon("ToggleButton.selectedIcon"));
        }
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(MaterialDrawingUtils.getAliasedGraphics(g), c);
    }
}
