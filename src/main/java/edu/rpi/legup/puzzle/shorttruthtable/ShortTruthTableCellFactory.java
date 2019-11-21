package edu.rpi.legup.puzzle.shorttruthtable;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.ElementFactory;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.save.InvalidFileFormatException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.awt.*;

public class ShortTruthTableCellFactory extends ElementFactory {
    /**
     * Creates a puzzleElement based on the xml document Node and adds it to the board
     *
     * @param node  node that represents the puzzleElement
     * @param board board to add the newly created cell
     * @return newly created cell from the xml document Node
     * @throws InvalidFileFormatException
     */
    @Override
    public ShortTruthTableCell importCell(Node node, Board board) throws InvalidFileFormatException {
        try {

            //sanity check that the name is a cell
            if (!node.getNodeName().equalsIgnoreCase("cell")) {
                throw new InvalidFileFormatException("nurikabe Factory: unknown puzzleElement puzzleElement");
            }

            //Cast the board object
            ShortTruthTableBoard sttBoard = (ShortTruthTableBoard) board;
            int width = sttBoard.getWidth();
            int height = sttBoard.getHeight();

            //Get the attributes for the cell
            NamedNodeMap attributeList = node.getAttributes();
            char symbol = attributeList.getNamedItem("symbol").getNodeValue().charAt(0);
            ShortTruthTableCellType cellType = ShortTruthTableCellType.valueOf(Integer.valueOf(attributeList.getNamedItem("cell_type").getNodeValue()));
            int x = Integer.valueOf(attributeList.getNamedItem("x").getNodeValue());
            int y = Integer.valueOf(attributeList.getNamedItem("y").getNodeValue());
            if (x >= width || y >= height) {
                throw new InvalidFileFormatException("short truth table Factory: cell location out of bounds");
            }

            //Construct the cell
            ShortTruthTableCell cell = new ShortTruthTableCell(symbol, cellType, new Point(x, y));
            cell.setIndex(y * height + x);
            return cell;

        } catch (NumberFormatException e) {
            throw new InvalidFileFormatException("short truth table Factory: unknown value where integer expected");
        } catch (NullPointerException e) {
            throw new InvalidFileFormatException("short truth table Factory: could not find attribute(s)");
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

        ShortTruthTableCell cell = (ShortTruthTableCell) puzzleElement;
        Point loc = cell.getLocation();

        cellElement.setAttribute("symbol", String.valueOf(cell.getSymbol()));
        cellElement.setAttribute("cell_type", String.valueOf(cell.getData()));
        cellElement.setAttribute("x", String.valueOf(loc.x));
        cellElement.setAttribute("y", String.valueOf(loc.y));

        return cellElement;
    }
}
