package edu.rpi.legup.ui;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends LegupPanel {
    private LegupUI legupUI;

    private JButton[] buttons;

    public HomePanel(LegupUI legupUI) {
        this.legupUI = legupUI;
        setLayout(new GridLayout(1, 2));
        initButtons();
    }

    @Override
    public void makeVisible() {
        render();
    }

    private void initButtons() {
        this.buttons = new JButton[2];
        this.buttons[0] = new JButton("Open Proof");
        this.buttons[0].setIcon(new ImageIcon("src/main/resources/edu/rpi/legup/homepanel/openproof.png"));
        this.buttons[0].setHorizontalTextPosition(AbstractButton.CENTER);
        this.buttons[0].setVerticalTextPosition(AbstractButton.BOTTOM);
        this.buttons[0].addActionListener(l -> this.legupUI.displayPanel(1));
        this.buttons[1] = new JButton("Create/Edit Puzzle");
        this.buttons[1].setIcon(new ImageIcon("src/main/resources/edu/rpi/legup/homepanel/edit.png"));
        this.buttons[1].setHorizontalTextPosition(AbstractButton.CENTER);
        this.buttons[1].setVerticalTextPosition(AbstractButton.BOTTOM);
        this.buttons[1].addActionListener(l -> this.legupUI.displayPanel(2));
    }

    private void render() {
        add(this.buttons[0]);
        add(this.buttons[1]);
    }
}
