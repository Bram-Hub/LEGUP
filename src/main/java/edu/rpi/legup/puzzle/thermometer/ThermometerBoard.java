package edu.rpi.legup.puzzle.thermometer;

import edu.rpi.legup.model.gameboard.GridBoard;
import edu.rpi.legup.model.gameboard.GridCell;

import java.util.ArrayList;

public class ThermometerBoard extends GridBoard{

    //an array containing all of our vials on the board
    private ArrayList<ThermometerVial> thermometerVials;

    //representations of the number requirements along rows and columns of the board
    private ArrayList<GridCell<Integer>> colNumbers;
    private ArrayList<GridCell<Integer>> rowNumbers;

    //constructors for the boards and variables
    public ThermometerBoard(int width, int height) {
        super(width, height);

        //filling the arrays with zeros, so they can be properly updated later
        colNumbers = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            GridCell<Integer> cell = new GridCell<Integer>(0, i, height);
            cell.setIndex((height-1) * this.getHeight() + i);
            colNumbers.add(cell);
            this.setCell(i, height, cell);
        }
        rowNumbers = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            GridCell<Integer> cell = new GridCell<Integer>(0, width, i);
            cell.setIndex(i * this.getHeight() + width);
            rowNumbers.add(cell);
            this.setCell(width, i, cell);
        }

        thermometerVials = new ArrayList<>();
    }

    //setters and accessors for our array of vials
    public void addVial(ThermometerVial v) {
        thermometerVials.add(v);
    }
    public ArrayList<ThermometerVial> getVials() {
        return thermometerVials;
    }


    //our getters for row/col numbers with simple input verification
    public boolean setRowNumber(int row, int num) {
        //first check is to verify we are updating an element in range
        //second check is to verify the new number can be achieved by the puzzle
        if (row < rowNumbers.size() && num <= colNumbers.size()){
            rowNumbers.get(row).setData(num);
            return true;
        }
        return false;
    }
    public boolean setColNumber(int col, int num) {
        //first check is to verify we are updating an element in range
        //second check is to verify the new number can be achieved by the puzzle
        if (col < colNumbers.size() && num <= rowNumbers.size()){
            rowNumbers.get(col).setData(num);
            return true;
        }
        return false;
    }

    //basic accessors, probably fine as is
    public int getRowNumber(int row){
        return rowNumbers.get(row).getData();
    }
    public int getColNumber(int col){
        return colNumbers.get(col).getData();
    }

    // Accessors for saving row/column
    public ArrayList<GridCell<Integer>> getRowNumbers() { return rowNumbers; }
    public ArrayList<GridCell<Integer>> getColNumbers() { return colNumbers; }


    //we all suck at programming so instead of using provided array list
    //instead just trusting vials to store the cells for us
    @Override
    public ThermometerCell getCell(int x, int y) {
        for(ThermometerVial vial: this.thermometerVials) {
            for(ThermometerCell cell: vial.getCells()){
                if(cell.getLocation().x == x && cell.getLocation().y == y) return cell;
            }
        }
        return null;
    }
}
