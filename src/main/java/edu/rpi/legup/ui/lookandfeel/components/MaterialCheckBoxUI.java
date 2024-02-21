package edu.rpi.legup.ui.lookandfeel.components;

import edu.rpi.legup.ui.lookandfeel.materialdesign.MaterialDrawingUtils;
import java.awt.Graphics;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicCheckBoxUI;

// TODO cambio colore icone combo box
public class MaterialCheckBoxUI extends BasicCheckBoxUI {

    public static ComponentUI createUI(JComponent c) {
        return new MaterialCheckBoxUI();
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);

        JCheckBox checkBox = (JCheckBox) c;
        checkBox.setFont(UIManager.getFont("CheckBox.font"));
        checkBox.setBackground(UIManager.getColor("CheckBox.background"));
        checkBox.setForeground(UIManager.getColor("CheckBox.foreground"));
        checkBox.setIcon(UIManager.getIcon("CheckBox.icon"));
        checkBox.setSelectedIcon(UIManager.getIcon("CheckBox.selectedIcon"));
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(MaterialDrawingUtils.getAliasedGraphics(g), c);
    }
}
