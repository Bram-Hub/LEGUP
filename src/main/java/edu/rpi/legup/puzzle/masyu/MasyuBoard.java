package edu.rpi.legup.puzzle.masyu;

import edu.rpi.legup.model.gameboard.GridBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;

import java.util.ArrayList;
import java.util.List;

public class MasyuBoard extends GridBoard {

    private List<MasyuLine> lines;

    public MasyuBoard(int width, int height) {
        super(width, height);
        this.lines = new ArrayList<>();
    }

    public MasyuBoard(int size) {
        this(size, size);
    }

    @Override
    public MasyuCell getCell(int x, int y) {
        return (MasyuCell) super.getCell(x, y);
    }

    public List<MasyuLine> getLines() {
        return lines;
    }

    public void setLines(List<MasyuLine> lines) {
        this.lines = lines;
    }

    @Override
    public void notifyChange(PuzzleElement puzzleElement) {
        if (puzzleElement instanceof MasyuLine) {
            lines.add((MasyuLine) puzzleElement);
        } else {
            super.notifyChange(puzzleElement);
        }
    }

    @Override
    public MasyuBoard copy() {
        MasyuBoard copy = new MasyuBoard(dimension.width, dimension.height);
        for (int x = 0; x < this.dimension.width; x++) {
            for (int y = 0; y < this.dimension.height; y++) {
                copy.setCell(x, y, getCell(x, y).copy());
            }
        }
        for (MasyuLine line : lines) {
            copy.lines.add(line.copy());
        }
        for(PuzzleElement e : modifiedData) {
            copy.getPuzzleElement(e).setModifiable(false);
        }
        return copy;
    }
}
