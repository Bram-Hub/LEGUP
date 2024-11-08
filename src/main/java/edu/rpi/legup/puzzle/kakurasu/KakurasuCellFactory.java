package edu.rpi.legup.puzzle.kakurasu;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.ElementFactory;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.save.InvalidFileFormatException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.awt.*;

// TODO: Include changes to Kakurasu cell into the cell factory
public class KakurasuCellFactory extends ElementFactory {
    @Override
    public KakurasuCell importCell(Node node, Board board) throws InvalidFileFormatException {
        try {
            if (!node.getNodeName().equalsIgnoreCase("cell")) {
                throw new InvalidFileFormatException("kakurasu Factory: unknown puzzleElement puzzleElement");
            }

            KakurasuBoard kakurasuBoard = (KakurasuBoard) board;
            int width = kakurasuBoard.getWidth();
            int height = kakurasuBoard.getHeight();

            NamedNodeMap attributeList = node.getAttributes();
            int value = Integer.valueOf(attributeList.getNamedItem("value").getNodeValue());
            int x = Integer.valueOf(attributeList.getNamedItem("x").getNodeValue());
            int y = Integer.valueOf(attributeList.getNamedItem("y").getNodeValue());
            if (x >= width || y >= height) {
                throw new InvalidFileFormatException("kakurasu Factory: cell location out of bounds");
            }
            if (value < 0 || value > 2) {
                throw new InvalidFileFormatException("kakurasu Factory: cell unknown value");
            }

            KakurasuCell cell = new KakurasuCell(value, new Point(x, y));
            cell.setIndex(y * height + x);
            return cell;
        } catch (NumberFormatException e) {
            throw new InvalidFileFormatException("kakurasu Factory: unknown value where integer expected");
        } catch (NullPointerException e) {
            throw new InvalidFileFormatException("kakurasu Factory: could not find attribute(s)");
        }
    }

    public org.w3c.dom.Element exportCell(Document document, PuzzleElement puzzleElement) {
        org.w3c.dom.Element cellElement = document.createElement("cell");

        KakurasuCell cell = (KakurasuCell) puzzleElement;
        Point loc = cell.getLocation();

        cellElement.setAttribute("value", String.valueOf(cell.getData()));
        cellElement.setAttribute("x", String.valueOf(loc.x));
        cellElement.setAttribute("y", String.valueOf(loc.y));

        return cellElement;
    }
}
