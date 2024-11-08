package edu.rpi.legup.puzzle.kakurasu;

import edu.rpi.legup.model.gameboard.GridCell;
import edu.rpi.legup.puzzle.nurikabe.NurikabeType;

import java.awt.*;

// TODO: Mirror the implementation of SkyscraperCell, in order to have clues on the edge
public class KakurasuCell extends GridCell<Integer> {

    KakurasuCell(int value, Point location) {
        super(value, location);
    }

    /**
     * Gets the type of this KakurasuCell
     *
     * @return type of KakurasuCell
     */
    public KakurasuType getType() {
        return switch (data) {
            case 0 -> KakurasuType.UNKNOWN;
            case 1 -> KakurasuType.FILLED;
            case 2 -> KakurasuType.EMPTY;
            default -> null;
        };
    }
}
