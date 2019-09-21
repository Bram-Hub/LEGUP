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

    public TreeTentBoard(int size) {
        this(size, size);
    }

    public ArrayList<TreeTentLine> getLines() {
        return lines;
    }

    public ArrayList<TreeTentClue> getRowClues() {
        return rowClues;
    }

    public ArrayList<TreeTentClue> getColClues() {
        return colClues;
    }

    @Override
    public TreeTentCell getCell(int x, int y) {
        return (TreeTentCell) super.getCell(x, y);
    }

    @Override
    public PuzzleElement getPuzzleElement(PuzzleElement element) {
        switch (element.getIndex()) {
            case -2:
                return element;
            case -1:
                TreeTentLine line = (TreeTentLine) element;
                TreeTentLine thisLine = null;
                for (TreeTentLine l : lines) {
                    if (line.compare(l)) {
                        thisLine = l;
                        break;
                    }
                }
                return thisLine;
            default:
                return super.getPuzzleElement(element);
        }
    }

    /**
     * Called when a {@link PuzzleElement} has been added and passes in the equivalent puzzle element with the data.
     *
     * @param puzzleElement equivalent puzzle element with the data.
     */
    @Override
    public void notifyAddition(PuzzleElement puzzleElement) {
        if(puzzleElement instanceof TreeTentLine) {
            lines.add((TreeTentLine) puzzleElement);
        }
    }

    /**
     * Called when a {@link PuzzleElement} has been deleted and passes in the equivalent puzzle element with the data.
     *
     * @param puzzleElement equivalent puzzle element with the data.
     */
    @Override
    public void notifyDeletion(PuzzleElement puzzleElement) {
        if(puzzleElement instanceof TreeTentLine) {
            for(TreeTentLine line : lines) {
                if(line.compare((TreeTentLine)puzzleElement)) {
                    lines.remove(line);
                    break;
                }
            }
        }
    }

    public List<TreeTentCell> getAdjacent(TreeTentCell cell, TreeTentType type) {
        List<TreeTentCell> adj = new ArrayList<>();
        Point loc = cell.getLocation();
        TreeTentCell up = getCell(loc.x, loc.y - 1);
        TreeTentCell right = getCell(loc.x + 1, loc.y);
        TreeTentCell down = getCell(loc.x, loc.y + 1);
        TreeTentCell left = getCell(loc.x - 1, loc.y);
        if (up != null && up.getType() == type) {
            adj.add(up);
        }
        if (right != null && right.getType() == type) {
            adj.add(right);
        }
        if (down != null && down.getType() == type) {
            adj.add(down);
        }
        if (left != null && left.getType() == type) {
            adj.add(left);
        }
        return adj;
    }

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

    public List<TreeTentCell> getRowCol(int index, TreeTentType type, boolean isRow) {
        List<TreeTentCell> list = new ArrayList<>();
        if (isRow) {
            for (int i = 0; i < dimension.height; i++) {
                TreeTentCell cell = getCell(i, index);
                if (cell.getType() == type) {
                    list.add(cell);
                }
            }
        } else {
            for (int i = 0; i < dimension.width; i++) {
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
        for(PuzzleElement e : modifiedData) {
            copy.getPuzzleElement(e).setModifiable(false);
        }
        copy.rowClues = rowClues;
        copy.colClues = colClues;
        return copy;
    }
}
