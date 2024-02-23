package edu.rpi.legup.puzzle.rippleeffect;

public enum RippleEffectCellType {
    EMPTY(0), FILLED(1);
    public int value;
    
    RippleEffectCellType(int value) {
        this.value = value;
    }
}
