package edu.rpi.legup.ui;

import edu.rpi.legup.app.GameBoardFacade;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends LegupPanel {
    private LegupUI legupUI;
    private JFrame frame;
    private JButton[] buttons;
    private JLabel[] text;
    private JMenuBar menuBar;

    private final int buttonSize = 100;

    public HomePanel(FileDialog fileDialog, JFrame frame, LegupUI legupUI) {
        this.legupUI = legupUI;
        this.frame = frame;
        setLayout(new GridLayout(2, 3));
        initText();
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
        frame.setJMenuBar(this.getMenuBar());
    }

    private static ImageIcon resizeButtonIcon(ImageIcon icon, int width, int height)
    {
        Image image = icon.getImage();
        Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
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

        ImageIcon button0Icon = new ImageIcon("src/main/resources/edu/rpi/legup/homepanel/openproof.png");
        this.buttons[0].setIcon(resizeButtonIcon(button0Icon, this.buttonSize, this.buttonSize));
        this.buttons[0].setHorizontalTextPosition(AbstractButton.CENTER);
        this.buttons[0].setVerticalTextPosition(AbstractButton.BOTTOM);
        this.buttons[0].addActionListener(l -> this.legupUI.displayPanel(1));
        this.buttons[0].setBackground(Color.WHITE);
        this.buttons[0].setOpaque(false);

        this.buttons[1] = new JButton("New Puzzle")
        {
            {
                setSize(buttonSize, buttonSize);
                setMaximumSize(getSize());
            }
        };
        ImageIcon button1Icon = new ImageIcon("src/main/resources/edu/rpi/legup/homepanel/edit.png");
        this.buttons[1].setIcon(resizeButtonIcon(button1Icon, this.buttonSize, this.buttonSize));
        this.buttons[1].setHorizontalTextPosition(AbstractButton.CENTER);
        this.buttons[1].setVerticalTextPosition(AbstractButton.BOTTOM);
        this.buttons[1].addActionListener(l -> this.openNewPuzzleDialog());
        this.buttons[1].setBackground(Color.WHITE);
        this.buttons[1].setOpaque(false);

        this.buttons[2] = new JButton("Edit Puzzle")
        {
            {
                setSize(buttonSize, buttonSize);
                setMaximumSize(getSize());
            }
        };
        ImageIcon button2Icon = new ImageIcon("src/main/resources/edu/rpi/legup/homepanel/edit.png"); // PLACEHOLDER
        this.buttons[2].setIcon(resizeButtonIcon(button2Icon, this.buttonSize, this.buttonSize));
        this.buttons[2].setHorizontalTextPosition(AbstractButton.CENTER);
        this.buttons[2].setVerticalTextPosition(AbstractButton.BOTTOM);
        this.buttons[2].addActionListener(l -> this.legupUI.displayPanel(2)); // PLACEHOLDER
        this.buttons[2].setBackground(Color.WHITE);
        this.buttons[2].setOpaque(false);

        for (int i = 0; i < this.buttons.length - 1; i++) // -1 to avoid the batch grader button
        {
            //this.buttons[i].setPreferredSize(new Dimension(100, 100));
            this.buttons[i].setBounds(200, 200, 700, 700);
        }

        this.buttons[3] = new JButton("Batch Grader");
        this.buttons[3].setHorizontalTextPosition(AbstractButton.CENTER);
        this.buttons[3].setVerticalTextPosition(AbstractButton.BOTTOM);
        this.buttons[3].setBackground(Color.WHITE);
        this.buttons[3].setOpaque(false);
    }

    private void initText()
    {
        this.text = new JLabel[3];
        
        JLabel welcome = new JLabel("Welcome to Legup");
        welcome.setFont(new Font("Roboto", Font.BOLD, 23));
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel version = new JLabel("Version 3.0.0"); // This should be autochanged in the future
        version.setFont(new Font("Roboto", Font.ITALIC, 10));
        version.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel credits = new JLabel("A project by Dr. Bram van Heuveln");
        credits.setFont(new Font("Roboto", Font.PLAIN, 12));
        credits.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        this.text[0] = welcome;
        this.text[1] = version;
        this.text[2] = credits;
    }

    private void render()
    {
        this.removeAll();
        //this.setLayout(new GridLayout(2, 3));
        this.setLayout(new BoxLayout(this, 3));
        this.legupUI.setTitle("Legup: A Better Way to Learn Formal Logic");
        JPanel temp =new JPanel();
        temp.add(buttons[3]);

        JPanel buttons = new JPanel();

        buttons.add(this.buttons[0]);

        buttons.add(this.buttons[1]);

        buttons.add(this.buttons[2]);
        buttons.add(Box.createRigidArea(new Dimension(5, 0)));//empty box to hold the space

        buttons.add(Box.createRigidArea(new Dimension(5, 0)));


        buttons.setBackground(Color.WHITE);
        buttons.add(temp);

        buttons.setLayout(new GridLayout(2,3)); //set the layout which let the batch below the left one


        for (int i = 0; i < this.text.length; i++)
            this.add(this.text[i]);
        this.add(buttons);

    }

    private void openNewPuzzleDialog() {
        CreatePuzzleDialog cpd = new CreatePuzzleDialog(this.frame, this);
        cpd.setVisible(true);
    }

    public void openEditorWithNewPuzzle(String game, int width, int height) throws IllegalArgumentException {
        // Set game type on the puzzle editor
        try {
            this.legupUI.displayPanel(2);
            this.legupUI.getPuzzleEditor().loadPuzzleFromHome(game, width, height);
        } catch (IllegalArgumentException exception) {
            this.legupUI.displayPanel(0);
            JOptionPane.showMessageDialog(null,
                    "The dimensions you entered are invalid. Please double check \n" +
                            "the number of rows and columns and try again.",
                    "ERROR: Invalid Dimensions",
                    JOptionPane.ERROR_MESSAGE);
            throw new IllegalArgumentException(exception.getMessage());
        }
    }
}
