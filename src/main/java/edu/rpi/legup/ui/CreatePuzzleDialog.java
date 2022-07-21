package edu.rpi.legup.ui;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.controller.CursorController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class CreatePuzzleDialog extends JDialog {
    private HomePanel homePanel;

    private String[] games;
    private JComboBox gameBox;

    private JLabel puzzleLabel = new JLabel("Puzzle:");
    private JTextField rows;
    private JTextField columns;

    private JButton ok = new JButton("Ok");
    private ActionListener okButtonListener = new ActionListener()
    {
        /**
         * Attempts to open the puzzle editor interface for the given game with the given dimensions
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            String game = (String) gameBox.getSelectedItem();
            try
            {
                homePanel.openEditorWithNewPuzzle(game, Integer.valueOf(rows.getText()), Integer.valueOf(columns.getText()));
                setVisible(false);
            }
            catch (IllegalArgumentException exception)
            {
                // Don't do anything. This is here to prevent the dialog from closing if the dimensions are invalid.
            }
        }
    };
    private JButton cancel = new JButton("Cancel");
    private ActionListener cancelButtonListener = new ActionListener()
    {
        /**
         * Hides the puzzle creation dialog
         *
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            setVisible(false);
        }
    };


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

        ActionListener cursorPressedOk = CursorController.createListener(this, okButtonListener);
        ok.addActionListener(cursorPressedOk);
        ActionListener cursorPressedCancel = CursorController.createListener(this, cancelButtonListener);
        cancel.addActionListener(cursorPressedCancel);
    }

    public void initPuzzles()
    {
        this.games = GameBoardFacade.getInstance().getConfig().getFileCreationEnabledPuzzles().toArray(new String[0]);
        Arrays.sort(this.games);
        gameBox = new JComboBox(games);
    }
}
