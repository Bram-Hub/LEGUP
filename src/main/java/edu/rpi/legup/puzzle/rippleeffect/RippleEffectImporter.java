package edu.rpi.legup.puzzle.rippleeffect;

import edu.rpi.legup.model.PuzzleImporter;
import edu.rpi.legup.save.InvalidFileFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class RippleEffectImporter extends PuzzleImporter {
    public RippleEffectImporter(RippleEffect rippleEffect) {
        super(rippleEffect);
    }

    private Map<Point, RippleEffectRegion> regionsMap;

    /**
     * Puzzle setting to support row and column inputs
     */
    @Override
    public boolean acceptsRowsAndColumnsInput() {
        return true;
    }

    /**
     * Puzzle setting to disable support for text input
     */
    @Override
    public boolean acceptsTextInput() {
        return false;
    }


    /**
     * Constructs empty RippleEffect gameboard as per the provided dimensions
     * @param rows number of rows for the gameboard
     * @param columns number of columns for the gameboard
     */
    @Override
    public void initializeBoard(int rows, int columns) {
        RippleEffectBoard rippleEffectBoard = new RippleEffectBoard(rows, columns);
        puzzle.setCurrentBoard(rippleEffectBoard);
    }



    /**
     * Constructs RippleEffect gameboard
     * @param node xml document node
     * @throws InvalidFileFormatException if file is invalid
     */
    @Override
    public void initializeBoard(Node node) throws InvalidFileFormatException {
        Element puzzleElement = (Element) node;

        NodeList regionNodes = puzzleElement.getElementsByTagName("region");
        if (regionNodes.getLength() == 0) {
            throw new InvalidFileFormatException("No regions found for the RippleEffect puzzle");
        }

        int width = Integer.parseInt(puzzleElement.getAttribute("width"));
        int height = Integer.parseInt(puzzleElement.getAttribute("height"));

        RippleEffectBoard rippleEffectBoard = new RippleEffectBoard(width, height); // Initialize the board with width and height from XML
        int cellType = 1; // Start with cell type 1

        for (int i = 0; i < regionNodes.getLength(); i++) {
            Element regionElement = (Element) regionNodes.item(i);
            NodeList cellNodes = regionElement.getElementsByTagName("cell");

            for (int j = 0; j < cellNodes.getLength(); j++) {
                Element cellElement = (Element) cellNodes.item(j);
                int x = Integer.parseInt(cellElement.getAttribute("x"));
                int y = Integer.parseInt(cellElement.getAttribute("y"));
                int value = Integer.parseInt(cellElement.getAttribute("value"));

                Point cellPoint = new Point(x, y);

                // Create the RippleEffectCell with the cell type and value
                RippleEffectCell cell = new RippleEffectCell(cellType, cellPoint, value);
                cell.setIndex(y * width + x); // Calculate the index based on width and height
                cell.setModifiable(true);

                // Add the cell to the board
                rippleEffectBoard.setCell(x, y, cell);
            }

            // Increment cell type for the next region
            cellType++;
        }

        puzzle.setCurrentBoard(rippleEffectBoard);
    }



    /**
     * Initialize board via string of statements.
     * @throws UnsupportedOperationException since RippleEffect does not support text input
     */
    @Override
    public void initializeBoard(String[] statements) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Ripple Effect does not accept text input");
    }
}