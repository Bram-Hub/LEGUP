package edu.rpi.legup.puzzle.thermometer;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.ElementFactory;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.save.InvalidFileFormatException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.awt.*;

import static edu.rpi.legup.puzzle.thermometer.ThermometerVial.verifyVial;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class ThermometerVialFactory extends ElementFactory{
    //We do not import single cells at a time, instead we are
    //importing/exporting entire vials hence these functions
    //remain empty
    @Override
    public PuzzleElement importCell(Node node, Board board) throws InvalidFileFormatException {
        return null;
    }
    @Override
    public Element exportCell(Document document, PuzzleElement puzzleElement) {
        return null;
    }

    public static void importThermometerVial(Node node, ThermometerBoard board) throws InvalidFileFormatException{
        int headX = Integer.parseInt(node.getAttributes().getNamedItem("headx").getNodeValue());
        int headY = Integer.parseInt(node.getAttributes().getNamedItem("heady").getNodeValue());
        int tipX = Integer.parseInt(node.getAttributes().getNamedItem("tailx").getNodeValue());
        int tipY = Integer.parseInt(node.getAttributes().getNamedItem("taily").getNodeValue());


        if(verifyVial(headX, headY, tipX, tipY, board)) {
            board.addVial(new ThermometerVial(headX, headY, tipX, tipY, board));
        }
        throw new InvalidFileFormatException("thermometer Importer: overlapping vials");
    }

    private static boolean verifyVial(int headX, int headY, int tipX, int tipY, ThermometerBoard board) {
        //figuring out which axis the thermometer travels along
        if(headX == tipX) {
            //finding start and end of Vial
            int top = min(headY, tipY);
            int bottom = max(headY, tipY);

            //verifying that every cell along path is currently unconstructed
            for (int i = top; i < bottom; i++) {
                if(board.getCell(headX, i) != null) return false;
            }
        }
        else if (headY == tipY) {
            //finding start and end of Vial
            int top = min(headX, tipX);
            int bottom = max(headX, tipX);

            //verifying that every cell along path is currently unconstructed
            for (int i = top; i < bottom; i++) {
                if(board.getCell(headX, i) != null) return false;
            }
        }
        else{
            //thermometer does not line up along a single axis
            return false;
        }
        return true;
    }
}
