package edu.rpi.legup.ui;

import edu.rpi.legup.ui.boardview.BoardView;

import javax.swing.*;

public class PuzzleCreatorView extends JFrame {

    private BoardView boardView;

    public PuzzleCreatorView(BoardView boardView) {
        this.boardView = boardView;

        setContentPane(new DynamicView(boardView));
    }

}
