package edu.rpi.legup.controller;

import edu.rpi.legup.ui.LegupUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

    /**
     * The ToolbarController class manages UI interactions for the toolbar in {@link LegupUI}.
     * It listens for action events triggered by toolbar buttons and handles them accordingly.
     */
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
     * Invoked when a toolbar button action occurs.
     * Handles action events fired by toolbar buttons in the {@link LegupUI}.
     *
     * @param e the action event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {}
}
