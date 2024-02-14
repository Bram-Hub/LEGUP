package edu.rpi.legup.controller;

import edu.rpi.legup.history.*;
import edu.rpi.legup.model.elements.Element;
import edu.rpi.legup.model.rules.*;
import edu.rpi.legup.ui.puzzleeditorui.elementsview.ElementButton;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class EditorElementController implements ActionListener {
    protected Object lastSource;
    protected ElementController elementController;
    protected ElementButton prevButton;

    public EditorElementController() {
        super();
        elementController = null;
        prevButton = null;
    }

    public void setElementController(ElementController elementController) {
        this.elementController = elementController;
    }

    public void buttonPressed(Element element) {
        // TODO: implement what happens when element is pressed

        System.out.printf("%s button pressed!\n", element.getElementName());
        if (elementController != null) {
            elementController.setSelectedElement(element);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        lastSource = e.getSource();
        ElementButton button = (ElementButton) lastSource;
        buttonPressed(button.getElement());

        // reset border in previous selected button
        if (this.prevButton != null) {
            this.prevButton.resetBorder();
        }

        // change border color when select a button
        button.setBorderToSelected();

        this.prevButton = button;
    }
}
