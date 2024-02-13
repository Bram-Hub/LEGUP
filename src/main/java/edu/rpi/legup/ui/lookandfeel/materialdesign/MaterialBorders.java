package edu.rpi.legup.ui.lookandfeel.materialdesign;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class MaterialBorders {

  public static final Border LIGHT_LINE_BORDER =
      BorderFactory.createLineBorder(MaterialColors.GRAY_200, 1);
  public static final Border THICK_LINE_BORDER =
      BorderFactory.createLineBorder(MaterialColors.GRAY_200, 2);

  public static final Border LIGHT_SHADOW_BORDER =
      new DropShadowBorder(Color.BLACK, 0, 4, 0.3f, 12, true, true, true, true);
  public static final Border DEFAULT_SHADOW_BORDER =
      new DropShadowBorder(Color.BLACK, 5, 5, 0.3f, 12, true, true, true, true);

  private MaterialBorders() {}
}
