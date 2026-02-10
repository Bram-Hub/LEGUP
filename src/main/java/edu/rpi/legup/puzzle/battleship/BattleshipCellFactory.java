package edu.rpi.legup.puzzle.battleship;

import edu.rpi.legup.model.Goal;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.ElementFactory;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.awt.*;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class BattleshipCellFactory extends ElementFactory {
    /**
     * Creates a puzzleElement based on the xml document Node and adds it to the board
     *
     * @param node node that represents the puzzleElement
     * @param board board to add the newly created cell
     * @return newly created cell from the xml document Node
     * @throws InvalidFileFormatException if file is invalid
     */
    @Override
    public PuzzleElement<BattleshipType> importCell(Node node, Board board)
            throws InvalidFileFormatException {
        try {
            BattleshipBoard battleShipBoard = (BattleshipBoard) board;
            int width = battleShipBoard.getWidth();
            int height = battleShipBoard.getHeight();
            NamedNodeMap attributeList = node.getAttributes();
            if (node.getNodeName().equalsIgnoreCase("cell")) {

                int value = Integer.parseInt(attributeList.getNamedItem("value").getNodeValue());
                int x = Integer.parseInt(attributeList.getNamedItem("x").getNodeValue());
                int y = Integer.parseInt(attributeList.getNamedItem("y").getNodeValue());
                if (x >= width || y >= height) {
                    throw new InvalidFileFormatException(
                            "BattleShip Factory: cell location out of bounds");
                }
                if (value < 0 || value > 3) {
                    throw new InvalidFileFormatException("BattleShip Factory: cell unknown value");
                }

                BattleshipCell cell =
                        new BattleshipCell(BattleshipType.getType(value), new Point(x, y));
                cell.setIndex(y * height + x);
                return cell;
            } else {
                throw new InvalidFileFormatException(
                        "BattleShip Factory: unknown puzzleElement puzzleElement");
            }
        } catch (NumberFormatException e) {
            throw new InvalidFileFormatException(
                    "BattleShip Factory: unknown value where integer expected");
        } catch (NullPointerException e) {
            throw new InvalidFileFormatException("BattleShip Factory: could not find attribute(s)");
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

        BattleshipCell cell = (BattleshipCell) puzzleElement;
        Point loc = cell.getLocation();

        cellElement.setAttribute("value", String.valueOf(cell.getData()));
        cellElement.setAttribute("x", String.valueOf(loc.x));
        cellElement.setAttribute("y", String.valueOf(loc.y));

        return cellElement;
    }
}
