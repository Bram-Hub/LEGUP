package edu.rpi.legup.puzzle.battleship;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.ElementFactory;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.save.InvalidFileFormatException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.awt.*;

public class BattleShipCellFactory extends ElementFactory {
    /**
     * Creates a puzzleElement based on the xml document Node and adds it to the board
     *
     * @param node  node that represents the puzzleElement
     * @param board board to add the newly created cell
     * @return newly created cell from the xml document Node
     * @throws InvalidFileFormatException
     */
    @Override
    public PuzzleElement importCell(Node node, Board board) throws InvalidFileFormatException {
        try {
            BattleShipBoard battleShipBoard = (BattleShipBoard) board;
            int width = battleShipBoard.getWidth();
            int height = battleShipBoard.getHeight();
            NamedNodeMap attributeList = node.getAttributes();
            if (node.getNodeName().equalsIgnoreCase("cell")) {

                int value = Integer.valueOf(attributeList.getNamedItem("value").getNodeValue());
                int x = Integer.valueOf(attributeList.getNamedItem("x").getNodeValue());
                int y = Integer.valueOf(attributeList.getNamedItem("y").getNodeValue());
                if (x >= width || y >= height) {
                    throw new InvalidFileFormatException("BattleShip Factory: cell location out of bounds");
                }
                if (value < 0 || value > 3) {
                    throw new InvalidFileFormatException("BattleShip Factory: cell unknown value");
                }

                BattleShipCell cell = new BattleShipCell(value, new Point(x, y));
                cell.setIndex(y * height + x);
                return cell;
            } else {
                throw new InvalidFileFormatException("BattleShip Factory: unknown puzzleElement puzzleElement");
            }
        } catch (NumberFormatException e) {
            throw new InvalidFileFormatException("BattleShip Factory: unknown value where integer expected");
        } catch (NullPointerException e) {
            throw new InvalidFileFormatException("BattleShip Factory: could not find attribute(s)");
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

        BattleShipCell cell = (BattleShipCell) puzzleElement;
        Point loc = cell.getLocation();

        cellElement.setAttribute("value", String.valueOf(cell.getData()));
        cellElement.setAttribute("x", String.valueOf(loc.x));
        cellElement.setAttribute("y", String.valueOf(loc.y));

        return cellElement;
    }
}
