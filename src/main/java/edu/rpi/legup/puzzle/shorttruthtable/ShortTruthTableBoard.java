package edu.rpi.legup.puzzle.shorttruthtable;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.GridBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ShortTruthTableBoard extends GridBoard {

    private ArrayList<ShortTruthTableStatement> lines;

    public ShortTruthTableBoard() {

        this.lines = new ArrayList<ShortTruthTableStatement>();

    }


    public Set<ShortTruthTableCell> getCellsWithSymbol(char symbol){
        Set<ShortTruthTableCell> cells = new HashSet<ShortTruthTableCell>();
        for(ShortTruthTableStatement statement : lines)
            cells.addAll(statement.getCellsWithSymbol(symbol));
        return cells;
    }


    @Override
    public ShortTruthTableBoard copy() {
        ShortTruthTableBoard copy = new ShortTruthTableBoard();
        for(int i = 0; i<lines.size(); i++){
            copy.add(lines.get(i).copy());
        }
        return copy;
    }
}
