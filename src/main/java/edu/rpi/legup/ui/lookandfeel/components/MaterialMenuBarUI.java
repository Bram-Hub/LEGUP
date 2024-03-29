package edu.rpi.legup.ui.lookandfeel.components;

import edu.rpi.legup.ui.lookandfeel.materialdesign.MaterialDrawingUtils;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuBarUI;

public class MaterialMenuBarUI extends BasicMenuBarUI {

    public static ComponentUI createUI(JComponent c) {
        return new MaterialMenuBarUI();
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);

        JMenuBar menuBar = (JMenuBar) c;
        menuBar.setFont(UIManager.getFont("MenuBar.font"));
        menuBar.setBackground(UIManager.getColor("MenuBar.background"));
        menuBar.setBorder(UIManager.getBorder("MenuBar.border"));
        menuBar.setForeground(UIManager.getColor("MenuBar.foreground"));
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(MaterialDrawingUtils.getAliasedGraphics(g), c);
    }
}
