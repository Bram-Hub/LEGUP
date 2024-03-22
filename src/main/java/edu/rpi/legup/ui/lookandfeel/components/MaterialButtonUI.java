package edu.rpi.legup.ui.lookandfeel.components;

import edu.rpi.legup.ui.lookandfeel.animation.MaterialUIMovement;
//import edu.rpi.legup.ui.lookandfeel.materialdesign.MaterialColors;
import edu.rpi.legup.ui.color.ColorPreferences.UIColor;
import edu.rpi.legup.ui.lookandfeel.materialdesign.MaterialDrawingUtils;
import java.awt.Graphics;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;
import edu.rpi.legup.ui.color.ColorPreferences.UIColor;

public class MaterialButtonUI extends BasicButtonUI {

    public static ComponentUI createUI(final JComponent c) {
        return new MaterialButtonUI();
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);

        AbstractButton button = (AbstractButton) c;
        MaterialUIMovement.add(c, UIColor.UI_MOVEMENT.get());
        button.setOpaque(UIManager.getBoolean("Button.opaque"));
        button.setBorder(UIManager.getBorder("Button.border"));
        button.setBackground(UIManager.getColor("Button.background"));
        button.setForeground(UIManager.getColor("Button.foreground"));
        button.setFont(UIManager.getFont("Button.font"));
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        AbstractButton b = (AbstractButton) c;
        g = MaterialDrawingUtils.getAliasedGraphics(g);
        paintBackground(g, b);
        super.paint(g, c);
    }

    private void paintBackground(Graphics g, JComponent c) {
        g.setColor(c.getBackground());
        g.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 7, 7);
    }
}
