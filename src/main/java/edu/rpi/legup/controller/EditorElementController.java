package edu.rpi.legup.controller;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.app.LegupPreferences;
import edu.rpi.legup.history.*;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.elements.Element;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.*;
import edu.rpi.legup.model.tree.TreeElement;
import edu.rpi.legup.model.tree.TreeElementType;
import edu.rpi.legup.ui.proofeditorui.rulesview.RuleButton;
import edu.rpi.legup.ui.proofeditorui.treeview.TreeElementView;
import edu.rpi.legup.ui.proofeditorui.treeview.TreePanel;
import edu.rpi.legup.ui.proofeditorui.treeview.TreeView;
import edu.rpi.legup.ui.proofeditorui.treeview.TreeViewSelection;
import edu.rpi.legup.ui.puzzleeditorui.elementsview.ElementButton;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import static edu.rpi.legup.app.GameBoardFacade.getInstance;

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

        if (this.prevButton != null) {
            this.prevButton.resetBorder();
        }
        button.setBorderToSelected();
        this.prevButton = button;

    }
}
