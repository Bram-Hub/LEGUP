package edu.rpi.legup.puzzle.nurikabe;

import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.utility.DisjointSets;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NurikabeUtilities {
    private static final Logger LOGGER = LogManager.getLogger(NurikabeUtilities.class.getName());

    /**
     * Gets all of the numbered cells in the Nurikabe board
     *
     * @param board nurikabe board
     * @return a list of all of the numbered cells
     */
    public static Set<NurikabeCell> getNurikabeNumberedCells(NurikabeBoard board) {
        Set<NurikabeCell> numberedCells = new HashSet<>();

        for (PuzzleElement data : board.getPuzzleElements()) {
            NurikabeCell cell = (NurikabeCell) data;
            if (cell.getType() == NurikabeType.NUMBER) {
                numberedCells.add(cell);
            }
        }
        return numberedCells;
    }

    /**
     * Gets nurikabe regions of black, white, and unknown cells
     *
     * @param board nurikabe board
     * @return a disjoint set of the regions
     */
    public static DisjointSets<NurikabeCell> getNurikabeRegions(NurikabeBoard board) {
        int width = board.getWidth();
        int height = board.getHeight();

        DisjointSets<NurikabeCell> regions = new DisjointSets<>();
        for (PuzzleElement data : board.getPuzzleElements()) {
            regions.createSet((NurikabeCell) data);
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                NurikabeCell cell = board.getCell(x, y);
                NurikabeCell rightCell = board.getCell(x + 1, y);
                NurikabeCell downCell = board.getCell(x, y + 1);

                if (cell.getType() == NurikabeType.NUMBER || cell.getType() == NurikabeType.WHITE) {
                    if (rightCell != null
                            && (rightCell.getType() == NurikabeType.NUMBER
                                    || rightCell.getType() == NurikabeType.WHITE)) {
                        regions.union(cell, rightCell);
                    }
                    if (downCell != null
                            && (downCell.getType() == NurikabeType.NUMBER
                                    || downCell.getType() == NurikabeType.WHITE)) {
                        regions.union(cell, downCell);
                    }
                } else {
                    if (cell.getType() == NurikabeType.BLACK) {
                        if (rightCell != null && rightCell.getType() == NurikabeType.BLACK) {
                            regions.union(cell, rightCell);
                        }
                        if (downCell != null && downCell.getType() == NurikabeType.BLACK) {
                            regions.union(cell, downCell);
                        }
                    } else {
                        if (cell.getType() == NurikabeType.UNKNOWN) {
                            if (rightCell != null && rightCell.getType() == NurikabeType.UNKNOWN) {
                                regions.union(cell, rightCell);
                            }
                            if (downCell != null && downCell.getType() == NurikabeType.UNKNOWN) {
                                regions.union(cell, downCell);
                            }
                        }
                    }
                }
            }
        }
        return regions;
    }

    /**
     * Gets the disjoint set containing the possible black regions be interpreting all unknown cells
     * as black cells
     *
     * @param board nurikabe board
     * @return the disjoint set containing the possible black regions
     */
    public static DisjointSets<NurikabeCell> getPossibleBlackRegions(NurikabeBoard board) {
        int width = board.getWidth();
        int height = board.getHeight();

        DisjointSets<NurikabeCell> blackRegions = new DisjointSets<>();
        for (PuzzleElement data : board.getPuzzleElements()) {
            NurikabeCell cell = (NurikabeCell) data;
            if (cell.getType() == NurikabeType.BLACK || cell.getType() == NurikabeType.UNKNOWN) {
                blackRegions.createSet(cell);
            }
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                NurikabeCell cell = board.getCell(x, y);
                NurikabeCell rightCell = board.getCell(x + 1, y);
                NurikabeCell downCell = board.getCell(x, y + 1);
                if (cell.getType() == NurikabeType.BLACK
                        || cell.getType() == NurikabeType.UNKNOWN) {
                    if (rightCell != null
                            && (rightCell.getType() == NurikabeType.BLACK
                                    || rightCell.getType() == NurikabeType.UNKNOWN)) {
                        blackRegions.union(cell, rightCell);
                    }
                    if (downCell != null
                            && (downCell.getType() == NurikabeType.BLACK
                                    || downCell.getType() == NurikabeType.UNKNOWN)) {
                        blackRegions.union(cell, downCell);
                    }
                }
            }
        }
        return blackRegions;
    }

    /**
     * Gets the disjoint set containing the possible white regions be interpreting all unknown cells
     * as white cells
     *
     * @param board nurikabe board
     * @return the disjoint set containing the possible white regions
     */
    public static DisjointSets<NurikabeCell> getPossibleWhiteRegions(NurikabeBoard board) {
        int width = board.getWidth();
        int height = board.getHeight();

        DisjointSets<NurikabeCell> whiteRegions = new DisjointSets<>();
        for (PuzzleElement data : board.getPuzzleElements()) {
            NurikabeCell cell = (NurikabeCell) data;
            if (cell.getType() == NurikabeType.WHITE
                    || cell.getType() == NurikabeType.NUMBER
                    || cell.getType() == NurikabeType.UNKNOWN) {
                whiteRegions.createSet(cell);
            }
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                NurikabeCell cell = board.getCell(x, y);
                NurikabeCell rightCell = board.getCell(x + 1, y);
                NurikabeCell downCell = board.getCell(x, y + 1);
                if (cell.getType() == NurikabeType.WHITE
                        || cell.getType() == NurikabeType.NUMBER
                        || cell.getType() == NurikabeType.UNKNOWN) {
                    if (rightCell != null
                            && (rightCell.getType() == NurikabeType.WHITE
                                    || rightCell.getType() == NurikabeType.NUMBER
                                    || rightCell.getType() == NurikabeType.UNKNOWN)) {
                        whiteRegions.union(cell, rightCell);
                    }
                    if (downCell != null
                            && (downCell.getType() == NurikabeType.WHITE
                                    || downCell.getType() == NurikabeType.NUMBER
                                    || downCell.getType() == NurikabeType.UNKNOWN)) {
                        whiteRegions.union(cell, downCell);
                    }
                }
            }
        }
        return whiteRegions;
    }

    /**
     * Makes a map where the keys are white/numbered cells and the values are the amount of cells
     * that need to be added to the region
     *
     * @param board nurikabe board
     * @return a map of cell keys to integer values
     */
    public static HashMap<NurikabeCell, Integer> getWhiteRegionMap(NurikabeBoard board) {
        int width = board.getWidth();
        int height = board.getHeight();

        Set<NurikabeCell> numberedCells = getNurikabeNumberedCells(board);
        // Final mapping of cell to size
        HashMap<NurikabeCell, Integer> whiteRegionMap = new HashMap<>();
        for (NurikabeCell center : numberedCells) {
            // BFS for each center to find the size of the region
            int size = 1;
            // Mark all the vertices as not visited(By default
            // set as false)
            HashMap<NurikabeCell, Boolean> visited = new HashMap<>();

            // Create a queue for BFS
            LinkedList<NurikabeCell> queue = new LinkedList<>();

            // Mark the current node as visited and enqueue it
            visited.put(center, true);
            queue.add(center);

            // Set of cells in the current region
            Set<NurikabeCell> connected = new HashSet<>();

            while (queue.size() != 0) {
                // Dequeue a vertex from queue and print it
                // s is the source node in the graph
                NurikabeCell s = queue.poll();
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace("{} ", s);
                }

                // Make a linked list of all adjacent squares
                Set<NurikabeCell> adj = new HashSet<>();

                Point loc = s.getLocation();
                // First check if the side is on the board
                if (loc.x >= 1) {
                    adj.add(board.getCell(loc.x - 1, loc.y));
                }
                if (loc.x < width - 1) {
                    adj.add(board.getCell(loc.x + 1, loc.y));
                }
                if (loc.y >= 1) {
                    adj.add(board.getCell(loc.x, loc.y - 1));
                }
                if (loc.y < height - 1) {
                    adj.add(board.getCell(loc.x, loc.y + 1));
                }
                // Get all adjacent vertices of the dequeued vertex s
                // If a adjacent has not been visited, then mark it
                // visited and enqueue it
                for (NurikabeCell n : adj) {
                    if (!visited.getOrDefault(n, false) && n.getType() == NurikabeType.WHITE) {
                        connected.add(n);
                        visited.put(n, true);
                        queue.add(n);
                        ++size;
                    }
                }
            }
            // Map the cells to the center-size (including the center)
            whiteRegionMap.put(center, center.getData() - size);
            for (NurikabeCell member : connected) {
                whiteRegionMap.put(member, center.getData() - size);
            }
        }
        return whiteRegionMap;
    }

    /**
     * Gets all the non-black cells connected to the given cell
     *
     * @param board nurikabe board
     * @param center nurikabe cell
     * @return a set of all white/numbered cells in the region
     */
    public static Set<NurikabeCell> getSurroundedRegionOf(
            NurikabeBoard board, NurikabeCell center) {
        int width = board.getWidth();
        int height = board.getHeight();

        // Mark all the vertices as not visited(By default
        // set as false)
        Set<NurikabeCell> visited = new HashSet<>();

        // Create a queue for BFS
        LinkedList<NurikabeCell> queue = new LinkedList<>();

        // Mark the current node as visited and enqueue it
        visited.add(center);
        queue.add(center);

        // Set of cells in the current region
        Set<NurikabeCell> connected = new HashSet<>();

        while (queue.size() != 0) {
            // Dequeue a vertex from queue and print it
            // s is the source node in the graph
            NurikabeCell s = queue.poll();
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("{} ", s);
            }

            // Make a set of all adjacent squares
            Set<NurikabeCell> adj = new HashSet<>();

            Point loc = s.getLocation();
            // First check if the side is on the board
            if (loc.x >= 1) {
                adj.add(board.getCell(loc.x - 1, loc.y));
            }
            if (loc.x < width - 1) {
                adj.add(board.getCell(loc.x + 1, loc.y));
            }
            if (loc.y >= 1) {
                adj.add(board.getCell(loc.x, loc.y - 1));
            }
            if (loc.y < height - 1) {
                adj.add(board.getCell(loc.x, loc.y + 1));
            }
            // Get all adjacent vertices of the dequeued vertex s
            // If a adjacent has not been visited, then mark it
            // visited and enqueue it
            for (NurikabeCell n : adj) {
                if (!visited.contains(n) && n.getType() != NurikabeType.BLACK) {
                    connected.add(n);
                    visited.add(n);
                    queue.add(n);
                }
            }
        }

        return connected;
    }
}
