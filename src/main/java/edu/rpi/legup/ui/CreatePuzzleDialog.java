package edu.rpi.legup.ui;

import edu.rpi.legup.app.Config;
import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.controller.CursorController;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Objects;
import javax.swing.*;

/**
 * Provides the user interface components for creating a new puzzle in the Legup application.
 * This package includes classes for displaying dialog boxes to configure and initialize puzzles.
 */
public class CreatePuzzleDialog extends JDialog {
    private HomePanel homePanel;

    private String[] games;
    private JComboBox gameBox;
    private ActionListener gameBoxListener =
            new ActionListener() {
                /**
                 * An ActionListener that handles changes in the drop-down menu for selecting puzzle types.
                 * When a new item is selected in the drop-down menu, this listener updates the visibility of
                 * the text input area and the row/column input fields based on the selected puzzle type.
                 * If "ShortTruthTable" is selected, the text input area is shown and the row/column fields are hidden.
                 * For other puzzle types, the row/column fields are shown and the text input area is hidden.
                 */
                @Override
                public void actionPerformed(ActionEvent e) {
                    JComboBox comboBox = (JComboBox) e.getSource();
                    String puzzleName = (String) comboBox.getSelectedItem();
                    if (puzzleName.equals("ShortTruthTable")) {
                        textInputScrollPane.setVisible(true);
                        rowsLabel.setVisible(false);
                        rows.setVisible(false);
                        columnsLabel.setVisible(false);
                        columns.setVisible(false);
                    } else {
                        textInputScrollPane.setVisible(false);
                        rowsLabel.setVisible(true);
                        rows.setVisible(true);
                        columnsLabel.setVisible(true);
                        columns.setVisible(true);
                    }
                }
            };

    private JLabel puzzleLabel;
    private JLabel rowsLabel;
    private JTextField rows;
    private JLabel columnsLabel;
    private JTextField columns;

    private JTextArea textArea;
    private JScrollPane textInputScrollPane;

    private JButton ok = new JButton("Ok");
    private ActionListener okButtonListener =
            new ActionListener() {
                /**
                 * Attempts to open the puzzle editor interface for the given game with the given
                 * dimensions
                 *
                 * @param ae the event to be processed
                 */
                @Override
                public void actionPerformed(ActionEvent ae) {
                    String game = getGame();

                    // Check if all 3 TextFields are filled
                    if (game.equals("ShortTruthTable") && textArea.getText().isEmpty()) {
                        System.out.println("Unfilled fields");
                        return;
                    }
                    if (!game.equals("ShortTruthTable")
                            && (game.isEmpty()
                                    || getRows().isEmpty()
                                    || getColumns().isEmpty())) {
                        System.out.println("Unfilled fields");
                        return;
                    }

                    try {
                        if (game.equals("ShortTruthTable")) {
                            homePanel.openEditorWithNewPuzzle(
                                    "ShortTruthTable", textArea.getText().split("\n"));
                        } else {
                            homePanel.openEditorWithNewPuzzle(
                                    game,
                                    Integer.valueOf(getRows()),
                                    Integer.valueOf(getColumns()));
                        }
                        setVisible(false);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Failed to open editor with new puzzle");
                        e.printStackTrace(System.out);
                    }
                }
            };

    private JButton cancel = new JButton("Cancel");
    private ActionListener cancelButtonListener =
            new ActionListener() {
                /**
                 * Dispose the puzzle creation dialog
                 *
                 * @param e the event to be processed
                 */
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            };

    /**
     * Constructs a new CreatePuzzleDialog
     *
     * @param parent the parent frame of the dialog
     * @param homePanel the home panel where the created puzzle will be added
     */
    public CreatePuzzleDialog(JFrame parent, HomePanel homePanel) {
        super(parent, true);

        this.homePanel = homePanel;

        initPuzzles();

        Rectangle b = parent.getBounds();

        setSize(350, 200);
        setLocation((int) b.getCenterX() - getWidth() / 2, (int) b.getCenterY() - getHeight() / 2);

        Container c = getContentPane();
        c.setLayout(null);

        puzzleLabel = new JLabel("Puzzle:");
        puzzleLabel.setBounds(10, 30, 70, 25);
        gameBox.setBounds(80, 30, 190, 25);

        ok.setBounds(20, 130, 60, 25);
        cancel.setBounds(170, 130, 90, 25);

        c.add(puzzleLabel);
        c.add(gameBox);

        rows = new JTextField();
        columns = new JTextField();

        rowsLabel = new JLabel("Rows:");
        columnsLabel = new JLabel("Columns:");

        rowsLabel.setBounds(30, 70, 60, 25);
        columnsLabel.setBounds(30, 95, 60, 25);

        rows.setBounds(100, 70, 60, 25);
        columns.setBounds(100, 95, 60, 25);

        c.add(rowsLabel);
        c.add(columnsLabel);

        c.add(rows);
        c.add(columns);

        textArea = new JTextArea();
        textInputScrollPane =
                new JScrollPane(
                        textArea,
                        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        textInputScrollPane.setBounds(10, 70, this.getWidth() - 30, 50);
        c.add(textInputScrollPane);

        c.add(ok);
        c.add(cancel);

        if (Objects.equals(this.gameBox.getSelectedItem(), "ShortTruthTable")) {
            textInputScrollPane.setVisible(true);
            rowsLabel.setVisible(false);
            rows.setVisible(false);
            columnsLabel.setVisible(false);
            columns.setVisible(false);
        } else {
            textInputScrollPane.setVisible(false);
            rowsLabel.setVisible(true);
            rows.setVisible(true);
            columnsLabel.setVisible(true);
            columns.setVisible(true);
        }

        ActionListener cursorSelectedGame = CursorController.createListener(this, gameBoxListener);
        gameBox.addActionListener(cursorSelectedGame);
        ActionListener cursorPressedOk = CursorController.createListener(this, okButtonListener);
        ok.addActionListener(cursorPressedOk);
        ActionListener cursorPressedCancel =
                CursorController.createListener(this, cancelButtonListener);
        cancel.addActionListener(cursorPressedCancel);
    }

    /**
     * Initializes the puzzle options available for selection in the dialog.
     * The options are retrieved from the game board facade and sorted alphabetically.
     */
    public void initPuzzles() {
        this.games =
                GameBoardFacade.getInstance()
                        .getConfig()
                        .getFileCreationEnabledPuzzles()
                        .toArray(new String[0]);
        Arrays.sort(this.games);
        gameBox = new JComboBox(this.games);
    }


    /**
     * Handles the action events for the dialog, including interactions with the Ok and Cancel buttons
     *
     * @param e The action event to be processed
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ok) {
            String game = Config.convertDisplayNameToClassName((String) gameBox.getSelectedItem());

            try {
                if (game.equals("ShortTruthTable")) {
                    this.homePanel.openEditorWithNewPuzzle(
                            "ShortTruthTable", this.textArea.getText().split("\n"));
                } else {
                    this.homePanel.openEditorWithNewPuzzle(
                            game,
                            Integer.valueOf(this.rows.getText()),
                            Integer.valueOf(this.columns.getText()));
                }
                this.setVisible(false);
            } catch (IllegalArgumentException exception) {
                // Do nothing. This is here to prevent the dialog from closing if the dimensions are invalid.
            }
        } else {
            if (e.getSource() == cancel) {
                this.setVisible(false);
            } else {
                // Unknown Action Event
            }
        }
    }

    /**
     * Retrieves the selected game from the combo box
     *
     * @return the class name of the selected game
     */
    public String getGame() {
        return Config.convertDisplayNameToClassName((String) gameBox.getSelectedItem());
    }

    /**
     * Retrieves the number of rows specified in the dialog
     *
     * @return the number of rows as a string
     */
    public String getRows() {
        return rows.getText();
    }

    /**
     * Retrieves the number of columns specified in the dialog
     *
     * @return the number of columns as a string
     */
    public String getColumns() {
        return columns.getText();
    }

    /**
     * Retrieves the text entered in the text area, split by new lines.
     *
     * @return an array of strings, each representing as a line of text
     */
    public String[] getTextArea() { return textArea.getText().split("\n"); }
}
