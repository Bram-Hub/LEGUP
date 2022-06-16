package edu.rpi.legup.ui;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends LegupPanel {
    private LegupUI legupUI;
    private JFrame frame;
    private JButton[] buttons;
    private JMenuBar menuBar;

    private final int buttonSize = 23;

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
        JMenuItem preferences = new JMenuItem("Preferences");
        preferences.addActionListener(a -> { System.out.println("Preferences clicked"); });
        settings.add(preferences);

        JMenuItem about = new JMenuItem("About");
        about.addActionListener(a -> { System.out.println("About clicked"); });
        settings.add(about);

        JMenuItem help = new JMenuItem("Help");
        about.addActionListener(a -> { System.out.println("Help clicked"); });
        settings.add(help);

        JMenuItem contribute = new JMenuItem("Contribute to Legup");
        contribute.addActionListener(a -> { System.out.println("Contribute to Legup clicked"); });
        settings.add(contribute);

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
        this.buttons = new JButton[4];

        this.buttons[0] = new JButton("Open Proof")
        {
            {
                setSize(buttonSize, buttonSize);
                setMaximumSize(getSize());
            }
        };
        this.buttons[0].setIcon(new ImageIcon("src/main/resources/edu/rpi/legup/homepanel/openproof.png"));
        this.buttons[0].setHorizontalTextPosition(AbstractButton.CENTER);
        this.buttons[0].setVerticalTextPosition(AbstractButton.BOTTOM);
        this.buttons[0].addActionListener(l -> this.legupUI.displayPanel(1));

        this.buttons[1] = new JButton("Create New Puzzle")
        {
            {
                setSize(buttonSize, buttonSize);
                setMaximumSize(getSize());
            }
        };
        this.buttons[1].setIcon(new ImageIcon("src/main/resources/edu/rpi/legup/homepanel/edit.png"));
        this.buttons[1].setHorizontalTextPosition(AbstractButton.CENTER);
        this.buttons[1].setVerticalTextPosition(AbstractButton.BOTTOM);
        this.buttons[1].addActionListener(l -> this.legupUI.displayPanel(2));

        this.buttons[2] = new JButton("Edit Puzzle")
        {
            {
                setSize(buttonSize, buttonSize);
                setMaximumSize(getSize());
            }
        };
        this.buttons[2].setIcon(new ImageIcon("src/main/resources/edu/rpi/legup/homepanel/edit.png")); // PLACEHOLDER
        this.buttons[2].setHorizontalTextPosition(AbstractButton.CENTER);
        this.buttons[2].setVerticalTextPosition(AbstractButton.BOTTOM);
        this.buttons[2].addActionListener(l -> this.legupUI.displayPanel(2)); // PLACEHOLDER

        this.buttons[3] = new JButton("Batch Grader")
        {
            {
                setSize(buttonSize, buttonSize);
                setMaximumSize(getSize());
            }
        };
        this.buttons[3].setIcon(new ImageIcon("src/main/resources/edu/rpi/legup/homepanel/edit.png")); // PLACEHOLDER
        this.buttons[3].setHorizontalTextPosition(AbstractButton.CENTER);
        this.buttons[3].setVerticalTextPosition(AbstractButton.BOTTOM);
        this.buttons[3].addActionListener(l -> this.legupUI.displayPanel(2)); // PLACEHOLDER
    }

    private void render()
    {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        JLabel welcome = new JLabel("Welcome to Legup");
        welcome.setFont(new Font("Roboto", Font.BOLD, 23));
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel version = new JLabel("Version 3.0.0"); // This should be autochanged in the future
        version.setFont(new Font("Roboto", Font.ITALIC, 10));
        version.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel credits = new JLabel("A project by Dr. Bram van Heuveln");
        credits.setFont(new Font("Roboto", Font.PLAIN, 12));
        credits.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttons = new JPanel();
        buttons.add(this.buttons[0]);
        buttons.add(Box.createRigidArea(new Dimension(5, 0)));
        buttons.add(this.buttons[1]);
        buttons.add(Box.createRigidArea(new Dimension(5, 0)));
        buttons.add(this.buttons[2]);
        buttons.add(Box.createRigidArea(new Dimension(5, 0)));
        buttons.add(this.buttons[3]);
        this.add(welcome);
        this.add(version);
        this.add(credits);
        this.add(buttons);
    }
}
