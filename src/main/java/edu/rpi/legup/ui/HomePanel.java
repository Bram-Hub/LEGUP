package edu.rpi.legup.ui;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends LegupPanel {
    private LegupUI legupUI;
    private JFrame frame;
    private JButton[] buttons;
    private JMenuBar menuBar;

    public HomePanel(FileDialog fileDialog, JFrame frame, LegupUI legupUI) {
        this.legupUI = legupUI;
        this.frame = frame;
        setLayout(new GridLayout(1, 2));
        initButtons();
    }

    public JMenuBar getMenuBar()
    {
        this.menuBar = new JMenuBar();
        JMenu settings = new JMenu("Settings");
        menuBar.add(settings);
        JMenuItem about = new JMenuItem("About");
        JMenuItem preferences = new JMenuItem("Preferences");
        settings.add(about);
        about.addActionListener(a -> {System.out.println("About clicked");});
        // settings.addSeparator();
        settings.add(preferences);
        preferences.addActionListener(a -> {System.out.println("Preferences clicked");});
        return this.menuBar;
    }

    @Override
    public void makeVisible()
    {
        render();
        this.frame.setVisible(true);
        this.frame.setJMenuBar(this.getMenuBar());
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
        this.add(this.buttons[0]);
        this.add(this.buttons[1]);
    }
}
