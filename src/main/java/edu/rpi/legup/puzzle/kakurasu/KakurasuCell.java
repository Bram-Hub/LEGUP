package edu.rpi.legup.puzzle.kakurasu;

import edu.rpi.legup.model.elements.Element;
import edu.rpi.legup.model.gameboard.GridCell;

import java.awt.*;
import java.awt.event.MouseEvent;

public class KakurasuCell extends GridCell<KakurasuType> {

    public KakurasuCell(KakurasuType value, Point location) {
        super(value, location);
    }

    public KakurasuType getType() {
        return data;
    }

    public int getValue() {
        switch (data) {
            case FILLED:
                return 1;
            case EMPTY:
                return 2;
            default:
                return 0;
        }
    }

    @Override
    public void setType(Element e, MouseEvent m) {
        switch (e.getElementName()) {
            case "Unknown Tile":
                this.data = KakurasuType.UNKNOWN;
                break;
            case "Filled Tile":
                this.data = KakurasuType.FILLED;
                break;
            case "Empty Tile":
                this.data = KakurasuType.EMPTY;
                break;
            default:
                System.out.println("KakurasuCell.setType: Unknown element");
                break;
        }
    }

    @Override
    public KakurasuCell copy() {
        KakurasuCell copy = new KakurasuCell(data, (Point) location.clone());
        copy.setIndex(index);
        copy.setModifiable(isModifiable);
        copy.setGiven(isGiven);
        return copy;
    }
}
