package edu.rpi.legup.puzzle.shorttruthtable;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.GridBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;

import edu.rpi.legup.puzzle.shorttruthtable.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class ShortTruthTableBoard extends GridBoard {

    public ShortTruthTableBoard(int width, int height) {

        super(width, height);

    }


    public Set<ShortTruthTableCell> getCellsWithSymbol(char symbol){
        Set<ShortTruthTableCell> cells = new HashSet<ShortTruthTableCell>();
        for (int x = 0; x < this.dimension.width; x++) {
            for (int y = 0; y < this.dimension.height; y++) {
                ShortTruthTableCell cell = (ShortTruthTableCell) getCell(x, y);
                if(cell.getSymbol() == symbol)
                    cells.add(cell);
            }
        }
        return cells;
    }


    @Override
    public ShortTruthTableBoard copy() {
        System.out.println("Board.copy()");
        ShortTruthTableBoard copy = new ShortTruthTableBoard(getWidth(), getHeight());
        for (int x = 0; x < this.dimension.width; x++) {
            for (int y = 0; y < this.dimension.height; y++) {
                copy.setCell(x, y, getCell(x, y).copy());
            }
        }
        for(PuzzleElement e : modifiedData) {
            copy.getPuzzleElement(e).setModifiable(false);
        }
        System.out.println("original:\n" + this);
        System.out.println("copy:\n" + copy);
        return copy;
    }

    public static List<ShortTruthTableStatement> copyStatementList(List<ShortTruthTableStatement> statements){
        List<ShortTruthTableStatement> copy = new ArrayList<ShortTruthTableStatement>();
        for(int i = 0; i<statements.size(); i++){
            copy.add(statements.get(i).copy());
        }
        return copy;
    }


    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        for(int i = 0; i<dimension.height; i+=2){
            for(int j = 0; j<dimension.width; j++){
                ShortTruthTableCell c = (ShortTruthTableCell) getCell(j, i);
                str.append(c.getSymbol());
            }
            str.append("  ");
            for(int j = 0; j<dimension.width; j++){
                ShortTruthTableCell c = (ShortTruthTableCell) getCell(j, i);
                str.append(ShortTruthTableCellType.toChar(c.getType()));
            }
            str.append('\n');
        }
        return str.toString();
    }

}
