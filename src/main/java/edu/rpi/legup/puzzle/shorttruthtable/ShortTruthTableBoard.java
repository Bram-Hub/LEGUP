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

    private List<ShortTruthTableStatement> statements;

    public ShortTruthTableBoard(int width, int height, List<ShortTruthTableStatement> statements) {

        super(width, height);

        this.statements = copyStatementList(statements);

    }


    public Set<ShortTruthTableCell> getCellsWithSymbol(char symbol){
        Set<ShortTruthTableCell> cells = new HashSet<ShortTruthTableCell>();
        for(ShortTruthTableStatement statement : statements)
            cells.addAll(statement.getCellsWithSymbol(symbol));
        return cells;
    }


    @Override
    public ShortTruthTableBoard copy() {
        ShortTruthTableBoard copy = new ShortTruthTableBoard(getWidth(), getHeight(), statements);
        for (int x = 0; x < this.dimension.width; x++) {
            for (int y = 0; y < this.dimension.height; y++) {
                copy.setCell(x, y, getCell(x, y).copy());
            }
        }
        for(PuzzleElement e : modifiedData) {
            copy.getPuzzleElement(e).setModifiable(false);
        }
        return copy;
    }

    public static List<ShortTruthTableStatement> copyStatementList(List<ShortTruthTableStatement> statements){
        List<ShortTruthTableStatement> copy = new ArrayList<ShortTruthTableStatement>();
        for(int i = 0; i<statements.size(); i++){
            copy.add(statements.get(i).copy());
        }
        return copy;
    }

}
