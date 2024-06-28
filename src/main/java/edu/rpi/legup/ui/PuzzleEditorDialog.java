package edu.rpi.legup.ui;

import javax.swing.*;

public class PuzzleEditorDialog {

    public PuzzleEditorDialog(HomePanel homePanel) {
        String game = "";
        int r = 0;
        int c = 0;

        try {
            homePanel.openEditorWithNewPuzzle(game, r, c);
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to open editor with new puzzle");
            e.printStackTrace(System.out);
        }
    }

}
