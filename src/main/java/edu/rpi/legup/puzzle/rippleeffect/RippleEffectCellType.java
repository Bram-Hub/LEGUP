package edu.rpi.legup.puzzle.rippleeffect;

public enum RippleEffectCellType {
    WHITE(1), BLUE(2), RED(3), YELLOW(4), GREEN(5);

    public int value;

    RippleEffectCellType(int value) {
        this.value = value;
    }
}