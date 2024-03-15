package edu.rpi.legup.puzzle.thermometer.elements;

import edu.rpi.legup.puzzle.thermometer.ThermometerCell;
import edu.rpi.legup.puzzle.thermometer.ThermometerBoard;
import edu.rpi.legup.puzzle.thermometer.ThermometerFill;
import edu.rpi.legup.puzzle.thermometer.ThermometerType;
import edu.rpi.legup.model.elements.PlaceableElement;

import java.awt.*;
import java.util.ArrayList;

import static java.lang.Math.*;

public class Vial {
    private ArrayList<ThermometerCell> cells;

    public Vial(ThermometerCell headCell, ThermometerCell tipCell, ThermometerBoard board) {
        cells = new ArrayList<>();
        fillData(headCell, tipCell, board);
    }

    //function called by the constructor which adds in all of the cells to the array
    //as well as updates their type on the board
    private void fillData(ThermometerCell headCell, ThermometerCell tipCell, ThermometerBoard board) {
        //shorthand for useful variables
        int headX = (int) headCell.getLocation().getX();
        int headY = (int) headCell.getLocation().getY();
        int tipX = (int) tipCell.getLocation().getX();
        int tipY = (int) tipCell.getLocation().getY();

        //not totally happy with layout of code but most readable version I can think of atm
        //top left coordinate is 0,0 cells are added from head to tip always
        //because cells have already been verified by time constructor is called
        //we can guarantee that only the x or only the y coordinates wont line up
        if(headY < tipY){
            addCell(headX, headY, ThermometerType.HEAD, board);
            for (int i = headY + 1; i < tipY - 1; i++) {
                addCell(headX, i, ThermometerType.SHAFT, board);
            }
            addCell(tipX, tipY, ThermometerType.TIP, board);
        }
        else if (tipY < headY) {
            addCell(headX, headY, ThermometerType.HEAD, board);
            for (int i = headY - 1; i > tipY; i--) {
                addCell(headX, i, ThermometerType.SHAFT, board);
            }
            addCell(tipX, tipY, ThermometerType.TIP, board);
        }
        else if (headX < tipX){
            addCell(headX, headY, ThermometerType.HEAD, board);
            for (int i = headX + 1; i < tipX - 1; i++) {
                addCell(i, headY, ThermometerType.SHAFT, board);
            }
            addCell(tipX, tipY, ThermometerType.TIP, board);
        }
        else{
            addCell(headX, headY, ThermometerType.HEAD, board);
            for (int i = headX - 1; i > tipX; i--) {
                addCell(i, headY, ThermometerType.SHAFT, board);
            }
            addCell(tipX, tipY, ThermometerType.TIP, board);
        }

    }

    //helper function for adding a single cell
    private void addCell(int x, int y, ThermometerType t, ThermometerBoard board){

        //this very likely is not updating the data in the way we want it to
        board.getCell(x, y).setData(t.ordinal());
        cells.add(board.getCell(x, y));
    }

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
                if(board.getCell(headX, i).getType() != ThermometerType.UNKNOWN) return false;
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
