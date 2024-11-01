package edu.rpi.legup.puzzle.kakurasu;

import edu.rpi.legup.model.gameboard.GridCell;
import edu.rpi.legup.puzzle.nurikabe.NurikabeType;

import java.awt.*;

public class KakurasuCell extends GridCell<KakurasuType> {

    KakurasuCell(KakurasuType value, Point location) {
        super(value, location);
    }

    /**
     * Gets the type of this KakurasuCell
     *
     * @return type of KakurasuCell
     */
    public KakurasuType getType() {
        return data;
    }
}
