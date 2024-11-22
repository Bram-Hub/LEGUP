package edu.rpi.legup.puzzle.yinyang;

import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.utility.DisjointSets;

import java.util.HashSet;
import java.util.Set;

public class YinYangUtilities {

    /**
     * Validates that there are no 2x2 blocks of the same type in the board.
     *
     * @param board the YinYang board to validate
     * @return true if no invalid 2x2 blocks exist, false otherwise
     */
    public static boolean validateNo2x2Blocks(YinYangBoard board) {
        for (int x = 0; x < board.getWidth() - 1; x++) {
            for (int y = 0; y < board.getHeight() - 1; y++) {
                YinYangCell c1 = board.getCell(x, y);
                YinYangCell c2 = board.getCell(x + 1, y);
                YinYangCell c3 = board.getCell(x, y + 1);
                YinYangCell c4 = board.getCell(x + 1, y + 1);

                if (c1.getType() == c2.getType() &&
                        c2.getType() == c3.getType() &&
                        c3.getType() == c4.getType() &&
                        c1.getType() != YinYangType.UNKNOWN) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Validates that all WHITE and BLACK groups are connected.
     *
     * @param board the YinYang board to validate
     * @return true if all groups are connected, false otherwise
     */
    public static boolean validateConnectivity(YinYangBoard board) {
        Set<YinYangCell> whiteCells = new HashSet<>();
        Set<YinYangCell> blackCells = new HashSet<>();

        for (PuzzleElement element : board.getPuzzleElements()) {
            YinYangCell cell = (YinYangCell) element;
            if (cell.getType() == YinYangType.WHITE) {
                whiteCells.add(cell);
            } else if (cell.getType() == YinYangType.BLACK) {
                blackCells.add(cell);
            }
        }

        return isConnected(whiteCells, board) && isConnected(blackCells, board);
    }

    /**
     * Checks if a given set of cells forms a single connected component.
     *
     * @param cells the set of cells to check
     * @param board the board to check connectivity on
     * @return true if the set is connected, false otherwise
     */
    private static boolean isConnected(Set<YinYangCell> cells, YinYangBoard board) {
        if (cells.isEmpty()) return true;

        Set<YinYangCell> visited = new HashSet<>();
        dfs(cells.iterator().next(), cells, visited, board);

        return visited.size() == cells.size();
    }

    /**
     * Depth-first search to explore connected cells.
     */
    private static void dfs(YinYangCell cell, Set<YinYangCell> cells, Set<YinYangCell> visited, YinYangBoard board) {
        if (!cells.contains(cell) || visited.contains(cell)) return;

        visited.add(cell);

        int x = cell.getX();
        int y = cell.getY();

        YinYangCell[] neighbors = {
                board.getCell(x - 1, y),
                board.getCell(x + 1, y),
                board.getCell(x, y - 1),
                board.getCell(x, y + 1),
        };

        for (YinYangCell neighbor : neighbors) {
            if (neighbor != null) {
                dfs(neighbor, cells, visited, board);
            }
        }
    }
}