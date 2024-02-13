package edu.rpi.legup.ui.puzzleeditorui.elementsview;

import javax.swing.*;

public class PlaceableElementPanel extends ElementPanel {
  public PlaceableElementPanel(ElementFrame elementFrame) {
    super(elementFrame);
    this.icon =
        new ImageIcon(
            ClassLoader.getSystemClassLoader()
                .getResource("edu/rpi/legup/images/Legup/Direct Rules.gif"));
    this.name = "Placeable Elements";
    this.toolTip = "Placeable Elements";
  }
}
