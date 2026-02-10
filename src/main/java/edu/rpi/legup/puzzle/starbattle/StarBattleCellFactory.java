package edu.rpi.legup.puzzle.starbattle;

import edu.rpi.legup.model.Goal;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.ElementFactory;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.awt.*;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class StarBattleCellFactory extends ElementFactory {
    @Override
    public StarBattleCell importCell(Node node, Board board) throws InvalidFileFormatException {
        try {
            if (!node.getNodeName().equalsIgnoreCase("cell")) {
                throw new InvalidFileFormatException(
                        "starbattle Factory: unknown puzzleElement puzzleElement");
            }

            StarBattleBoard starbattleBoard = (StarBattleBoard) board;
            int size = starbattleBoard.getSize();

            NamedNodeMap attributeList = node.getAttributes();
            int value = Integer.valueOf(attributeList.getNamedItem("value").getNodeValue());
            int x = Integer.valueOf(attributeList.getNamedItem("x").getNodeValue());
            int y = Integer.valueOf(attributeList.getNamedItem("y").getNodeValue());
            int groupIndex =
                    Integer.valueOf(attributeList.getNamedItem("groupIndex").getNodeValue());
            if (x >= size || y >= size) {
                throw new InvalidFileFormatException(
                        "starbattle Factory: cell location out of bounds");
            }
            if (groupIndex >= size || groupIndex < 0) {
                throw new InvalidFileFormatException("starbattle Factory: not in a valid region");
            }
            if (value != 0) { // ALL INITIAL PUZZLES ARE BLANK, SUBJECT TO CHANGE
                throw new InvalidFileFormatException("starbattle Factory: cell unknown value");
            }

            StarBattleCell cell = new StarBattleCell(value, new Point(x, y), groupIndex, size);
            cell.setIndex(y * size + x);
            return cell;
        } catch (NumberFormatException e1) {
            e1.printStackTrace();
            throw new InvalidFileFormatException(
                    "starbattle Factory: unknown value where integer expected");
        } catch (NullPointerException e2) {
            e2.printStackTrace();
            throw new InvalidFileFormatException("starbattle Factory: could not find attribute(s)");
        }
    }


    public org.w3c.dom.Element exportCell(Document document, PuzzleElement puzzleElement) {
        org.w3c.dom.Element cellElement = document.createElement("cell");

        StarBattleCell cell = (StarBattleCell) puzzleElement;
        Point loc = cell.getLocation();

        cellElement.setAttribute("value", String.valueOf(cell.getData()));
        cellElement.setAttribute("x", String.valueOf(loc.x));
        cellElement.setAttribute("y", String.valueOf(loc.y));

        return cellElement;
    }
}
