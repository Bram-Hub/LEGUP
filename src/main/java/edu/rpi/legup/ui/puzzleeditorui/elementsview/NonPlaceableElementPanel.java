package edu.rpi.legup.ui.puzzleeditorui.elementsview;

import javax.swing.*;

public class NonPlaceableElementPanel extends ElementPanel {
  public NonPlaceableElementPanel(ElementFrame elementFrame) {
    super(elementFrame);
    this.icon =
        new ImageIcon(
            ClassLoader.getSystemClassLoader()
                .getResource("edu/rpi/legup/images/Legup/Direct Rules.gif"));
    this.name = "Non-Placeable Elements";
    this.toolTip = "Non-Placeable Elements";
  }
}
