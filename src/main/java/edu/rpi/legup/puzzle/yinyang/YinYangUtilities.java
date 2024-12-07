package edu.rpi.legup.puzzle.yinyang;

import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.utility.DisjointSets;
import java.util.HashMap;
import java.util.Map;
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
        Set<YinYangCell> whiteCells = getCellsByType(board, YinYangType.WHITE);
        Set<YinYangCell> blackCells = getCellsByType(board, YinYangType.BLACK);

        return isConnected(whiteCells, board) && isConnected(blackCells, board);
    }

    /**
<<<<<<< HEAD
     * Extracts all cells of a specific type from the board.
     *
     * @param board The board to extract cells from
     * @param type The type of cells to extract
     * @return A set of cells of the specified type
     */
    private static Set<YinYangCell> getCellsByType(YinYangBoard board, YinYangType type) {
        Set<YinYangCell> cells = new HashSet<>();
        for (PuzzleElement element : board.getPuzzleElements()) {
            YinYangCell cell = (YinYangCell) element;
            if (cell.getType() == type) {
                cells.add(cell);
            }
        }
        return cells;
    }

    /**
     * Checks if a given set of cells forms a single connected group.
=======
     * Checks if a given set of cells forms a single connected component.
>>>>>>> parent of 1a8c8182 (Introducing Yin Yang Rules)
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
<<<<<<< HEAD
     *
     * @param cell The current cell
     * @param cells The set of target cells
     * @param visited The set of already visited cells
     * @param board The board to traverse
=======
>>>>>>> parent of 1a8c8182 (Introducing Yin Yang Rules)
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
<<<<<<< HEAD

    /**
     * Utility function to print the current board state for debugging.
     *
     * @param board The board to print
     */
    public static void debugPrintBoard(YinYangBoard board) {
        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                YinYangCell cell = board.getCell(x, y);
                System.out.print(cell.getType().toString().charAt(0) + " ");
            }
            System.out.println();
        }
    }
}
=======
    public static Map<YinYangCell, Set<YinYangCell>> getRegions(YinYangBoard board) {
        // Placeholder for region calculation logic
        return new HashMap<>();
    }
}
>>>>>>> parent of 1a8c8182 (Introducing Yin Yang Rules)
