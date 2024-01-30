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

    public void addCell(GridCell cell) {
        regionCells.add(cell);
    }

    public void removeCell(GridCell cell) {
        regionCells.remove(cell);
    }

    public List<GridCell> getCells() {
        return regionCells;
    }

    public int getSize(){
        return regionCells.size();
    }

    /*
    public void colorRegion(){}
     */
    
}
