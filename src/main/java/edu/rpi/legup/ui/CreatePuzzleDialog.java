package edu.rpi.legup.ui;

import edu.rpi.legup.app.GameBoardFacade;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreatePuzzleDialog extends JDialog implements ActionListener {
    private String[] games;
    JComboBox gameBox;
    JLabel gameLabel = new JLabel("Game:");
    JButton ok = new JButton("Ok");
    JButton cancel = new JButton("Cancel");
    JTextField width;
    JTextField height;
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

        gameLabel.setBounds(10, 30, 70, 25);
        gameBox.setBounds(80, 30, 190, 25);

        ok.setBounds(20, 130, 60, 25);
        cancel.setBounds(170, 130, 90, 25);

        c.add(gameLabel);
        c.add(gameBox);

        width = new JTextField();
        height = new JTextField();

        JLabel widthLabel = new JLabel("Width:");
        JLabel heightLabel = new JLabel("Height:");

        widthLabel.setBounds(30, 70, 60, 25);
        heightLabel.setBounds(30, 95, 60, 25);

        width.setBounds(100, 70, 60, 25);
        height.setBounds(100, 95, 60, 25);

        c.add(widthLabel);
        c.add(heightLabel);

        c.add(width);
        c.add(height);

        c.add(ok);
        c.add(cancel);

        ok.addActionListener(this);
        cancel.addActionListener(this);
    }

    public void initPuzzles() {
        Object[] o = GameBoardFacade.getInstance().getConfig().getPuzzleNames().toArray();

        games = new String[o.length];

        for (int x = 0; x < o.length; ++x) {
            games[x] = (String) o[x];
            if (games[x].equals("ShortTruthTable")) {
                games[x] = games[x] + " (Unsupported)";
            }
        }

        gameBox = new JComboBox(games);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ok) {
            String game = (String) gameBox.getSelectedItem();
            this.homePanel.openEditorWithNewPuzzle(game);
            setVisible(false);
        }
        else if (e.getSource() == cancel) {
            setVisible(false);
        }
    }
}
