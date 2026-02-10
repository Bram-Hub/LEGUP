package edu.rpi.legup.puzzle.skyscrapers;

import edu.rpi.legup.model.Goal;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.ElementFactory;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.awt.*;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class SkyscrapersCellFactory extends ElementFactory {
    /**
     * Creates a puzzleElement based on the xml document Node and adds it to the board
     *
     * @param node node that represents the puzzleElement
     * @param board board to add the newly created cell
     * @return newly created cell from the xml document Node
     * @throws InvalidFileFormatException if input is invalid
     */
    @Override
    public PuzzleElement importCell(Node node, Board board) throws InvalidFileFormatException {
        try {
            if (!node.getNodeName().equalsIgnoreCase("cell")) {
                throw new InvalidFileFormatException(
                        "Skyscrapers Factory: unknown puzzleElement puzzleElement");
            }

            SkyscrapersBoard skyscrapersBoard = (SkyscrapersBoard) board;
            int size = skyscrapersBoard.getSize();
            NamedNodeMap attributeList = node.getAttributes();

            int value = Integer.valueOf(attributeList.getNamedItem("value").getNodeValue());
            int x = Integer.valueOf(attributeList.getNamedItem("x").getNodeValue());
            int y = Integer.valueOf(attributeList.getNamedItem("y").getNodeValue());
            if (x >= size || y >= size) {
                throw new InvalidFileFormatException(
                        "Skyscrapers Factory: cell location out of bounds");
            }
            if (value < 0 || value > size) {
                throw new InvalidFileFormatException("Skyscrapers Factory: cell unknown value");
            }

            SkyscrapersCell cell = new SkyscrapersCell(value, new Point(x, y), size);
            cell.setIndex(y * size + x);

            return cell;
        } catch (NumberFormatException e) {
            throw new InvalidFileFormatException(
                    "Skyscrapers Factory: unknown value where integer expected");
        } catch (NullPointerException e) {
            throw new InvalidFileFormatException(
                    "Skyscrapers Factory: could not find attribute(s)");
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

        SkyscrapersCell cell = (SkyscrapersCell) puzzleElement;
        Point loc = cell.getLocation();

        cellElement.setAttribute("value", String.valueOf(cell.getData()));
        cellElement.setAttribute("x", String.valueOf(loc.x));
        cellElement.setAttribute("y", String.valueOf(loc.y));

        return cellElement;
    }
}
