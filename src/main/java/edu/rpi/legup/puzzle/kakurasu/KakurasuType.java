package edu.rpi.legup.puzzle.kakurasu;

public enum KakurasuType {
    UNKNOWN(0), FILLED(1), EMPTY(2),
    HORIZONTAL_CLUE(-1), VERTICAL_CLUE(-2);

    public int value;
    KakurasuType(int value) {this.value = value;}
    int toValue() {return value;}
    public static KakurasuType convertToKakuType(int num) {
        return switch (num) {
            case 1 -> KakurasuType.FILLED;
            case 2 -> KakurasuType.EMPTY;
            case -1 -> KakurasuType.HORIZONTAL_CLUE;
            case -2 -> KakurasuType.VERTICAL_CLUE;
            default -> KakurasuType.UNKNOWN;
        };
    }
}
