package edu.rpi.legup.puzzle.thermometer;

import edu.rpi.legup.model.gameboard.GridBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.puzzle.thermometer.elements.Vial;

import java.util.ArrayList;

import static edu.rpi.legup.puzzle.thermometer.elements.Vial.verifyVial;

public class ThermometerBoard extends GridBoard{

    private ArrayList<Vial> vials;

    public ThermometerBoard(int width, int height){
        super(width, height);
        vials = new ArrayList<>();
    }

    public ThermometerBoard(int size){
        super(size, size);
        vials = new ArrayList<>();
    }

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

    @Override
    public ThermometerCell getCell(int x, int y){
        return (ThermometerCell) super.getCell(x, y);
    }
}
