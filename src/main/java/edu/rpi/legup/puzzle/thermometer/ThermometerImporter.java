package edu.rpi.legup.puzzle.thermometer;

import static java.lang.Math.max;
import static java.lang.Math.min;

import edu.rpi.legup.model.Goal;
import edu.rpi.legup.model.GoalType;
import edu.rpi.legup.model.PuzzleImporter;
import edu.rpi.legup.puzzle.nurikabe.NurikabeCell;
import edu.rpi.legup.save.InvalidFileFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ThermometerImporter extends PuzzleImporter {

    // basic stuff stolen from dev guide/filled in by default
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
    public void initializeBoard(int rows, int columns) {}

    // method for initializing board from an xml file which has
    // a provided width/height
    @Override
    public void initializeBoard(Node node) throws InvalidFileFormatException {
        // sticking everything in a try statement because god has forsaken everyone
        try {
            // checking basic formatting of file
            if (!node.getNodeName().equalsIgnoreCase("board")) {
                throw new InvalidFileFormatException(
                        "thermometer Importer: cannot find board puzzleElement");
            }

            // getting the list of vials to turn into real vials
            Element boardElement = (Element) node;
            if (boardElement.getElementsByTagName("vials").getLength() == 0) {
                throw new InvalidFileFormatException(
                        "thermometer Importer: no puzzleElement found for board");
            }
            Element dataElement = (Element) boardElement.getElementsByTagName("vials").item(0);
            NodeList elementDataList = dataElement.getElementsByTagName("vial");

            // checking both a width and height were provided for the board
            ThermometerBoard thermometerBoard = null;
            if (!boardElement.getAttribute("width").isEmpty()
                    && !boardElement.getAttribute("height").isEmpty()) {

                // grabbing the height/width of the board
                int width = Integer.parseInt(boardElement.getAttribute("width"));
                int height = Integer.parseInt(boardElement.getAttribute("height"));

                // grabbing the lists of rowNumbers/colNumbers
                Element rowElement =
                        (Element) boardElement.getElementsByTagName("rowNumbers").item(0);
                NodeList rowNodeList = rowElement.getElementsByTagName("row");

                Element colElement =
                        (Element) boardElement.getElementsByTagName("colNumbers").item(0);
                NodeList colNodeList = colElement.getElementsByTagName("col");

                // checking that the number of row and col numbers agrees with height/width of board
                if (colNodeList.getLength() != width) {
                    throw new InvalidFileFormatException(
                            "Mismatch between width and number of colNums.\n colNodeList.length:"
                                    + colNodeList.getLength()
                                    + " width:"
                                    + width);
                }
                if (rowNodeList.getLength() != height) {
                    throw new InvalidFileFormatException(
                            "thermometer Importer: no rowNumbers found for board");
                }

                // finally creating our thermometer board, we add one to the size since row/col
                // numbers
                // are considered cells on the grid
                thermometerBoard = new ThermometerBoard(width + 1, height + 1);
                // adding row and column numbers to our board
                importRowColNums(rowNodeList, colNodeList, thermometerBoard);
            } else {
                throw new InvalidFileFormatException(
                        "thermometer Importer: invalid board height/width");
            }

            // grabbing height/width from board, need to subtract 1
            // because grids height/width is 1 bigger than number of vials on board
            int width = thermometerBoard.getWidth() - 1;
            int height = thermometerBoard.getHeight() - 1;

            // adding in the vials
            for (int i = 0; i < elementDataList.getLength(); i++) {
                importThermometerVial(elementDataList.item(i), thermometerBoard);
            }

            // verifying all vial cells were filled by vials
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (thermometerBoard.getCell(x, y) == null) {
                        throw new InvalidFileFormatException(
                                "Thermometer importer Undefined tile at (" + x + "," + y + ")");
                    }
                }
            }

            puzzle.setCurrentBoard(thermometerBoard);
            if (boardElement.getElementsByTagName("goal").getLength() != 0) {
                Element goalElement = (Element) boardElement.getElementsByTagName("goal").item(0);
                Goal goal = puzzle.getFactory().importGoal(goalElement, thermometerBoard);

                NodeList cellList = goalElement.getElementsByTagName("cell");
                for (int i = 0; i < cellList.getLength(); i++) {
                    ThermometerCell cell =
                            (ThermometerCell)
                                    puzzle.getFactory()
                                            .importCell(cellList.item(i), thermometerBoard);
                    goal.addCell(cell);
                }
                puzzle.setGoal(goal);
            } else {
                Goal goal = new Goal(null, GoalType.DEFAULT);

                puzzle.setGoal(goal);
            }
        } catch (NumberFormatException e) {
            throw new InvalidFileFormatException(
                    "thermometer Importer: unknown value where integer expected");
        }
    }

    @Override
    public void initializeBoard(String[] statements)
            throws UnsupportedOperationException, IllegalArgumentException {}

    private void importRowColNums(NodeList rowNodes, NodeList colNodes, ThermometerBoard board)
            throws InvalidFileFormatException {

        // going through our list or row nodes grabbed from the xml file and
        // then calling the thermometer boards setRowNumber function to update the value
        for (int i = 0; i < rowNodes.getLength(); i++) {
            Node node = rowNodes.item(i);
            int rowNum =
                    Integer.parseInt(node.getAttributes().getNamedItem("value").getNodeValue());
            if (!board.setRowNumber(i, rowNum)) {
                throw new InvalidFileFormatException("thermometer Importer: out of bounds rowNum");
            }
        }

        // same process but for col numbers
        for (int i = 0; i < colNodes.getLength(); i++) {
            Node node = colNodes.item(i);
            int colNum =
                    Integer.parseInt(node.getAttributes().getNamedItem("value").getNodeValue());
            if (!board.setColNumber(i, colNum)) {
                throw new InvalidFileFormatException("thermometer Importer: out of bounds colNum");
            }
        }
    }

    private void importThermometerVial(Node node, ThermometerBoard board)
            throws InvalidFileFormatException {
        // head is the top of the thermometer and tip is the end of the thermometer
        // thermometers in the xml are specified only by their head and tip cells
        int headX = Integer.parseInt(node.getAttributes().getNamedItem("headx").getNodeValue());
        int headY = Integer.parseInt(node.getAttributes().getNamedItem("heady").getNodeValue());
        int tipX = Integer.parseInt(node.getAttributes().getNamedItem("tailx").getNodeValue());
        int tipY = Integer.parseInt(node.getAttributes().getNamedItem("taily").getNodeValue());

        // making sure we can add the vial before doing so
        if (verifyVial(headX, headY, tipX, tipY, board)) {
            // adding the vial to the board
            board.addVial(new ThermometerVial(headX, headY, tipX, tipY, board));
        } else {
            throw new InvalidFileFormatException("thermometer Vial Factory: overlapping vials");
        }
    }

    private boolean verifyVial(int headX, int headY, int tipX, int tipY, ThermometerBoard board) {
        // figuring out which axis the thermometer travels along
        if (headX == tipX) {
            // finding start and end of Vial
            int top = min(headY, tipY);
            int bottom = max(headY, tipY);

            // verifying that every cell along path is currently unconstructed
            for (int i = top; i <= bottom; i++) {
                if (board.getCell(headX, i) != null) return false;
            }
        } else if (headY == tipY) {
            // finding start and end of Vial
            // I have words to say to james
            int left = min(headX, tipX);
            int right = max(headX, tipX);

            // verifying that every cell along path is currently unconstructed
            for (int i = left; i <= right; i++) {
                if (board.getCell(i, headY) != null) return false;
            }
        } else {
            // thermometer does not line up along a single axis
            return false;
        }
        return true;
    }
}
