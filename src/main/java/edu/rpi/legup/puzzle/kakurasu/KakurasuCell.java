package edu.rpi.legup.puzzle.kakurasu;

import edu.rpi.legup.model.elements.Element;
import edu.rpi.legup.model.gameboard.GridCell;
import edu.rpi.legup.puzzle.nurikabe.NurikabeType;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersCell;

import java.awt.*;
import java.awt.event.MouseEvent;

// TODO: Mirror the implementation of SkyscraperCell, in order to have clues on the edge
public class KakurasuCell extends GridCell<Integer> {

    public KakurasuCell(int value, Point location) {
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

    /**
     * For use in constructing Kakurasu puzzles in the puzzle editor
     *
     * @param e The element that is being placed at the location
     * @param m The location being edited to include e element
     */
    @Override
    public void setType(Element e, MouseEvent m) {
        // TODO: Complete this function
        switch (e.getElementID()) {
            case "KAKU-ELEM-0001":

                break;
            case "KAKU-ELEM-0002":

                break;
            case "KAKU-ELEM-0003":
                this.data = 0;
                break;
            case "KAKU-ELEM-0004":
                this.data = 1;
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
