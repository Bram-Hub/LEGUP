package edu.rpi.legup.ui;

import javax.swing.*;
import java.awt.*;

public class PuzzleEditorPanel extends LegupPanel {

    private JMenu[] menus;
    private JMenuBar menuBar;
    private LegupUI legupUI;
    private JButton[] buttons;
    private JPanel mainPanel;
    public PuzzleEditorPanel(LegupUI legupUI) {
        this.legupUI = legupUI;
        setLayout(new GridLayout(2, 1));
        setup();
    }

    private void setup() {
        mainPanel = new JPanel();
        buttons = new JButton[3];
        buttons[0] = new JButton("element 0");
        buttons[1] = new JButton("element 1");
        buttons[2] = new JButton("element 2");
        for (int i = 0; i < buttons.length; i++) {
            mainPanel.add(buttons[i]);
        }
        mainPanel.setSize(100,300);
        menuBar = new JMenuBar();
        menus = new JMenu[3];
        menus[0] = new JMenu("File");
        menus[1] = new JMenu("Edit");
        menus[2] = new JMenu("Help");
        for (JMenu menu : menus) {
            menuBar.add(menu);
        }
        mainPanel.add(menuBar);
    }

    @Override
    public void makeVisible() {
        render();
    }

    private void render() {

        add(new JLabel("Welcome to the puzzle editor!"));
        add(mainPanel);
    }
}
