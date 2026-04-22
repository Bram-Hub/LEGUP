package edu.rpi.legup.puzzle.treetent;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.GridBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TreeTentBoard extends GridBoard {

    private ArrayList<TreeTentLine> lines;

    private ArrayList<TreeTentClue> rowClues;
    private ArrayList<TreeTentClue> colClues;

    /**
     * Constructs a TreeTentBoard with the specified width and height
     *
     * @param width the number of columns in the board
     * @param height the number of rows in the board
     */
    public TreeTentBoard(int width, int height) {
        super(width, height);

        this.lines = new ArrayList<>();

        this.rowClues = new ArrayList<>();
        this.colClues = new ArrayList<>();

        for (int i = 0; i < height; i++) {
            rowClues.add(null);
        }
        for (int i = 0; i < width; i++) {
            colClues.add(null);
        }
    }

    /**
     * Constructs a square TreeTentBoard with the specified size
     *
     * @param size the number of rows and columns in the board
     */
    public TreeTentBoard(int size) {
        this(size, size);
    }

    /**
     * Returns the list of lines on the board
     *
     * @return the list of {@link TreeTentLine} objects on the board
     */
    public ArrayList<TreeTentLine> getLines() {
        return lines;
    }

    /**
     * Returns the list of row clues for the board
     *
     * @return the list of {@link TreeTentClue} objects representing row clues
     */
    public ArrayList<TreeTentClue> getRowClues() {
        return rowClues;
    }

    /**
     * Returns the list of column clues for the board
     *
     * @return the list of {@link TreeTentClue} objects representing column clues
     */
    public ArrayList<TreeTentClue> getColClues() {
        return colClues;
    }

    /**
     * Returns the {@link TreeTentCell} at the specified coordinates
     *
     * @param x the x coordinate of the cell
     * @param y the y coordinate of the cell
     * @return the TreeTentCell at the given coordinates
     */
    @Override
    public TreeTentCell getCell(int x, int y) {
        return (TreeTentCell) super.getCell(x, y);
    }

    /**
     * Returns the puzzle element corresponding to the given element. For elements with index -2 or
     * -1, the element itself is returned directly.
     *
     * @param element the puzzle element to look up
     * @return the corresponding puzzle element
     */
    @Override
    public PuzzleElement getPuzzleElement(PuzzleElement element) {
        return switch (element.getIndex()) {
            case -2 -> element;
            case -1 -> element;
            default -> super.getPuzzleElement(element);
        };
    }

    /**
     * Sets the puzzle element at the specified index. If the index is -1, the element is added to
     * the lines list. Otherwise, the element is set at the given index in the puzzle elements list.
     *
     * @param index the index at which to set the puzzle element
     * @param puzzleElement the puzzle element to set
     */
    @Override
    public void setPuzzleElement(int index, PuzzleElement puzzleElement) {
        if (index == -1) {
            lines.add((TreeTentLine) puzzleElement);
        } else if (index < puzzleElements.size()) {
            puzzleElements.set(index, puzzleElement);
        }
    }

    /**
     * Notifies the board that a puzzle element has changed. If the element's index is -1, it is
     * added to the lines list. Otherwise, the element is updated at the given index.
     *
     * @param puzzleElement the puzzle element that has changed
     */
    @Override
    public void notifyChange(PuzzleElement puzzleElement) {
        int index = puzzleElement.getIndex();
        if (index == -1) {
            lines.add((TreeTentLine) puzzleElement);
        } else if (index < puzzleElements.size()) {
            puzzleElements.set(index, puzzleElement);
        }
    }

    /**
     * Returns the clue at the specified coordinates. Row clues are stored at x equal to the board
     * width, and column clues are stored at y equal to the board height.
     *
     * @param x the x coordinate of the clue
     * @param y the y coordinate of the clue
     * @return the {@link TreeTentClue} at the specified coordinates, or null if none exists
     */
    public TreeTentClue getClue(int x, int y) {
        if (x == getWidth() && 0 <= y && y < getHeight()) {
            return rowClues.get(y);
        } else {
            if (y == getHeight() && 0 <= x && x < getWidth()) {
                return colClues.get(x);
            }
        }
        return null;
    }

    /**
     * Called when a {@link PuzzleElement} has been added and passes in the equivalent puzzle
     * element with the data.
     *
     * @param puzzleElement equivalent puzzle element with the data.
     */
    @Override
    public void notifyAddition(PuzzleElement puzzleElement) {
        if (puzzleElement instanceof TreeTentLine) {
            lines.add((TreeTentLine) puzzleElement);
        }
    }

    /**
     * Called when a {@link PuzzleElement} has been deleted and passes in the equivalent puzzle
     * element with the data.
     *
     * @param puzzleElement equivalent puzzle element with the data.
     */
    @Override
    public void notifyDeletion(PuzzleElement puzzleElement) {
        if (puzzleElement instanceof TreeTentLine) {
            for (TreeTentLine line : lines) {
                if (line.compare((TreeTentLine) puzzleElement)) {
                    lines.remove(line);
                    break;
                }
            }
        }
    }

    /**
     * Get a list of all orthogonally adjacent cells.
     *
     * @param cell The cell to get adjacent cells from.
     * @param type The cell types to get.
     * @return List of adjacent cells in the form { up, right, down, left }. If an adjacent cell is
     *     null, it will not be added to the list.
     */
    public List<TreeTentCell> getAdjacent(TreeTentCell cell, TreeTentType type) {
        List<TreeTentCell> adj = new ArrayList<>();
        Point loc = cell.getLocation();
        for (int i = -2; i < 2; i++) {
            TreeTentCell adjCell = getCell(loc.x + (i % 2), loc.y + ((i + 1) % 2));
            if (adjCell != null && adjCell.getType() == type) {
                adj.add(adjCell);
            }
        }
        return adj;
    }

    /**
     * Gets all cells of a specified type that are diagonals of a specified cell
     *
     * @param cell the base cell
     * @param type the type to look for
     * @return a list of TreeTentCells that are diagonals of the given TreeTentCell and are of the
     *     given TreeTentType
     */
    public List<TreeTentCell> getDiagonals(TreeTentCell cell, TreeTentType type) {
        List<TreeTentCell> dia = new ArrayList<>();
        Point loc = cell.getLocation();
        TreeTentCell upRight = getCell(loc.x + 1, loc.y - 1);
        TreeTentCell downRight = getCell(loc.x + 1, loc.y + 1);
        TreeTentCell downLeft = getCell(loc.x - 1, loc.y + 1);
        TreeTentCell upLeft = getCell(loc.x - 1, loc.y - 1);
        if (upRight != null && upRight.getType() == type) {
            dia.add(upRight);
        }
        if (downLeft != null && downLeft.getType() == type) {
            dia.add(downLeft);
        }
        if (downRight != null && downRight.getType() == type) {
            dia.add(downRight);
        }
        if (upLeft != null && upLeft.getType() == type) {
            dia.add(upLeft);
        }
        return dia;
    }

    /**
     * Creates and returns a list of TreeTentCells that match the given TreeTentType
     *
     * @param index the row or column number
     * @param type type of TreeTent element
     * @param isRow boolean value based on whether a row of column is being checked
     * @return List of TreeTentCells that match the given TreeTentType
     */
    public List<TreeTentCell> getRowCol(int index, TreeTentType type, boolean isRow) {
        List<TreeTentCell> list = new ArrayList<>();
        if (isRow) {
            for (int i = 0; i < dimension.width; i++) {
                TreeTentCell cell = getCell(i, index);
                if (cell.getType() == type) {
                    list.add(cell);
                }
            }
        } else {
            for (int i = 0; i < dimension.height; i++) {
                TreeTentCell cell = getCell(index, i);
                if (cell.getType() == type) {
                    list.add(cell);
                }
            }
        }
        return list;
    }

    /**
     * Determines if this board contains the equivalent puzzle elements as the one specified
     *
     * @param board board to check equivalence
     * @return true if the boards are equivalent, false otherwise
     */
    @Override
    public boolean equalsBoard(Board board) {
        TreeTentBoard treeTentBoard = (TreeTentBoard) board;
        for (TreeTentLine l1 : lines) {
            boolean hasLine = false;
            for (TreeTentLine l2 : treeTentBoard.lines) {
                if (l1.compare(l2)) {
                    hasLine = true;
                }
            }
            if (!hasLine) {
                return false;
            }
        }
        return super.equalsBoard(treeTentBoard);
    }

    /**
     * Performs a deep copy of the TreeTentBoard
     *
     * @return a TreeTentBoard object that is a deep copy of the current TreeTentBoard
     */
    @Override
    public TreeTentBoard copy() {
        TreeTentBoard copy = new TreeTentBoard(dimension.width, dimension.height);
        for (int x = 0; x < this.dimension.width; x++) {
            for (int y = 0; y < this.dimension.height; y++) {
                copy.setCell(x, y, getCell(x, y).copy());
            }
        }
        for (TreeTentLine line : lines) {
            TreeTentLine lineCpy = line.copy();
            lineCpy.setModifiable(false);
            copy.getLines().add(lineCpy);
        }
        for (PuzzleElement e : modifiedData) {
            copy.getPuzzleElement(e).setModifiable(false);
        }
        copy.rowClues = rowClues;
        copy.colClues = colClues;
        return copy;
    }
}
