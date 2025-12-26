package edu.rpi.legup.puzzle.skyscrapers;

import edu.rpi.legup.model.elements.Element;
import edu.rpi.legup.model.gameboard.GridBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.ui.proofeditorui.treeview.TreeView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SkyscrapersBoard extends GridBoard {
    private static final Logger LOGGER = LogManager.getLogger(SkyscrapersBoard.class.getName());

    private ArrayList<SkyscrapersClue> eastClues;
    // EAST clues
    private ArrayList<SkyscrapersClue> southClues;
    // SOUTH clues
    private ArrayList<SkyscrapersClue> westClues;
    // WEST clues
    private ArrayList<SkyscrapersClue> northClues;
    // NORTH clues

    private boolean viewFlag = false;
    private boolean dupeFlag = false;

    public SkyscrapersBoard(int size) {
        super(size, size);

        this.eastClues = new ArrayList<>();
        this.southClues = new ArrayList<>();
        this.westClues = new ArrayList<>();
        this.northClues = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            eastClues.add(null);
            southClues.add(null);
            westClues.add(null);
            northClues.add(null);
        }
    }

    /**
     * @return eastClues a list of the eastern clues ordered from loc.y = 0 to max
     */
    public ArrayList<SkyscrapersClue> getEastClues() {
        return eastClues;
    }

    /**
     * @return southClues a list of the southern clues ordered from loc.x = 0 to max
     */
    public ArrayList<SkyscrapersClue> getSouthClues() {
        return southClues;
    }

    /**
     * @return westClues a list of the western clues ordered from loc.y = 0 to max
     */
    public ArrayList<SkyscrapersClue> getWestClues() {
        return westClues;
    }

    /**
     * @return northClues a list of the northern clues ordered from loc.x = 0 to max
     */
    public ArrayList<SkyscrapersClue> getNorthClues() {
        return northClues;
    }

    public boolean getDupeFlag() {
        return dupeFlag;
    }

    public boolean getViewFlag() {
        return viewFlag;
    }

    public void setDupeFlag(boolean newFlag) {
        dupeFlag = newFlag;
    }

    public void setViewFlag(boolean newFlag) {
        viewFlag = newFlag;
    }

    @Override
    public SkyscrapersCell getCell(int x, int y) {
        return (SkyscrapersCell) super.getCell(x, y);
    }

    public int getSize() {
        return this.getWidth();
    }

    @Override
    public PuzzleElement getPuzzleElement(PuzzleElement element) {
        // If the element index is -2, it is a clue and should be returned separately
        if (element.getIndex() == -2) {
            return element;
        }
        return super.getPuzzleElement(element);
    }

    /**
     * Gets the cells of a certain type directly adjacent to a given cell
     *
     * @param cell at the center,
     * @param type of cell to collect
     * @return list of cells of the given type
     */
    public List<SkyscrapersCell> getAdjacent(SkyscrapersCell cell, SkyscrapersType type) {
        List<SkyscrapersCell> adj = new ArrayList<>();
        Point loc = cell.getLocation();
        SkyscrapersCell up = getCell(loc.x, loc.y - 1);
        SkyscrapersCell right = getCell(loc.x + 1, loc.y);
        SkyscrapersCell down = getCell(loc.x, loc.y + 1);
        SkyscrapersCell left = getCell(loc.x - 1, loc.y);
        if (up != null && (up.getType() == type || type == SkyscrapersType.ANY)) {
            adj.add(up);
        }
        if (right != null && (right.getType() == type || type == SkyscrapersType.ANY)) {
            adj.add(right);
        }
        if (down != null && (down.getType() == type || type == SkyscrapersType.ANY)) {
            adj.add(down);
        }
        if (left != null && (left.getType() == type || type == SkyscrapersType.ANY)) {
            adj.add(left);
        }
        return adj;
    }

    /**
     * Gets the cells of a certain type directly diagonal to a given cell
     *
     * @param cell at the center,
     * @param type of cell to collect
     * @return list of cells of the given type
     */
    public List<SkyscrapersCell> getDiagonals(SkyscrapersCell cell, SkyscrapersType type) {
        List<SkyscrapersCell> dia = new ArrayList<>();
        Point loc = cell.getLocation();
        SkyscrapersCell upRight = getCell(loc.x + 1, loc.y - 1);
        SkyscrapersCell downRight = getCell(loc.x + 1, loc.y + 1);
        SkyscrapersCell downLeft = getCell(loc.x - 1, loc.y + 1);
        SkyscrapersCell upLeft = getCell(loc.x - 1, loc.y - 1);
        if (upRight != null && (upRight.getType() == type || type == SkyscrapersType.ANY)) {
            dia.add(upRight);
        }
        if (downLeft != null && (downLeft.getType() == type || type == SkyscrapersType.ANY)) {
            dia.add(downLeft);
        }
        if (downRight != null && (downRight.getType() == type || type == SkyscrapersType.ANY)) {
            dia.add(downRight);
        }
        if (upLeft != null && (upLeft.getType() == type || type == SkyscrapersType.ANY)) {
            dia.add(upLeft);
        }
        return dia;
    }

    /**
     * Gets the cells of a certain type in a given row/column
     *
     * @param index: y pos of row or x pos of col,
     * @param type of cell to collect,
     * @param isRow true if row, false if col
     * @return list of cells of the given type, ordered west to east or north to south
     */
    public List<SkyscrapersCell> getRowCol(int index, SkyscrapersType type, boolean isRow) {
        List<SkyscrapersCell> list = new ArrayList<>();
        for (int i = 0; i < dimension.height; i++) {
            SkyscrapersCell cell;
            if (isRow) {
                cell = getCell(i, index);
            } else {
                cell = getCell(index, i);
            }

            if (cell.getType() == type || type == SkyscrapersType.ANY) {
                list.add(cell);
            }
        }
        return list;
    }

    /** Prints a semblance of the board to console (helps in debugging) */
    public void printBoard() {
        for (int i = 0; i < this.dimension.height; i++) {
            for (SkyscrapersCell cell : this.getRowCol(i, SkyscrapersType.ANY, true)) {
                if (LOGGER.isDebugEnabled()) {
                    if (cell.getType() == SkyscrapersType.Number) {
                        LOGGER.debug("{} ", cell.getData());
                    } else {
                        LOGGER.debug(0 + " ");
                    }
                }
            }
            System.out.println();
        }
    }

    /**
     * @param x position of cell
     * @param y position of cell
     * @param e Element to be placed (null if nothing selected)
     * @param m MouseEvent Increases clue values if in editor mode. Currently allows for presetting
     *     tile values, though they will not be saved.
     */
    @Override
    public void setCell(int x, int y, Element e, MouseEvent m) {
        SkyscrapersClue clue = this.getClue(x, y);
        if (e == null) return;
        if (clue != null) {
            if (!e.getElementID().equals("SKYS-ELEM-0001")) {
                return;
            }

            if (m.getButton() == MouseEvent.BUTTON1) {
                if (clue.getData() < dimension.height) {
                    clue.setData(clue.getData() + 1);
                } else {
                    clue.setData(1);
                }
            } else {
                if (clue.getData() > 1) {
                    clue.setData(clue.getData() - 1);
                } else {
                    clue.setData(dimension.height);
                }
            }
        } else {
            super.setCell(x - 1, y - 1, e, m);
        }
    }

    /**
     * @param x position of element on boardView
     * @param y position of element on boardView
     * @return The clue at the given position
     */
    public SkyscrapersClue getClue(int x, int y) {
        int viewIndex = getSize() + 1;
        if (x == 0 && y > 0 && y < viewIndex) {
            return westClues.get(y - 1);
        } else if (x == viewIndex && y > 0 && y < viewIndex) {
            return eastClues.get(y - 1);
        } else if (y == 0 && x > 0 && x < viewIndex) {
            return northClues.get(x - 1);
        } else if (y == viewIndex && x > 0 && x < viewIndex) {
            return southClues.get(x - 1);
        }
        return null;
    }

    @Override
    public SkyscrapersBoard copy() {
        SkyscrapersBoard copy = new SkyscrapersBoard(dimension.width);
        for (int x = 0; x < this.dimension.width; x++) {
            for (int y = 0; y < this.dimension.height; y++) {
                copy.setCell(x, y, getCell(x, y).copy());
            }
        }
        for (PuzzleElement e : modifiedData) {
            copy.getPuzzleElement(e).setModifiable(false);
        }
        copy.eastClues = eastClues;
        copy.southClues = southClues;
        copy.westClues = westClues;
        copy.northClues = northClues;

        copy.dupeFlag = dupeFlag;
        copy.viewFlag = viewFlag;
        return copy;
    }
}
