package app;

import ui.LegupUI;
import ui.ToolbarName;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToolbarController implements ActionListener
{
    private LegupUI legupUI;

    /**
     * ToolbarController Constructor - creates a new ToolbarController to listen
     * for button pressed from the tool mBar
     *
     * @param legupUI legupUI
     */
    public ToolbarController(LegupUI legupUI)
    {
        this.legupUI = legupUI;
    }

    /**
     * Action Performed event -
     *
     * @param e action event
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {

    }
}
