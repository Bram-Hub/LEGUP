package edu.rpi.legup.puzzle.heyawake;

import edu.rpi.legup.model.gameboard.GridBoard;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeyawakeBoard extends GridBoard {

    private Map<Integer, List<HeyawakeCell>> regions;

    public HeyawakeBoard(int width, int height) {
        super(width, height);
        this.regions = new HashMap<>();
    }

    public HeyawakeBoard(int size) {
        this(size, size);
    }

    @Override
    public HeyawakeCell getCell(int x, int y)
    {
        return (HeyawakeCell)super.getCell(x, y);
    }

    public List<HeyawakeCell> getRegion(int regionIndex) {
        return this.regions.get(regionIndex);
    }

    public Map<Integer, List<HeyawakeCell>> getRegions() {
        return this.regions;
    }

    @Override
    public HeyawakeBoard copy()
    {
        HeyawakeBoard copy = new HeyawakeBoard(dimension.width, dimension.height);
        for(List<HeyawakeCell> region : regions.values()) {
            List<HeyawakeCell> newCpy = new ArrayList<>();
            for(HeyawakeCell cell : region) {
                Point point = cell.getLocation();
                HeyawakeCell cellCpy = cell.copy();
                copy.setCell(point.x, point.y, cellCpy);
                newCpy.add(cellCpy);
            }
            copy.regions.put(region.get(0).getRegionIndex(), newCpy);
        }
        return copy;
    }
}
