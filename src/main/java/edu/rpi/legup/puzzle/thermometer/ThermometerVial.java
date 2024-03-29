package edu.rpi.legup.puzzle.thermometer;

import java.awt.*;
import java.util.ArrayList;

import static java.lang.Math.*;

public class ThermometerVial {
    private ArrayList<ThermometerCell> cells;

    public ThermometerVial(int headX, int headY, int tipX, int tipY, ThermometerBoard board) {
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
        cell.setModifiable(true);
        this.cells.add(board.getCell(x, y));
    }


    //TODO DOES NOT WORK AS INTENDED
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

    public ThermometerCell getHead(){return cells.get(0);}
    public ThermometerCell getTail(){return cells.get(cells.size());}
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


    //used before calling the constructor to make sure the vial we are attempting to add is valid
    public static boolean verifyVial(ThermometerCell headCell, ThermometerCell tipCell, ThermometerBoard board) {
        //shorthand for useful variables
        int headX = (int) headCell.getLocation().getX();
        int headY = (int) headCell.getLocation().getY();
        int tipX = (int) tipCell.getLocation().getX();
        int tipY = (int) tipCell.getLocation().getY();


        //figuring out which axis the thermometer travels along
        if(headX == tipX) {
            //finding start and end of Vial
            int top = min(headY, tipY);
            int bottom = max(headY, tipY);

            //verifying that every cell along path is currently unclaimed
            for (int i = top; i < bottom; i++) {
                if(board.getCell(headX, i) != null || board.getCell(headX, i).getType() != ThermometerType.UNKNOWN) return false;
            }
        }
        else if (headY == tipY) {
            //finding start and end of Vial
            int top = min(headX, tipX);
            int bottom = max(headX, tipX);

            //verifying that every cell along path is currently unclaimed
            for (int i = top; i < bottom; i++) {
                if(board.getCell(i, headY).getType() != ThermometerType.UNKNOWN) return false;
            }
        }
        else{
            //thermometer does not line up along a single axis
            return false;
        }
        return true;
    }
}
