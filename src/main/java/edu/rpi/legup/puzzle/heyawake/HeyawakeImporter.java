package edu.rpi.legup.puzzle.heyawake;

import edu.rpi.legup.model.Goal;
import edu.rpi.legup.model.GoalType;
import edu.rpi.legup.model.PuzzleImporter;
import edu.rpi.legup.puzzle.nurikabe.NurikabeCell;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.awt.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class HeyawakeImporter extends PuzzleImporter {

    public HeyawakeImporter(Heyawake heyawake) {
        super(heyawake);
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
                        "Heyawake Importer: cannot find board puzzleElement");
            }
            Element boardElement = (Element) node;
            if (boardElement.getElementsByTagName("cells").getLength() == 0) {
                throw new InvalidFileFormatException(
                        "Heyawake Importer: no puzzleElement found for board");
            }
            Element dataElement = (Element) boardElement.getElementsByTagName("cells").item(0);
            NodeList elementDataList = dataElement.getElementsByTagName("cell");

            HeyawakeBoard heyawakeBoard = null;
            if (!boardElement.getAttribute("size").isEmpty()) {
                int size = Integer.valueOf(boardElement.getAttribute("size"));
                heyawakeBoard = new HeyawakeBoard(size);
            } else {
                if (!boardElement.getAttribute("width").isEmpty()
                        && !boardElement.getAttribute("height").isEmpty()) {
                    int width = Integer.valueOf(boardElement.getAttribute("width"));
                    int height = Integer.valueOf(boardElement.getAttribute("height"));
                    heyawakeBoard = new HeyawakeBoard(width, height);
                }
            }

            if (heyawakeBoard == null) {
                throw new InvalidFileFormatException("Heyawake Importer: invalid board dimensions");
            }

            int width = heyawakeBoard.getWidth();
            int height = heyawakeBoard.getHeight();

            for (int i = 0; i < elementDataList.getLength(); i++) {
                HeyawakeCell cell =
                        (HeyawakeCell)
                                puzzle.getFactory()
                                        .importCell(elementDataList.item(i), heyawakeBoard);
                Point loc = cell.getLocation();
                if (cell.getData() != -2) {
                    cell.setModifiable(false);
                    cell.setGiven(true);
                }
                heyawakeBoard.setCell(loc.x, loc.y, cell);
            }

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (heyawakeBoard.getCell(x, y) == null) {
                        HeyawakeCell cell = new HeyawakeCell(0, new Point(x, y), -1);
                        cell.setIndex(y * height + x);
                        cell.setModifiable(false);
                        heyawakeBoard.setCell(x, y, cell);
                    }
                }
            }

            puzzle.setCurrentBoard(heyawakeBoard);
            if (boardElement.getElementsByTagName("goal").getLength() != 0) {
                Element goalElement = (Element) boardElement.getElementsByTagName("goal").item(0);
                Goal goal = puzzle.getFactory().importGoal(goalElement, heyawakeBoard);

                NodeList cellList = goalElement.getElementsByTagName("cell");
                for (int i = 0; i < cellList.getLength(); i++) {
                    HeyawakeCell cell =
                            (HeyawakeCell)
                                    puzzle.getFactory()
                                            .importCell(cellList.item(i), heyawakeBoard);
                    goal.addCell(cell);
                }
                puzzle.setGoal(goal);
            } else {
                Goal goal = new Goal(null, GoalType.DEFAULT);

                puzzle.setGoal(goal);
            }
        } catch (NumberFormatException e) {
            throw new InvalidFileFormatException(
                    "Heyawake Importer: unknown value where integer expected");
        }
    }

    @Override
    public void initializeBoard(String[] statements) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Hey Awake cannot accept text input");
    }
}
