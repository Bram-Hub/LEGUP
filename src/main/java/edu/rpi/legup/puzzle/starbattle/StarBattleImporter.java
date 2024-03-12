package edu.rpi.legup.puzzle.starbattle;

import edu.rpi.legup.model.PuzzleImporter;
import edu.rpi.legup.save.InvalidFileFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.awt.*;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StarBattleImporter  extends PuzzleImporter{
    
   
    public StarBattleImporter(StarBattle starbattle) {
        super(starbattle);
    }

    private Map<Point, StarBattleRegion> regionsMap;

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
     * Constructs empty StarBattle gameboard as per the provided dimensions
     * @param rows number of rows and columns for the gameboard
     */
    @Override
    public void initializeBoard(int rows, int columns) {
        StarBattleBoard StarBattleBoard = new StarBattleBoard(rows);
        puzzle.setCurrentBoard(StarBattleBoard);
    }



    /**
     * Constructs StarBattle gameboard
     * @param node xml document node
     * @throws InvalidFileFormatException if file is invalid
     */
    @Override
    public void initializeBoard(Node node) throws InvalidFileFormatException {
        Element puzzleElement = (Element) node;

        NodeList regionNodes = puzzleElement.getElementsByTagName("region");
        if (regionNodes.getLength() == 0) {
            throw new InvalidFileFormatException("No regions found for the StarBattle puzzle");
        }

        int size = Integer.parseInt(puzzleElement.getAttribute("size"));

        StarBattleBoard StarBattleBoard = new StarBattleBoard(size); // Initialize the board with width and height from XML

        for (int i = 0; i < regionNodes.getLength(); i++) {
            Element regionElement = (Element) regionNodes.item(i);
            NodeList cellNodes = regionElement.getElementsByTagName("cell");

            for (int j = 0; j < cellNodes.getLength(); j++) {
                Element cellElement = (Element) cellNodes.item(j);
                int x = Integer.parseInt(cellElement.getAttribute("x"));
                int y = Integer.parseInt(cellElement.getAttribute("y"));
                int value = Integer.parseInt(cellElement.getAttribute("value"));

                Point cellPoint = new Point(x, y);

                // Create the StarBattleCell with the cell type and value
                StarBattleCell cell = new StarBattleCell(value, cellPoint, i, size);
                cell.setIndex(y * size + x); // Calculate the index based on size
                cell.setModifiable(true);

                // Add the cell to the board
                StarBattleBoard.setCell(x, y, cell);
            }
        }

        puzzle.setCurrentBoard(StarBattleBoard);
    }



    /**
     * Initialize board via string of statements.
     * @throws UnsupportedOperationException since StarBattle does not support text input
     */
    @Override
    public void initializeBoard(String[] statements) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Ripple Effect does not accept text input");
    }
}



