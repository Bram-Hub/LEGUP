package edu.rpi.legup.puzzle.thermometer;

import edu.rpi.legup.model.PuzzleImporter;
import edu.rpi.legup.save.InvalidFileFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static java.lang.Math.max;
import static java.lang.Math.min;

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
            Element dataElement = (Element) boardElement.getElementsByTagName("vials").item(0);
            NodeList elementDataList = dataElement.getElementsByTagName("vial");

            ThermometerBoard thermometerBoard = null;
            if (!boardElement.getAttribute("width").isEmpty() && !boardElement.getAttribute("height").isEmpty()) {
                int width = Integer.parseInt(boardElement.getAttribute("width"));
                int height = Integer.parseInt(boardElement.getAttribute("height"));

                Element rowElement = (Element) boardElement.getElementsByTagName("rowNumbers").item(0);
                NodeList rowNodeList = rowElement.getElementsByTagName("row");

                Element colElement = (Element) boardElement.getElementsByTagName("colNumbers").item(0);
                NodeList colNodeList = colElement.getElementsByTagName("col");

                if (colNodeList.getLength() != width) {
                    throw new InvalidFileFormatException("Mismatch between width and number of colNums.\n colNodeList.length:" + colNodeList.getLength() + " width:" + width);
                }
                if (rowNodeList.getLength() != height) {
                    throw new InvalidFileFormatException("thermometer Importer: no rowNumbers found for board");
                }

                //TODO: potentially have to deal with size issues and non interactable cells
                thermometerBoard = new ThermometerBoard(width, height);
                importRowColNums(rowNodeList, colNodeList, thermometerBoard);
            }

            if (thermometerBoard == null) {
                throw new InvalidFileFormatException("thermometer Importer: invalid board dimensions");
            }

            int width = thermometerBoard.getWidth();
            int height = thermometerBoard.getHeight();

            for (int i = 0; i < elementDataList.getLength(); i++) {
                importThermometerVial(elementDataList.item(i), thermometerBoard);
            }

            //verifying all vials were used
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (thermometerBoard.getCell(x, y) == null) {
                        throw new InvalidFileFormatException("Thermometer importer Undefined tile at (" + x + "," + y + ")");
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

    private void importRowColNums(NodeList rowNodes, NodeList colNodes, ThermometerBoard board) {

        for (int i = 0; i < rowNodes.getLength(); i++) {
            Node node = rowNodes.item(i);
            int rowNum = Integer.parseInt(node.getAttributes().getNamedItem("value").getNodeValue());
            //board.setRowNumber();
        }
    }

    private void importThermometerVial(Node node, ThermometerBoard board) throws InvalidFileFormatException{
        int headX = Integer.parseInt(node.getAttributes().getNamedItem("headx").getNodeValue());
        int headY = Integer.parseInt(node.getAttributes().getNamedItem("heady").getNodeValue());
        int tipX = Integer.parseInt(node.getAttributes().getNamedItem("tailx").getNodeValue());
        int tipY = Integer.parseInt(node.getAttributes().getNamedItem("taily").getNodeValue());

        if(verifyVial(headX, headY, tipX, tipY, board)) {
            System.out.println("Vial successfully created");
            board.addVial(new ThermometerVial(headX, headY, tipX, tipY, board));
        }
        else {
            throw new InvalidFileFormatException("thermometer Vial Factory: overlapping vials");
        }
    }

    private boolean verifyVial(int headX, int headY, int tipX, int tipY, ThermometerBoard board) {
        //figuring out which axis the thermometer travels along
        if(headX == tipX) {
            //finding start and end of Vial
            int top = min(headY, tipY);
            int bottom = max(headY, tipY);

            //verifying that every cell along path is currently unconstructed
            for (int i = top; i <= bottom; i++) {
                if(board.getCell(headX, i) != null) return false;
            }
        }
        else if (headY == tipY) {
            //finding start and end of Vial
            //I have words to say to james
            int left = min(headX, tipX);
            int right = max(headX, tipX);

            //verifying that every cell along path is currently unconstructed
            for (int i = left; i <= right; i++) {
                if(board.getCell(i, headY) != null) return false;
            }
        }
        else{
            //thermometer does not line up along a single axis
            return false;
        }
        return true;
    }
}