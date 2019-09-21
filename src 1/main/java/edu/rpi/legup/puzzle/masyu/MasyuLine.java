package edu.rpi.legup.puzzle.masyu;

import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.utility.Entry;

public class MasyuLine extends PuzzleElement<Entry<MasyuCell, MasyuCell>> {
    public MasyuLine(MasyuCell c1, MasyuCell c2) {
        this.data = new Entry<>(c1, c2);
    }

    public MasyuCell getC1() {
        return data.getKey();
    }

    public void setC1(MasyuCell c1) {
        this.data.setKey(c1);
    }

    public MasyuCell getC2() {
        return data.getValue();
    }

    public void setC2(MasyuCell c2) {
        this.data.setValue(c2);
    }

    public boolean compare(MasyuLine line) {
        return ((line.getC1().getLocation().equals(data.getKey().getLocation()) && line.getC2().getLocation().equals(data.getValue().getLocation())) ||
                (line.getC1().getLocation().equals(data.getValue().getLocation()) && line.getC2().getLocation().equals(data.getKey().getLocation())));
    }

    /**
     * Copies this elements puzzleElement to a new PuzzleElement object
     *
     * @return copied PuzzleElement object
     */
    @Override
    public MasyuLine copy() {
        return new MasyuLine(data.getKey().copy(), data.getValue().copy());
    }
}
