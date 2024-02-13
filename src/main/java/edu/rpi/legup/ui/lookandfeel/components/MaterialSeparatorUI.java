package edu.rpi.legup.ui.lookandfeel.components;

import edu.rpi.legup.ui.lookandfeel.materialdesign.MaterialDrawingUtils;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSeparatorUI;

public class MaterialSeparatorUI extends BasicSeparatorUI {

  public static ComponentUI createUI(JComponent c) {
    return new MaterialSeparatorUI();
  }

  @Override
  public void installUI(JComponent c) {
    super.installUI(c);
  }

  @Override
  public void paint(Graphics g, JComponent c) {
    super.paint(MaterialDrawingUtils.getAliasedGraphics(g), c);
  }
}
