package edu.rpi.legup.ui;

import javax.swing.*;

public class PuzzleEditorPanel extends LegupPanel {
    public PuzzleEditorPanel() {

    }

    @Override
    public void makeVisible() {
        render();
    }

    private void render() {
        add(new JLabel("Puzzle Editor goes here"));
    }
}
