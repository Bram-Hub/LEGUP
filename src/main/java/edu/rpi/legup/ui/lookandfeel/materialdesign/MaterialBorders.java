package edu.rpi.legup.ui.lookandfeel.materialdesign;

import edu.rpi.legup.ui.color.ColorPreferences.UIColor;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class MaterialBorders {

    public static final Border LIGHT_LINE_BORDER =
            BorderFactory.createLineBorder(UIColor.LIGHT_LINE_BORDER.getOrThrow(), 1);
    public static final Border THICK_LINE_BORDER =
            BorderFactory.createLineBorder(UIColor.THICK_LINE_BORDER.getOrThrow(), 2);

    public static final Border LIGHT_SHADOW_BORDER =
            new DropShadowBorder(Color.BLACK, 0, 4, 0.3f, 12, true, true, true, true);
    public static final Border DEFAULT_SHADOW_BORDER =
            new DropShadowBorder(Color.BLACK, 5, 5, 0.3f, 12, true, true, true, true);

    private MaterialBorders() {}
}
