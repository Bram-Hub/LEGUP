package edu.rpi.legup.controller;

import edu.rpi.legup.history.*;
import edu.rpi.legup.model.elements.Element;
import edu.rpi.legup.model.rules.*;
import edu.rpi.legup.ui.puzzleeditorui.elementsview.ElementButton;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * {@code EditorElementController} manages actions related to UI elements within the puzzle editor environment.
 * It handles button presses, updates element selection, and manages visual states of buttons
 */
public class EditorElementController implements ActionListener {
    protected Object lastSource;
    protected ElementController elementController;
    protected ElementButton prevButton;

    public EditorElementController() {
        super();
        elementController = null;
        prevButton = null;
    }

    /**
     * Sets the ElementController instance for this controller
     *
     * @param elementController the ElementController instance to be set
     */
    public void setElementController(ElementController elementController) {
        this.elementController = elementController;
    }

    /**
     * Handles the event when a button associated with an Element is pressed
     *
     * @param element the Element associated with the button that was pressed
     */
    public void buttonPressed(Element element) {
        // TODO: implement what happens when element is pressed

        if (elementController != null) {
            elementController.setSelectedElement(element);
        }
    }

    /**
     * Handles action events triggered by buttons
     *
     * @param e the event to be processed
     */
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
