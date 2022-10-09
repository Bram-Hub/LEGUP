package edu.rpi.legup.puzzle.nurikabe;

import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.utility.DisjointSets;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class NurikabeUtilities {

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

                if (cell.getType() == NurikabeType.NUMBER ||
                        cell.getType() == NurikabeType.WHITE) {
                    if (rightCell != null && (rightCell.getType() == NurikabeType.NUMBER ||
                            rightCell.getType() == NurikabeType.WHITE)) {
                        regions.union(cell, rightCell);
                    }
                    if (downCell != null && (downCell.getType() == NurikabeType.NUMBER ||
                            downCell.getType() == NurikabeType.WHITE)) {
                        regions.union(cell, downCell);
                    }
                } else if (cell.getType() == NurikabeType.BLACK) {
                    if (rightCell != null && rightCell.getType() == NurikabeType.BLACK) {
                        regions.union(cell, rightCell);
                    }
                    if (downCell != null && downCell.getType() == NurikabeType.BLACK) {
                        regions.union(cell, downCell);
                    }
                } else if (cell.getType() == NurikabeType.UNKNOWN) {
                    if (rightCell != null && rightCell.getType() == NurikabeType.UNKNOWN) {
                        regions.union(cell, rightCell);
                    }
                    if (downCell != null && downCell.getType() == NurikabeType.UNKNOWN) {
                        regions.union(cell, downCell);
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
                if (cell.getType() == NurikabeType.BLACK || cell.getType() == NurikabeType.UNKNOWN) {
                    if (rightCell != null && (rightCell.getType() == NurikabeType.BLACK ||
                            rightCell.getType() == NurikabeType.UNKNOWN)) {
                        blackRegions.union(cell, rightCell);
                    }
                    if (downCell != null && (downCell.getType() == NurikabeType.BLACK ||
                            downCell.getType() == NurikabeType.UNKNOWN)) {
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
            if (cell.getType() == NurikabeType.WHITE || cell.getType() == NurikabeType.NUMBER || cell.getType() == NurikabeType.UNKNOWN) {
                whiteRegions.createSet(cell);
            }
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                NurikabeCell cell = board.getCell(x, y);
                NurikabeCell rightCell = board.getCell(x + 1, y);
                NurikabeCell downCell = board.getCell(x, y + 1);
                if (cell.getType() == NurikabeType.WHITE || cell.getType() == NurikabeType.NUMBER ||
                        cell.getType() == NurikabeType.UNKNOWN) {
                    if (rightCell != null && (rightCell.getType() == NurikabeType.WHITE ||
                            rightCell.getType() == NurikabeType.NUMBER || rightCell.getType() == NurikabeType.UNKNOWN)) {
                        whiteRegions.union(cell, rightCell);
                    }
                    if (downCell != null && (downCell.getType() == NurikabeType.WHITE ||
                            downCell.getType() == NurikabeType.NUMBER || downCell.getType() == NurikabeType.UNKNOWN)) {
                        whiteRegions.union(cell, downCell);
                    }
                }
            }
        }
        return whiteRegions;
    }

    /**
     * Gets a list of flood filled white regions with remaining white cells
     *
     * @param board nurikabe board
     * @return a list of flood filled white regions
     */
    public static ArrayList<Set<NurikabeCell>> getFloodFillWhite(NurikabeBoard board) {
        int width = board.getWidth();
        int height = board.getHeight();

        DisjointSets<NurikabeCell> whiteRegions = new DisjointSets<>();
        for (PuzzleElement data : board.getPuzzleElements()) {
            NurikabeCell cell = (NurikabeCell) data;
            whiteRegions.createSet(cell);
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                NurikabeCell cell = board.getCell(x, y);
                NurikabeCell rightCell = board.getCell(x + 1, y);
                NurikabeCell downCell = board.getCell(x, y + 1);
                if (cell.getType() == NurikabeType.WHITE || cell.getType() == NurikabeType.NUMBER) {
                    if (rightCell != null && (rightCell.getType() == NurikabeType.WHITE ||
                            rightCell.getType() == NurikabeType.NUMBER)) {
                        whiteRegions.union(cell, rightCell);
                    }
                    if (downCell != null && (downCell.getType() == NurikabeType.WHITE ||
                            downCell.getType() == NurikabeType.NUMBER)) {
                        whiteRegions.union(cell, downCell);
                    }
                }
            }
        }

        Set<NurikabeCell> numberedCells = getNurikabeNumberedCells(board);
        ArrayList<Set<NurikabeCell>> floodFilledRegions = new ArrayList<>();
        for (NurikabeCell numberCell : numberedCells) {
            int number = numberCell.getData();
            Set<NurikabeCell> region = whiteRegions.getSet(numberCell);
            floodFilledRegions.add(region);

            int flood = number - region.size();
            for (int i = 0; i < flood; i++) {
                Set<NurikabeCell> newSet = new HashSet<>();
                for (NurikabeCell c : region) {
                    Point loc = c.getLocation();
                    NurikabeCell upCell = board.getCell(loc.x, loc.y - 1);
                    NurikabeCell rightCell = board.getCell(loc.x + 1, loc.y);
                    NurikabeCell downCell = board.getCell(loc.x, loc.y + 1);
                    NurikabeCell leftCell = board.getCell(loc.x - 1, loc.y);
                    if (upCell != null) {
                        newSet.add(upCell);
                    }
                    if (rightCell != null) {
                        newSet.add(rightCell);
                    }
                    if (downCell != null) {
                        newSet.add(downCell);
                    }
                    if (leftCell != null) {
                        newSet.add(leftCell);
                    }
                }
                region.addAll(newSet);
            }
        }

        return floodFilledRegions;
    }
}
