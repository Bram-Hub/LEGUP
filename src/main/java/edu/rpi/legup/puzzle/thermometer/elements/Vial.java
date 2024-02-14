package edu.rpi.legup.puzzle.thermometer.elements;

import edu.rpi.legup.puzzle.thermometer.ThermometerCell;
import edu.rpi.legup.puzzle.thermometer.ThermometerBoard;
import edu.rpi.legup.puzzle.thermometer.ThermometerType;
import edu.rpi.legup.model.elements.PlaceableElement;

import java.util.ArrayList;

import static java.lang.Math.*;

public class Vial {
    private final ArrayList<ThermometerCell> cells;

    public Vial(ThermometerCell headCell, ThermometerCell tipCell, ThermometerBoard board) {
        cells = new ArrayList<>();
        fillCells(headCell, tipCell, board);

    }

    private void fillCells(ThermometerCell headCell, ThermometerCell tipCell, ThermometerBoard board) {
        //shorthand for useful variables
        int headX = (int) headCell.getLocation().getX();
        int headY = (int) headCell.getLocation().getY();
        int tipX = (int) tipCell.getLocation().getX();
        int tipY = (int) tipCell.getLocation().getY();

        //checking if the vial travels along the x or y axis
        if(headX == tipX) {
            int top = min(headY, tipY);
            int bottom = max(headY, tipY);

            //Likely is not setting the data the way we think it is
            board.getCell(headX, headY).setData(ThermometerType.HEAD.ordinal());
            cells.add(board.getCell(headX, headY));
            for (int i = top + 1; i < bottom - 1; i++){
                board.getCell(headX, i).setData(ThermometerType.SHAFT.ordinal());
                cells.add(board.getCell(headX, i));
            }
            board.getCell(tipX, tipY).setData(ThermometerType.TIP.ordinal());
            cells.add(board.getCell(tipX, tipY));

        }
        else{
            int top = min(headX, tipX);
            int bottom = max(headX, tipX);

            board.getCell(headX, headY).setData(ThermometerType.HEAD.ordinal());
            cells.add(board.getCell(headX, headY));
            for (int i = top + 1; i < bottom - 1; i++){
                board.getCell(i, headY).setData(ThermometerType.SHAFT.ordinal());
                cells.add(board.getCell(i, headY));
            }
            board.getCell(tipX, tipY).setData(ThermometerType.TIP.ordinal());
            cells.add(board.getCell(headX, headY));
        }
    }

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
