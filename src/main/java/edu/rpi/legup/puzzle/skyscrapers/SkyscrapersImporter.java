package edu.rpi.legup.puzzle.skyscrapers;

import edu.rpi.legup.model.PuzzleImporter;
import edu.rpi.legup.save.InvalidFileFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.*;

public class SkyscrapersImporter extends PuzzleImporter {
    public SkyscrapersImporter(Skyscrapers treeTent) {
        super(treeTent);
    }

    /**
     * Creates an empty board for building
     *
     * @param rows    the number of rows on the board
     * @param columns the number of columns on the board
     * @throws RuntimeException
     */
    @Override
    public void initializeBoard(int rows, int columns) {

    }

    /**
     * Creates the board for building
     *
     * @param node xml document node
     * @throws InvalidFileFormatException
     */
    @Override
    public void initializeBoard(Node node) throws InvalidFileFormatException {
        try {
            if (!node.getNodeName().equalsIgnoreCase("board")) {
                throw new InvalidFileFormatException("TreeTent Importer: cannot find board puzzleElement");
            }
            Element boardElement = (Element) node;
            if (boardElement.getElementsByTagName("cells").getLength() == 0) {
                throw new InvalidFileFormatException("TreeTent Importer: no puzzleElement found for board");
            }
            Element dataElement = (Element) boardElement.getElementsByTagName("cells").item(0);
            NodeList elementDataList = dataElement.getElementsByTagName("cell");

            SkyscrapersBoard treeTentBoard = null;
            if (!boardElement.getAttribute("size").isEmpty()) {
                int size = Integer.valueOf(boardElement.getAttribute("size"));
                treeTentBoard = new SkyscrapersBoard(size);
            }
            else {
                if (!boardElement.getAttribute("width").isEmpty() && !boardElement.getAttribute("height").isEmpty()) {
                    int width = Integer.valueOf(boardElement.getAttribute("width"));
                    int height = Integer.valueOf(boardElement.getAttribute("height"));
                    treeTentBoard = new SkyscrapersBoard(width, height);
                }
            }

            if (treeTentBoard == null) {
                throw new InvalidFileFormatException("TreeTent Importer: invalid board dimensions");
            }

            int width = treeTentBoard.getWidth();
            int height = treeTentBoard.getHeight();

            for (int i = 0; i < elementDataList.getLength(); i++) {
                SkyscrapersCell cell = (SkyscrapersCell) puzzle.getFactory().importCell(elementDataList.item(i), treeTentBoard);
                Point loc = cell.getLocation();
                if (cell.getData() != 0) {
                    cell.setModifiable(false);
                    cell.setGiven(true);
                }
                treeTentBoard.setCell(loc.x, loc.y, cell);
            }

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (treeTentBoard.getCell(x, y) == null) {
                        SkyscrapersCell cell = new SkyscrapersCell(0, new Point(x, y), width);
                        cell.setIndex(y * height + x);
                        cell.setModifiable(true);
                        treeTentBoard.setCell(x, y, cell);
                    }
                }
            }

            NodeList axes = boardElement.getElementsByTagName("axis");
            if (axes.getLength() != 2) {
                throw new InvalidFileFormatException("TreeTent Importer: cannot find axes");
            }

            Element axis1 = (Element) axes.item(0);
            Element axis2 = (Element) axes.item(1);

            if (!axis1.hasAttribute("side") || !axis1.hasAttribute("side")) {
                throw new InvalidFileFormatException("TreeTent Importer: side attribute of axis not specified");
            }
            String side1 = axis1.getAttribute("side");
            String side2 = axis2.getAttribute("side");
            if (side1.equalsIgnoreCase(side2) || !(side1.equalsIgnoreCase("east") || side1.equalsIgnoreCase("south")) ||
                    !(side2.equalsIgnoreCase("east") || side2.equalsIgnoreCase("south"))) {
                throw new InvalidFileFormatException("TreeTent Importer: axes must be different and be {east | south}");
            }
            NodeList eastClues = side1.equalsIgnoreCase("east") ? axis1.getElementsByTagName("clue") : axis2.getElementsByTagName("clue");
            NodeList southClues = side1.equalsIgnoreCase("south") ? axis1.getElementsByTagName("clue") : axis2.getElementsByTagName("clue");

            if (eastClues.getLength() != treeTentBoard.getHeight() || southClues.getLength() != treeTentBoard.getWidth()) {
                throw new InvalidFileFormatException("TreeTent Importer: there must be same number of clues as the dimension of the board");
            }

            for (int i = 0; i < eastClues.getLength(); i++) {
                Element clue = (Element) eastClues.item(i);
                int value = Integer.valueOf(clue.getAttribute("value"));
                //int index = SkyscrapersClue.colStringToColNum(clue.getAttribute("index"));
                int index = Integer.valueOf(clue.getAttribute("index"));
                /*if (index - 1 < 0 || index - 1 > treeTentBoard.getHeight()) {
                    throw new InvalidFileFormatException("TreeTent Importer: clue index out of bounds");
                }

                if (treeTentBoard.getRowClues().get(index - 1) != null) {
                    throw new InvalidFileFormatException("TreeTent Importer: duplicate clue index");
                }*/
                treeTentBoard.getRow().set(/*index - 1*/i, new SkyscrapersClue(index, index, SkyscrapersType.CLUE_WEST));
                treeTentBoard.getRowClues().set(/*index - 1*/i, new SkyscrapersClue(value, index, SkyscrapersType.CLUE_EAST));
            }

            for (int i = 0; i < southClues.getLength(); i++) {
                Element clue = (Element) southClues.item(i);
                int value = Integer.valueOf(clue.getAttribute("value"));
                int index = Integer.valueOf(clue.getAttribute("index"));

                /*if (index - 1 < 0 || index - 1 > treeTentBoard.getWidth()) {
                    throw new InvalidFileFormatException("TreeTent Importer: clue index out of bounds");
                }

                if (treeTentBoard.getColClues().get(index - 1) != null) {
                    throw new InvalidFileFormatException("TreeTent Importer: duplicate clue index");
                }*/
                treeTentBoard.getCol().set(/*index - 1*/i, new SkyscrapersClue(index, index, SkyscrapersType.CLUE_NORTH));
                treeTentBoard.getColClues().set(/*index - 1*/i, new SkyscrapersClue(value, index, SkyscrapersType.CLUE_SOUTH));
            }

            if (boardElement.getElementsByTagName("lines").getLength() == 1) {
                Element linesElement = (Element) boardElement.getElementsByTagName("lines").item(0);
                NodeList linesList = linesElement.getElementsByTagName("line");
                for (int i = 0; i < linesList.getLength(); i++) {
                    treeTentBoard.getLines().add((SkyscrapersLine) puzzle.getFactory().importCell(linesList.item(i), treeTentBoard));
                }
            }

            puzzle.setCurrentBoard(treeTentBoard);
        }
        catch (NumberFormatException e) {
            throw new InvalidFileFormatException("TreeTent Importer: unknown value where integer expected");
        }
    }
}
