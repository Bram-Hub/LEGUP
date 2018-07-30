package edu.rpi.legup.puzzle.sudoku;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.Element;
import edu.rpi.legup.model.gameboard.ElementFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import edu.rpi.legup.save.InvalidFileFormatException;

import java.awt.*;

public class SudokuCellFactory extends ElementFactory
{
    /**
     * Creates a element based on the xml document Node and adds it to the board
     *
     * @param node node that represents the element
     * @param board board to add the newly created cell
     * @return newly created cell from the xml document Node
     * @throws InvalidFileFormatException
     */
    @Override
    public SudokuCell importCell(Node node, Board board) throws InvalidFileFormatException
    {
        try
        {
            if(!node.getNodeName().equalsIgnoreCase("cell"))
            {
                throw new InvalidFileFormatException("Sudoku Factory: unknown element element");
            }

            SudokuBoard sudokuBoard = (SudokuBoard)board;
            int size = sudokuBoard.getSize();
            int minorSize = (int)Math.sqrt(size);

            NamedNodeMap attributeList = node.getAttributes();
            int value = Integer.valueOf(attributeList.getNamedItem("value").getNodeValue());
            int x = Integer.valueOf(attributeList.getNamedItem("x").getNodeValue());
            int y = Integer.valueOf(attributeList.getNamedItem("y").getNodeValue());
            if(x >= size || y >= size)
            {
                throw new InvalidFileFormatException("Sudoku Factory: cell location out of bounds");
            }
            if(value < 0 || value > 9)
            {
                throw new InvalidFileFormatException("Sudoku Factory: cell unknown value");
            }
            int groupIndex = x / minorSize * minorSize + y / minorSize;
            SudokuCell cell = new SudokuCell(value, new Point(x, y), groupIndex);
            cell.setIndex(y * size + x);
            return cell;
        }
        catch(NumberFormatException e)
        {
            throw new InvalidFileFormatException("Sudoku Factory: unknown value where integer expected");
        }
        catch(NullPointerException e)
        {
            throw new InvalidFileFormatException("Sudoku Factory: could not find attribute(s)");
        }
    }

    /**
     * Creates a xml document element from a cell for exporting
     *
     * @param document xml document
     * @param data Element cell
     * @return xml Element
     */
    public org.w3c.dom.Element exportCell(Document document, Element data)
    {
        org.w3c.dom.Element cellElement = document.createElement("cell");

        SudokuCell cell = (SudokuCell)data;
        Point loc = cell.getLocation();

        cellElement.setAttribute("value", String.valueOf(cell.getData()));
        cellElement.setAttribute("x", String.valueOf(loc.x));
        cellElement.setAttribute("y", String.valueOf(loc.y));

        return cellElement;
    }
}
