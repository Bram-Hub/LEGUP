package edu.rpi.legup.puzzle.kakurasu;

import edu.rpi.legup.model.gameboard.GridCell;
import java.awt.*;

public class KakurasuCell extends GridCell<Integer> {
    KakurasuCell(int value, Point location) {
        super(value, location);
    }
}
