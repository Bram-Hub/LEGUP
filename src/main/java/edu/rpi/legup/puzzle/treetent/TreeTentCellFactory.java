package edu.rpi.legup.puzzle.treetent;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.ElementFactory;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.save.InvalidFileFormatException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.awt.*;

public class TreeTentCellFactory extends ElementFactory {
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
            TreeTentBoard treeTentBoard = (TreeTentBoard) board;
            int width = treeTentBoard.getWidth();
            int height = treeTentBoard.getHeight();
            NamedNodeMap attributeList = node.getAttributes();
            if (node.getNodeName().equalsIgnoreCase("cell")) {

                int value = Integer.valueOf(attributeList.getNamedItem("value").getNodeValue());
                int x = Integer.valueOf(attributeList.getNamedItem("x").getNodeValue());
                int y = Integer.valueOf(attributeList.getNamedItem("y").getNodeValue());
                if (x >= width || y >= height) {
                    throw new InvalidFileFormatException("TreeTent Factory: cell location out of bounds");
                }
                if (value < 0 || value > 3) {
                    throw new InvalidFileFormatException("TreeTent Factory: cell unknown value");
                }

                TreeTentCell cell = new TreeTentCell(value, new Point(x, y));
                cell.setIndex(y * height + x);
                return cell;
            }
            else {
                if (node.getNodeName().equalsIgnoreCase("line")) {
                    int x1 = Integer.valueOf(attributeList.getNamedItem("x1").getNodeValue());
                    int y1 = Integer.valueOf(attributeList.getNamedItem("y1").getNodeValue());
                    int x2 = Integer.valueOf(attributeList.getNamedItem("x2").getNodeValue());
                    int y2 = Integer.valueOf(attributeList.getNamedItem("y2").getNodeValue());
                    if (x1 >= width || y1 >= height || x2 >= width || y2 >= height) {
                        throw new InvalidFileFormatException("TreeTent Factory: line location out of bounds");
                    }

                    TreeTentCell c1 = treeTentBoard.getCell(x1, y1);
                    TreeTentCell c2 = treeTentBoard.getCell(x2, y2);
                    return new TreeTentLine(c1, c2);
                }
                else {
                    throw new InvalidFileFormatException("TreeTent Factory: unknown puzzleElement puzzleElement");
                }
            }
        }
        catch (NumberFormatException e) {
            throw new InvalidFileFormatException("TreeTent Factory: unknown value where integer expected");
        }
        catch (NullPointerException e) {
            throw new InvalidFileFormatException("TreeTent Factory: could not find attribute(s)");
        }
    }

    /**
     * Creates a xml document puzzleElement from a cell for exporting
     *
     * @param document      xml document
     * @param puzzleElement PuzzleElement cell
     * @return xml PuzzleElement
     */
    public org.w3c.dom.Element exportCell(Document document, PuzzleElement puzzleElement) {
        if (puzzleElement instanceof TreeTentCell) {
            org.w3c.dom.Element cellElement = document.createElement("cell");

            TreeTentCell cell = (TreeTentCell) puzzleElement;
            Point loc = cell.getLocation();

            cellElement.setAttribute("value", String.valueOf(cell.getData()));
            cellElement.setAttribute("x", String.valueOf(loc.x));
            cellElement.setAttribute("y", String.valueOf(loc.y));

            return cellElement;
        }
        else {
            org.w3c.dom.Element lineElement = document.createElement("line");

            TreeTentLine line = (TreeTentLine) puzzleElement;

            lineElement.setAttribute("x1", String.valueOf(line.getC1().getLocation().x));
            lineElement.setAttribute("y1", String.valueOf(line.getC1().getLocation().y));
            lineElement.setAttribute("x2", String.valueOf(line.getC2().getLocation().x));
            lineElement.setAttribute("y2", String.valueOf(line.getC2().getLocation().y));

            return lineElement;
        }
    }
}
