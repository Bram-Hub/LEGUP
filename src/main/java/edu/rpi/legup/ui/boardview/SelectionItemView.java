package edu.rpi.legup.ui.boardview;

import edu.rpi.legup.model.gameboard.PuzzleElement;
import javax.swing.*;

public class SelectionItemView extends JMenuItem {
    private PuzzleElement data;

    public SelectionItemView(PuzzleElement data, Icon icon) {
        super(icon);
        this.data = data;
    }

    public SelectionItemView(PuzzleElement data, String display) {
        super(display);
        this.data = data;
    }

    public SelectionItemView(PuzzleElement data, int display) {
        super(String.valueOf(display));
        this.data = data;
    }

    public SelectionItemView(PuzzleElement data) {
        this(data, (Integer) data.getData());
    }

    public PuzzleElement getData() {
        return data;
    }
}
