package edu.rpi.legup.puzzle.sudoku.elements;

import edu.rpi.legup.model.elements.PlaceableElement;

public class NumberTile extends PlaceableElement {
  private int object_num;

  public NumberTile() {
    super("SUDO-PLAC-0001", "Number Tile", "A numbered tile", null);
    object_num = 0;
  }

  /**
   * @return this object's tile number...
   */
  public int getTileNumber() {
    return object_num;
  }

  /**
   * @param num Amount to set tile object to.
   */
  public void setTileNumber(int num) {
    object_num = num;
  }
}
