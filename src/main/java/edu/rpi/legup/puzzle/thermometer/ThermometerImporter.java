package edu.rpi.legup.puzzle.thermometer;

import edu.rpi.legup.model.PuzzleImporter;
import edu.rpi.legup.save.InvalidFileFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.*;

public class ThermometerImporter extends PuzzleImporter {
    public ThermometerImporter(Thermometer thermometer) {
        super(thermometer);
    }

    @Override
    public boolean acceptsRowsAndColumnsInput() {
        return false;
    }

    @Override
    public boolean acceptsTextInput() {
        return false;
    }

    @Override
    public void initializeBoard(int rows, int columns) {

    }

    @Override
    public void initializeBoard(Node node) throws InvalidFileFormatException {
        try {
            if (!node.getNodeName().equalsIgnoreCase("board")) {
                throw new InvalidFileFormatException("thermometer Importer: cannot find board puzzleElement");
            }
            Element boardElement = (Element) node;
            if (boardElement.getElementsByTagName("vials").getLength() == 0) {
                throw new InvalidFileFormatException("thermometer Importer: no puzzleElement found for board");
            }
            Element dataElement = (Element) boardElement.getElementsByTagName("cells").item(0);
            NodeList elementDataList = dataElement.getElementsByTagName("cell");

            ThermometerBoard thermometerBoard = null;
            if (!boardElement.getAttribute("size").isEmpty()) {
                int size = Integer.parseInt(boardElement.getAttribute("size"));
                thermometerBoard = new ThermometerBoard(size);
                if (boardElement.getElementsByTagName("rowNumbers").getLength() != size) {
                    throw new InvalidFileFormatException("thermometer Importer: no rowNumbers found for board");
                }
                if (boardElement.getElementsByTagName("colNumbers").getLength() != size) {
                    throw new InvalidFileFormatException("thermometer Importer: no colNumbers found for board");
                }
            } else if (!boardElement.getAttribute("width").isEmpty() && !boardElement.getAttribute("height").isEmpty()) {
                int width = Integer.parseInt(boardElement.getAttribute("width"));
                int height = Integer.parseInt(boardElement.getAttribute("height"));
                if (boardElement.getElementsByTagName("colNumbers").getLength() != width) {
                    throw new InvalidFileFormatException("thermometer Importer: no colNumbers found for board");
                }
                if (boardElement.getElementsByTagName("rowNumbers").getLength() != height) {
                    throw new InvalidFileFormatException("thermometer Importer: no rowNumbers found for board");
                }
                //TODO: potentially have to deal with size issues and non interactable cells
                thermometerBoard = new ThermometerBoard(width, height);
            }

            if (thermometerBoard == null) {
                throw new InvalidFileFormatException("thermometer Importer: invalid board dimensions");
            }

            int width = thermometerBoard.getWidth();
            int height = thermometerBoard.getHeight();

            for (int i = 0; i < elementDataList.getLength(); i++) {
                int headx = Integer.parseInt(elementDataList.item(i).getAttributes().getNamedItem("headx").getNodeValue());
                int heady = Integer.parseInt(elementDataList.item(i).getAttributes().getNamedItem("heady").getNodeValue());
                int tailx = Integer.parseInt(elementDataList.item(i).getAttributes().getNamedItem("tailx").getNodeValue());
                int taily = Integer.parseInt(elementDataList.item(i).getAttributes().getNamedItem("taily").getNodeValue());

                //value int issue again will have to fix later
                ThermometerCell head = new ThermometerCell(new Point(headx, heady));
                ThermometerCell tail = new ThermometerCell(new Point(tailx, taily));
                thermometerBoard.addVial(head, tail);
            }

            /* Potentially useless keeping to appease James ego
            for (int i = 0; i < elementDataList.getLength(); i++) {
                ThermometerCell cell = (ThermometerCell) puzzle.getFactory().importCell(elementDataList.item(i), thermometerBoard);
                Point loc = cell.getLocation();
                if (cell.getType() != ThermometerType.UNKNOWN) {
                    cell.setModifiable(false);
                    cell.setGiven(true);
                }
                thermometerBoard.setCell(loc.x, loc.y, cell);
            }
            */
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (thermometerBoard.getCell(x, y) == null) {
                        ThermometerCell cell = new ThermometerCell(new Point(x, y));
                        cell.setIndex(y * height + x);
                        cell.setModifiable(true);
                        thermometerBoard.setCell(x, y, cell);
                    }
                }
            }
            puzzle.setCurrentBoard(thermometerBoard);
        } catch (NumberFormatException e) {
            throw new InvalidFileFormatException("thermometer Importer: unknown value where integer expected");
        }
    }

    @Override
    public void initializeBoard(String[] statements) throws UnsupportedOperationException, IllegalArgumentException {

    }
}