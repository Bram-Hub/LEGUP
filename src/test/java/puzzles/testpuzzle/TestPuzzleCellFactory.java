package puzzles.testpuzzle;

import edu.rpi.legup.model.gameboard.*;
import edu.rpi.legup.save.InvalidFileFormatException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.awt.*;

public class TestPuzzleCellFactory extends ElementFactory {

    @Override
    public GridCell importCell(Node node, Board board) throws InvalidFileFormatException {
        try {
            if (!node.getNodeName().equalsIgnoreCase("cell")) {
                throw new InvalidFileFormatException(
                        "testpuzzle Factory: unknown puzzleElement puzzleElement");
            }

            GridBoard gridBoard = (GridBoard) board;
            int width = gridBoard.getWidth();
            int height = gridBoard.getHeight();

            NamedNodeMap attributeList = node.getAttributes();
            int value = Integer.valueOf(attributeList.getNamedItem("value").getNodeValue());
            int x = Integer.valueOf(attributeList.getNamedItem("x").getNodeValue());
            int y = Integer.valueOf(attributeList.getNamedItem("y").getNodeValue());
            if (x >= width || y >= height) {
                throw new InvalidFileFormatException(
                        "testpuzzle Factory: cell location out of bounds");
            }
            if (value < -2) {
                throw new InvalidFileFormatException("testpuzzle Factory: cell unknown value");
            }

            GridCell cell = new GridCell(value, new Point(x, y));
            cell.setIndex(y * height + x);
            return cell;
        } catch (NumberFormatException e) {
            throw new InvalidFileFormatException(
                    "testpuzzle Factory: unknown value where integer expected");
        } catch (NullPointerException e) {
            throw new InvalidFileFormatException("testpuzzle Factory: could not find attribute(s)");
        }
    }

    @Override
    public Element exportCell(Document document, PuzzleElement puzzleElement) {
        return null;
    }
}
