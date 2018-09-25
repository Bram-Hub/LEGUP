package edu.rpi.legup.puzzle.heyawake;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.ElementFactory;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.save.InvalidFileFormatException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.awt.*;

public class HeyawakeFactory extends ElementFactory {
    /**
     * Creates a puzzleElement based on the xml document Node and adds it to the board
     *
     * @param node  node that represents the puzzleElement
     * @param board board to add the newly created cell
     * @return newly created cell from the xml document Node
     * @throws InvalidFileFormatException
     */
    @Override
    public HeyawakeCell importCell(Node node, Board board) throws InvalidFileFormatException {
        try {
            if (!node.getNodeName().equalsIgnoreCase("cell")) {
                throw new InvalidFileFormatException("Heyawake Factory: unknown puzzleElement puzzleElement");
            }

            HeyawakeBoard heyawakeBoard = (HeyawakeBoard) board;
            int width = heyawakeBoard.getWidth();
            int height = heyawakeBoard.getHeight();

            NamedNodeMap attributeList = node.getAttributes();
            int value = Integer.valueOf(attributeList.getNamedItem("value").getNodeValue());
            int x = Integer.valueOf(attributeList.getNamedItem("x").getNodeValue());
            int y = Integer.valueOf(attributeList.getNamedItem("y").getNodeValue());
            int regionIndex = Integer.valueOf(attributeList.getNamedItem("region").getNodeValue());
            if (x >= width || y >= height) {
                throw new InvalidFileFormatException("Heyawake Factory: cell location out of bounds");
            }
            if (value < -4 || value > 4) {
                throw new InvalidFileFormatException("Heyawake Factory: cell unknown value");
            }

            HeyawakeCell cell = new HeyawakeCell(value, new Point(x, y), regionIndex);
            cell.setIndex(y * height + x);
            heyawakeBoard.getRegions();
            return cell;
        } catch (NumberFormatException e) {
            throw new InvalidFileFormatException("Heyawake Factory: unknown value where integer expected");
        } catch (NullPointerException e) {
            throw new InvalidFileFormatException("Heyawake Factory: could not find attribute(s)");
        }
    }

    /**
     * Creates a xml document puzzleElement from a cell for exporting
     *
     * @param document xml document
     * @param puzzleElement     PuzzleElement cell
     * @return xml PuzzleElement
     */
    public org.w3c.dom.Element exportCell(Document document, PuzzleElement puzzleElement) {
        org.w3c.dom.Element cellElement = document.createElement("cell");

        HeyawakeCell cell = (HeyawakeCell) puzzleElement;
        Point loc = cell.getLocation();

        cellElement.setAttribute("value", String.valueOf(cell.getData()));
        cellElement.setAttribute("x", String.valueOf(loc.x));
        cellElement.setAttribute("y", String.valueOf(loc.y));
        cellElement.setAttribute("region", String.valueOf(cell.getRegionIndex()));

        return cellElement;
    }
}
