package puzzle.fillapix;

import model.gameboard.Board;
import model.gameboard.ElementData;
import model.gameboard.ElementFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import puzzle.lightup.LightUpBoard;
import puzzle.lightup.LightUpCell;
import save.InvalidFileFormatException;

import java.awt.*;

public class FillapixCellFactory extends ElementFactory
{
    /**
     * Creates a element based on the xml document Node and adds it to the board
     *
     * @param node  node that represents the element
     * @param board board to add the newly created cell
     *
     * @return newly created cell from the xml document Node
     *
     * @throws InvalidFileFormatException
     */
    @Override
    public FillapixCell importCell(Node node, Board board) throws InvalidFileFormatException
    {
        try
        {
            if(!node.getNodeName().equalsIgnoreCase("cell"))
            {
                throw new InvalidFileFormatException("Fillapix Factory: unknown data element");
            }

            FillapixBoard fillapixBoard = (FillapixBoard) board;
            int width = fillapixBoard.getWidth();
            int height = fillapixBoard.getHeight();

            NamedNodeMap attributeList = node.getAttributes();
            int value = Integer.valueOf(attributeList.getNamedItem("value").getNodeValue());
            int x = Integer.valueOf(attributeList.getNamedItem("x").getNodeValue());
            int y = Integer.valueOf(attributeList.getNamedItem("y").getNodeValue());
            if(x >= width || y >= height)
            {
                throw new InvalidFileFormatException("Fillapix Factory: cell location out of bounds");
            }
            if(value < -50 || value > 50)
            {
                throw new InvalidFileFormatException("Fillapix Factory: cell unknown value");
            }

            FillapixCell cell = new FillapixCell(value, new Point(x, y));
            cell.setIndex(y * height + x);
            return cell;
        }
        catch(NumberFormatException e)
        {
            throw new InvalidFileFormatException("Fillapix Factory: unknown value where integer expected");
        }
        catch(NullPointerException e)
        {
            throw new InvalidFileFormatException("Fillapix Factory: could not find attribute(s)");
        }
    }

    /**
     * Creates a xml document element from a cell for exporting
     *
     * @param document xml document
     * @param data     ElementData cell
     *
     * @return xml Element
     */
    public Element exportCell(Document document, ElementData data)
    {
        Element cellElement = document.createElement("cell");

        FillapixCell cell = (FillapixCell) data;
        Point loc = cell.getLocation();

        cellElement.setAttribute("value", String.valueOf(cell.getValueInt()));
        cellElement.setAttribute("x", String.valueOf(loc.x));
        cellElement.setAttribute("y", String.valueOf(loc.y));

        return cellElement;
    }
}
