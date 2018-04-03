package puzzle.treetent;

import model.gameboard.Board;
import model.gameboard.ElementData;
import model.gameboard.ElementFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import save.InvalidFileFormatException;

import java.awt.*;

public class TreeTentCellFactory extends ElementFactory
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
    public TreeTentCell importCell(Node node, Board board) throws InvalidFileFormatException
    {
        try
        {
            if(!node.getNodeName().equalsIgnoreCase("cell"))
            {
                throw new InvalidFileFormatException("TreeTent Factory: unknown data element");
            }

            TreeTentBoard TreeTentBoard = (TreeTentBoard)board;
            int width = TreeTentBoard.getWidth();
            int height = TreeTentBoard.getHeight();

            NamedNodeMap attributeList = node.getAttributes();
            int value = Integer.valueOf(attributeList.getNamedItem("value").getNodeValue());
            int x = Integer.valueOf(attributeList.getNamedItem("x").getNodeValue());
            int y = Integer.valueOf(attributeList.getNamedItem("y").getNodeValue());
            if(x >= width || y >= height)
            {
                throw new InvalidFileFormatException("TreeTent Factory: cell location out of bounds");
            }
            if(value < 0 || value > 3)
            {
                throw new InvalidFileFormatException("TreeTent Factory: cell unknown value");
            }

            TreeTentCell cell = new TreeTentCell(value, new Point(x, y));
            cell.setIndex(y * height + x);
            return cell;
        }
        catch(NumberFormatException e)
        {
            throw new InvalidFileFormatException("TreeTent Factory: unknown value where integer expected");
        }
        catch(NullPointerException e)
        {
            throw new InvalidFileFormatException("TreeTent Factory: could not find attribute(s)");
        }
    }

    /**
     * Creates a xml document element from a cell for exporting
     *
     * @param document xml document
     * @param data ElementData cell
     * @return xml Element
     */
    public Element exportCell(Document document, ElementData data)
    {
        Element cellElement = document.createElement("cell");

        TreeTentCell cell = (TreeTentCell)data;
        Point loc = cell.getLocation();

        cellElement.setAttribute("value", String.valueOf(cell.getValueInt()));
        cellElement.setAttribute("x", String.valueOf(loc.x));
        cellElement.setAttribute("y", String.valueOf(loc.y));

        return cellElement;
    }
}
