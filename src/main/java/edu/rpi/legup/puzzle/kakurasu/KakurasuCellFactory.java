package edu.rpi.legup.puzzle.kakurasu;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.ElementFactory;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.save.InvalidFileFormatException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.awt.*;

public class KakurasuCellFactory extends ElementFactory {
    /**
     * Creates a puzzleElement based on the xml document Node and adds it to the board
     *
     * @param node node that represents the puzzleElement
     * @param board board to add the newly created cell
     * @return newly created cell from the xml document Node
     * @throws InvalidFileFormatException if file is invalid
     */
    @Override
    public PuzzleElement importCell(Node node, Board board) throws InvalidFileFormatException {
        try {
            KakurasuBoard kakurasuBoard = (KakurasuBoard) board;
            int width = kakurasuBoard.getWidth();
            int height = kakurasuBoard.getHeight();
            NamedNodeMap attributeList = node.getAttributes();
            if (node.getNodeName().equalsIgnoreCase("cell")) {

                int value = Integer.valueOf(attributeList.getNamedItem("value").getNodeValue());
                int x = Integer.valueOf(attributeList.getNamedItem("x").getNodeValue());
                int y = Integer.valueOf(attributeList.getNamedItem("y").getNodeValue());
                if (x >= width || y >= height) {
                    throw new InvalidFileFormatException(
                            "Kakurasu Factory: cell location out of bounds");
                }
                if (value < 0 || value > 3) {
                    throw new InvalidFileFormatException("Kakurasu Factory: cell unknown value");
                }

                KakurasuCell cell = new KakurasuCell(KakurasuType.valueOf(value), new Point(x, y));
                cell.setIndex(y * width + x);
                return cell;
            } else {
                throw new InvalidFileFormatException(
                            "Kakurasu Factory: unknown puzzleElement puzzleElement");
            }
        } catch (NumberFormatException e) {
            throw new InvalidFileFormatException(
                    "Kakurasu Factory: unknown value where integer expected");
        } catch (NullPointerException e) {
            throw new InvalidFileFormatException("Kakurasu Factory: could not find attribute(s)");
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

        KakurasuCell cell = (KakurasuCell) puzzleElement;
        Point loc = cell.getLocation();

        cellElement.setAttribute("value", String.valueOf(cell.getValue()));
        cellElement.setAttribute("x", String.valueOf(loc.x));
        cellElement.setAttribute("y", String.valueOf(loc.y));

        return cellElement;
    }
}
