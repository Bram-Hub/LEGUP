package edu.rpi.legup.puzzle.thermometer;

import java.util.ArrayList;

import edu.rpi.legup.model.PuzzleExporter;
import org.w3c.dom.Document;
import edu.rpi.legup.puzzle.thermometer.ThermometerVial;

public class ThermometerExporter extends PuzzleExporter {

    public ThermometerExporter(Thermometer thermometer) {
        super(thermometer);
    }

    @Override
    protected org.w3c.dom.Element createBoardElement(Document newDocument) {
        ThermometerBoard board = (ThermometerBoard) puzzle.getTree().getRootNode().getBoard();

        // Creating the XML section for the board
        org.w3c.dom.Element boardElement = newDocument.createElement("board");
        boardElement.setAttribute("width", String.valueOf(board.getWidth() - 1));
        boardElement.setAttribute("height", String.valueOf(board.getHeight() - 1));

        // Creating the XML section for the vials and appending to the board
        org.w3c.dom.Element vialsElement = newDocument.createElement("vials");
        ArrayList<ThermometerVial> vials = board.getVials();
        for (ThermometerVial vial : vials) {
            org.w3c.dom.Element vialElement = newDocument.createElement("vial");
            // Temp for now, waiting on final implementation of vial
            vialElement.setAttribute("headx", String.valueOf((int) vial.getHead().getLocation().getX()));
            vialElement.setAttribute("heady", String.valueOf((int) vial.getHead().getLocation().getY()));
            vialElement.setAttribute("tailx", String.valueOf((int) vial.getTail().getLocation().getX()));
            vialElement.setAttribute("taily", String.valueOf((int) vial.getTail().getLocation().getY()));
            vialsElement.appendChild(vialElement);
        }
        boardElement.appendChild(vialsElement);

        // Creating the XML section for the row numbers and appending to the board
        org.w3c.dom.Element rowNumbersElement = newDocument.createElement("rowNumbers");
        //when time available change to expect arraylist of gridcells
        ArrayList<ThermometerCell> rowNumbers = board.getRowNumbers();
        for (ThermometerCell cell : rowNumbers) {
            int number = cell.getRotation();
            org.w3c.dom.Element rowNumberElement = newDocument.createElement("row");
            rowNumberElement.setAttribute("value", String.valueOf(number));
            rowNumbersElement.appendChild(rowNumberElement);
        }
        boardElement.appendChild(rowNumbersElement);

        // Creating the XML section for the col numbers and appending ot the board
        org.w3c.dom.Element colNumbersElement = newDocument.createElement("colNumbers");
        //when time available change to expect arraylist of gridcells
        ArrayList<ThermometerCell> colNumbers = board.getColNumbers();
        for (ThermometerCell cell : colNumbers) {
            int number = cell.getRotation();
            org.w3c.dom.Element colNumberElement = newDocument.createElement("col");
            colNumberElement.setAttribute("value", String.valueOf(number));
            colNumbersElement.appendChild(colNumberElement);
        }
        boardElement.appendChild(colNumbersElement);

        return boardElement;
    }
}
