package edu.rpi.legup.ui.boardview;

import edu.rpi.legup.model.gameboard.PuzzleElement;
import javax.swing.*;

/**
 * A menu item view class that represents a selectable item in a menu, associated with a
 * PuzzleElement. This class extends JMenuItem and provides additional functionality to handle
 * PuzzleElement data.
 */
public class SelectionItemView extends JMenuItem {
    private PuzzleElement data;

    /**
     * Constructs a SelectionItemView with the specified PuzzleElement and icon. Initializes the
     * menu item with the given icon and associates it with the provided PuzzleElement.
     *
     * @param data the PuzzleElement associated with this menu item
     * @param icon the icon to be displayed on the menu item
     */
    public SelectionItemView(PuzzleElement data, Icon icon) {
        super(icon);
        this.data = data;
    }

    /**
     * Constructs a SelectionItemView with the specified PuzzleElement and display text. Initializes
     * the menu item with the given display text and associates it with the provided PuzzleElement.
     *
     * @param data the PuzzleElement associated with this menu item
     * @param display the text to be displayed on the menu item
     */
    public SelectionItemView(PuzzleElement data, String display) {
        super(display);
        this.data = data;
    }

    /**
     * Constructs a SelectionItemView with the specified PuzzleElement and display integer.
     * Initializes the menu item with the integer converted to a string and associates it with the
     * provided PuzzleElement.
     *
     * @param data the PuzzleElement associated with this menu item
     * @param display the integer to be displayed on the menu item
     */
    public SelectionItemView(PuzzleElement data, int display) {
        super(String.valueOf(display));
        this.data = data;
    }

    /**
     * Constructs a SelectionItemView with the specified PuzzleElement. Initializes the menu item
     * with the data's integer representation as display text.
     *
     * @param data the PuzzleElement associated with this menu item
     */
    public SelectionItemView(PuzzleElement data) {
        this(data, (Integer) data.getData());
    }

    public PuzzleElement getData() {
        return data;
    }
}
