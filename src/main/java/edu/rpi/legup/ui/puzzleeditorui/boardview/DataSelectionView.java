package edu.rpi.legup.ui.puzzleeditorui.boardview;

import edu.rpi.legup.controller.ElementController;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class DataSelectionView extends JPopupMenu {

    public DataSelectionView(ElementController controller) {
        setBackground(Color.GRAY);
        setBorder(new BevelBorder(BevelBorder.RAISED));
    }
}
