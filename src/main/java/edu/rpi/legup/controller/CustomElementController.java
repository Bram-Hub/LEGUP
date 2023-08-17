package edu.rpi.legup.controller;

import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.ui.boardview.BoardView;
import java.awt.event.MouseEvent;

public class CustomElementController extends ElementController {

    private PuzzleElement puzzleElement;

    public CustomElementController(BoardView boardView) {
        super();
        this.setBoardView(boardView);
    }

    // Override the mouseEntered method with your custom implementation
    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX(); // X coordinate of the click
        int y = e.getY(); // Y coordinate of the click

        // Assuming boardView is accessible and properly set
        this.puzzleElement = boardView.getElement(e.getPoint()).getPuzzleElement();

        super.mouseEntered(e);
    }

    public PuzzleElement getPuzzleElement() {
        return this.puzzleElement;
    }
}
