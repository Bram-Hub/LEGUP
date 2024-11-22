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