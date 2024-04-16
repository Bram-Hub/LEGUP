package edu.rpi.legup.puzzle.thermometer;

import edu.rpi.legup.model.gameboard.GridBoard;

import java.awt.*;
import java.util.ArrayList;

public class ThermometerBoard extends GridBoard{

    //an array containing all of our vials on the board
    private ArrayList<ThermometerVial> thermometerVials;

    //representations of the number requirements along rows and columns of the board
    //we use rotation to store the number
    private ArrayList<ThermometerCell> colNumbers;
    private ArrayList<ThermometerCell> rowNumbers;

    private ThermometerCell dummyCell;

    //constructors for the boards and variables
    public ThermometerBoard(int width, int height) {
        super(width, height);

        //initializing the row/col number arrays with zeros, so they can be
        //easily updated using the setRow/ColNumber functions
        colNumbers = new ArrayList<>();
        for (int i = 0; i < width-1; i++) {
            ThermometerCell cell = new ThermometerCell(new Point(i, height-1),
                    ThermometerType.UNKNOWN, ThermometerFill.UNKNOWN, 0);
            cell.setIndex((height-1) * height + i);
            colNumbers.add(cell);
            this.setCell(i, height-1, cell);
        }
        rowNumbers = new ArrayList<>();
        for (int i = 0; i < height-1; i++) {
            ThermometerCell cell = new ThermometerCell(new Point(width-1, i),
                    ThermometerType.UNKNOWN, ThermometerFill.UNKNOWN, 0);
            cell.setIndex(i * height + (width-1));
            rowNumbers.add(cell);
            this.setCell(width-1, i, cell);
        }

        //setting a dummy cell so board doesn't have null cells
        dummyCell = new ThermometerCell(new Point(width-1, height-1),
                ThermometerType.UNKNOWN, ThermometerFill.UNKNOWN, -1);
        dummyCell.setIndex((height-1) * height + width);
        this.setCell(width-1, height-1, dummyCell);

        //creating our empty vial of thermometers to add to
        thermometerVials = new ArrayList<>();
    }

    //setters and accessors for our array of vials
    public void addVial(ThermometerVial v) {
        thermometerVials.add(v);
    }
    public ArrayList<ThermometerVial> getVials() {
        return thermometerVials;
    }


    //our setters for row/col numbers with simple input verification
    public boolean setRowNumber(int row, int num) {
        //first check is to verify we are updating an element in range
        //second check is to verify the new number can be achieved by the puzzle
        if (row < rowNumbers.size() && num <= colNumbers.size()){
            rowNumbers.get(row).setRotation(num);
            return true;
        }
        return false;
    }
    public boolean setColNumber(int col, int num) {
        //first check is to verify we are updating an element in range
        //second check is to verify the new number can be achieved by the puzzle
        if (col < colNumbers.size() && num <= rowNumbers.size()){
            colNumbers.get(col).setRotation(num);
            return true;
        }
        return false;
    }

    //basic accessors for row/col numbers
    public int getRowNumber(int row){
        if (row < 0 || row >= rowNumbers.size()) return -1;
        return rowNumbers.get(row).getRotation();
    }
    public int getColNumber(int col){
        if (col < 0 || col >= rowNumbers.size()) return -1;
        return colNumbers.get(col).getRotation();
    }

    // Accessors for saving row/column
    public ArrayList<ThermometerCell> getRowNumbers() { return rowNumbers; }
    public ArrayList<ThermometerCell> getColNumbers() { return colNumbers; }


    //we all suck at programming so instead of using provided array list
    //we use our own array lists to keep track of the vials
    //marginally useful because it means we are guaranteed to get a
    //thermometer cell when calling get cell, but using some type casting
    //this override function could very likely be refactored out
    @Override
    public ThermometerCell getCell(int x, int y) {
        for(ThermometerVial vial: this.thermometerVials) {
            for(ThermometerCell cell: vial.getCells()){
                if(cell.getLocation().x == x && cell.getLocation().y == y) return cell;
            }
        }

        for (ThermometerCell cell : rowNumbers) {
            if(cell.getLocation().x == x && cell.getLocation().y == y) return cell;
        }

        for (ThermometerCell cell : colNumbers) {
            if(cell.getLocation().x == x && cell.getLocation().y == y) return cell;
        }

        if(x == this.getWidth() - 1 && y == this.getHeight() - 1) return dummyCell;

        return null;
    }
}
