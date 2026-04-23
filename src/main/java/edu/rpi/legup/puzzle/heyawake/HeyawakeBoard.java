package edu.rpi.legup.puzzle.heyawake;

import edu.rpi.legup.model.gameboard.GridBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeyawakeBoard extends GridBoard {

    private Map<Integer, List<HeyawakeCell>> regions;

    /**
     * Constructs a HeyawakeBoard with the given width and height
     *
     * @param width the width of the board
     * @param height the height of the board
     */
    public HeyawakeBoard(int width, int height) {
        super(width, height);
        this.regions = new HashMap<>();
    }

    /**
     * Constructs a square HeyawakeBoard with the given size
     *
     * @param size the width and height of the board
     */
    public HeyawakeBoard(int size) {
        this(size, size);
    }

    /**
     * Gets the cell at the given coordinates
     *
     * @param x the x coordinate of the cell
     * @param y the y coordinate of the cell
     * @return the HeyawakeCell at the given coordinates
     */
    @Override
    public HeyawakeCell getCell(int x, int y) {
        return (HeyawakeCell) super.getCell(x, y);
    }

    /**
     * Gets the list of cells belonging to the specified region
     *
     * @param regionIndex the index of the region to retrieve
     * @return the list of HeyawakeCells in the specified region
     */
    public List<HeyawakeCell> getRegion(int regionIndex) {
        return this.regions.get(regionIndex);
    }

    /**
     * Gets the map of all regions on the board
     *
     * @return a map of region indices to their corresponding lists of HeyawakeCells
     */
    public Map<Integer, List<HeyawakeCell>> getRegions() {
        return this.regions;
    }

    /**
     * Creates a deep copy of this board, including all cells and regions
     *
     * @return a new HeyawakeBoard that is a copy of this board
     */
    @Override
    public HeyawakeBoard copy() {
        HeyawakeBoard copy = new HeyawakeBoard(dimension.width, dimension.height);
        for (List<HeyawakeCell> region : regions.values()) {
            List<HeyawakeCell> newCpy = new ArrayList<>();
            for (HeyawakeCell cell : region) {
                Point point = cell.getLocation();
                HeyawakeCell cellCpy = cell.copy();
                copy.setCell(point.x, point.y, cellCpy);
                newCpy.add(cellCpy);
            }
            copy.regions.put(region.get(0).getRegionIndex(), newCpy);
        }
        for (PuzzleElement e : modifiedData) {
            copy.getPuzzleElement(e).setModifiable(false);
        }
        return copy;
    }
}
