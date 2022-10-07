package edu.rpi.legup.ui.puzzleeditorui.elementsview;

import edu.rpi.legup.model.elements.Element;
import edu.rpi.legup.model.rules.Rule;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class ElementButton extends JButton {

    private Element element;
    private final Border originalBorder;

    ElementButton(Element e) {
        super(e.getImageIcon());
        this.element = e;
        this.originalBorder = this.getBorder();
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element e) {
        this.element = e;
    }

    public void setBorderToSelected() {
        Border newBorderIn = BorderFactory.createLineBorder(new Color(20, 140, 70), 2, true);
        Border newBorder = BorderFactory.createCompoundBorder(newBorderIn, this.originalBorder);
        this.setBorder(newBorder);
    }

    public void resetBorder() {
        this.setBorder(this.originalBorder);
    }
}
