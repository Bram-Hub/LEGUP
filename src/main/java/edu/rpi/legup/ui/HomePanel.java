package edu.rpi.legup.ui;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends LegupPanel {
    private LegupUI legupUI;

    private JButton[] buttons;

    private JPanel buttonPanel;

    public HomePanel(LegupUI legupUI) {
        this.legupUI = legupUI;
        setLayout(new GridLayout(2, 1));
        initButtons();
    }

    @Override
    public void makeVisible() {
        render();
    }

    private void initButtons() {
        this.buttons = new JButton[2];
        this.buttons[0] = new JButton("Solve a puzzle!");
        this.buttons[0].addActionListener(l -> this.legupUI.displayPanel(1));
        this.buttons[1] = new JButton("Create/Edit a puzzle!");
        this.buttons[1].addActionListener(l -> this.legupUI.displayPanel(2));
        this.buttonPanel = new JPanel();
        for (JButton button : this.buttons) {
            this.buttonPanel.add(button);
        }
    }

    private void render() {
        add(new JLabel("Welcome to LEGUP! What would you like to do?"));
        add(this.buttonPanel);
    }
}
