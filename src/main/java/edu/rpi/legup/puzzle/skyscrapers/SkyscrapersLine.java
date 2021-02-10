package edu.rpi.legup.puzzle.skyscrapers;

import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.utility.Entry;

public class SkyscrapersLine extends PuzzleElement<Entry<SkyscrapersCell, SkyscrapersCell>> {

    public SkyscrapersLine(SkyscrapersCell c1, SkyscrapersCell c2) {
        this.data = new Entry<>(c1, c2);
    }

    public SkyscrapersCell getC1() {
        return data.getKey();
    }

    public void setC1(SkyscrapersCell c1) {
        this.data.setKey(c1);
    }

    public SkyscrapersCell getC2() {
        return data.getValue();
    }

    public void setC2(SkyscrapersCell c2) {
        this.data.setValue(c2);
    }

    public boolean compare(SkyscrapersLine line) {
        return ((line.getC1().getLocation().equals(data.getKey().getLocation()) && line.getC2().getLocation().equals(data.getValue().getLocation())) ||
                (line.getC1().getLocation().equals(data.getValue().getLocation()) && line.getC2().getLocation().equals(data.getKey().getLocation())));
    }

    /**
     * Copies this elements puzzleElement to a new PuzzleElement object
     *
     * @return copied PuzzleElement object
     */
    @Override
    public SkyscrapersLine copy() {
        return new SkyscrapersLine(data.getKey().copy(), data.getValue().copy());
    }
}
