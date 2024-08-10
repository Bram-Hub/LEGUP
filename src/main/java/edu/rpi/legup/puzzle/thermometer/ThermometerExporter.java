package edu.rpi.legup.puzzle.thermometer;

import edu.rpi.legup.model.PuzzleExporter;
import java.util.ArrayList;
import org.w3c.dom.Document;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ThermometerExporter extends PuzzleExporter {

    public ThermometerExporter(@NotNull Thermometer thermometer) {
        super(thermometer);
    }

    @Override
    @Contract(pure = true)
    protected @NotNull org.w3c.dom.Element createBoardElement(@NotNull Document newDocument) {
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
            // The way the vials are created are with the head (bulb) position and the final
            // position
            // This implementation doesn't allow for curved thermometers, but for right now that's
            // fine
            vialElement.setAttribute(
                    "headx", String.valueOf((int) vial.getHead().getLocation().getX()));
            vialElement.setAttribute(
                    "heady", String.valueOf((int) vial.getHead().getLocation().getY()));
            vialElement.setAttribute(
                    "tailx", String.valueOf((int) vial.getTail().getLocation().getX()));
            vialElement.setAttribute(
                    "taily", String.valueOf((int) vial.getTail().getLocation().getY()));
            vialsElement.appendChild(vialElement);
        }
        boardElement.appendChild(vialsElement);

        // Creating the XML section for the row numbers and appending to the board
        org.w3c.dom.Element rowNumbersElement = newDocument.createElement("rowNumbers");
        ArrayList<ThermometerCell> rowNumbers = board.getRowNumbers();
        // The row numbers are the numbers on the right most column, labeling how many filled
        // sections
        // are in the row
        for (ThermometerCell cell : rowNumbers) {
            int number = cell.getRotation();
            org.w3c.dom.Element rowNumberElement = newDocument.createElement("row");
            rowNumberElement.setAttribute("value", String.valueOf(number));
            rowNumbersElement.appendChild(rowNumberElement);
        }
        boardElement.appendChild(rowNumbersElement);

        // Creating the XML section for the col numbers and appending ot the board
        org.w3c.dom.Element colNumbersElement = newDocument.createElement("colNumbers");
        // The col numbers are the numbers on the bottom row, labeling how many filled sections
        // are in the column
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
