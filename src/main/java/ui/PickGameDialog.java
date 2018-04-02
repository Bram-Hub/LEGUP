package ui;


import app.GameBoardFacade;

import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class PickGameDialog extends JDialog implements ActionListener
{
    JLabel gameLabel = new JLabel("Game:");
    String[] games;
    JComboBox gameBox;

    JLabel puzzleLabel = new JLabel("Puzzle:");
    String[][] puzzles;
    String puzzle;

    JTextField puzzleBox;

    JButton puzzleButton = new JButton("...");
    JFileChooser puzzleChooser = new JFileChooser();

    JButton ok = new JButton("Ok");
    JButton cancel = new JButton("Cancel");


    JCheckBox autotreeCheckBox = new JCheckBox("Auto-tree");
    JCheckBox showtreeCheckBox = new JCheckBox("Show tree");
    JCheckBox autojustifyCheckBox = new JCheckBox("Auto-justify");

    public boolean okPressed = false;
    private boolean pickBoth;

    /**
     * Initialize the dialog
     * @param parent the parent JFrame
     * @param pickBothAtOnce if true they can pick a game type and a specific puzzle, if
     *  false they can only pick a game type
     */
    public PickGameDialog(JFrame parent, boolean pickBothAtOnce)
    {
        super(parent, true);

        pickBoth = pickBothAtOnce;
        initPuzzles();

        Rectangle b = parent.getBounds();

        setSize(350,200);
        setLocation((int)b.getCenterX() - getWidth() / 2, (int)b.getCenterY() - getHeight() / 2);
        setTitle("Select Puzzle");

        // listeners
        gameBox.addActionListener(this);
        ok.addActionListener(this);
        cancel.addActionListener(this);

        // add components
        Container c = getContentPane();
        c.setLayout(null);

        if (pickBoth)
        {
            gameLabel.setBounds(10,10,70,25);
            gameBox.setBounds(80,10,190,25);
        }
        else
        {
            gameLabel.setBounds(10,30,70,25);
            gameBox.setBounds(80,30,190,25);
        }

        puzzleLabel.setBounds(10,40,70,25);


        puzzleBox.setBounds(80,40,190,25);
        puzzleButton.setBounds(270,40,25,25);

        ok.setBounds(20,130,60,25);
        cancel.setBounds(170,130,90,25);

        c.add(gameLabel);
        c.add(gameBox);


        if (pickBoth) {
            c.add(puzzleLabel);
            c.add(puzzleBox);
        }

        c.add(puzzleButton);
        puzzleButton.addActionListener(this);

        c.add(ok);
        c.add(cancel);

        autotreeCheckBox.setBounds(20, 70, 100, 25);
        showtreeCheckBox.setBounds(20, 90, 100, 25);
        autojustifyCheckBox.setBounds(20, 110, 100, 25);

        c.add(autotreeCheckBox);
        c.add(showtreeCheckBox);
        c.add(autojustifyCheckBox);
    }

    public void initPuzzles()
    {
        Object[] o = GameBoardFacade.getInstance().getConfig().getPuzzleNames().toArray();

        games = new String[o.length];

        for (int x = 0; x < o.length; ++x)
            games[x] = (String)o[x];

        puzzles = new String[games.length][];
        puzzleBox = new JTextField();
        for (int x = 0; x < games.length; ++x)
        {
           // o = GameBoardFacade.getInstance().getConfig().getBoardsForPuzzle(games[x]).toArray();
            puzzles[x] = new String[o.length];

            for (int y = 0; y < o.length; ++y)
                puzzles[x][y] = (String)o[y];
        }

        gameBox = new JComboBox(games);
    }

    public String getPuzzle()
    {
        return puzzleBox.getText();
    }

    public String getGame()
    {
        return (String)gameBox.getSelectedItem();
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == gameBox)
        {
            int index = gameBox.getSelectedIndex();
        }
        else if (e.getSource() == ok)
        {
            okPressed = true;
            setVisible(false);
        }
        else if (e.getSource() == cancel)
        {
            okPressed = false;
            setVisible(false);
        }
        else if(e.getSource() == puzzleButton)
        {
            File f = new File("puzzlefiles" + File.separator + gameBox.getSelectedItem().toString().toLowerCase() + File.separator);
            if (f.exists() && f.isDirectory())
                puzzleChooser = new JFileChooser(f);
            else
                puzzleChooser = new JFileChooser();
            if(puzzleChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
                puzzleBox.setText(puzzleChooser.getSelectedFile().getAbsolutePath());
        }
    }
}
