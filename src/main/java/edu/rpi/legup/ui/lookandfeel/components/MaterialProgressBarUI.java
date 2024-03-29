package edu.rpi.legup.ui.lookandfeel.components;

import edu.rpi.legup.ui.lookandfeel.materialdesign.MaterialBorders;
import edu.rpi.legup.ui.lookandfeel.materialdesign.MaterialColors;
import edu.rpi.legup.ui.lookandfeel.materialdesign.MaterialDrawingUtils;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JProgressBar;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicProgressBarUI;

/**
 * @author https://github.com/vincenzopalazzo
 */
public class MaterialProgressBarUI extends BasicProgressBarUI {

    public static ComponentUI createUI(JComponent c) {
        return new MaterialProgressBarUI();
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);

        JProgressBar progressBar = (JProgressBar) c;
        progressBar.setBorder(MaterialBorders.LIGHT_LINE_BORDER);
        progressBar.setBackground(MaterialColors.GRAY_200);
        progressBar.setForeground(MaterialColors.LIGHT_BLUE_400);
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(MaterialDrawingUtils.getAliasedGraphics(g), c);
    }
}
