package edu.rpi.legup.puzzle.skyscrapers;

import edu.rpi.legup.model.PuzzleImporter;
import edu.rpi.legup.save.InvalidFileFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.*;

public class SkyscrapersImporter extends PuzzleImporter {
    public SkyscrapersImporter(Skyscrapers skyscrapers) {
        super(skyscrapers);
    }

    /**
     * Creates an empty board for building
     *
     * @param rows    the number of rows on the board
     * @param columns the number of columns on the board
     * @throws RuntimeException if board can not be created
     */
    @Override
    public void initializeBoard(int rows, int columns) {

    }

    /**
     * Creates the board for building
     *
     * @param node xml document node
     * @throws InvalidFileFormatException if file is invalid
     */
    @Override
    public void initializeBoard(Node node) throws InvalidFileFormatException {
        try {
            if (!node.getNodeName().equalsIgnoreCase("board")) {
                throw new InvalidFileFormatException("Skyscrapers Importer: cannot find board puzzleElement");
            }
            Element boardElement = (Element) node;
            if (boardElement.getElementsByTagName("cells").getLength() == 0) {
                throw new InvalidFileFormatException("Skyscrapers Importer: no puzzleElement found for board");
            }
            Element dataElement = (Element) boardElement.getElementsByTagName("cells").item(0);
            NodeList elementDataList = dataElement.getElementsByTagName("cell");



            SkyscrapersBoard skyscrapersBoard = null;

            if (!boardElement.getAttribute("size").isEmpty()) {
                int size = Integer.valueOf(boardElement.getAttribute("size"));
                skyscrapersBoard = new SkyscrapersBoard(size);
            }

            if (skyscrapersBoard == null) {
                throw new InvalidFileFormatException("Skyscraper Importer: invalid board dimensions");
            }

            int size = skyscrapersBoard.getSize();


            for (int i = 0; i < elementDataList.getLength(); i++) {
                SkyscrapersCell cell = (SkyscrapersCell) puzzle.getFactory().importCell(elementDataList.item(i), skyscrapersBoard);
                Point loc = cell.getLocation();
                if (cell.getData() != 0) {
                    cell.setModifiable(false);
                    cell.setGiven(true);
                }
                skyscrapersBoard.setCell(loc.x, loc.y, cell);
            }

            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if (skyscrapersBoard.getCell(x, y) == null) {
                        SkyscrapersCell cell = new SkyscrapersCell(0, new Point(x, y), size);
                        cell.setIndex(y * size + x);
                        cell.setModifiable(true);
                        skyscrapersBoard.setCell(x, y, cell);
                    }
                }
            }

            NodeList axes = boardElement.getElementsByTagName("axis");
            if (axes.getLength() != 2) {
                throw new InvalidFileFormatException("Skyscraper Importer: cannot find axes");
            }

            Element axis1 = (Element) axes.item(0);
            Element axis2 = (Element) axes.item(1);

            if (!axis1.hasAttribute("side") || !axis1.hasAttribute("side")) {
                throw new InvalidFileFormatException("Skyscraper Importer: side attribute of axis not specified");
            }
            String side1 = axis1.getAttribute("side");
            String side2 = axis2.getAttribute("side");
            if (side1.equalsIgnoreCase(side2) || !(side1.equalsIgnoreCase("east") || side1.equalsIgnoreCase("south")) ||
                    !(side2.equalsIgnoreCase("east") || side2.equalsIgnoreCase("south"))) {
                throw new InvalidFileFormatException("Skyscraper Importer: axes must be different and be {east | south}");
            }
            NodeList eastClues = side1.equalsIgnoreCase("east") ? axis1.getElementsByTagName("clue") : axis2.getElementsByTagName("clue");
            NodeList southClues = side1.equalsIgnoreCase("south") ? axis1.getElementsByTagName("clue") : axis2.getElementsByTagName("clue");

            if (eastClues.getLength() != skyscrapersBoard.getHeight() || southClues.getLength() != skyscrapersBoard.getWidth()) {
                throw new InvalidFileFormatException("Skyscraper Importer: there must be same number of clues as the dimension of the board");
            }

            for (int i = 0; i < eastClues.getLength(); i++) {
                Element clue = (Element) eastClues.item(i);
                int value = Integer.valueOf(clue.getAttribute("value"));
                //int index = SkyscrapersClue.colStringToColNum(clue.getAttribute("index"));
                int index = Integer.valueOf(clue.getAttribute("index"));

                skyscrapersBoard.getWestClues().set(/*index - 1*/i, new SkyscrapersClue(index, i, SkyscrapersType.CLUE_WEST));
                skyscrapersBoard.getEastClues().set(/*index - 1*/i, new SkyscrapersClue(value, i, SkyscrapersType.CLUE_EAST));
            }

            for (int i = 0; i < southClues.getLength(); i++) {
                Element clue = (Element) southClues.item(i);
                int value = Integer.valueOf(clue.getAttribute("value"));
                int index = Integer.valueOf(clue.getAttribute("index"));


                skyscrapersBoard.getNorthClues().set(/*index - 1*/i, new SkyscrapersClue(index, i, SkyscrapersType.CLUE_NORTH));
                skyscrapersBoard.getSouthClues().set(/*index - 1*/i, new SkyscrapersClue(value, i, SkyscrapersType.CLUE_SOUTH));
            }

            if (boardElement.getElementsByTagName("lines").getLength() == 1) {
                Element linesElement = (Element) boardElement.getElementsByTagName("lines").item(0);
                NodeList linesList = linesElement.getElementsByTagName("line");
                for (int i = 0; i < linesList.getLength(); i++) {
                    skyscrapersBoard.getLines().add((SkyscrapersLine) puzzle.getFactory().importCell(linesList.item(i), skyscrapersBoard));
                }
            }

            //Initialize present flags
            NodeList flagList = boardElement.getElementsByTagName("flags");
            if(flagList.getLength()==1){
                Element flags = (Element) flagList.item(0);
                if(flags.hasAttribute("dupe")){
                    skyscrapersBoard.setDupeFlag(Boolean.parseBoolean(flags.getAttribute("dupe")));
                }
                if(flags.hasAttribute("view")){
                    skyscrapersBoard.setViewFlag(Boolean.parseBoolean(flags.getAttribute("view")));
                }
            }

            puzzle.setCurrentBoard(skyscrapersBoard);
        }
        catch (NumberFormatException e) {
            throw new InvalidFileFormatException("Skyscraper Importer: unknown value where integer expected");
        }
    }
}
