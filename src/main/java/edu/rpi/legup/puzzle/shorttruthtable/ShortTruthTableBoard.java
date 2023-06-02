package edu.rpi.legup.puzzle.shorttruthtable;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.GridBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;

import edu.rpi.legup.puzzle.lightup.LightUpCell;
import edu.rpi.legup.puzzle.shorttruthtable.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class ShortTruthTableBoard extends GridBoard {

    private ShortTruthTableStatement[] statements;

    public ShortTruthTableBoard(int width, int height, ShortTruthTableStatement[] statements) {

        super(width, height);

        this.statements = statements;

    }


    public Set<ShortTruthTableCell> getCellsWithSymbol(char symbol) {
        Set<ShortTruthTableCell> cells = new HashSet<ShortTruthTableCell>();
        for (int x = 0; x < this.dimension.width; x++) {
            for (int y = 0; y < this.dimension.height; y++) {
                ShortTruthTableCell cell = (ShortTruthTableCell) getCell(x, y);
                if (cell.getSymbol() == symbol) {
                    cells.add(cell);
                }
            }
        }
        return cells;
    }

    public ShortTruthTableCell getCellFromElement(PuzzleElement element) {
        return (ShortTruthTableCell) getPuzzleElement(element);
    }

    @Override
    public ShortTruthTableCell getCell(int x, int y) {
        return (ShortTruthTableCell) super.getCell(x, y);
    }


    @Override
    public ShortTruthTableBoard copy() {

        //Copy the statements
        ShortTruthTableStatement[] statementsCopy = new ShortTruthTableStatement[this.statements.length];
        for (int i = 0; i < statements.length; i++) {
            statementsCopy[i] = this.statements[i].copy();
        }
        //copy the board and set the cells
        ShortTruthTableBoard boardCopy = new ShortTruthTableBoard(getWidth(), getHeight(), statementsCopy);
        for (int r = 0; r < this.dimension.height; r++) {
            for (int c = 0; c < this.dimension.width; c++) {
                if (r % 2 == 0 && c < statementsCopy[r / 2].getLength()) {
                    boardCopy.setCell(c, r, statementsCopy[r / 2].getCell(c));
                }
                else {
                    boardCopy.setCell(c, r, getCell(c, r).copy());
                }
            }
        }
        for (PuzzleElement e : modifiedData) {
            boardCopy.getPuzzleElement(e).setModifiable(false);
        }
        System.out.println("Board.copy()");
        System.out.println("original:\n" + this);
        System.out.println("copy:\n" + boardCopy);
        return boardCopy;

    }

    public ShortTruthTableStatement[] getStatements() {
        return statements;
    }

    public static List<ShortTruthTableStatement> copyStatementList(List<ShortTruthTableStatement> statements) {
        List<ShortTruthTableStatement> copy = new ArrayList<ShortTruthTableStatement>();
        for (int i = 0; i < statements.size(); i++) {
            copy.add(statements.get(i).copy());
        }
        return copy;
    }


//    @Override
//    public void setPuzzleElement(int index, PuzzleElement element){
//        ShortTruthTableCell cellElement = (ShortTruthTableCell) element;
//        ShortTruthTableCell cell = getCell(cellElement.getX(), cellElement.getY());
//        cell.setType(cellElement.getType());
//    }


    @Override
    public String toString() {
        System.out.println("ShortTruthTableBoard toString() called");
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < dimension.height; i += 2) {
            for (int j = 0; j < dimension.width; j++) {
                ShortTruthTableCell c = (ShortTruthTableCell) getCell(j, i);
                str.append(c.getSymbol());
            }
            str.append("  ");
            for (int j = 0; j < dimension.width; j++) {
                ShortTruthTableCell c = (ShortTruthTableCell) getCell(j, i);
                str.append(ShortTruthTableCellType.toChar(c.getType()));
            }
            str.append('\n');
        }
        return str.toString();
    }

}
