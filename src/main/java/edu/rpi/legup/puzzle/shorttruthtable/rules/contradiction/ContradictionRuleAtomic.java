package edu.rpi.legup.puzzle.shorttruthtable.rules.contradiction;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import java.util.Iterator;
import java.util.Set;

public class ContradictionRuleAtomic extends ContradictionRule {

  public ContradictionRuleAtomic() {
    super(
        "STTT-CONT-0002",
        "Contradicting Variable",
        "A single variable can not be both True and False",
        "edu/rpi/legup/images/shorttruthtable/ruleimages/contradiction/Atomic.png");
  }

  @Override
  public String checkContradictionAt(Board puzzleBoard, PuzzleElement puzzleElement) {

    // cast the board toa shortTruthTableBoard
    ShortTruthTableBoard board = (ShortTruthTableBoard) puzzleBoard;

    // get the cell that contradicts another cell in the board
    ShortTruthTableCell cell = (ShortTruthTableCell) board.getPuzzleElement(puzzleElement);

    if (!cell.isVariable()) {
      System.out.println("  Not Var");
      return "Can not check for contradiction on a non-variable element";
    }

    ShortTruthTableCellType cellType = cell.getType();
    if (!cellType.isTrueOrFalse()) {
      return "Can only check for a contradiction against a cell that is assigned a value of"
          + " True or False";
    }

    // get all the cells with the same value
    Set<ShortTruthTableCell> varCells = board.getCellsWithSymbol(cell.getSymbol());

    // check if there are any contradictions
    Iterator<ShortTruthTableCell> itr = varCells.iterator();
    while (itr.hasNext()) {
      ShortTruthTableCell checkCell = itr.next();
      ShortTruthTableCellType checkCellType = checkCell.getType();
      // if there is an assigned contradiction, return null
      if (checkCellType.isTrueOrFalse() && checkCellType != cellType) {
        return null;
      }
    }

    // if it made it through the while loop, thene there is no contradiction
    return "There is no contradiction for the variable " + cell.getSymbol();
  }
}
