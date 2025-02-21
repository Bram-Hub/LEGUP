package edu.rpi.legup.controller;

import edu.rpi.legup.ui.LegupUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToolbarController implements ActionListener {
    private LegupUI legupUI;

    /**
     * ToolbarController Constructor - creates a new {@link ToolbarController} to listen for button
     * pressed from the tool mBar
     *
     * @param legupUI legupUI
     */
    public ToolbarController(LegupUI legupUI) {
        this.legupUI = legupUI;
    }

    /**
     * ICommand Performed event -
     *
     * @param e action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {}
}
