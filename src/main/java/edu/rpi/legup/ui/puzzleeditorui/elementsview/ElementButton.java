package edu.rpi.legup.ui.puzzleeditorui.elementsview;

import edu.rpi.legup.model.elements.Element;
import edu.rpi.legup.model.rules.Rule;

import javax.swing.*;

public class ElementButton extends JButton {

    private Element element;

    ElementButton(Element e) {
        super(e.getImageIcon());
        this.element = e;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element e) {
        this.element = e;
    }
}
