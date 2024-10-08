package edu.rpi.legup.model.gameboard;

import java.util.ArrayList;
import java.util.List;

/**
 * GridRegion represents a collection of cells within a grid. It manages a list of cells and
 * provides methods to add, remove, and retrieve cells from the region.
 *
 * @param <T> the type of cell managed by the GridRegion
 */
public abstract class GridRegion<T> {

    protected List<T> regionCells;

    /** Region Constructor */
    public GridRegion() {
        this.regionCells = new ArrayList<>();
    }

    /**
     * Adds the cell to the region
     *
     * @param cell cell to be added to the region
     */
    public void addCell(T cell) {
        regionCells.add(cell);
    }

    /**
     * Removes the cell from the region
     *
     * @param cell cell to be remove from the region
     */
    public void removeCell(T cell) {
        regionCells.remove(cell);
    }

    /**
     * Returns the list of cells in the region
     *
     * @return list of cells in region
     */
    public List<T> getCells() {
        return regionCells;
    }

    /**
     * Returns the number of cells in the region
     *
     * @return number of cells in the region
     */
    public int getSize() {
        return regionCells.size();
    }

    /*
    public void colorRegion(){}
     */

}
