package edu.rpi.legup.puzzle.yinyang;

import edu.rpi.legup.model.gameboard.PuzzleElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class YinYangBoard {
    private final int width;
    private final int height;
    private final List<PuzzleElement> elements; // Manages puzzle elements manually

    public YinYangBoard(int width, int height) {
        this.width = width;
        this.height = height;
        this.elements = new ArrayList<>();
        initializeBoard();
    }

    /**
     * Initializes the board with UNKNOWN cells.
     */
    private void initializeBoard() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                YinYangCell cell = new YinYangCell(YinYangType.UNKNOWN.toValue(), x, y);
                this.elements.add(cell); // Add cells directly to the elements list
            }
        }
    }

    /**
     * Gets the cell at the specified (x, y) location.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the YinYangCell at (x, y) or null if out of bounds
     */
    public YinYangCell getCell(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return null; // Out of bounds
        }

        for (PuzzleElement element : elements) {
            YinYangCell cell = (YinYangCell) element;
            if (cell.getX() == x && cell.getY() == y) {
                return cell;
            }
        }
        return null;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * Calculates the sizes of all connected WHITE and BLACK regions.
     *
     * @return a map of YinYangType to lists of region sizes
     */
    public Map<YinYangType, List<Integer>> calculateRegionSizes() {
        Map<YinYangType, List<Integer>> regionSizes = new HashMap<>();
        Map<YinYangCell, Set<YinYangCell>> regions = YinYangUtilities.getRegions(this);

        for (Set<YinYangCell> region : regions.values()) {
            YinYangType type = region.iterator().next().getType();
            regionSizes.computeIfAbsent(type, k -> new ArrayList<>()).add(region.size());
        }
        return regionSizes;
    }

    /**
     * Gets all puzzle elements.
     *
     * @return the list of puzzle elements
     */
    public List<PuzzleElement> getPuzzleElements() {
        return elements;
    }
}