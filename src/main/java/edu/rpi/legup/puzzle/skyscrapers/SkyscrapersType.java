package edu.rpi.legup.puzzle.skyscrapers;

public enum SkyscrapersType {
  UNKNOWN(0),
  Number(1),
  CLUE_NORTH(-1),
  CLUE_EAST(-2),
  CLUE_SOUTH(-3),
  CLUE_WEST(-4),
  ANY(-5);

  public int value;

  SkyscrapersType(int value) {
    this.value = value;
  }

  public int toValue() {
    return value;
  }

  public static SkyscrapersType convertToSkyType(int num) {
    switch (num) {
      case 0:
        return UNKNOWN;
      case -1:
        return CLUE_NORTH;
      case -2:
        return CLUE_EAST;
      case -3:
        return CLUE_SOUTH;
      case -4:
        return CLUE_WEST;
      case -5:
        return ANY;
      default:
        return Number;
    }
  }
}
