package edu.rpi.legup.ui;

import edu.rpi.legup.app.GameBoardFacade;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class CreatePuzzleDialog extends JDialog implements ActionListener {
    private String[] games;
    JComboBox gameBox;
    JLabel puzzleLabel = new JLabel("Puzzle:");
    JButton ok = new JButton("Ok");
    JButton cancel = new JButton("Cancel");
    JTextField rows;
    JTextField columns;
    private HomePanel homePanel;

    public CreatePuzzleDialog(JFrame parent, HomePanel homePanel) {
        super(parent, true);

        this.homePanel = homePanel;

        initPuzzles();

        Rectangle b = parent.getBounds();

        setSize(350, 200);
        setLocation((int) b.getCenterX() - getWidth() / 2, (int) b.getCenterY() - getHeight() / 2);

        Container c = getContentPane();
        c.setLayout(null);

        puzzleLabel.setBounds(10, 30, 70, 25);
        gameBox.setBounds(80, 30, 190, 25);

        ok.setBounds(20, 130, 60, 25);
        cancel.setBounds(170, 130, 90, 25);

        c.add(puzzleLabel);
        c.add(gameBox);

        rows = new JTextField();
        columns = new JTextField();

        JLabel rowsLabel = new JLabel("Rows:");
        JLabel columnsLabel = new JLabel("Columns:");

        rowsLabel.setBounds(30, 70, 60, 25);
        columnsLabel.setBounds(30, 95, 60, 25);

        rows.setBounds(100, 70, 60, 25);
        columns.setBounds(100, 95, 60, 25);

        c.add(rowsLabel);
        c.add(columnsLabel);

        c.add(rows);
        c.add(columns);

        c.add(ok);
        c.add(cancel);

        ok.addActionListener(this);
        cancel.addActionListener(this);
    }

    public void initPuzzles() {
        this.games = GameBoardFacade.getInstance().getConfig().getFileCreationEnabledPuzzles().toArray(new String[0]);
        Arrays.sort(this.games);
        gameBox = new JComboBox(games);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ok) {
            String game = (String) gameBox.getSelectedItem();
            this.homePanel.openEditorWithNewPuzzle(game, Integer.valueOf(this.rows.getText()), Integer.valueOf(this.columns.getText()));
            setVisible(false);
        }
        else if (e.getSource() == cancel) {
            setVisible(false);
        }
    }

    private boolean isValidDimensions()
    {
        // Needs to be implemented
        return false;
    }
}
