package edu.rpi.legup.puzzle.nurikabe;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.ElementFactory;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.awt.*;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class NurikabeCellFactory extends ElementFactory {
    /**
     * Creates a puzzleElement based on the xml document Node and adds it to the board
     *
     * @param node node that represents the puzzleElement
     * @param board board to add the newly created cell
     * @return newly created cell from the xml document Node
     * @throws InvalidFileFormatException if file is invalid
     */
    @Override
    public NurikabeCell importCell(Node node, Board board) throws InvalidFileFormatException {
        try {
            if (!node.getNodeName().equalsIgnoreCase("cell")) {
                throw new InvalidFileFormatException(
                        "nurikabe Factory: unknown puzzleElement puzzleElement");
            }

            NurikabeBoard nurikabeBoard = (NurikabeBoard) board;
            int width = nurikabeBoard.getWidth();
            int height = nurikabeBoard.getHeight();

            NamedNodeMap attributeList = node.getAttributes();
            int value = Integer.valueOf(attributeList.getNamedItem("value").getNodeValue());
            int x = Integer.valueOf(attributeList.getNamedItem("x").getNodeValue());
            int y = Integer.valueOf(attributeList.getNamedItem("y").getNodeValue());
            if (x >= width || y >= height) {
                throw new InvalidFileFormatException(
                        "nurikabe Factory: cell location out of bounds");
            }
            if (value < -2) {
                throw new InvalidFileFormatException("nurikabe Factory: cell unknown value");
            }

            NurikabeCell cell = new NurikabeCell(value, new Point(x, y));
            cell.setIndex(y * width + x);
            return cell;
        } catch (NumberFormatException e) {
            throw new InvalidFileFormatException(
                    "nurikabe Factory: unknown value where integer expected");
        } catch (NullPointerException e) {
            throw new InvalidFileFormatException("nurikabe Factory: could not find attribute(s)");
        }
    }

    /**
     * Creates a xml document puzzleElement from a cell for exporting
     *
     * @param document xml document
     * @param puzzleElement PuzzleElement cell
     * @return xml PuzzleElement
     */
    public org.w3c.dom.Element exportCell(Document document, PuzzleElement puzzleElement) {
        org.w3c.dom.Element cellElement = document.createElement("cell");

        NurikabeCell cell = (NurikabeCell) puzzleElement;
        Point loc = cell.getLocation();

        cellElement.setAttribute("value", String.valueOf(cell.getData()));
        cellElement.setAttribute("x", String.valueOf(loc.x));
        cellElement.setAttribute("y", String.valueOf(loc.y));

        return cellElement;
    }
}
