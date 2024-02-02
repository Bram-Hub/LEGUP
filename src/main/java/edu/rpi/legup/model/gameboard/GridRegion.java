package edu.rpi.legup.model.gameboard;

import edu.rpi.legup.model.gameboard.GridCell;

import java.util.ArrayList;
import java.util.List;

public abstract class GridRegion {
    
    protected List<GridCell> regionCells;
    
    /**
     * Region Constructor
     */
    public GridRegion() {
        this.regionCells = new ArrayList<>();
    }

    /**
     * Adds the cell to the region
     * @param cell cell to be added to the region
     */
    public void addCell(GridCell cell) {
        regionCells.add(cell);
    }

    /**
     * Removes the cell from the region
     * @param cell cell to be remove from the region
     */
    public void removeCell(GridCell cell) {
        regionCells.remove(cell);
    }

    /**
     * Returns the list of cells in the region
     * @return list of cells in region
     */
    public List<GridCell> getCells() {
        return regionCells;
    }

    /**
     * Returns the number of cells in the region
     * @return number of cells in the region
     */
    public int getSize(){
        return regionCells.size();
    }

    /*
    public void colorRegion(){}
     */
    
}
