package edu.rpi.legup.puzzle.kakurasu;

public enum KakurasuType {
    UNKNOWN(0), FILLED(1), EMPTY(2);

    int value;
    private KakurasuType(int value) {}
    int toValue() {return value;}
}
