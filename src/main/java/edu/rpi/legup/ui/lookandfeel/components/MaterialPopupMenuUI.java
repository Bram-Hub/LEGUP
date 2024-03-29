package edu.rpi.legup.ui.lookandfeel.components;

import edu.rpi.legup.ui.lookandfeel.materialdesign.MaterialDrawingUtils;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPopupMenuUI;

public class MaterialPopupMenuUI extends BasicPopupMenuUI {

    public static ComponentUI createUI(JComponent c) {
        return new MaterialPopupMenuUI();
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);

        JPopupMenu popupMenu = (JPopupMenu) c;
        popupMenu.setBorder(UIManager.getBorder("PopupMenu.border"));
        popupMenu.setBackground(UIManager.getColor("PopupMenu.background"));
        popupMenu.setForeground(UIManager.getColor("PopupMenu.foreground"));
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(MaterialDrawingUtils.getAliasedGraphics(g), c);
    }
}
