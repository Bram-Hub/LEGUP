package edu.rpi.legup.puzzle.thermometer;

import edu.rpi.legup.model.gameboard.GridBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.puzzle.thermometer.elements.Vial;

import java.util.ArrayList;

import static edu.rpi.legup.puzzle.thermometer.elements.Vial.verifyVial;

public class ThermometerBoard extends GridBoard{

    //an array containing all of our vials on the board
    private ArrayList<Vial> vials;

    //representations of the number requirements along rows and columns of the board
    private ArrayList<Integer> colNumbers;
    private ArrayList<Integer> rowNumbers;

    //constructors for the boards and variables
    public ThermometerBoard(int width, int height){
        super(width, height);

        //filling the arrays with zeros so they can be properly updated later
        colNumbers = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            colNumbers.add(0);
        }
        rowNumbers = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            rowNumbers.add(0);
        }

        vials = new ArrayList<>();
    }
    public ThermometerBoard(int size){
        super(size, size);

        //filling the arrays with zeros so they can be properly updated later
       colNumbers = new ArrayList<>();
       rowNumbers = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            colNumbers.add(0);
            rowNumbers.add(0);
        }

        vials = new ArrayList<>();
    }

    //setters and accessors for our array of vials
    public boolean addVial(ThermometerCell headCell, ThermometerCell tipCell) {
        if(verifyVial(headCell, tipCell, this)) {
            vials.add(new Vial(headCell, tipCell, this));
            return true;
        }
        return false;
    }
    public ArrayList<Vial> getVials() {
        return vials;
    }


    //our getters for row/col numbers with simple input verification
    public boolean setRowNumber(int row, int num) {
        //first check is to verify we are updating an element in range
        //second check is to verify the new number can be achieved by the puzzle
        if (row < rowNumbers.size() && num <= colNumbers.size()){
            rowNumbers.set(row, num);
            return true;
        }
        return false;
    }
    public boolean setColNumber(int col, int num) {
        //first check is to verify we are updating an element in range
        //second check is to verify the new number can be achieved by the puzzle
        if (col < colNumbers.size() && num <= rowNumbers.size()){
            rowNumbers.set(col, num);
            return true;
        }
        return false;
    }

    //basic accessors, probably fine as is
    public int getRowNumber(int row){
        return rowNumbers.get(row);
    }
    public int getColNumber(int col){
        return colNumbers.get(col);
    }

    // Accessors for saving row/column
    public ArrayList<Integer> getRowNumbers() { return rowNumbers; }
    public ArrayList<Integer> getColNumbers() { return colNumbers; }


    @Override
    public ThermometerCell getCell(int x, int y){
        if(x < 0 || y < 0 || x > dimension.height || y > dimension.width) return null;
        return (ThermometerCell) super.getCell(x, y);
    }
}
