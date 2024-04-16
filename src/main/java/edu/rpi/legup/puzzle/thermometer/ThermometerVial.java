package edu.rpi.legup.puzzle.thermometer;

import java.awt.*;
import java.util.ArrayList;

public class ThermometerVial {
    private ArrayList<ThermometerCell> cells;

    public ThermometerVial(int headX, int headY, int tipX, int tipY, ThermometerBoard board) {
        //basic constructor, instantiating our members field and then
        //calling helper function to do all the heavy lifting
        cells = new ArrayList<ThermometerCell>();
        fillData(headX, headY, tipX, tipY, board);
    }

    //function called by the constructor which adds in all of the cells to the array
    //as well as updates their type on the board
    private void fillData(int headX, int headY, int tipX, int tipY, ThermometerBoard board) {
        //not totally happy with layout of code but most readable version I can think of atm
        //top left coordinate is 0,0 cells are added from head to tip always
        //because cells have already been verified by time constructor is called
        //we can guarantee that only the x or only the y coordinates wont line up
        if(headY < tipY){
            addCell(headX, headY, ThermometerType.HEAD, 0, board);
            for (int i = headY + 1; i < tipY; i++) {
                addCell(headX, i, ThermometerType.SHAFT, 0, board);
            }
            addCell(tipX, tipY, ThermometerType.TIP, 0, board);
        }
        else if (tipY < headY) {
            addCell(headX, headY, ThermometerType.HEAD, 180, board);
            for (int i = headY - 1; i > tipY; i--) {
                addCell(headX, i, ThermometerType.SHAFT, 180, board);
            }
            addCell(tipX, tipY, ThermometerType.TIP, 180, board);
        }
        else if (headX < tipX){
            addCell(headX, headY, ThermometerType.HEAD, 90, board);
            for (int i = headX + 1; i < tipX; i++) {
                addCell(i, headY, ThermometerType.SHAFT,90, board);
            }
            addCell(tipX, tipY, ThermometerType.TIP, 90, board);
        }
        else{
            addCell(headX, headY, ThermometerType.HEAD, 270, board);
            for (int i = headX - 1; i > tipX; i--) {
                addCell(i, headY, ThermometerType.SHAFT, 270, board);
            }
            addCell(tipX, tipY, ThermometerType.TIP, 270, board);
        }

    }

    //helper function for adding a single cell
    private void addCell(int x, int y, ThermometerType t, int rotation, ThermometerBoard board){
        ThermometerCell cell = new ThermometerCell(new Point(x, y), t, ThermometerFill.EMPTY, rotation);
        cell.setIndex(y * board.getHeight() + x);
        this.cells.add(cell);
        //still important for element view stuff
        board.setCell(x, y, cell);
    }


    //TODO (probably) DOES NOT WORK AS INTENDED
    // BECAUSE MOST RULES GET A PUZZLE ELEMENT PASSED IN AND WEIRD
    // TYPE CASTING STUFF, PAY ATTENTION TO THIS WHEN WE START
    // DEBUGGING RULES
    //a basic accessor to check if a cell is contained in vial
    public boolean containsCell(ThermometerCell cell){
        for (ThermometerCell c : cells) {
            if (c.getLocation() == cell.getLocation()) {
                return true;
            }
        }
        return false;
    }

    //basic accessors
    public ThermometerCell getHead(){return cells.getFirst();}
    public ThermometerCell getTail(){return cells.getLast();}
    public ArrayList<ThermometerCell> getCells(){return cells;}

    //checking for discontinuous flow inside of vial
    public boolean continuousFlow(){
        //bool which is true until it runs into an empty/blocked cell in the vial
        //if an empty cell in the vial is found while flow is set to false
        //we know there is a break in the flow
        boolean flow = true;

        for(ThermometerCell c : cells){
            if(c.getFill() != ThermometerFill.FILLED && flow) flow = false;

            if(c.getFill() == ThermometerFill.FILLED && !flow) return false;
        }
        return true;
    }

}
