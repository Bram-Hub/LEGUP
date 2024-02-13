package edu.rpi.legup.puzzle.treetent;

public enum TreeTentType {
  UNKNOWN,
  TREE,
  GRASS,
  TENT,
  CLUE_NORTH,
  CLUE_EAST,
  CLUE_SOUTH,
  CLUE_WEST;

  public static TreeTentType valueOf(int num) {
    switch (num) {
      case 1:
        return TREE;
      case 2:
        return GRASS;
      case 3:
        return TENT;
      default:
        return UNKNOWN;
    }
  }
}
