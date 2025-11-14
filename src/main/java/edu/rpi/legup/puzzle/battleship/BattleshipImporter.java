package edu.rpi.legup.puzzle.battleship;

import edu.rpi.legup.model.PuzzleImporter;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.awt.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BattleshipImporter extends PuzzleImporter {
    public BattleshipImporter(Battleship battleShip) {
        super(battleShip);
    }

    @Override
    public boolean acceptsRowsAndColumnsInput() {
        return true;
    }

    @Override
    public boolean acceptsTextInput() {
        return false;
    }

    /**
     * Creates an empty board for building
     *
     * @param rows the number of rows on the board
     * @param columns the number of columns on the board
     * @throws RuntimeException if board can not be created
     */
    @Override
    public void initializeBoard(int rows, int columns) {}

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
                throw new InvalidFileFormatException(
                        "BattleShip Importer: " + "cannot find board puzzleElement");
            }
            Element boardElement = (Element) node;
            if (boardElement.getElementsByTagName("cells").getLength() == 0) {
                throw new InvalidFileFormatException(
                        "BattleShip Importer: " + "no puzzleElement found for board");
            }
            Element dataElement = (Element) boardElement.getElementsByTagName("cells").item(0);
            NodeList elementDataList = dataElement.getElementsByTagName("cell");

            BattleshipBoard battleShipBoard = null;
            if (!boardElement.getAttribute("size").isEmpty()) {
                int size = Integer.valueOf(boardElement.getAttribute("size"));
                battleShipBoard = new BattleshipBoard(size);
            } else {
                if (!boardElement.getAttribute("width").isEmpty()
                        && !boardElement.getAttribute("height").isEmpty()) {
                    int width = Integer.valueOf(boardElement.getAttribute("width"));
                    int height = Integer.valueOf(boardElement.getAttribute("height"));
                    battleShipBoard = new BattleshipBoard(width, height);
                }
            }

            if (battleShipBoard == null) {
                throw new InvalidFileFormatException(
                        "BattleShip Importer: " + "invalid board dimensions");
            }

            int width = battleShipBoard.getWidth();
            int height = battleShipBoard.getHeight();

            for (int i = 0; i < elementDataList.getLength(); i++) {
                BattleshipCell cell =
                        (BattleshipCell)
                                puzzle.getFactory()
                                        .importCell(elementDataList.item(i), battleShipBoard);
                Point loc = cell.getLocation();
                if (cell.getData() != BattleshipType.getType(0)) {
                    cell.setModifiable(false);
                    cell.setGiven(true);
                }
                battleShipBoard.setCell(loc.x, loc.y, cell);
            }

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (battleShipBoard.getCell(x, y) == null) {
                        BattleshipCell cell =
                                new BattleshipCell(BattleshipType.UNKNOWN, new Point(x, y));
                        cell.setIndex(y * width + x);
                        cell.setModifiable(true);
                        battleShipBoard.setCell(x, y, cell);
                    }
                }
            }

            NodeList axes = boardElement.getElementsByTagName("axis");
            if (axes.getLength() != 2) {
                throw new InvalidFileFormatException("BattleShip Importer: " + "cannot find axes");
            }

            Element axis1 = (Element) axes.item(0);
            Element axis2 = (Element) axes.item(1);

            if (!axis1.hasAttribute("side") || !axis2.hasAttribute("side")) {
                throw new InvalidFileFormatException(
                        "BattleShip Importer: " + "side attribute of axis not specified");
            }
            String side1 = axis1.getAttribute("side");
            String side2 = axis2.getAttribute("side");
            if (side1.equalsIgnoreCase(side2)
                    || !(side1.equalsIgnoreCase("east") || side1.equalsIgnoreCase("south"))
                    || !(side2.equalsIgnoreCase("east") || side2.equalsIgnoreCase("south"))) {
                throw new InvalidFileFormatException(
                        "BattleShip Importer: " + "axes must be different and be {east | south}");
            }
            NodeList eastClues =
                    side1.equalsIgnoreCase("east")
                            ? axis1.getElementsByTagName("clue")
                            : axis2.getElementsByTagName("clue");
            NodeList southClues =
                    side1.equalsIgnoreCase("south")
                            ? axis1.getElementsByTagName("clue")
                            : axis2.getElementsByTagName("clue");

            if (eastClues.getLength() != battleShipBoard.getHeight()
                    || southClues.getLength() != battleShipBoard.getWidth()) {
                throw new InvalidFileFormatException(
                        "BattleShip Importer: "
                                + "there must be same number of clues as the dimension "
                                + "of the board");
            }

            for (int i = 0; i < eastClues.getLength(); i++) {
                Element clue = (Element) eastClues.item(i);
                int value = Integer.valueOf(clue.getAttribute("value"));
                int index = BattleshipClue.colStringToColNum(clue.getAttribute("index"));

                if (index - 1 < 0 || index - 1 > battleShipBoard.getHeight()) {
                    throw new InvalidFileFormatException(
                            "BattleShip Importer: " + "clue index out of bounds");
                }

                if (battleShipBoard.getEast().get(index - 1) != null) {
                    throw new InvalidFileFormatException(
                            "BattleShip Importer: " + "duplicate clue index");
                }
                battleShipBoard
                        .getEast()
                        .set(index - 1, new BattleshipClue(value, index, BattleshipType.CLUE_EAST));
            }

            for (int i = 0; i < southClues.getLength(); i++) {
                Element clue = (Element) southClues.item(i);
                int value = Integer.valueOf(clue.getAttribute("value"));
                int index = Integer.valueOf(clue.getAttribute("index"));

                if (index - 1 < 0 || index - 1 > battleShipBoard.getWidth()) {
                    throw new InvalidFileFormatException(
                            "BattleShip Importer: " + "clue index out of bounds");
                }

                if (battleShipBoard.getSouth().get(index - 1) != null) {
                    throw new InvalidFileFormatException(
                            "BattleShip Importer: " + "duplicate clue index");
                }
                battleShipBoard
                        .getSouth()
                        .set(
                                index - 1,
                                new BattleshipClue(value, index, BattleshipType.CLUE_SOUTH));
            }

            puzzle.setCurrentBoard(battleShipBoard);
        } catch (NumberFormatException e) {
            throw new InvalidFileFormatException(
                    "BattleShip Importer: " + "unknown value where integer expected");
        }
    }

    @Override
    public void initializeBoard(String[] statements) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Battleship cannot accept text input");
    }
}
