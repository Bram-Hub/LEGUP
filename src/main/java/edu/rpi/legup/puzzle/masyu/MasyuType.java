package edu.rpi.legup.puzzle.masyu;

public enum MasyuType {
  UNKNOWN,
  BLACK,
  WHITE,
  LINE;

  public static MasyuType convertToMasyuType(int num) {
    switch (num) {
      case 1:
        return BLACK;
      case 2:
        return WHITE;
      case 3:
        return LINE;
      default:
        return UNKNOWN;
    }
  }
}
