package edu.rpi.legup.puzzle.thermometer;

import java.util.ArrayList;

import edu.rpi.legup.model.PuzzleExporter;
import org.w3c.dom.Document;
import edu.rpi.legup.puzzle.thermometer.elements.Vial;

public class ThermometerExporter extends PuzzleExporter {

    public ThermometerExporter(Thermometer thermometer) {
        super(thermometer);
    }

    @Override
    protected org.w3c.dom.Element createBoardElement(Document newDocument) {
        ThermometerBoard board = (ThermometerBoard) puzzle.getTree().getRootNode().getBoard();

        // Creating the XML section for the board
        org.w3c.dom.Element boardElement = newDocument.createElement("board");
        boardElement.setAttribute("width", String.valueOf(board.getWidth()));
        boardElement.setAttribute("height", String.valueOf(board.getHeight()));

        // Creating the XML section for the vials and appending to the board
        org.w3c.dom.Element vialsElement = newDocument.createElement("vials");
        ArrayList<Vial> vials = board.getVials();
        for (Vial vial : vials) {
            org.w3c.dom.Element vialElement = newDocument.createElement("vial");
            // Temp for now, waiting on final implementation of vial
//            vialElement.setAttribute("headx", vial.getHeadX());
//            vialElement.setAttribute("heady", vial.getHeadY());
//            vialElement.setAttribute("tailx", vial.getTailX());
//            vialElement.setAttribute("taily", vial.getTailY());
            vialsElement.appendChild(vialElement);
        }
        boardElement.appendChild(vialsElement);

        // Creating the XML section for the row numbers and appending to the board
        org.w3c.dom.Element rowNumbersElement = newDocument.createElement("rowNumbers");
        ArrayList<Integer> rowNumbers = board.getRowNumbers();
        for (int number : rowNumbers) {
            org.w3c.dom.Element rowNumberElement = newDocument.createElement("row");
            rowNumberElement.setAttribute("value", String.valueOf(number));
            rowNumbersElement.appendChild(rowNumberElement);
        }
        boardElement.appendChild(rowNumbersElement);

        // Creating the XML section for the col numbers and appending ot the board
        org.w3c.dom.Element colNumbersElement = newDocument.createElement("colNumbers");
        ArrayList<Integer> colNumbers = board.getColNumbers();
        for (int number : colNumbers) {
            org.w3c.dom.Element colNumberElement = newDocument.createElement("col");
            colNumberElement.setAttribute("value", String.valueOf(number));
            colNumbersElement.appendChild(colNumberElement);
        }
        boardElement.appendChild(colNumbersElement);

        return boardElement;
    }
}
