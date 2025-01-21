package edu.rpi.legup.puzzle.kakurasu;

import edu.rpi.legup.model.gameboard.PuzzleElement;

public class KakurasuClue extends PuzzleElement<Integer> {
    private KakurasuType type;
    private int clueIndex;

    public KakurasuClue(int value, int clueIndex, KakurasuType type) {
        super(value);
        this.index = -2;
        this.clueIndex = clueIndex;
        this.type = type;
    }

    public int getClueIndex() {
        return clueIndex;
    }

    public void setClueIndex(int clueIndex) {
        this.clueIndex = clueIndex;
    }

    public KakurasuType getType() {
        return type;
    }

    public void setType(KakurasuType type) {
        this.type = type;
    }

    public KakurasuClue copy() {
        return new KakurasuClue(data, clueIndex, type);
    }
}
